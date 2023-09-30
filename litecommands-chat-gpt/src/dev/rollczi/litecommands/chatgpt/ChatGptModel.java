package dev.rollczi.litecommands.chatgpt;

/*
  https://platform.openai.com/docs/models/gpt-3-5
 */
public class ChatGptModel {

    public static final ChatGptModel GPT_4 = new ChatGptModel("gpt-4");
    public static final ChatGptModel GPT_4_0613 = new ChatGptModel("gpt-4-0613");
    public static final ChatGptModel GPT_4_32K = new ChatGptModel("gpt-4-32k");
    public static final ChatGptModel GPT_4_32K_0613 = new ChatGptModel("gpt-4-32k-0613");

    @Deprecated public static final ChatGptModel GPT_4_0314 = new ChatGptModel("gpt-4-0314");
    @Deprecated public static final ChatGptModel GPT_4_32K_0314 = new ChatGptModel("gpt-4-32k-0314");

    public static final ChatGptModel GPT_3_5_TURBO = new ChatGptModel("gpt-3.5-turbo");
    public static final ChatGptModel GPT_3_5_TURBO_16K = new ChatGptModel("gpt-3.5-turbo-16k");
    public static final ChatGptModel GPT_3_5_TURBO_INSTRUCT = new ChatGptModel("gpt-3.5-turbo-instruct");
    public static final ChatGptModel GPT_3_5_TURBO_0613 = new ChatGptModel("gpt-3.5-turbo-0613");
    public static final ChatGptModel GPT_3_5_TURBO_16K_0613 = new ChatGptModel("gpt-3.5-turbo-16k-0613");

    @Deprecated public static final ChatGptModel GPT_3_5_TURBO_0301 = new ChatGptModel("gpt-3.5-turbo-0301");
    @Deprecated public static final ChatGptModel TEXT_DAVINCI_003 = new ChatGptModel("text-davinci-003");
    @Deprecated public static final ChatGptModel TEXT_DAVINCI_002 = new ChatGptModel("text-davinci-002");
    @Deprecated public static final ChatGptModel CODE_DAVINCI_002 = new ChatGptModel("code-davinci-002");

    private final String name;

    public ChatGptModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
