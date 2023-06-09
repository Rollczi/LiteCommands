package dev.rollczi.litecommands.jda;

import dev.rollczi.litecommands.result.ResultHandler;
import dev.rollczi.litecommands.invocation.Invocation;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.RestAction;

class RestActionHandler implements ResultHandler<User, RestAction> {

    @Override
    public void handle(Invocation<User> invocation, RestAction result) {
        result.queue();
    }

}
