package dev.rollczi.litecommands.annotations.parser;

import dev.rollczi.litecommands.annotations.Between;
import dev.rollczi.litecommands.annotations.IgnoreClass;
import dev.rollczi.litecommands.annotations.MaxArgs;
import dev.rollczi.litecommands.annotations.Permission;
import dev.rollczi.litecommands.annotations.PermissionExclude;
import dev.rollczi.litecommands.annotations.Permissions;
import dev.rollczi.litecommands.annotations.PermissionsExclude;
import dev.rollczi.litecommands.annotations.Required;
import dev.rollczi.litecommands.annotations.Section;
import dev.rollczi.litecommands.annotations.UsageMessage;
import dev.rollczi.litecommands.annotations.Execute;
import dev.rollczi.litecommands.annotations.IgnoreMethod;
import dev.rollczi.litecommands.annotations.MinArgs;
import dev.rollczi.litecommands.scope.ScopeMetaData;
import dev.rollczi.litecommands.argument.ArgumentHandler;
import dev.rollczi.litecommands.valid.ValidationInfo;

import panda.std.Option;
import panda.utilities.text.Formatter;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class LiteAnnotationParser implements AnnotationParser {

    private final Map<Class<?>, Set<ArgumentHandler<?>>> argumentHandlers;
    private final Formatter placeholders;

    public LiteAnnotationParser(Map<Class<?>, Set<ArgumentHandler<?>>> argumentHandlers) {
        this.argumentHandlers = argumentHandlers;
        this.placeholders = new Formatter();
    }

    public LiteAnnotationParser(Map<Class<?>, Set<ArgumentHandler<?>>> argumentHandlers, Formatter placeholders) {
        this.argumentHandlers = argumentHandlers;
        this.placeholders = placeholders;
    }

    @Override
    public Option<ScopeMetaData> parse(AnnotatedElement annotatedElement) {
        if (!(annotatedElement instanceof Class) && !(annotatedElement instanceof Method)) {
            throw new IllegalArgumentException();
        }

        ScopeMetaData.Builder builder = ScopeMetaData.builder();

        if (annotatedElement.isAnnotationPresent(IgnoreMethod.class) || annotatedElement.isAnnotationPresent(IgnoreClass.class)) {
            return Option.none();
        }

        if (!annotatedElement.isAnnotationPresent(Section.class) && !annotatedElement.isAnnotationPresent(Execute.class)) {
            return Option.none();
        }

        for (Annotation annotation : annotatedElement.getAnnotations()) {
            if (annotation instanceof Section) {
                Section section = (Section) annotation;
                List<String> aliases = Arrays.stream(section.aliases())
                        .map(placeholders::format)
                        .collect(Collectors.toList());

                builder
                    .name(placeholders.format(section.route()))
                    .aliases(aliases)
                    .priority(section.priority());

                if (section.required() > - 1) {
                    builder.amountValidator(validator -> validator.required(section.required()));
                }

                continue;
            }

            if (annotation instanceof Execute) {
                Execute execute = (Execute) annotation;
                List<String> aliases = Arrays.stream(execute.aliases())
                        .map(placeholders::format)
                        .collect(Collectors.toList());

                builder
                    .name(placeholders.format(execute.route()))
                    .aliases(aliases);

                if (execute.required() > - 1) {
                    builder.amountValidator(validator -> validator.required(execute.required()));
                }

                continue;
            }

            if (annotation instanceof Permissions) {
                Permissions permissions = (Permissions) annotation;

                for (Permission permission : permissions.value()) {
                    builder.permissions(placeholders.format(permission.value()));
                }

                continue;
            }

            if (annotation instanceof Permission) {
                Permission permission = (Permission) annotation;
                builder.permissions(placeholders.format(permission.value()));
                continue;
            }

            if (annotation instanceof PermissionsExclude) {
                PermissionsExclude excludes = (PermissionsExclude) annotation;
                for (PermissionExclude exclude : excludes.value()) {
                    builder.permissionsExclude(placeholders.format(exclude.value()));
                }

                continue;
            }

            if (annotation instanceof PermissionExclude) {
                PermissionExclude exclude = (PermissionExclude) annotation;
                builder.permissionsExclude(placeholders.format(exclude.value()));
                continue;
            }

            if (annotation instanceof UsageMessage) {
                UsageMessage usageMessage = (UsageMessage) annotation;
                builder.message(ValidationInfo.INVALID_USE, placeholders.format(usageMessage.value()));
                continue;
            }

            if (annotation instanceof Required) {
                Required required = (Required) annotation;
                builder.amountValidator(validator -> validator.required(required.value()));
                continue;
            }

            if (annotation instanceof MinArgs) {
                MinArgs minArgs = (MinArgs) annotation;
                builder.amountValidator(validator -> validator.min((minArgs).value()));
                continue;
            }

            if (annotation instanceof MaxArgs) {
                MaxArgs max = (MaxArgs) annotation;
                builder.amountValidator(validator -> validator.max(max.value()));
                continue;
            }

            if (annotation instanceof Between) {
                Between between = (Between) annotation;
                builder.amountValidator(validator -> validator.min(between.min()).max(between.max()));
            }
        }

        return Option.of(builder.build());
    }

    @Override
    public Set<ArgumentHandler<?>> getArgumentHandler(Class<?> argumentClass) {
        return Collections.unmodifiableSet(argumentHandlers.get(argumentClass));
    }

    @Override
    public Map<Class<?>, Set<ArgumentHandler<?>>> getArgumentHandlers() {
        return Collections.unmodifiableMap(argumentHandlers);
    }

}
