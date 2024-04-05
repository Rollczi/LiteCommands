package dev.rollczi.litecommands.cooldown;

import dev.rollczi.litecommands.identifier.Identifier;
import java.util.Objects;

class CooldownCompositeKey {

    private final Identifier identifier;
    private final String cooldownKey;

    public CooldownCompositeKey(Identifier identifier, String cooldownKey) {
        this.identifier = identifier;
        this.cooldownKey = cooldownKey;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public String getCooldownKey() {
        return cooldownKey;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        CooldownCompositeKey that = (CooldownCompositeKey) object;
        return Objects.equals(identifier, that.identifier) && Objects.equals(cooldownKey, that.cooldownKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, cooldownKey);
    }

}
