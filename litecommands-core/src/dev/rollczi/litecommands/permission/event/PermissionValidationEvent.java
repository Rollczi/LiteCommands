package dev.rollczi.litecommands.permission.event;

import dev.rollczi.litecommands.event.Event;
import dev.rollczi.litecommands.meta.MetaHolder;
import dev.rollczi.litecommands.permission.PermissionValidationResult;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public class PermissionValidationEvent implements Event, PermissionValidationResult {

    private final MetaHolder source;
    private List<Verdict> verdicts;

    public PermissionValidationEvent(MetaHolder source, List<Verdict> verdicts) {
        this.source = source;
        this.verdicts = verdicts;
    }

    public MetaHolder getSource() {
        return source;
    }

    @Override
    public List<Verdict> getVerdicts() {
        return Collections.unmodifiableList(verdicts);
    }

    public void setVerdicts(List<Verdict> verdicts) {
        this.verdicts = new ArrayList<>(verdicts);
    }

}
