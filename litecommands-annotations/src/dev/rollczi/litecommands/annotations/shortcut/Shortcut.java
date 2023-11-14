package dev.rollczi.litecommands.annotations.shortcut;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Is used to register a shortcut for the sub command executor.
 * It has to be declared on the method annotated with {@link dev.rollczi.litecommands.annotations.execute.Execute}
 * with name specified (cannot be used with root executor).
 * <p>
 * Example:
 * <pre>
 * &#64;Command(name = "base")
 * public static class TestCommand {
 *
 *  &#64;Execute(name = "sub") // /base sub
 *  <b>&#64;Shortcut(name = "short", aliases = "shortcut")</b> // /short
 *  void executeSub()
 *      ...
 *  }
 *
 * }
 * </pre>
 * Class above would register command <b>/base sub</b> and its shortcut <b>/short</b> or <b>/shortcut</b>.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Shortcut {

    String[] value();

}
