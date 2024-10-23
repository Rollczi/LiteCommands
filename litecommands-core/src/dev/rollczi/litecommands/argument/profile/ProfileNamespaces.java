package dev.rollczi.litecommands.argument.profile;

import dev.rollczi.litecommands.argument.resolver.collector.VarargsProfile;
import dev.rollczi.litecommands.argument.resolver.nullable.NullableProfile;
import dev.rollczi.litecommands.flag.FlagProfile;
import dev.rollczi.litecommands.join.JoinProfile;
import dev.rollczi.litecommands.literal.LiteralProfile;
import dev.rollczi.litecommands.quoted.QuotedProfile;
import org.jetbrains.annotations.ApiStatus;

/**
 * Contains default argument profiles defined by LiteCommands-core.
 * @since 3.7.0
 */
@ApiStatus.Experimental
public final class ProfileNamespaces {

    private ProfileNamespaces() {
    }

    /**
     * Literal profile allows
     * parsing static values such as {@code /user Rollczi group Admin} where {@code group} is a literal.
     * @since 3.8.0
     */
    public static final ArgumentProfileNamespace<LiteralProfile> LITERAL = LiteralProfile.NAMESPACE;

    /**
     * Join profile allows joining multiple arguments into one.
     * @since 3.7.0
     */
    public static final ArgumentProfileNamespace<JoinProfile> JOIN = JoinProfile.NAMESPACE;

    /**
     * Nullable profile allows passing optional arguments. If the argument is not present, the value will be null.
     * @since 3.7.0
     */
    public static final ArgumentProfileNamespace<NullableProfile> NULLABLE = NullableProfile.NAMESPACE;

    /**
     * Varargs profile allows passing multiple arguments of the same type. e.g. {@code /command arg1,arg2,arg3}.
     * The arguments are separated by a delimiter specified in the profile.
     * @since 3.7.0
     */
    public static final ArgumentProfileNamespace<VarargsProfile> VARARGS = VarargsProfile.NAMESPACE;

    /**
     * Quoted profile allows passing arguments with spaces. e.g. {@code /command "argument with spaces"}.
     * @since 3.7.0
     */
    public static final ArgumentProfileNamespace<QuotedProfile> QUOTED = QuotedProfile.NAMESPACE;

    /**
     * Flag profile allows passing boolean flags. e.g. {@code /command --flag}.
     * @since 3.7.0
     */
    public static final ArgumentProfileNamespace<FlagProfile> FLAG = FlagProfile.NAMESPACE;

}
