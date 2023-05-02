Modern TODO

- [x] @Route
- [x] @RootRoute
- [x] @Execute
- [ ] @ExecuteAsync vs @Execute(async = true)
- [ ] @Timeout(second = 10)
- [x] @Arg
- [x] @Arg Option<T>
- [ ] @Arg @Strict Option<T>
- [x] @Arg Optional<T>
- [x] @Arg CompletableFuture<User>
- [ ] Support String and basic types
- [ ] @Joiner String
- [ ] @AsyncArg
- [ ] @Permission
- [ ] @ExcludePermission
- [ ] @Arg @Range(min = 1, max = 10) int
- [ ] @Flag for booleans
- [x] @Editor
- [ ] @Arg Enum
- [ ] @Arg List<String> // for example text, text, text
- [ ] @Arg Set<T> // for example 1, 4, 5
- [ ] @Arg Map<K, V> // for example key1=value1, key2=value2 // do wyrzucenia chyba
- [ ] @Suggest
- [ ] @Literal
- [ ] @Validate(validator = MyValidator.class)
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
- [x] Support Velocity
- [x] Support Bukkit
- [ ] Support Nukkit
- [x] Support Adventure
- [x] Support Paper
- [x] Add support to provide other types of arguments by platforms