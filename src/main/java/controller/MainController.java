package controller;

import command.Command;
import command.LoginCommand;
import command.ReservationCommand;
import command.SignUpCommand;
import exception.ExceptionController;
import member.MemberService;
import movie.MovieService;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MainController {

    private final MemberService memberService;
    private final MovieService movieService;
    private final ExceptionController exceptionController;
    private final Map<String, Command> commandMap = new HashMap<>();
    private static final Scanner scanner = new Scanner(System.in);

    public MainController(MemberService memberService, MovieService movieService, ExceptionController exceptionController) {
        this.memberService = memberService;
        this.movieService = movieService;
        this.exceptionController = exceptionController;
        initCommands();
    }

    public void initCommands() {
        commandMap.put("/signup", new SignUpCommand(memberService, exceptionController, scanner));
        commandMap.put("/login", new LoginCommand(memberService, exceptionController, scanner));
        commandMap.put("/res", new ReservationCommand(memberService, movieService, exceptionController, scanner));
        // ... 추가 커맨드
    }

    public boolean call(String command, boolean login) {
        Command inputCmd = commandMap.get(command);

        if (command.equals("/signup")) {
            inputCmd.execute();
            System.out.println("회원가입 완료");
            return false;
        }
        if (command.equals("/login")) {
            boolean success = inputCmd.executeReturnBoolean();
            return success;
        }
        if (command.equals("/logout")) {
            if (login) {
                System.out.println("로그아웃 완료");
                return true;
            } else {
                System.out.println("로그인 상태가 아닙니다");
                return false;
            }
        }
        if (command.equals("/res")) {
            if (!login) {
                System.out.println("로그인이 필요한 기능입니다");
                return false;
            }
            inputCmd.execute();
            return false;
        }
        System.out.println("올바른 명령어를 입력해주세요.");
        return false;
    }
}
