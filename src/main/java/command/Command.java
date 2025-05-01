package command;

public interface Command {
    public void execute();

    //용도와 왜 default 인지 적기
    default boolean executeReturnBoolean() {
        execute();
        return false;
    }
}
