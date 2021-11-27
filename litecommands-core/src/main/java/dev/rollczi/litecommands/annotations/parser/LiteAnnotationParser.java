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
import dev.rollczi.litecommands.component.ScopeMetaData;
import dev.rollczi.litecommands.inject.SingleArgumentHandler;
import dev.rollczi.litecommands.valid.ValidationInfo;
import dev.rollczi.litecommands.annotations.Execute;
import dev.rollczi.litecommands.annotations.IgnoreMethod;
import dev.rollczi.litecommands.annotations.MinArgs;
import panda.std.Option;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Map;

public class LiteAnnotationParser implements AnnotationParser {

    private final Map<Class<?>, SingleArgumentHandler<?>> argumentHandlers;

    public LiteAnnotationParser(Map<Class<?>, SingleArgumentHandler<?>> argumentHandlers) {
        this.argumentHandlers = argumentHandlers;
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
                builder
                    .name(section.route())
                    .aliases(section.aliases());

                if (section.required() > - 1) {
                    builder.amountValidator(validator -> validator.required(section.required()));
                }

                continue;
            }

            if (annotation instanceof Execute) {
                Execute execute = (Execute) annotation;
                builder.name(execute.route());

                if (execute.required() > - 1) {
                    builder.amountValidator(validator -> validator.required(execute.required()));
                }

                continue;
            }

            if (annotation instanceof Permissions) {
                Permissions permissions = (Permissions) annotation;

                for (Permission permission : permissions.value()) {
                    builder.permissions(permission.value());
                }

                continue;
            }

            if (annotation instanceof Permission) {
                Permission permission = (Permission) annotation;
                builder.permissions(permission.value());
                continue;
            }

            if (annotation instanceof PermissionsExclude) {
                PermissionsExclude excludes = (PermissionsExclude) annotation;
                for (PermissionExclude exclude : excludes.value()) {
                    builder.permissionsExclude(exclude.value());
                }

                continue;
            }

            if (annotation instanceof PermissionExclude) {
                PermissionExclude exclude = (PermissionExclude) annotation;
                builder.permissionsExclude(exclude.value());
                continue;
            }

            if (annotation instanceof UsageMessage) {
                UsageMessage usageMessage = (UsageMessage) annotation;
                builder.message(ValidationInfo.INVALID_USE, usageMessage.value());
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
                builder.amountValidator(validator -> validator.min(between.min()).min(between.max()));
            }
        }

        return Option.of(builder.build());
    }

    @Override
    public Option<SingleArgumentHandler<?>> getArgumentHandler(Class<?> argumentClass) {
        return Option.of(argumentHandlers.get(argumentClass));
    }

}
