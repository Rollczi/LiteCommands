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
import dev.rollczi.litecommands.valid.ValidationInfo;
import dev.rollczi.litecommands.annotations.Execute;
import dev.rollczi.litecommands.annotations.IgnoreMethod;
import dev.rollczi.litecommands.annotations.MinArgs;
import panda.std.Option;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

public class LiteAnnotationParser implements AnnotationParser {

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
            if (annotation instanceof Section section) {
                builder
                    .name(section.route())
                    .aliases(section.aliases());
                continue;
            }

            if (annotation instanceof Execute execute) {
                builder.name(execute.route());
                continue;
            }

            if (annotation instanceof Permissions permissions) {
                for (Permission permission : permissions.value()) {
                    builder.permissions(permission.value());
                }

                continue;
            }

            if (annotation instanceof Permission permission) {
                builder.permissions(permission.value());
                continue;
            }

            if (annotation instanceof PermissionsExclude excludes) {
                for (PermissionExclude exclude : excludes.value()) {
                    builder.permissionsExclude(exclude.value());
                }

                continue;
            }

            if (annotation instanceof PermissionExclude exclude) {
                builder.permissionsExclude(exclude.value());
                continue;
            }

            if (annotation instanceof UsageMessage usageMessage) {
                builder.message(ValidationInfo.INCORRECT_USE, usageMessage.value());
                continue;
            }

            if (annotation instanceof Required required) {
                builder.amountValidator(validator -> validator.equals(required.value()));
                continue;
            }

            if (annotation instanceof MinArgs min) {
                builder.amountValidator(validator -> validator.min(min.value()));
                continue;
            }

            if (annotation instanceof MaxArgs max) {
                builder.amountValidator(validator -> validator.max(max.value()));
                continue;
            }

            if (annotation instanceof Between between) {
                builder.amountValidator(validator -> validator.min(between.min()).min(between.max()));
            }
        }

        return Option.of(builder.build());
    }

}
