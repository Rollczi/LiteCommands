package dev.rollczi.litecommands.multisections;

import dev.rollczi.litecommands.annotations.Execute;
import dev.rollczi.litecommands.annotations.Section;
import dev.rollczi.litecommands.valid.ValidationCommandException;
import dev.rollczi.litecommands.valid.ValidationInfo;

@Section(route = "ac")
public class AppendMultiSectionTestCommand {

    @Execute(route = "tp")
    public void executeTp() {
        throw new ValidationCommandException(ValidationInfo.CUSTOM, "tp");
    }

}
