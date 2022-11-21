package dev.rollczi.litecommands.modern.argument.invocation;

import dev.rollczi.litecommands.modern.argument.ArgumentContext;
import dev.rollczi.litecommands.modern.argument.ArgumentKey;

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

        private final Class<DETERMINANT> contextType;
        private final Class<EXPECTED> expectedType;
        private final Class<CONTEXT> contextBoxType;
        private final ArgumentKey argumentKey;

        private IndexKey(Class<DETERMINANT> contextType, Class<EXPECTED> expectedType, Class<CONTEXT> contextBoxType, ArgumentKey argumentKey) {
            this.contextType = contextType;
            this.expectedType = expectedType;
            this.contextBoxType = contextBoxType;
            this.argumentKey = argumentKey;
        }

        public ArgumentKey argumentKey() {
            return argumentKey;
        }

        public Class<DETERMINANT> contextType() {
            return contextType;
        }

        public Class<EXPECTED> expectedType() {
            return expectedType;
        }

        public Class<CONTEXT> boxType() {
            return contextBoxType;
        }

        public static <DETERMINANT, EXPECTED, CONTEXT extends ArgumentContext<DETERMINANT, EXPECTED>> IndexKey<DETERMINANT, EXPECTED, CONTEXT> from(CONTEXT contextBox, ArgumentKey argumentKey) {
            return of(contextBox.getContextType(), contextBox.getReturnType(), contextBox.getClass(), argumentKey);
        }

        public static <DETERMINANT, EXPECTED, CONTEXT extends ArgumentContext<DETERMINANT, EXPECTED>> IndexKey<DETERMINANT, EXPECTED, CONTEXT> from(CONTEXT contextBox) {
            return of(contextBox.getContextType(), contextBox.getReturnType(), contextBox.getClass(), ArgumentKey.DEFAULT);
        }

        public static <DETERMINANT, EXPECTED, CONTEXT extends ArgumentContext<DETERMINANT, EXPECTED>> IndexKey<DETERMINANT, EXPECTED, CONTEXT> of(
            Class<DETERMINANT> contextType,
            Class<EXPECTED> expectedType,
            Class<?> contextBoxType
        ) {
            return of(contextType, expectedType, contextBoxType, ArgumentKey.DEFAULT);
        }

        @SuppressWarnings("unchecked")
        public static <DETERMINANT, EXPECTED, CONTEXT extends ArgumentContext<DETERMINANT, EXPECTED>> IndexKey<DETERMINANT, EXPECTED, CONTEXT> of(
            Class<DETERMINANT> contextType,
            Class<EXPECTED> expectedType,
            Class<?> contextBoxType,
            ArgumentKey argumentKey
        ) {
            return new IndexKey<>(contextType, expectedType, (Class<CONTEXT>) contextBoxType, argumentKey);
        }

    }

}
