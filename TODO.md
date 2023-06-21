Modern TODO

- [x] @Route
- [x] @RootRoute
- [x] @Execute
- [x] @Async @Execute
- [ ] @Timeout(second = 10)
- [x] @Arg
- [x] @Arg Option<T>
- [ ] @Arg @Strict Option<T>
- [x] @Arg Optional<T>
- [x] @Arg CompletableFuture<User>
- [ ] Support String and basic types
- [ ] @Joiner String
- [x] @Async @Arg
- [x] @Permission
- [ ] @Arg @Range(min = 1, max = 10) int
- [ ] @Flag for booleans
- [x] @Editor
- [ ] @Arg Enum
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
- [ ] .errorHandler()
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

```java
@Command("report")
@Permission("myplugin.report")
class ReportCommand {
    
    private final ReportService reportService;
    
    ReportCommand(ReportService reportService) {
        this.reportService = reportService;
    }

    @Command("create")
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
    
    @Command("list")
    void list(@Arg @Range(min = 1, max = 10) int page) {
        // ...
    }

    @Command("remove")
    void remove(@Async @Arg("to-remove") Report report) {
        reportService.remove(id);
    }
    
    @Parser("to-remove")
    Restult<Report> parseReport(String input) {
        return this.reportService.getReport(input);
    }

    @Suggest("to-remove")
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

    @Command("ban")
    void ban(
        @Arg User user,
        @Arg Duration duration,
        @Flag("-s") boolean silent,
        @Join String reason
    ) {
        // ...
    }

    @Command("unban")
    void unban(@Arg User user) {
        // ...
    }

}
```

```java
@Command("teleport")
class TeleportCommand {

    @Command("here")
    void teleportHere(
            @Sender Player sender,
            @Context Location here,
            @Arg Player target
    ) {
        // ...
    }

    @Command("to")
    void teleportTo(@Sender Player sender, @Arg Player target) {
        // ...
    }

}
```

```java
@Command("gamemode")
class GamemodeCommand {

    @Command
    void gamemode(@Sender Player sender, @Arg GameMode gameMode) {
        // ...
    }
    
    @Command
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
    void vanish(@Sender Player sender, @Arg Option<Boolean> vanish) {
        // ...
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

    @Command("set")
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
    @Command("query")
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