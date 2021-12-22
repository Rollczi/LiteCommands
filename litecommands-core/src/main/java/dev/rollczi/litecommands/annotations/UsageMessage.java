package dev.rollczi.litecommands.annotations;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.valid.ValidationInfo;
import dev.rollczi.litecommands.valid.messages.LiteMessage;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * See: {@link LiteCommandsBuilder#message(ValidationInfo, LiteMessage)}
 */

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UsageMessage {

    String value();

}
