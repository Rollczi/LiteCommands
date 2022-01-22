package dev.rollczi.litecommands.component;

import dev.rollczi.litecommands.scope.ScopeUtils;
import dev.rollczi.litecommands.valid.ValidationInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExecutionResult {

    private final LiteComponent.ContextOfResolving lastContext;
    private final ValidationInfo validationInfo;
    private final String validationMessage;
    private final boolean canIgnore;
    private final List<String> missingPermissions = new ArrayList<>();

    private ExecutionResult(LiteComponent.ContextOfResolving lastContext, ValidationInfo validationInfo, String validationMessage, boolean canIgnore) {
        this.lastContext = lastContext;
        this.validationInfo = validationInfo;
        this.validationMessage = validationMessage;
        this.canIgnore = canIgnore;
    }

    private ExecutionResult(List<String> missingPermissions, LiteComponent.ContextOfResolving lastContext) {
        this.lastContext = lastContext;
        this.validationInfo = ValidationInfo.NO_PERMISSION;
        this.validationMessage = null;
        this.canIgnore = false;
        this.missingPermissions.addAll(missingPermissions);
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

    public LiteComponent.ContextOfResolving getLastContext() {
        return lastContext;
    }

    public boolean canIgnore() {
        return canIgnore;
    }

    public List<String> getMissingPermissions() {
        return Collections.unmodifiableList(missingPermissions);
    }

    public static ExecutionResult valid(LiteComponent.ContextOfResolving lastContext) {
        return new ExecutionResult(lastContext, ValidationInfo.NONE, null, false);
    }

    public static ExecutionResult invalid(ValidationInfo info, String validationMessage, LiteComponent.ContextOfResolving lastContext) {
        return new ExecutionResult(lastContext, info, validationMessage, false);
    }

    public static ExecutionResult invalid(ValidationInfo info, LiteComponent.ContextOfResolving lastContext) {
        return new ExecutionResult(lastContext, info, ScopeUtils.getLastMessage(info, lastContext), false);
    }

    public static ExecutionResult invalidPermission(List<String> missingPermissions, LiteComponent.ContextOfResolving lastContext) {
        return new ExecutionResult(missingPermissions, lastContext);
    }

    public static ExecutionResult invalid(ValidationInfo info, LiteComponent.ContextOfResolving lastContext, boolean canIgnore) {
        return new ExecutionResult(lastContext, info, ScopeUtils.getLastMessage(info, lastContext), canIgnore);
    }

}
