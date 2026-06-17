package dev.rollczi.example.bukkit.jakarta.command;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import org.bukkit.command.CommandSender;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Negative;
import jakarta.validation.constraints.NegativeOrZero;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.Instant;
import java.util.List;

@Command(name = "jakarta")
public class JakartaCommand {

    @Execute(name = "age")
    String age(@Min(1) @Max(120) @Arg int age) {
        return "Your age is " + age;
    }

    @Execute(name = "name")
    String name(@NotBlank @Size(min = 3, max = 16) @Arg String name) {
        return "Your name is " + name;
    }

    @Execute(name = "boolean-true")
    String booleanTrue(@AssertTrue @Arg boolean value) {
        return "Value is " + value;
    }

    @Execute(name = "boolean-false")
    String booleanFalse(@AssertFalse @Arg boolean value) {
        return "Value is " + value;
    }

    @Execute(name = "positive")
    String positive(@Positive @Arg int value) {
        return "Positive value: " + value;
    }

    @Execute(name = "positive-or-zero")
    String positiveOrZero(@PositiveOrZero @Arg int value) {
        return "Positive or zero value: " + value;
    }

    @Execute(name = "negative")
    String negative(@Negative @Arg int value) {
        return "Negative value: " + value;
    }

    @Execute(name = "negative-or-zero")
    String negativeOrZero(@NegativeOrZero @Arg int value) {
        return "Negative or zero value: " + value;
    }

    @Execute(name = "digits")
    String digits(@Digits(integer = 3, fraction = 2) @Arg double value) {
        return "Digits: " + value;
    }

    @Execute(name = "decimal")
    String decimal(@DecimalMin("0.1") @DecimalMax("1.0") @Arg double value) {
        return "Decimal value: " + value;
    }

    @Execute(name = "past")
    String past(@Past @Arg Instant instant) {
        return "Past instant: " + instant;
    }

    @Execute(name = "past-or-present")
    String pastOrPresent(@PastOrPresent @Arg Instant instant) {
        return "Past or present instant: " + instant;
    }

    @Execute(name = "future")
    String future(@Future @Arg Instant instant) {
        return "Future instant: " + instant;
    }

    @Execute(name = "future-or-present")
    String futureOrPresent(@FutureOrPresent @Arg Instant instant) {
        return "Future or present instant: " + instant;
    }

    @Execute(name = "pattern")
    String pattern(@Pattern(regexp = "[a-z]+") @Arg String text) {
        return "Pattern match: " + text;
    }

    @Execute(name = "email")
    String email(@Email @Arg String email) {
        return "Email: " + email;
    }

    @Execute(name = "list")
    String list(@NotEmpty @Size(min = 2) @Arg List<String> list) {
        return "List size: " + list.size();
    }

    @Execute(name = "user-info")
    String userInfo(@NotBlank @Size(min = 3) @Arg String name, @Min(1) @Max(100) @Arg int level) {
        return "User: " + name + ", Level: " + level;
    }

    @Execute(name = "transfer")
    String transfer(@NotBlank @Arg String from, @NotBlank @Arg String to, @Positive @Arg double amount) {
        return "Transferred " + amount + " from " + from + " to " + to;
    }

    @Execute(name = "user")
    String user(@Valid @Arg User user) {
        return "User: " + user.name + " (" + user.age + ")";
    }

    public record User(@NotBlank String name, @Min(18) int age) {

        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }

    public static class UserArgumentResolver extends ArgumentResolver<CommandSender, User> {

        @Override
        protected ParseResult<User> parse(Invocation<CommandSender> invocation, Argument<User> context, String argument) {
            String[] split = argument.split(":", 2);

            if (split.length < 2) {
                return ParseResult.failure("Invalid user format. Use name:age");
            }

            try {
                String name = split[0];
                int age = Integer.parseInt(split[1]);

                return ParseResult.success(new User(name, age));
            } catch (NumberFormatException e) {
                return ParseResult.failure("Age must be a number.");
            }
        }

        @Override
        public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<User> argument, SuggestionContext context) {
            return SuggestionResult.of("name:18");
        }

    }

    // Custom annotations and custom validators
    @Execute(name = "custom")
    private String custom(@NotAdmin @Arg String name) {
        return "Hello, " + name;
    }

    @Target({ ElementType.PARAMETER, ElementType.FIELD })
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = NotAdminValidator.class)
    @Documented
    private @interface NotAdmin {
        String message() default "Name cannot be 'admin'";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }

    private static class NotAdminValidator implements ConstraintValidator<NotAdmin, String> {
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return value == null || !value.equalsIgnoreCase("admin");
        }
    }

}
