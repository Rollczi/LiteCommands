package dev.rollczi.litecommands.command.count;

import dev.rollczi.litecommands.factory.FactoryAnnotationResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Set exact number of raw arguments in the command.
 * You can use this annotation to make command with no raw arguments or with exact number of raw arguments.
 * Route of command is not counted to raw arguments.
 *  <p>
 *      Example usage:
 *      <pre>{@code
 *          @Route("command")
 *          public class Command {
 *
 *              @Required(2)
 *              @Route("sub")
 *              @Execute
 *              public void command(@Args String... args) {
 *                  // ... /command sub {string[]}
 *                  // command has unlimited raw arguments
 *              }
 *         }
 *     }</pre>
 *     After execute command:
 *     <ul>
 *         <li>/command sub hello world -> valid</li>
 *         <li>/command sub -> invalid</li>
 *         <li>/command sub hello -> invalid</li>
 *         <li>/command sub hello world other -> invalid</li>
 *     </ul>
 * </p>
 *
 * @see dev.rollczi.litecommands.command.count.Min
 * @see dev.rollczi.litecommands.command.count.Max
 * @see dev.rollczi.litecommands.command.count.Range
 *
 * @author Rollczi
 * @since 2.7.0
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Required {

    int value();

    FactoryAnnotationResolver<Required> RESOLVER = FactoryAnnotationResolver.of(
            Required.class,
            (required, state) -> state.validator(validator -> validator.required(required.value()))
    );


}
