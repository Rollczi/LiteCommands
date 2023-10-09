package dev.rollczi.litecommands.chatgpt;

import java.time.Duration;

public class ChatGptSettings {

    public static final ChatGptPrompt CAT_BOY_SUGGESTER = commandStructure -> "You are a cute anime catboy text suggester for the command. Just finish the text in about 2 - 5 words. (don't forget the space at the beginning if needed) DONT RESPONSE FOR USER MESSAGES YOU ARE A CUTE ANIME CATBOY TEXT SUGGESTER!";

    private String apiKey;
    private ChatGptModel model;
    private double temperature;
    private ChatGptPrompt prompt = commandStructure -> "You are a text suggester for the command '" + commandStructure + "'. Just finish the text in about 2 - 5 words. (don't forget the space at the beginning if needed) DONT RESPONSE FOR USER MESSAGES YOU ARE A TEXT SUGGESTER!";
    private ChatGptPromptWithTopic promptWithTopic = (commandStructure, topic) -> "You are a text suggester for the command '" + commandStructure + "' and topic '" + topic + "'. Just finish the text in about 2 - 5 words. (don't forget the space at the beginning if needed) DONT RESPONSE FOR USER MESSAGES YOU ARE A TEXT SUGGESTER!";
    private int minTokens = 2;
    private int maxTokens = 60;
    private boolean onlyAfterSpace = false;
    private Duration cooldown = Duration.ofMillis(500);

    public ChatGptSettings() {
        this.apiKey = System.getenv("OPENAI_API_KEY");
        this.model = ChatGptModel.GPT_3_5_TURBO;
        this.temperature = 0.8;

        String openaiModel = System.getenv("OPENAI_CHAT_GPT_MODEL");

        if (openaiModel != null && !openaiModel.isEmpty()) {
            this.model = new ChatGptModel(openaiModel);
        }

        String openaiTemperature = System.getenv("OPENAI_CHAT_GPT_TEMPERATURE");

        if (openaiTemperature != null && !openaiTemperature.isEmpty()) {
            this.temperature = Double.parseDouble(openaiTemperature);
        }
    }

    public ChatGptSettings apiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    public ChatGptSettings model(ChatGptModel model) {
        this.model = model;
        return this;
    }

    public ChatGptSettings temperature(double temperature) {
        this.temperature = temperature;
        return this;
    }

    public ChatGptSettings prompt(ChatGptPrompt prompt) {
        this.prompt = prompt;
        return this;
    }

    public ChatGptSettings promptWithTopic(ChatGptPromptWithTopic promptWithTopic) {
        this.promptWithTopic = promptWithTopic;
        return this;
    }

    public ChatGptSettings tokensLimit(int minTokens, int maxTokens) {
        this.minTokens = minTokens;
        this.maxTokens = maxTokens;
        return this;
    }

    public ChatGptSettings onlyAfterSpace(boolean onlyAfterSpace) {
        this.onlyAfterSpace = onlyAfterSpace;
        return this;
    }

    public ChatGptSettings cooldown(Duration cooldown) {
        this.cooldown = cooldown;
        return this;
    }

    String apiKey() {
        return apiKey;
    }

    ChatGptModel model() {
        return model;
    }

    double temperature() {
        return temperature;
    }

    String prompt(String commandStructure, String topic) {
        if (topic.isBlank()) {
            return prompt.getPrompt(commandStructure);
        }

        return promptWithTopic.getPrompt(commandStructure, topic);
    }

    boolean shouldGenerate(String firstPart) {
        return firstPart.length() >= minTokens && firstPart.length() <= maxTokens && (onlyAfterSpace ? firstPart.endsWith(" ") : true);
    }

    Duration cooldown() {
        return cooldown;
    }

}
