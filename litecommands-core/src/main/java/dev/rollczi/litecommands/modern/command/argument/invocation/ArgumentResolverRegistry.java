package dev.rollczi.litecommands.modern.command.argument.invocation;

import dev.rollczi.litecommands.modern.command.argument.ArgumentContext;
import dev.rollczi.litecommands.modern.command.argument.ArgumentKey;

import java.util.Optional;

public interface ArgumentResolverRegistry<SENDER> {

    <DETERMINANT, EXPECTED, CONTEXT extends ArgumentContext<DETERMINANT, EXPECTED>> void registerResolver(
        IndexKey<DETERMINANT, EXPECTED, CONTEXT> indexKey,
        ArgumentResolver<SENDER, DETERMINANT, EXPECTED, ? extends ArgumentContext<DETERMINANT, EXPECTED>> resolver
    );

    <DETERMINANT, EXPECTED, CONTEXT extends ArgumentContext<DETERMINANT, EXPECTED>> Optional<ArgumentResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT>> getResolver(
        IndexKey<DETERMINANT, EXPECTED, CONTEXT> indexKey
    );

    class IndexKey<DETERMINANT, EXPECTED, CONTEXT extends ArgumentContext<DETERMINANT, EXPECTED>> {

        private final Class<DETERMINANT> determinantType;
        private final Class<EXPECTED> expectedType;
        private final Class<CONTEXT> contextType;
        private final ArgumentKey argumentKey;

        private IndexKey(Class<DETERMINANT> determinantType, Class<EXPECTED> expectedType, Class<CONTEXT> contextType, ArgumentKey argumentKey) {
            this.determinantType = determinantType;
            this.expectedType = expectedType;
            this.contextType = contextType;
            this.argumentKey = argumentKey;
        }

        public ArgumentKey argumentKey() {
            return argumentKey;
        }

        public Class<DETERMINANT> determinantType() {
            return determinantType;
        }

        public Class<EXPECTED> expectedType() {
            return expectedType;
        }

        public Class<CONTEXT> contextType() {
            return contextType;
        }

        public static <DETERMINANT, EXPECTED, CONTEXT extends ArgumentContext<DETERMINANT, EXPECTED>> IndexKey<DETERMINANT, EXPECTED, CONTEXT> from(CONTEXT contextBox, ArgumentKey argumentKey) {
            return of(contextBox.getDeterminantType(), contextBox.getExpectedType(), contextBox.getClass(), argumentKey);
        }

        public static <DETERMINANT, EXPECTED, CONTEXT extends ArgumentContext<DETERMINANT, EXPECTED>> IndexKey<DETERMINANT, EXPECTED, CONTEXT> from(CONTEXT contextBox) {
            return of(contextBox.getDeterminantType(), contextBox.getExpectedType(), contextBox.getClass(), ArgumentKey.DEFAULT);
        }

        public static <DETERMINANT, EXPECTED, CONTEXT extends ArgumentContext<DETERMINANT, EXPECTED>> IndexKey<DETERMINANT, EXPECTED, CONTEXT> of(
            Class<DETERMINANT> determinantType,
            Class<EXPECTED> expectedType,
            Class<?> contextType
        ) {
            return of(determinantType, expectedType, contextType, ArgumentKey.DEFAULT);
        }

        @SuppressWarnings("unchecked")
        public static <DETERMINANT, EXPECTED, CONTEXT extends ArgumentContext<DETERMINANT, EXPECTED>> IndexKey<DETERMINANT, EXPECTED, CONTEXT> of(
            Class<DETERMINANT> determinantType,
            Class<EXPECTED> expectedType,
            Class<?> contextType,
            ArgumentKey argumentKey
        ) {
            return new IndexKey<>(determinantType, expectedType, (Class<CONTEXT>) contextType, argumentKey);
        }

    }

}
