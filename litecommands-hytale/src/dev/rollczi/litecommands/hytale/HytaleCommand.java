package dev.rollczi.litecommands.hytale;

import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.hytale.stubs.HytaleSource;
import dev.rollczi.litecommands.permission.PermissionService;
import dev.rollczi.litecommands.platform.PlatformInvocationListener;
import dev.rollczi.litecommands.platform.PlatformSuggestionListener;

class HytaleCommand implements CommandBase {

    private final LiteHytaleSettings settings;
    private final CommandRoute<HytaleSource> commandSection;
    private final PermissionService permissionService;
    private final PlatformInvocationListener<HytaleSource> executeListener;
    private final PlatformSuggestionListener<HytaleSource> suggestionListener;

    public HytaleCommand(
        LiteHytaleSettings settings,
        CommandRoute<HytaleSource> command,
        PermissionService permissionService,
        PlatformInvocationListener<HytaleSource> executeListener,
        PlatformSuggestionListener<HytaleSource> suggestionListener
    ) {
        this.settings = settings;
        this.commandSection = command;
        this.permissionService = permissionService;
        this.executeListener = executeListener;
        this.suggestionListener = suggestionListener;
    }

    @Override
    protected void executeSync(@Nonnull CommandContext commandContext) {
        //FIXME: What are and how to process OptionalArg<T>?
        //ParseableInput<?> raw = ParseableInput.raw(invocation.arguments());
        //this.executeListener.execute(this.newInvocation(invocation, raw), raw);
    }

    /*
    // FIXME: How and Where to check permissions?
    @Override
    public boolean hasPermission(Invocation invocation) {
        boolean isNative = commandSection.meta().get(Meta.NATIVE_PERMISSIONS);

        if (isNative || settings.isNativePermissions()) {
            return permissionService.isPermitted(new VelocitySender(invocation.source()), this.commandSection);
        }

        return true;
    }
    */

    /*
    @Override
    public CompletableFuture<List<String>> suggestAsync(Invocation invocation) {
        List<String> arguments = new ArrayList<>(Arrays.asList(invocation.arguments()));

        if (arguments.isEmpty()) {
            arguments.add(StringUtil.EMPTY);
        }

        SuggestionInput<?> input = SuggestionInput.raw(arguments.toArray(new String[0]));

        return CompletableFuture.supplyAsync(() -> this.suggestionListener.suggest(this.newInvocation(invocation, input), input)
            .asMultiLevelList());
    }
    */

    /*
    // FIXME: How to create LiteCommands Invocations?
    private dev.rollczi.litecommands.invocation.Invocation<HytaleSource> newInvocation(Invocation invocation, Input<?> input) {
        return new dev.rollczi.litecommands.invocation.Invocation<>(
            new HytaleSender(invocation.source()),
            this.commandSection.getName(),
            invocation.alias(),
            input
        );
    }
     */

}
