package dev.rollczi.litecommands.command.count;

import dev.rollczi.litecommands.factory.FactoryAnnotationResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Set the range of raw arguments in the command.
 * Route of command is not counted to raw arguments.
 *  <p>
 *      Example usage:
 *      <pre>{@code
 *          @Route("command")
 *          public class Command {
 *
 *              @Range(min = 2, max = 3)
 *              @Route("sub")
 *              @Execute
 *              public void command(@Arg Instant time) {
 *                  // ... /command sub {yyyy-MM-dd} {HH:mm:ss}
 *                  // command has 2 raw arguments
 *              }
 *          }
 *      }</pre>
 *
 *      After execute command:
 *      <ul>
 *          <li>/command sub 2021-01-01 22:00:00 -> valid</li>
 *          <li>/command sub 2021-01-01 22:00:00 1 -> valid</li>
 *          <li>/command sub 2021-01-01 22:00:00 1 bad -> invalid</li>
 *          <li>/command sub 2021-01-01 -> invalid and missing {HH:mm:ss}</li>
 *      </ul>
 * </p>
 *
 * @see dev.rollczi.litecommands.command.count.Min
 * @see dev.rollczi.litecommands.command.count.Max
 * @see dev.rollczi.litecommands.command.count.Required
 *
 * @author Rollczi
 * @since 2.7.0
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Range {

    int min();

    int max();

    FactoryAnnotationResolver<Range> RESOLVER = FactoryAnnotationResolver.of(
            Range.class,
            (range, state) -> state.validator(validator -> validator
                    .min(range.min())
                    .max(range.max())
            )
    );

}
