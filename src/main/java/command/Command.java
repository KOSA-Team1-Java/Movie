package command;

import controller.MainController;

public interface Command {
    //실행 (성공 여부 반환)
    boolean execute(MainController context);
    //로그인 필요 여부
    default boolean requiresLogin() { return false; }
    //로그아웃 필요 여부 (예: /login)
    default boolean requiresLogout() { return false; }
}
