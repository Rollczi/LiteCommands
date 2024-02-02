TODO

- [x] @Command
- [x] @RootCommand
- [x] @Execute
- [x] @Async @Execute
- [ ] @Timeout(second = 10)
- [x] @Arg
- [x] @Arg Option<T>
- [ ] @Arg @Strict Option<T>
- [x] @Arg Optional<T>
- [x] @Arg CompletableFuture<User>
- [x] Support String and basic types
- [x] @Join String
- [x] @Async @Arg
- [x] @Permission
- [ ] @Arg @Range(min = 1, max = 10) int
- [x] @Flag for booleans
- [ ] @Editor
- [x] @Arg Enum
- [ ] @Arg List<String> // for example text, text, text
- [ ] @Arg Set<T> // for example 1, 4, 5
- [ ] @Arg Map<K, V> // for example key1=value1, key2=value2 // do wyrzucenia chyba
- [ ] @Suggest
- [ ] @Literal
- [x] @Validate(validator = MyValidator.class)
- [ ] @Cooldown - Określa cza s, przez który użytkownik nie może ponownie użyć danej komendy po jej wykonaniu.
- [ ] @Cooldown(second = 10, bypass = "myplugin.bypass.cooldown", scope = CooldownScope.GLOBAL)
- [ ] @MaxCalls(second = 10, max = 5)
- [ ] @Delay - Adnotacja ta może być użyta przed metodą, aby określić czas opóźnienia (w milisekundach) przed wykonaniem
  danej metody.
- [ ] ??? @Warmup - Adnotacja ta pozwala na określenie czasu, przez który komenda będzie miała stan "rozgrzewania",
  czyli czasu, w którym użytkownik będzie miał możliwość anulowania wykonania komendy. Po tym czasie komenda zostanie
  wykonana automatycznie. Ta adnotacja jest przydatna, gdy chcesz dać graczowi czas na zastanowienie się przed
  wykonaniem danej akcji.
- [ ] @Confirmation .confirmation()
- [x] @Context
- [ ] .debugger(true)
- [x] .errorHandler()
- [ ] @Arg @Regex("") String
- [ ] @Arg @Regex("[a-zA-Z0-9_]") @Length(min = 3, max = 16) String playerName
- [x] Return result handler
- [x] Return result handler remapper
- [x] Support JDA
- [ ] Support Sponge
- [x] Support BungeeCord
- [ ] Support Waterfall
- [x] Support Velocity
- [x] Support Bukkit
- [ ] Support Nukkit
- [x] Support Adventure
- [x] Support Paper
- [x] Add support to provide other types of arguments by platforms

### This is not a command example.  They're just concepts!

```java
@Command("report")
@Permission("myplugin.report")
class ReportCommand {
    
    private final ReportService reportService;
    
    ReportCommand(ReportService reportService) {
        this.reportService = reportService;
    }

    @Execute("create")
    @Cooldown(seconds = 5, bypass = "myplugin.bypass.cooldown")
    void report(
            @Arg User user, 
            @Flag("-s") boolean silent,
            @Join String reason
    ) {
        reportService.report(user, reason);
        
        if (!silent) {
            user.sendMessage("You have been reported!");
        }
    }
    
    @Execute("list")
    void list(@Arg @Range(min = 1, max = 10) int page) {
        // ...
    }

    @Execute("remove")
    void remove(@Async @Arg("to-remove") Report report) {
        reportService.remove(id);
    }
    
    @ArgParser("to-remove")
    ParseResult<Report> parseReport(String x) {
     
    }

    @ArgParser(value = "to-remove", range = 2)
    ParseResult<Report> parseReport(String x, String y) {

    }

    @ArgSuggester("to-remove")
    SuggestResult suggestId() {
        return this.reportService.getReports().stream()
            .map(Report::getId)
            .map(String::valueOf)
            .collect(SuggestResult.collector());
    }
    
}
```

```java
@RootCommand
@Permission("myplugin.admin")
class RootBanCommand {

    @Execute("ban")
    void ban(
        @Arg User user,
        @Arg Duration duration,
        @Flag("-s") boolean silent,
        @Join String reason
    ) {
        // ...
    }

    @Execute("unban")
    void unban(@Arg User user) {
        // ...
    }

}
```

```java
@Command("teleport")
class TeleportCommand {
    
    @Execute("gpt")
    void teleport(@Arg @ChatGpt String text) {
        // ...
    }
    
}

```java
@Command("teleport")
class TeleportCommand {

