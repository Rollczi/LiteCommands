package dev.rollczi.litecommands.valid;

public class ValidationCommandException extends RuntimeException {

    private final ValidationInfo validationInfo;

    public ValidationCommandException(ValidationInfo validationInfo) {
        this.validationInfo = validationInfo;
    }

    public ValidationCommandException(ValidationInfo validationInfo, String customValidInfo) {
        super(customValidInfo);
        this.validationInfo = validationInfo;
    }

    public ValidationInfo getValidationInfo() {
        return validationInfo;
    }

}
