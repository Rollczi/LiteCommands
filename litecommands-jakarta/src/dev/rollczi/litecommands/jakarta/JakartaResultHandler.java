package dev.rollczi.litecommands.jakarta;

import dev.rollczi.litecommands.handler.result.ResultHandler;
import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invocation.Invocation;
import jakarta.validation.ConstraintViolation;

class JakartaResultHandler<SENDER> implements ResultHandler<SENDER, JakartaResult> {

    @Override
    public void handle(Invocation<SENDER> invocation, JakartaResult result, ResultHandlerChain<SENDER> chain) {
        for (ConstraintViolation<Object> violation : result.getViolations()) {
            chain.resolve(invocation, violation.getMessage());
        }
    }

}
