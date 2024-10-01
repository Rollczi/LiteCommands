package dev.rollczi.litecommands.annotations;

import dev.rollczi.litecommands.annotations.inject.Injector;
import dev.rollczi.litecommands.LiteCommandsInternal;
import dev.rollczi.litecommands.LiteCommandsProvider;
import dev.rollczi.litecommands.annotations.validator.method.MethodValidatorService;
import dev.rollczi.litecommands.command.builder.CommandBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class LiteCommandsAnnotations<SENDER> implements LiteCommandsProvider<SENDER> {

    private final List<Object> commandInstances;
    private final List<Class<?>> commandClasses;
    private final AnnotationProcessorService<SENDER> annotationProcessorService;
    private final MethodValidatorService<SENDER> validatorService;

    private LiteCommandsAnnotations(AnnotationProcessorService<SENDER> annotationProcessorService, MethodValidatorService<SENDER> validatorService) {
        this.annotationProcessorService = annotationProcessorService;
        this.validatorService = validatorService;
        this.commandInstances = new ArrayList<>();
        this.commandClasses = new ArrayList<>();
    }

    private LiteCommandsAnnotations() {
        this(AnnotationProcessorService.defaultService(), new MethodValidatorService<>());
    }

    public AnnotationProcessorService<SENDER> getAnnotationProcessorService() {
        return annotationProcessorService;
    }

    public MethodValidatorService<SENDER> getValidatorService() {
        return validatorService;
    }

    @Override
    public List<CommandBuilder<SENDER>> provide(LiteCommandsInternal<SENDER, ?> builder) {
        InstanceSourceProcessor<SENDER> processor = new InstanceSourceProcessor<>(annotationProcessorService, validatorService);
        Injector injector = new Injector(builder.getBindRegistry());

        List<Object> instances = new ArrayList<>(this.commandInstances);

        for (Class<?> commandClass : this.commandClasses) {
            Object instance = injector.createInstance(commandClass);

            instances.add(instance);
        }

        return instances.stream()
            .map(instance -> processor.processBuilder(new InstanceSource(instance)))
            .collect(Collectors.toList());
    }

    public LiteCommandsAnnotations<SENDER> load(Object... commands) {
        this.commandInstances.addAll(Arrays.asList(commands));
        return this;
    }

    public LiteCommandsAnnotations<SENDER> loadClasses(Class<?>... commands) {
        this.commandClasses.addAll(Arrays.asList(commands));
        return this;
    }

    public static <SENDER> LiteCommandsAnnotations<SENDER> create() {
        return new LiteCommandsAnnotations<>();
    }

    public static <SENDER> LiteCommandsAnnotations<SENDER> of(Object... commands) {
        return new LiteCommandsAnnotations<SENDER>().load(commands);
    }

    public static <SENDER> LiteCommandsAnnotations<SENDER> ofClasses(Class<?>... commands) {
        return new LiteCommandsAnnotations<SENDER>().loadClasses(commands);
    }

}