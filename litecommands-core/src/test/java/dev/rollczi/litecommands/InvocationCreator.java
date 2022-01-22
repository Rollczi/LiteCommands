package dev.rollczi.litecommands;

import dev.rollczi.litecommands.component.LiteComponent;
import dev.rollczi.litecommands.platform.LiteSender;

public interface InvocationCreator {

     LiteComponent.ContextOfResolving get(LiteSender liteSender);

}
