package dev.rollczi.litecommands.modern.annotation.command;

import dev.rollczi.litecommands.modern.command.CommandExecutor;

import java.lang.reflect.Method;
import java.util.List;

public class MethodCommandExecutorFactory<SENDER> {

    private final ParameterPreparedArgumentFactory<SENDER> parameterPreparedArgumentFactory;

    public MethodCommandExecutorFactory(ParameterPreparedArgumentFactory<SENDER> parameterPreparedArgumentFactory) {
        this.parameterPreparedArgumentFactory = parameterPreparedArgumentFactory;
    }

    public CommandExecutor<SENDER> create(Object instance, Method method) {
        List<ParameterPreparedArgument<SENDER, ?>> arguments = parameterPreparedArgumentFactory.resolve(method);

        return new MethodCommandExecutor<>(method, instance, arguments);
    }

}
