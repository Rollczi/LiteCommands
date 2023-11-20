package dev.rollczi.litecommands.annotations.shortcut;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Is used to register a shortcut for the sub command executor.
 * It has to be declared on the method annotated with {@link dev.rollczi.litecommands.annotations.execute.Execute}
 * with name specified (cannot be used with root executor).
 * Example:
 * <pre>
 *   &#64;Command(name = "team")
 *   public class TeamCommand {
 *      &#64;Execute(name = "add")
 *      &#64;Shortcut("t-add")
 *      void execute() {
 *          // ...
 *      }
 *   }
 * </pre>
 * Class above would register command <b>/team add</b> and its shortcut <b>/t-add</b>.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Shortcut {

    String[] value();

}
