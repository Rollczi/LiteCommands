package dev.rollczi.litecommands.permission;

import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.meta.MetaHolder;
import dev.rollczi.litecommands.platform.PlatformSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class MissingPermissions {

    private final List<String> checkedPermissions;
    private final List<String> missingPermissions;

    private MissingPermissions(List<String> checkedPermissions, List<String> missingPermissions) {
        this.checkedPermissions = checkedPermissions;
        this.missingPermissions = missingPermissions;
    }

    public List<String> getChecked() {
        return Collections.unmodifiableList(checkedPermissions);
    }

    public List<String> getPermissions() {
        return Collections.unmodifiableList(missingPermissions);
    }

    public String asJoinedText() {
        return asJoinedText(", ");
    }

    public String asJoinedText(String separator) {
        return String.join(separator, missingPermissions);
    }

    public boolean isMissing() {
        return !missingPermissions.isEmpty();
    }

    public boolean isPermitted() {
        return missingPermissions.isEmpty();
    }

    public static MissingPermissions check(PermissionValidator validator, PlatformSender platformSender, MetaHolder metaHolder) {
        List<String> collected = new ArrayList<>();
        List<String> missingPermissions = new ArrayList<>();
        MetaHolder current = metaHolder;

        while (current != null) {
            if (!current.meta().has(Meta.PERMISSIONS)) {
                current = current.parentMeta();
                continue;
            }

            @NotNull Set<Set<String>> permissions = current.meta().get(Meta.PERMISSIONS);

            validator.check(platformSender, permissions, collected, missingPermissions);

            current = current.parentMeta();
        }

        return new MissingPermissions(collected, missingPermissions);
    }

    public static MissingPermissions check(PlatformSender platformSender, MetaHolder metaHolder) {
        // TODO: Configurable validator in `testSilent` methods
        return check(PermissionValidator.STRICT, platformSender, metaHolder);
    }

    public static MissingPermissions missing(String... permissions) {
        return new MissingPermissions(Arrays.asList(permissions), Arrays.asList(permissions));
    }

}
