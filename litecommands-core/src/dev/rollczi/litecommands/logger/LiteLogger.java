package dev.rollczi.litecommands.logger;

import java.util.logging.Level;

public interface LiteLogger {

    void log(Level level, String message);

    void log(Level level, String message, Throwable throwable);

    void log(Level level, Throwable throwable);

    void setLevel(Level level);

}
