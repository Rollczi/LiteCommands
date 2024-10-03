package dev.rollczi.litecommands.chatgpt;

/*
  https://platform.openai.com/docs/models/
 */
public class ChatGptModel {

    public static final ChatGptModel GPT_4o = new ChatGptModel("gpt-4o");
    public static final ChatGptModel GPT_4o_2024_08_06 = new ChatGptModel("gpt-4o-2024-08-06");
    public static final ChatGptModel GPT_4o_2024_05_13 = new ChatGptModel("gpt-4o-2024-05-13");
    public static final ChatGptModel CHATGPT_4o_LATEST = new ChatGptModel("chatgpt-4o-latest");

    public static final ChatGptModel GPT_4o_MINI = new ChatGptModel("gpt-4o-mini");
    public static final ChatGptModel GPT_4o_MINI_2024_07_18 = new ChatGptModel("gpt-4o-mini-2024-07-18");

    public static final ChatGptModel GPT_4o_REALTIME_PREVIEW = new ChatGptModel("gpt-4o-realtime-preview");
    public static final ChatGptModel GPT_4o_REALTIME_PREVIEW_2024_10_01 = new ChatGptModel("gpt-4o-realtime-preview-2024-10-01");

    public static final ChatGptModel O1_PREVIEW = new ChatGptModel("o1-preview");
    public static final ChatGptModel O1_PREVIEW_2024_09_12 = new ChatGptModel("o1-preview-2024-09-12");

    public static final ChatGptModel O1_MINI = new ChatGptModel("o1-mini");
    public static final ChatGptModel O1_MINI_2024_09_12 = new ChatGptModel("o1-mini-2024-09-12");

    public static final ChatGptModel GPT_4_TURBO = new ChatGptModel("gpt-4-turbo");
    public static final ChatGptModel GPT_4_TURBO_2024_04_09 = new ChatGptModel("gpt-4-turbo-2024-04-09");
    public static final ChatGptModel GPT_4_TURBO_PREVIEW = new ChatGptModel("gpt-4-turbo-preview");
    public static final ChatGptModel GPT_4_0125_PREVIEW = new ChatGptModel("gpt-4-0125-preview");

    @Deprecated public static final ChatGptModel GPT_4_1106_PREVIEW = new ChatGptModel("gpt-4-1106-preview");

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
