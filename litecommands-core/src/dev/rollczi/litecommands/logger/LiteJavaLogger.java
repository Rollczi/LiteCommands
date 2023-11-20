package dev.rollczi.litecommands.logger;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LiteJavaLogger implements LiteLogger {

    private final Logger logger;

    public LiteJavaLogger(Level level) {
        this.logger = Logger.getLogger("LiteCommands");
        this.logger.setLevel(level);
    }

    @Override
    public void log(Level level, String message) {
        this.logger.log(level, message);
    }

    @Override
    public void log(Level level, String message, Throwable throwable) {
        this.logger.log(level, message, throwable);
    }

    @Override
    public void log(Level level, Throwable throwable) {
        this.logger.log(level, throwable.getMessage(), throwable);
    }

    @Override
    public void setLevel(Level level) {
        this.logger.setLevel(level);
    }

}
