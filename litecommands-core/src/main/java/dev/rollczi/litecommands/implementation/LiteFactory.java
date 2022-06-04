package dev.rollczi.litecommands.implementation;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.argument.block.Block;
import dev.rollczi.litecommands.argument.block.BlockArgument;
import dev.rollczi.litecommands.argument.flag.Flag;
import dev.rollczi.litecommands.argument.flag.FlagArgument;
import dev.rollczi.litecommands.argument.joiner.Joiner;
import dev.rollczi.litecommands.argument.joiner.JoinerArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.amount.Between;
import dev.rollczi.litecommands.command.amount.Max;
import dev.rollczi.litecommands.command.amount.Min;
import dev.rollczi.litecommands.command.amount.Required;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.ExecutedPermission;
import dev.rollczi.litecommands.command.permission.ExecutedPermissions;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.permission.PermissionUtils;
import dev.rollczi.litecommands.command.permission.Permissions;
import dev.rollczi.litecommands.command.section.Section;
import dev.rollczi.litecommands.handle.LiteException;
import dev.rollczi.litecommands.platform.LiteSender;
import panda.std.Blank;
import panda.std.Option;
import panda.std.Result;

public final class LiteFactory {

    private LiteFactory() {
    }

    public static <SENDER> LiteCommandsBuilder<SENDER> builder(Class<SENDER> senderType) {
        return LiteCommandsBuilderImpl.<SENDER>builder()
                .configureFactory(factory -> {
                    factory.annotationResolver(Section.RESOLVER);
                    factory.annotationResolver(Execute.RESOLVER);
                    factory.annotationResolver(Permission.RESOLVER);
                    factory.annotationResolver(Permissions.RESOLVER);
                    factory.annotationResolver(PermissionUtils.EXECUTED_PERMISSION_RESOLVER);
                    factory.annotationResolver(PermissionUtils.EXECUTED_PERMISSIONS_RESOLVER);
                    factory.annotationResolver(Min.RESOLVER);
                    factory.annotationResolver(Max.RESOLVER);
                    factory.annotationResolver(Required.RESOLVER);
                    factory.annotationResolver(Between.RESOLVER);
                })
                .argument(Flag.class, boolean.class, new FlagArgument())
                .argument(Joiner.class, String.class, new JoinerArgument())
                .argument(Block.class, Object.class, new BlockArgument())

                .argument(String.class, (invocation, argument) -> Result.ok(argument))
                .argument(Integer.class, (invocation, argument) -> Option.attempt(NumberFormatException.class, () -> Integer.parseInt(argument)).toResult(Blank.BLANK))
                .argument(Double.class, (invocation, argument) -> Option.attempt(NumberFormatException.class, () -> Double.parseDouble(argument)).toResult(Blank.BLANK))
                .argument(Float.class, (invocation, argument) -> Option.attempt(NumberFormatException.class, () -> Float.parseFloat(argument)).toResult(Blank.BLANK))

                .contextualBind(LiteInvocation.class, (sender, invocation) -> Result.ok(invocation))
                .contextualBind(LiteSender.class, (sender, invocation) -> Result.ok(invocation.sender()))
                .contextualBind(String[].class, (sender, invocation) -> Result.ok(invocation.arguments()))
                .contextualBind(senderType, (sender, invocation) -> Result.ok(sender));
    }

}
