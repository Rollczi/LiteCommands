package dev.rollczi.litecommands;

import dev.rollczi.litecommands.component.LiteComponent;

public interface InvocationCreator {

     LiteComponent.ContextOfResolving get(LiteSender liteSender);

}
