package dev.rollczi.litecommands.annotations.cooldown;

import dev.rollczi.litecommands.annotations.AnnotationInvoker;
import dev.rollczi.litecommands.annotations.AnnotationProcessor;
import dev.rollczi.litecommands.cooldown.CooldownContext;
import dev.rollczi.litecommands.meta.Meta;
import java.time.Duration;

public class CooldownAnnotationResolver<SENDER> implements AnnotationProcessor<SENDER> {

    @Override
    public AnnotationInvoker<SENDER> process(AnnotationInvoker<SENDER> invoker) {
        return invoker
            .on(Cooldown.class, ((annotation, metaHolder) -> metaHolder.meta()
                .put(Meta.COOLDOWN, getCooldownContext(annotation))));
    }

    private CooldownContext getCooldownContext(Cooldown cooldown) {
        return new CooldownContext(cooldown.key(), Duration.of(cooldown.count(), cooldown.unit()));
    }

}
