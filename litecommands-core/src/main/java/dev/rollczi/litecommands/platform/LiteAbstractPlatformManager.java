package dev.rollczi.litecommands.platform;

public abstract class LiteAbstractPlatformManager<T> implements LitePlatformManager<T> {

    protected LiteSenderCreator<T> liteSenderCreator;

    protected LiteAbstractPlatformManager(LiteSenderCreator<T> liteSenderCreator) {
        this.liteSenderCreator = liteSenderCreator;
    }

    @Override
    public LiteSenderCreator<T> getLiteSenderCreator() {
        return liteSenderCreator;
    }

    @Override
    public void setLiteSenderCreator(LiteSenderCreator<T> liteSenderCreator) {
        this.liteSenderCreator = liteSenderCreator;
    }

}
