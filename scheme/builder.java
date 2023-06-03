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

            .commands(LiteAnnotations.create()
                .load(
                    new TestCommand(),
                    new TestCommand(),
                    new TestCommand(),
                    new TestCommand()
                )
                .loadClasses(
                    TestCommand.class,
                    TestCommand.class,
                    TestCommand.class
                )
                .loadPackage("dev.rollczi.litecommands.test.commands")
            )

            .commands(LiteAnnotations.load(
                new TestCommand(),
                new TestCommand(),
                new TestCommand(),
                new TestCommand()
            ))

            .commands(LiteAnnotations.loadClasses(
                TestCommand.class,
                TestCommand.class,
                TestCommand.class
            ))

            .commands(LiteAnnotations.loadPackage("dev.rollczi.litecommands.test.commands"))

            .suggesters(suggester -> suggester
                .suggest("test", new TestSuggester())
                .suggest(TestCommand.class, new CommandSuggester())
            )

            .arguments(arguments -> arguments
                .argument(Player.class, new PlayerArgument())
                .argument(String.class, new StringArgument())
            )

            .contexts(contexts -> contexts
                .context(Player.class, new PlayerContext())
                .context(String.class, new StringContext())
            )

            .binds(binds -> binds
                .bind(SomeSrvice.class, () -> new SomeService())
            )

            .editor(editor -> editor
                .editGlobal(new GlobalEditor())
                .edit("test", new TestEditor())
                .edit(TestCommand.class, new CommandEditor())
            )

            .validator(validator -> validator
                .validGlobal(new GlobalValidator())
                .valid("test", new TestValidator())
                .valid(TestCommand.class, new CommandValidator())
            )

            .resultHandler(resultHandler -> resultHandler
                .sucessful(String.class, new StringHandler())
                .sucessful(Component.class, new ComponentHandler())
                .failure(Throwable.class, new ThrowableHandler())
                .failure(InvalidUsage.class, new InvalidUsageHandler())
                .unexpected(new UnexpectedHandler())
            )

            .permission(permission -> permission
                .validator(new PermissionValidator())
                .missing(new MissingPermissionHandler())
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

            .preprocessor(new Preprocessor())
            .postprocessor(new Postprocessor())

            .build();

        liteCommands.register();
        liteCommands.unregister();
    }
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