    @Execute("here")
    void teleportHere(
            @Sender Player sender,
            @Context Location here,
            @Arg Player target
    ) {
        // ...
    }

    @Execute("to")
    void teleportTo(@Sender Player sender, @Arg Player target) {
        // ...
    }

}
```

```java
@Command("gamemode")
class GamemodeCommand {

    @Execute
    void gamemode(@Sender Player sender, @Arg GameMode gameMode) {
        // ...
    }
    
    @Execute
    void gamemode(
            @Sender Player sender, 
            @Arg("gamemode") @Range(min = 0, max = 3) int gameMode
    ) {
        // ...
    }
    
    @Suggest("gamemode")
    SuggestResult suggestGameMode = SuggestResult.of("0", "1", "2", "3");
    
}
```

```java
@Command(value = "vanish", aliases = {"v", "invisible"})
class VanishCommand {

    @Command
    String vanish(@Sender Player sender, @Arg Option<Boolean> vanish) {
        return "Vanish is now " + (vanish.orElseGet(() -> !sender.isInvisible()));
    }

}
```

```java
@Command("give")
class GiveCommand {

    @Command
    void give(
            @Sender Player sender,
            @Arg Player target,
            @Arg ItemStack item,
            @Arg @Range(min = 1, max = 64) int amount
    ) {
        // ...
    }

}
```

```java
@Command("discord")

class DiscordCommand {

    @Execute("set")
    void set(
            @Sender Player sender, 
            @Arg @Regex("[a-zA-Z0-9_]") @Length(min = 3, max = 16) String playerName,
            @Arg @Regex(".*#\\d{4}") String discordName
    ) {
        // ...
    }

}
```

```java
@Command("database")
class DatabaseCommand {

    @Async
    @Execute("query")
    @Timeout(seconds = 5)
    @Cooldown(seconds = 5)
    void query(@Arg @Join String query) {
        // ...
    }

}
```

```java
class Main {
    public static void main(String[] args) {
        LiteCommands<?> liteCommands = LiteCommands.builder()
            .platform(LiteBukkitPlatform.standard())
            
            .argumentParser(User.class, new UserArgumentParser())
            
            .commands(
                    new ReportCommand()
            )
            
            .build();
        
        liteCommands.start();
    }
    
}

```

Programmatic

```java
import java.util.Optional;

@Command(name = "ban", aliases = "ban-alias")
@Permission("example.ban")
public class BanCommand {
    
    @Execute
    public void ban(
            @Context CommandSender sender,
            @Arg User user,
            @Flag("-s") boolean silent,
            @Join(separator = ", ", limit = 5) String reason
    ) {
        // ...
    }

}

public class BanCommand extends LiteCommand {

    private final static LiteArg<CommandSender> SENDER = LiteArg.of("sender", CommandSender.class);
    private final static LiteArg<User> USER = LiteArg.of("user", User.class);
    private final static LiteFlag SILENT = LiteFlag.of("-s");
    private final static LiteJoin REASON = LiteJoin.of("reason", ", ", 5);

    BanCommand() {
        super("ban", "ban-alias");
        this.permissions("example.ban")
            .context(SENDER)
            .arguments(USER, SILENT, REASON);
    }

    @Override
    public void execute(CommandContext context) {
        CommandSender sender = context.get(SENDER);
        User user = context.get(USER);
        boolean silent = context.get(SILENT);
        String reason = context.get(REASON);
        
        // ...
    }

}

public class BanCommand extends LiteCommand {

    BanCommand() {
        super("ban", "ban-alias");
        this.permissions("example.ban")
            .context("sender", CommandSender.class)
            .argument("user", User.class)
            .flag("-s")
            .join("reason", ", ", 5);
    }

    @Override
    public void execute(CommandContext context) {
        CommandSender sender = context.get("sender");
        User user = context.getArg("user");
        boolean silent = context.getFlag("-s");
        String reason = context.getJoin("reason");

        // ...
    }

}

class Main {
    public static void main(String[] args) {
Command banCommand = new Command("ban")
    .aliases("ban-alias")
    .permission("example.ban")
    .context("sender", CommandSender.class)
    .argument("user", User.class)
    .flag("-s")
    .join("reason", ", ", 5)
    .executor((context) -> {
        CommandSender sender = context.get("sender");
        User user = context.getArg("user");
        boolean silent = context.getFlag("-s");
        String reason = context.getJoin("reason");

        // ...
    });
        
    }
}


```
