package dev.rollczi.example.bukkit.user;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import java.util.regex.Pattern;
import org.bukkit.command.CommandSender;

public class UserArgumentResolver extends ArgumentResolver<CommandSender, User> {

    private final Pattern VALID_USER_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{3,16}$");
    private final UserService userService;

    public UserArgumentResolver(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected ParseResult<User> parse(Invocation<CommandSender> invocation, Argument<User> context, String argument) {
        return ParseResult.completableFuture(userService.getUser(argument), user -> {
            if (user == null) {
                return ParseResult.failure("User not found.");
            }

            return ParseResult.success(user);
        });
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<User> argument, SuggestionContext context) {
        return userService.getCachedUsers().stream().map(user -> user.getName())
            .collect(SuggestionResult.collector());
    }

    @Override
    public boolean match(Invocation<CommandSender> invocation, Argument<User> context, String argument) {
        return VALID_USER_PATTERN.matcher(argument).matches();
    }

}
