package dev.rollczi.litecommands.chatgpt;

import com.google.gson.Gson;
import dev.rollczi.litecommands.LiteCommandsException;
import dev.rollczi.litecommands.util.StringUtil;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

class ChatGptClient {

    private static final Logger LOGGER = Logger.getLogger("LiteCommands");
    public static final String CHAT_GPT_URL = "https://api.openai.com/v1/chat/completions";

    private final Gson gson = new Gson();
    private final OkHttpClient client = new OkHttpClient();

    private final ChatGptSettings settings;

    public ChatGptClient(ChatGptSettings settings) {
        this.settings = settings;
    }

    public String chat(String message) {
        return chat(new ChatGptMessage(ChatGptRole.USER, message));
    }

    public String chat(String context, String message) {
        return chat(new ChatGptMessage(ChatGptRole.SYSTEM, context), new ChatGptMessage(ChatGptRole.USER, message));
    }

    public String chat(ChatGptMessage... messages) {
        Request request = new Request.Builder()
            .url(CHAT_GPT_URL)
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + settings.apiKey())
            .post(RequestBody.create(gson.toJson(new ChatGptRequest(messages)), MediaType.get("application/json")))
            .build();

        try (Response response = client.newCall(request).execute()) {
            ResponseBody responseBody = response.body();
            String rawBody = responseBody == null ? null : responseBody.string();

            if (response.code() == 429 && rawBody != null) {
                ErrorBody errorBody = gson.fromJson(rawBody, ErrorBody.class);

                if ("rate_limit_exceeded".equals(errorBody.error.code)) {
                    LOGGER.warning("OpenAI API rate limit exceeded!");
                    return StringUtil.EMPTY;
                }
            }

            if (response.code() == 401 && rawBody != null) {
                ErrorBody errorBody = gson.fromJson(rawBody, ErrorBody.class);

                if ("invalid_api_key".equals(errorBody.error.code)) {
                    LOGGER.warning("OpenAI API key is invalid!");
                    return StringUtil.EMPTY;
                }
            }

            if (response.code() != 200) {
                throw new LiteCommandsException("OpenAI API returned code " + response.code() + " with message: " + rawBody);
            }

            if (rawBody == null) {
                throw new LiteCommandsException("OpenAI API returned empty body");
            }

            ChatGptResponse chatGptResponse = gson.fromJson(rawBody, ChatGptResponse.class);
            List<ChatGptResponse.Choice> choices = chatGptResponse.getChoices();

            if (choices.isEmpty()) {
                throw new LiteCommandsException("OpenAI API returned empty choices");
            }

            if (choices.size() != 1) {
                throw new LiteCommandsException("OpenAI API returned more than one choice: " + choices.size());
            }

            return choices.get(0).getMessage().getContent();
        } catch (IOException exception) {
            throw new LiteCommandsException(exception);
        }
    }

    private class ErrorBody {
        private final ErrorResponse error;

        public ErrorBody(ErrorResponse error) {
            this.error = error;
        }
    }

    private class ErrorResponse {

        private final String message;
        private final String type;
        private final String param;
        private final String code;

        public ErrorResponse(String message, String type, String param, String code) {
            this.message = message;
            this.type = type;
            this.param = param;
            this.code = code;
        }
    }

    private class ChatGptRequest {

        private final String model;
        private final List<ChatGptMessage> messages = new ArrayList<>();
        private final double temperature;

        public ChatGptRequest(ChatGptMessage... messages) {
            this.model = settings.model().getName();
            this.messages.addAll(Arrays.asList(messages));
            this.temperature = settings.temperature();
        }

    }

    private static class ChatGptResponse {

        private final String id;
        private final String object;
        private final long created;
        private final String model;
        private final Usage usage;
        private final List<Choice> choices;

        private ChatGptResponse(String id, String object, long created, String model, Usage usage, List<Choice> choices) {
            this.id = id;
            this.object = object;
            this.created = created;
            this.model = model;
            this.usage = usage;
            this.choices = choices;
        }

        public String getId() {
            return id;
        }

        public String getObject() {
            return object;
        }

        public long getCreated() {
            return created;
        }

        public String getModel() {
            return model;
        }

        public Usage getUsage() {
            return usage;
        }

        public List<Choice> getChoices() {
            return choices;
        }

        private static class Usage {

            private final int prompt_tokens;
            private final int completion_tokens;
            private final int total_tokens;

            public Usage(int prompt_tokens, int completion_tokens, int total_tokens) {
                this.prompt_tokens = prompt_tokens;
                this.completion_tokens = completion_tokens;
                this.total_tokens = total_tokens;
            }

            public int getPrompt_tokens() {
                return prompt_tokens;
            }

            public int getCompletion_tokens() {
                return completion_tokens;
            }

            public int getTotal_tokens() {
                return total_tokens;
            }
        }

        private static class Choice {

            private final ChatGptMessage message;
            private final String finish_reason;
            private final int index;

            public Choice(ChatGptMessage message, String finish_reason, int index) {
                this.message = message;
                this.finish_reason = finish_reason;
                this.index = index;
            }

            public ChatGptMessage getMessage() {
                return message;
            }

            public String getFinishReason() {
                return finish_reason;
            }

            public int getIndex() {
                return index;
            }
        }
    }

}
