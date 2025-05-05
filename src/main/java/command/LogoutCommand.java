package command;

import controller.MainController;

public class LogoutCommand implements Command {
    @Override
    public void execute(MainController context) {
        context.setLoginMember(null);
        System.out.println("로그아웃 성공");
    }

    @Override
    public boolean requiresLogin() {
        return true;
    }
}
