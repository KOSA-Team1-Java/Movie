package command;

import controller.MainController;

public class CommandListCommand implements Command {

    @Override
    public boolean execute(MainController context) {
        System.out.println("사용 가능한 명령어 목록:");
        System.out.println("/signup - 회원가입");
        System.out.println("/login - 로그인");
        System.out.println("/logout - 로그아웃");
        System.out.println("/book - 영화 예매");
        System.out.println("/movie - 영화 목록");
        System.out.println("/exit - 종료");
        return true;
    }
}
