package dev.rollczi.litecommands.permission;

import dev.rollczi.litecommands.event.EventListener;
import dev.rollczi.litecommands.event.Subscriber;

import dev.rollczi.litecommands.suggestion.event.SuggestionNodeEvent;

public class PermissionSuggestionController implements EventListener {

    private final PermissionService permissionService;

    public PermissionSuggestionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @Subscriber
    void onSuggestion(SuggestionNodeEvent event) {
        PermissionValidationResult validationResult = permissionService.validate(event.getInvocation().platformSender(), event.getNode());
        if (!validationResult.isPermitted()) {
            event.setCancelled(true);
        }
    }

}
