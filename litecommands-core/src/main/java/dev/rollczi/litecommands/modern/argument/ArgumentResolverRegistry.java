package dev.rollczi.litecommands.modern.argument;

import org.jetbrains.annotations.ApiStatus;

import java.util.Optional;

public interface ArgumentResolverRegistry<SENDER> {

    <EXPECTED, ARGUMENT extends Argument<EXPECTED>> void registerResolver(
        IndexKey<EXPECTED, ARGUMENT> indexKey,
        ArgumentParser<SENDER, EXPECTED, ? extends Argument<EXPECTED>> resolver
    );

    <EXPECTED> Optional<ArgumentParser<SENDER, EXPECTED, Argument<EXPECTED>>> getResolver(
        IndexKey<EXPECTED, Argument<EXPECTED>> indexKey
    );

    class IndexKey<EXPECTED, ARGUMENT extends Argument<EXPECTED>> {

        private final Class<EXPECTED> expectedType;
        private final Class<ARGUMENT> argumentType;
        private final ArgumentKey argumentKey;

        @SuppressWarnings("unchecked")
        private IndexKey(Class<EXPECTED> expectedType, Class<?> argumentType, ArgumentKey argumentKey) {
            this.expectedType = expectedType;
            this.argumentType = (Class<ARGUMENT>) argumentType;
            this.argumentKey = argumentKey;
        }

        public ArgumentKey argumentKey() {
            return this.argumentKey;
        }
        public Class<EXPECTED> expectedType() {
            return this.expectedType;
        }

        public Class<ARGUMENT> argumentType() {
            return this.argumentType;
        }


        public boolean isUniversal() {
            return Argument.class.equals(this.argumentType);
        }

        @ApiStatus.Internal
        public static <EXPECTED, ARGUMENT extends Argument<EXPECTED>> IndexKey<EXPECTED, ARGUMENT> from(ARGUMENT argument) {
            return new IndexKey<>(argument.getWrapperFormat().getType(), argument.getClass(), ArgumentKey.universal());
        }

        @ApiStatus.Internal
        public static <EXPECTED, ARGUMENT extends Argument<EXPECTED>> IndexKey<EXPECTED, ARGUMENT> from(ARGUMENT argument, String customKey) {
            return new IndexKey<>(argument.getWrapperFormat().getType(), argument.getClass(), ArgumentKey.universal(customKey));
        }

        public static <EXPECTED> IndexKey<EXPECTED, Argument<EXPECTED>> universal(Class<EXPECTED> expectedType) {
            return new IndexKey<>(expectedType, Argument.class, ArgumentKey.universal());
        }

        public static <EXPECTED> IndexKey<EXPECTED, Argument<EXPECTED>> universal(Class<EXPECTED> expectedType, String customKey) {
            return new IndexKey<>(expectedType, Argument.class, ArgumentKey.universal(customKey));
        }

        public static <EXPECTED, ARGUMENT extends Argument<EXPECTED>> IndexKey<EXPECTED, ARGUMENT> of(
            Class<EXPECTED> expectedType,
            Class<ARGUMENT> argumentType
        ) {
            return new IndexKey<>(expectedType, argumentType, ArgumentKey.key(argumentType));
        }

        public static <EXPECTED, ARGUMENT extends Argument<EXPECTED>> IndexKey<EXPECTED, ARGUMENT> of(
            Class<EXPECTED> expectedType,
            Class<ARGUMENT> contextType,
            String customKey
        ) {
            return new IndexKey<>(expectedType, contextType, ArgumentKey.key(contextType, customKey));
        }

    }

}
