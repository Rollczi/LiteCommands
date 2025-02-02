package dev.rollczi.litecommands.permission;

import dev.rollczi.litecommands.event.EventPublisher;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.meta.MetaHolder;
import dev.rollczi.litecommands.permission.event.PermissionValidationEvent;
import dev.rollczi.litecommands.platform.PlatformSender;
import java.util.ArrayList;
import java.util.List;

public class PermissionValidationServiceImpl implements PermissionValidationService {

    private final EventPublisher publisher;

    public PermissionValidationServiceImpl(EventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public PermissionValidationResult validate(MetaHolder metaHolder, PlatformSender sender) {
        List<PermissionValidationResult.Verdict> checked = check(sender, metaHolder);
        return publisher.publish(new PermissionValidationEvent(metaHolder, checked));
    }

    static List<PermissionValidationResult.Verdict> check(PlatformSender platformSender, MetaHolder metaHolder) {
        MetaHolder current = metaHolder;

        List<PermissionValidationResult.Verdict> results = new ArrayList<>();
        while (current != null) {
            if (!current.meta().has(Meta.PERMISSIONS)) {
                current = current.parentMeta();
                continue;
            }

            results.add(checkCurrent(platformSender, current));
            current = current.parentMeta();
        }

        return results;
    }

    private static PermissionValidationResult.Verdict checkCurrent(PlatformSender platformSender, MetaHolder current) {
        List<PermissionValidationResult.Check> checks = new ArrayList<>();
        for (PermissionSet permissionSet : current.meta().get(Meta.PERMISSIONS)) {
            List<String> checked = new ArrayList<>();
            List<String> missing = new ArrayList<>();

            for (String permission : permissionSet.getPermissions()) {
                checked.add(permission);

                if (!platformSender.hasPermission(permission)) {
                    missing.add(permission);
                }
            }

            if (missing.isEmpty()) {
                return PermissionValidationResult.Verdict.permitted(current);
            }

            checks.add(new PermissionValidationResult.Check(checked, missing));
        }

        return new PermissionValidationResult.Verdict(current, checks);
    }

}
