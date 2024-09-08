package dev.rollczi.litecommands.jakarta;

import dev.rollczi.litecommands.requirement.Requirement;
import dev.rollczi.litecommands.schematic.SchematicFormat;
import jakarta.validation.ConstraintViolation;

import java.lang.annotation.Annotation;
import java.util.Locale;

public class JakartaViolation<A extends Annotation, T> {

    private final Class<A> annotationType;
    private final T value;
    private final ConstraintViolation<Object> violation;
    private final Requirement<?> requirement;
    private final String message;
    private final String parameterName;
    private final SchematicFormat schematicFormat;
    private final Locale locale;

    public JakartaViolation(Class<A> annotationType, T value, ConstraintViolation<Object> violation, Requirement<?> requirement, String message, String parameterName, SchematicFormat schematicFormat, Locale locale) {
        this.annotationType = annotationType;
        this.value = value;
        this.violation = violation;
        this.requirement = requirement;
        this.message = message;
        this.parameterName = parameterName;
        this.schematicFormat = schematicFormat;
        this.locale = locale;
    }

    public T getInvalidValue() {
        return value;
    }

    public Class<A> getAnnotationType() {
        return annotationType;
    }

    public A getAnnotation() {
        return (A) violation.getConstraintDescriptor().getAnnotation();
    }

    public Requirement<?> getRequirement() {
        return requirement;
    }

    public ConstraintViolation<Object> getViolation() {
        return violation;
    }

    public String getMessage() {
        return message;
    }

    public String getParameterName() {
        return parameterName;
    }

    public String getFormattedParameterName() {
        return String.format(schematicFormat.argumentFormat(), parameterName);
    }

    public SchematicFormat getFormat() {
        return schematicFormat;
    }

    public Locale getLocale() {
        return locale;
    }

}
