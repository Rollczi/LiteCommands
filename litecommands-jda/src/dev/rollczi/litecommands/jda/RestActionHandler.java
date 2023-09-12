package dev.rollczi.litecommands.jda;

import dev.rollczi.litecommands.handler.result.ResultHandler;
import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invocation.Invocation;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.RestAction;

@SuppressWarnings("rawtypes")
class RestActionHandler implements ResultHandler<User, RestAction> {

    @Override
    public void handle(Invocation<User> invocation, RestAction result, ResultHandlerChain<User> chain) {
        result.queue();
    }

}
