package dev.rollczi.litecommands.argument.parser;

class NamespacedParserSet<SENDER, T> extends ParserSetImpl<SENDER, T> {

    private final String namespace;

    NamespacedParserSet(String namespace) {
        this.namespace = namespace;
    }

    String getNamespace() {
        return namespace;
    }

}
