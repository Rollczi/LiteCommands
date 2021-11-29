package dev.rollczi.litecommands.component;

import dev.rollczi.litecommands.valid.ValidationInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExecutionResult {

    private final ValidationInfo validationInfo;
    private final String validationMessage;
    private final boolean canIgnore;
    private final List<String> missedPermissions = new ArrayList<>();

    private ExecutionResult(ValidationInfo validationInfo, String validationMessage, boolean canIgnore) {
        this.validationInfo = validationInfo;
        this.validationMessage = validationMessage;
        this.canIgnore = canIgnore;
    }

    private ExecutionResult(List<String> missedPermissions) {
        this.validationInfo = ValidationInfo.NO_PERMISSION;
        this.validationMessage = null;
        this.canIgnore = false;
        this.missedPermissions.addAll(missedPermissions);
    }

    public boolean isValid() {
        return validationInfo == ValidationInfo.NONE;
    }

    public boolean isInvalid() {
        return validationInfo != ValidationInfo.NONE;
    }

    public String getValidMessage() {
        return validationMessage;
    }

    public ValidationInfo getValidInfo() {
        return validationInfo;
    }

    public boolean canIgnore() {
        return canIgnore;
    }

    public List<String> getMissedPermissions() {
        return Collections.unmodifiableList(missedPermissions);
    }

    public static ExecutionResult valid() {
        return new ExecutionResult(ValidationInfo.NONE, null, false);
    }

    public static ExecutionResult invalid(ValidationInfo info, String validationMessage) {
        return new ExecutionResult(info, validationMessage, false);
    }

    public static ExecutionResult invalid(ValidationInfo info, LiteComponent.ContextOfResolving context) {
        return new ExecutionResult(info, ScopeUtils.getLastMessage(info, context), false);
    }

    public static ExecutionResult invalidPermission(List<String> missedPermissions) {
        return new ExecutionResult(missedPermissions);
    }

    public static ExecutionResult invalid(ValidationInfo info, String validationMessage, boolean canIgnore) {
        return new ExecutionResult(info, validationMessage, canIgnore);
    }

    public static ExecutionResult invalid(ValidationInfo info, LiteComponent.ContextOfResolving context, boolean canIgnore) {
        return new ExecutionResult(info, ScopeUtils.getLastMessage(info, context), canIgnore);
    }

}
