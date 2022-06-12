package dev.rollczi.litecommands.handle;

public interface Redirector<FROM, TO> {

    TO redirect(FROM from);

}
