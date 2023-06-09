class Main {
    public static void main(String[] args) {
        LiteCommands<CommandSender> liteCommands = LiteCommandsBukkit.create(this)
            .settings(settings -> settings
                .fallbackPrefix("some-prefix")
                .nativePermission(true)
            )

            .extension(AdventrueExtension.create()
                .minimessage(MiniMessage.miniMessage())
                .minimessage(true)
                .legacyColors(true)
            )

            .commands(LiteAnnotations.load(
                new TestCommand(),
                new TestCommand(),
                new TestCommand(),
                new TestCommand()
            ))

            .suggester("test", new TestSuggester())
            .suggester(TestCommand.class, new CommandSuggester())

            .argument(Player.class, new PlayerArgument())
            .argument(String.class, new StringArgument())

            .context(Player.class, new PlayerContext())
            .context(String.class, new StringContext())

            .bind(SomeSrvice.class, () -> new SomeService())

            .editorGlobal(new GlobalEditor())
            .editor(Scope.global(), new TestEditor())
            .editor(Scope.name("name"), new TestEditor())
            .editor(Scope.type(TestCommand.class), new TestEditor())
            .editor(Scope.meta("key"), new TestEditor())

            .validatorGlobal(new GlobalValidator())
            .validator("test", new TestValidator())
            .validator(TestCommand.class, new CommandValidator())

            .exception(NumberFormatException.class, new NumberFormatExceptionHandler())
            .exception(IllegalArgumentException.class, new IllegalArgumentExceptionHandler())
            .exceptionUnexpected(new UnexpectedExceptionHandler())

            .result(String.class, new StringResultHandler())
            .result(Integer.class, new IntegerResultHandler())
            .resultUnexpected(new UnexpectedResultHandler())
            .exception(IllegalArgumentException.class, new IllegalArgumentExceptionHandler())
            .exceptionUnexpected(new UnexpectedExceptionHandler())

            .invalidUsage(new InvalidUsageHandler())
            .missingPermission(new MissingPermissionHandler())

            .arguments(new ArgumentsConfig())

            .core(core -> core

            .permissions(permissions -> permissions
                .validator(new PermissionValidator())
                .handler(new MissingPermissionHandler())
            )

            .wrappers(wrappers -> wrappers
                .registerDefault(false)
                .wrapper(new Wrapper())
            )

            .scheduler(scheduler -> scheduler
                .async(new AsyncScheduler())
                .sync(new SyncScheduler())
            )


            .schematic(schematic -> schematic
                .generator(new SchematicGenerator())
                .format(new SchematicFormat())
            )

            .logger(logger -> logger
                .logger(new Logger())
                .level(Level.DEBUG)
            )

            .locale(locale -> locale
                .locale(Locale.ENGLISH)
                .zone(ZoneId.systemDefault())
                .translator(new Translator())
            )

            .preProcessor(new Preprocessor())
            .postProcessor(new Postprocessor())

            .build();

        liteCommands.register();
        liteCommands.unregister();
    }
}

class UserContext {

    @Argumen

}

@Namespace("test")
@Command(name = "test", aliases = {"t"})
@Permission("test")
class TestCommand {

    @Default
    void test(@Arg("player") Player player, @Arg("message") String message) {
        player.sendMessage(message);
    }

    @Command("edit")
    void edit(@Arg("player") Player player, @Arg("message") String message) {
        player.sendMessage(message);
    }

}
