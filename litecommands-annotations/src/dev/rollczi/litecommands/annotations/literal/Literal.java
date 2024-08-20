package dev.rollczi.litecommands.annotations.literal;

import dev.rollczi.litecommands.annotations.intellij.IntelliJArgumentInput;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.jetbrains.annotations.ApiStatus;

@IntelliJArgumentInput(format = "%s", modification = IntelliJArgumentInput.ModificationType.APPEND)
@ApiStatus.Internal
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface Literal {

    String value();

}
