package dev.rollczi.litecommands.valid;

public class ValidationCommandException extends RuntimeException {

    private final ValidationInfo validationInfo;

    public ValidationCommandException(ValidationInfo validationInfo) {
        this.validationInfo = validationInfo;
    }

    public ValidationCommandException(ValidationInfo validationInfo, String message) {
        super(message);
        this.validationInfo = validationInfo;
    }

    public ValidationCommandException(String customMessage) {
        this(ValidationInfo.CUSTOM, customMessage);
    }

    public ValidationInfo getValidationInfo() {
        return validationInfo;
    }

}
