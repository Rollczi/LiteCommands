package dev.rollczi.litecommands.modern.argument;

import java.util.Optional;

public interface ArgumentResolverRegistry<SENDER> {

    <DETERMINANT, EXPECTED, ARGUMENT extends Argument<DETERMINANT, EXPECTED>> void registerResolver(
        IndexKey<DETERMINANT, EXPECTED, ARGUMENT> indexKey,
        ArgumentResolver<SENDER, DETERMINANT, EXPECTED, ? extends Argument<DETERMINANT, EXPECTED>> resolver
    );

    <DETERMINANT, EXPECTED, ARGUMENT extends Argument<DETERMINANT, EXPECTED>> Optional<ArgumentResolver<SENDER, DETERMINANT, EXPECTED, ARGUMENT>> getResolver(
        IndexKey<DETERMINANT, EXPECTED, ARGUMENT> indexKey
    );

    class IndexKey<DETERMINANT, EXPECTED, ARGUMENT extends Argument<DETERMINANT, EXPECTED>> {

        private final Class<DETERMINANT> determinantType;
        private final Class<EXPECTED> expectedType;
        private final Class<ARGUMENT> contextType;
        private final ArgumentKey argumentKey;

        private IndexKey(Class<DETERMINANT> determinantType, Class<EXPECTED> expectedType, Class<ARGUMENT> contextType, ArgumentKey argumentKey) {
            this.determinantType = determinantType;
            this.expectedType = expectedType;
            this.contextType = contextType;
            this.argumentKey = argumentKey;
        }

        public ArgumentKey argumentKey() {
            return this.argumentKey;
        }

        public Class<DETERMINANT> determinantType() {
            return this.determinantType;
        }

        public Class<EXPECTED> expectedType() {
            return this.expectedType;
        }

        public Class<ARGUMENT> contextType() {
            return this.contextType;
        }


        public boolean isUniversal() {
            return Argument.class.equals(this.contextType) && Object.class.equals(this.determinantType);
        }

        public static <DETERMINANT, EXPECTED, ARGUMENT extends Argument<DETERMINANT, EXPECTED>> IndexKey<DETERMINANT, EXPECTED, ARGUMENT> from(ARGUMENT context, ArgumentKey argumentKey) {
            return of(context.getDeterminantType(), context.getExpectedType(), context.getClass(), argumentKey);
        }

        public static <DETERMINANT, EXPECTED, ARGUMENT extends Argument<DETERMINANT, EXPECTED>> IndexKey<DETERMINANT, EXPECTED, ARGUMENT> from(ARGUMENT context) {
            return of(context.getDeterminantType(), context.getExpectedType(), context.getClass(), ArgumentKey.DEFAULT);
        }

        public static <EXPECTED> IndexKey<Object, EXPECTED, Argument<Object, EXPECTED>> universal(
            Class<EXPECTED> expectedType
        ) {
            return IndexKey.of(Object.class, expectedType, Argument.class, ArgumentKey.DEFAULT);
        }

        public static <EXPECTED> IndexKey<Object, EXPECTED, Argument<Object, EXPECTED>> universal(
            Class<EXPECTED> expectedType,
            ArgumentKey argumentKey
        ) {
            return IndexKey.of(Object.class, expectedType, Argument.class, argumentKey);
        }

        public static <DETERMINANT, EXPECTED, ARGUMENT extends Argument<DETERMINANT, EXPECTED>> IndexKey<DETERMINANT, EXPECTED, ARGUMENT> of(
            Class<DETERMINANT> determinantType,
            Class<EXPECTED> expectedType,
            Class<?> contextType
        ) {
            return of(determinantType, expectedType, contextType, ArgumentKey.DEFAULT);
        }

        @SuppressWarnings("unchecked")
        public static <DETERMINANT, EXPECTED, ARGUMENT extends Argument<DETERMINANT, EXPECTED>> IndexKey<DETERMINANT, EXPECTED, ARGUMENT> of(
            Class<DETERMINANT> determinantType,
            Class<EXPECTED> expectedType,
            Class<?> contextType,
            ArgumentKey argumentKey
        ) {
            return new IndexKey<>(determinantType, expectedType, (Class<ARGUMENT>) contextType, argumentKey);
        }

    }

}
