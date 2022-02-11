package dev.rollczi.litecommands.bind;

import dev.rollczi.litecommands.annotations.parser.AnnotationParser;
import org.panda_lang.utilities.inject.Resources;

public interface NativeBind {

    void bind(AnnotationParser annotationParser, Resources resources);

}
