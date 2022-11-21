package dev.rollczi.litecommands.command.count;

import dev.rollczi.litecommands.factory.FactoryAnnotationResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Set the maximum number of raw arguments in the command.
 * Route of command is not counted to raw arguments.
 *  <p>
 *      Example usage:
 *      <pre>{@code
 *          @Route("command")
 *          public class Command {
 *
 *              @Max(4)
 *              @Route("sub")
 *              @Execute
 *              public void command(@Arg String text, @Arg Instant time, @Arg int number) {
 *                  // ... /command sub {string} {yyyy-MM-dd} {HH:mm:ss} {int}
 *                  // command has 4 raw arguments
 *              }
 *
 *          }
 *      }</pre>
 *      After execute command:
 *      <ul>
 *          <li>/command sub text 2021-01-01 22:00:00 1 -> valid</li>
 *          <li>/command sub text 2021-01-01 22:00:00 1 other -> invalid</li>
 *          <li>/command sub text 2021-01-01 22:00:00 -> missing {int}</li>
 *          <li>/command sub text 2021-01-01 -> missing {HH:mm:ss} and {int}</li>
 *      </ul>
 *  </p>
 *
 * @see dev.rollczi.litecommands.command.count.Min
 * @see dev.rollczi.litecommands.command.count.Range
 * @see dev.rollczi.litecommands.command.count.Required
 *
 * @author Rollczi
 * @since 2.7.0
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Max {

    int value();

    FactoryAnnotationResolver<Max> RESOLVER = FactoryAnnotationResolver.of(Max.class, (max, state) -> state.validator(validator -> validator.max(max.value())));

}
