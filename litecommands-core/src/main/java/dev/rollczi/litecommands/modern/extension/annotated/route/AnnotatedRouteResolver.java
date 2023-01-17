package dev.rollczi.litecommands.modern.extension.annotated.route;

import dev.rollczi.litecommands.modern.command.editor.CommandEditorContextStructurePiece;

import java.lang.annotation.Annotation;

public interface AnnotatedRouteResolver {

    void resolve(Annotation annotation, CommandEditorContextStructurePiece context);

}
