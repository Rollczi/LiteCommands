package dev.rollczi.litecommands.permission;

import dev.rollczi.litecommands.meta.MetaHolder;
import dev.rollczi.litecommands.platform.PlatformSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.jetbrains.annotations.ApiStatus;

public class MissingPermissions implements PermissionValidationResult {

    private final List<Verdict> verdicts;

    MissingPermissions(List<Verdict> verdicts) {
        this.verdicts = verdicts;
    }

    @Override
    @ApiStatus.Experimental
    public List<Verdict> getVerdicts() {
        return Collections.unmodifiableList(verdicts);
    }

    public List<String> getChecked() {
        return verdicts.stream()
            .flatMap(result -> result.getChecks().stream())
            .flatMap(check -> check.getCheckedPermissions().stream())
            .distinct()
            .collect(Collectors.toList());
    }

    public List<String> getPermissions() {
        return verdicts.stream()
            .flatMap(result -> result.getChecks().stream())
            .flatMap(check -> check.getMissingPermissions().stream())
            .distinct()
            .collect(Collectors.toList());
    }

    public String asJoinedText() {
        return asJoinedText(", ");
    }

    public String asJoinedText(String separator) {
        return String.join(separator, getPermissions());
    }

    public boolean isMissing() {
        return !isPermitted();
    }

    public static MissingPermissions check(PlatformSender platformSender, MetaHolder metaHolder) {
        return new MissingPermissions(new ArrayList<>()); //TODO
    }

    public static MissingPermissions missing(String... permissions) {
        return new MissingPermissions(Collections.singletonList(new Verdict(MetaHolder.empty(), Collections.singletonList(new Check(Arrays.asList(permissions), Arrays.asList(permissions))))));
    }

}
