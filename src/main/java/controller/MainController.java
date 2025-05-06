package controller;

import command.*;
import exception.ExceptionController;
import member.Member;
import member.MemberService;
import movie.MovieService;
import reservation.ReservationRepository;
import reservation.ReservationService;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MainController {

    private final Map<String, Command> commandMap = new HashMap<>();
    private final ExceptionController exceptionController;
    private Member loginMember = null;
    public void setLoginMember(Member m) { loginMember = m; }
    public boolean isLoggedIn() { return loginMember != null; }

    public MainController(MemberService memberService, MovieService movieService, ReservationService reservationService, ExceptionController exceptionController, Scanner scanner) {
        this.exceptionController = exceptionController;
        commandMap.put("/signup", new SignUpCommand(memberService, scanner));
        commandMap.put("/login", new LoginCommand(memberService, scanner));
        commandMap.put("/logout", new LogoutCommand());
        commandMap.put("/NameChange", new ChangeNameCommand(memberService));
        commandMap.put("/PW", new ChangePasswordCommand(memberService));
        commandMap.put("/book", new BookCommand(memberService, movieService, reservationService, scanner));
        commandMap.put("/movie",new MoviesCommand(movieService, scanner));
        commandMap.put("/checkReservation", new CheckReservationCommand());
        commandMap.put("/cancel", new CancelCommand(memberService, movieService, reservationService, scanner));
        commandMap.put("/command", new CommandListCommand());
    }

    public void call(String command) {
        Command cmd = commandMap.get(command);
        if (cmd == null) {
            System.out.println("올바른 명령어를 입력해주세요.");
            return;
        }
        if (cmd.requiresLogin() && !isLoggedIn()) {
            System.out.println("로그인이 필요한 기능입니다.");
            return;
        }
        if (cmd.requiresLogout() && isLoggedIn()) {
            System.out.println(loginMember.getName() + "님 이미 로그인 중입니다. 로그아웃 후 이용하세요.");
            return;
        }
        // 로그인된 회원 정보가 필요한 경우(RequiredMember), setMember()
        if (cmd.requiresLogin() && isLoggedIn() && cmd instanceof RequiredMember) {
            ((RequiredMember) cmd).setMember(loginMember);
        }
        //실행
        try {
            cmd.execute(this);
        } catch (Exception e) {
            exceptionController.handle(e);
        }
    }
}