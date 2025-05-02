package command;

import controller.MainController;

public class LogoutCommand implements Command {
    @Override
    public boolean execute(MainController context) {
        context.setLoginMember(null);
        System.out.println("로그아웃 성공");
        return true;
    }
    @Override
    public boolean requiresLogout() { return false; } // 로그인 상태에서만 실행 허용
}
