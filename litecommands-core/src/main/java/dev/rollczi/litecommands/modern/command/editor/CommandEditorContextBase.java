package dev.rollczi.litecommands.modern.command.editor;

import java.util.List;

abstract class CommandEditorContextBase implements CommandEditorContext {

    protected String command;
    protected List<String> aliases;

    @Override
    public CommandEditorContext name(String name) {
        name = name.trim();

        if (!name.contains(" ")) {
            this.command = name;
            return this;
        }

        int separatorIndex = name.indexOf(" ");
        this.command = name.substring(0, separatorIndex);

        CommandEditorContextDummy structurePiece = new CommandEditorContextDummy(this);

        return structurePiece.name(name.substring(separatorIndex + 1));
    }
    
    @Override
    public CommandEditorContext aliases(List<String> aliases) {
        if (aliases.isEmpty()) {
            return this;
        }
        
        int countDummy = countDummy(aliases.get(0));
        
        for (String alias : this.aliases) {
            validName(alias);

            if (countDummy(alias) != countDummy) {
                throw new IllegalArgumentException("Aliases must have the same structure");
            }
        }
        
        if (countDummy == 0) {
            this.aliases = aliases;
            return this;
        }

        for (String alias : this.aliases) {
            alias = alias.trim();

            int separatorIndex = alias.indexOf(" ");
            String command = alias.substring(0, separatorIndex);

            this.aliases.add(command);

            CommandEditorContextDummy structurePiece = new CommandEditorContextDummy(this);

            structurePiece.name(alias.substring(separatorIndex + 1));
        }

        // TODO
    }
    
    private int countDummy(String name) {
        return name.split(" ").length - 1;
    }

    private void validName(String name) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        if (!name.trim().equals(name)) {
            throw new IllegalArgumentException("Name cannot start or end with space");
        }
    }
    
}
