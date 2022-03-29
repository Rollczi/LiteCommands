package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.LiteInvocation;
import dev.rollczi.litecommands.component.LiteComponent;
import dev.rollczi.litecommands.valid.ValidationCommandException;

import java.util.List;

public interface SingleArgumentHandler<T> extends ArgumentHandler<T> {

    @Override
    default T parse(LiteComponent.ContextOfResolving context, int rawIndex) throws ValidationCommandException {
        LiteInvocation invocation = context.getInvocation();
        int index = context.getArgsMargin() + rawIndex;
        String[] arguments = invocation.arguments();

        if (arguments.length <= index) {
            throw new IllegalStateException("Missing arguments! Did you add @Required/@MinArgs/@MaxArgs/@Between annotations correctly before the execute method?");
        }

        return parse(invocation, arguments[index]);
    }

    T parse(LiteInvocation invocation, String argument) throws ValidationCommandException;

    @Override
    default List<String> tabulation(LiteComponent.ContextOfResolving context) {
        LiteInvocation invocation = context.getInvocation();

        return tabulation(invocation, invocation.alias(), invocation.arguments());
    }

    List<String> tabulation(LiteInvocation invocation, String command, String[] args);

    default OptionArgumentHandler<T> toOptionHandler() {
        return new OptionArgumentHandler<T>() {
            @Override
            public T parseIfPresent(LiteInvocation invocation, String argument) throws ValidationCommandException {
                return SingleArgumentHandler.this.parse(invocation, argument);
            }

            @Override
            public List<String> tabulation(LiteInvocation invocation, String command, String[] args) {
                return SingleArgumentHandler.this.tabulation(invocation, command, args);
            }

            @Override
            public Class<? extends ArgumentHandler> getNativeClass() {
                return SingleArgumentHandler.this.getNativeClass();
            }

            @Override
            public String getName() {
                return SingleArgumentHandler.this.getName();
            }
        };
    }

}
