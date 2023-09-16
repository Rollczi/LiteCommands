package dev.rollczi.litecommands.shared;

@Deprecated
public interface LiteResult<SUCCESS, FAILURE> {

    SUCCESS getSuccess();

    FAILURE getFailure();

    boolean isSuccess();

    boolean isFailure();

}

