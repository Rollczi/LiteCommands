package dev.rollczi.litecommands.annotations;

import dev.rollczi.litecommands.inject.Injector;
import dev.rollczi.litecommands.builder.LiteCommandsInternalBuilderApi;
import dev.rollczi.litecommands.builder.LiteCommandsProvider;
import dev.rollczi.litecommands.command.builder.CommandBuilder;
import dev.rollczi.litecommands.annotation.processor.AnnotationProcessorService;
import dev.rollczi.litecommands.wrapper.WrapperRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class LiteAnnotationCommands<SENDER> implements LiteCommandsProvider<SENDER> {

    private final List<Object> commandInstances;
    private final List<Class<?>> commandClasses;

    private LiteAnnotationCommands() {
        this.commandInstances = new ArrayList<>();
        this.commandClasses = new ArrayList<>();
    }

    @Override
    public List<CommandBuilder<SENDER>> provide(LiteCommandsInternalBuilderApi<SENDER, ?> builder) {
        WrapperRegistry wrapperRegistry = builder.getWrapperRegistry();
        AnnotationProcessorService<SENDER> annotationProcessorService = builder.getAnnotationProcessorRegistry();

        InstanceSourceProcessor<SENDER> processor = new InstanceSourceProcessor<>(annotationProcessorService, wrapperRegistry);
        Injector<SENDER> injector = new Injector<>(builder.getBindRegistry());

        List<Object> instances = new ArrayList<>(this.commandInstances);

        for (Class<?> commandClass : this.commandClasses) {
            Object instance = injector.createInstance(commandClass);

            instances.add(instance);
        }

        return instances.stream()
            .map(instance -> processor.processBuilder(new InstanceSource(instance)))
            .map(senderCommandBuilder -> builder.getEditorService().edit(senderCommandBuilder))
            .collect(Collectors.toList());
    }

    public LiteAnnotationCommands<SENDER> load(Object... commands) {
        this.commandInstances.addAll(Arrays.asList(commands));
        return this;
    }

    public LiteAnnotationCommands<SENDER> loadClasses(Class<?>... commands) {
        this.commandClasses.addAll(Arrays.asList(commands));
        return this;
    }

    public LiteAnnotationCommands<SENDER> loadPackages(String... packageNames) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public LiteAnnotationCommands<SENDER> loadPackages(Package... packages) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public static <SENDER> LiteAnnotationCommands<SENDER> create() {
        return new LiteAnnotationCommands<>();
    }

    public static <SENDER> LiteAnnotationCommands<SENDER> of(Object... commands) {
        return new LiteAnnotationCommands<SENDER>().load(commands);
    }

    public static <SENDER> LiteAnnotationCommands<SENDER> ofClasses(Class<?>... commands) {
        return new LiteAnnotationCommands<SENDER>().loadClasses(commands);
    }

    public static <SENDER> LiteAnnotationCommands<SENDER> ofPackages(String... packageNames) {
        return new LiteAnnotationCommands<SENDER>().loadPackages(packageNames);
    }

    public static <SENDER> LiteAnnotationCommands<SENDER> ofPackages(Package... packages) {
        return new LiteAnnotationCommands<SENDER>().loadPackages(packages);
    }

}