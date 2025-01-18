package dev.rollczi.litecommands.permission;

import dev.rollczi.litecommands.meta.MetaHolder;
import dev.rollczi.litecommands.shared.Preconditions;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.ApiStatus;

/**
 * Represents the result of the permission validation.
 */
public interface PermissionValidationResult {

    @ApiStatus.Experimental
    List<Verdict> getVerdicts();

    default boolean isPermitted() {
        for (Verdict verdict : getVerdicts()) {
            if (!verdict.isPermitted()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Represents the cause of the missing permissions.
     */
    @ApiStatus.Experimental
    final class Verdict {

        private final MetaHolder owner;
        private final List<Check> checks;

        Verdict(MetaHolder owner, List<Check> checks) {
            Preconditions.notNull(owner, "owner");
            this.owner = owner;
            this.checks = checks;
        }

        public MetaHolder getOwner() {
            return owner;
        }

        public List<Check> getChecks() {
            return checks;
        }

        public boolean isPermitted() {
            if (checks.isEmpty()) {
                return true;
            }

            for (Check check : checks) {
                if (check.missingPermissions.isEmpty()) {
                    return true;
                }
            }

            return false;
        }

        public static Verdict permitted(MetaHolder current) {
            return new Verdict(current, Collections.emptyList());
        }

    }

    @ApiStatus.Experimental
    class Check {

        private final List<String> checkedPermissions;
        private final List<String> missingPermissions;

        Check(List<String> checkedPermissions, List<String> missingPermissions) {
            this.checkedPermissions = checkedPermissions;
            this.missingPermissions = missingPermissions;
        }

        public List<String> getCheckedPermissions() {
            return checkedPermissions;
        }

        public List<String> getMissingPermissions() {
            return missingPermissions;
        }

    }

}
