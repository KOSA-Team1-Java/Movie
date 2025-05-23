package command;

import controller.MainController;
import member.MemberService;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpCommand implements Command {

    private final MemberService memberService;
    private final Scanner scanner;

    public SignUpCommand(MemberService memberService, Scanner scanner) {
        this.memberService = memberService;
        this.scanner = scanner;
    }

    @Override
    public  void execute(MainController context){
        // 아이디 유효성 검사 메시지 출력
        System.out.println("아이디 규칙: 영어와 숫자만 사용 가능, 12자리 이하");

        // ID 입력
        String id = null;
        while (true) {
            System.out.print("ID : ");
            id = scanner.nextLine();
            if (isValidId(id)) {
                break;
            } else {
                System.out.println("❌ 아이디는 영어와 숫자만 사용 가능하며 12자 이하로 입력해야 합니다.");
            }
        }

        // 비밀번호 유효성 검사 메시지 출력
        System.out.println("비밀번호 규칙: 최소 4글자 이상");

        // Password 입력
        String password = null;
        while (true) {
            System.out.print("Password : ");
            password = scanner.nextLine();
            if (isValidPassword(password)) {
                break;
            } else {
                System.out.println("❌ 비밀번호는 최소 4글자 이상이어야 합니다.");
            }
        }

        // Name 입력
        System.out.print("Name : ");
        String name = scanner.nextLine();

        // Age 입력
        int age = 0;
        while (true) {
            System.out.print("Age : ");
            try {
                age = Integer.parseInt(scanner.nextLine());
                if (age >= 0) break;
                else System.out.println("❌ 나이는 0 이상이어야 합니다.");
            } catch (NumberFormatException e) {
                System.out.println("❌ 유효한 나이를 입력해주세요.");
            }
        }

        memberService.signUp(id, password, name, age);  // 회원가입 처리

    }

    @Override
    public boolean requiresLogout() {
        return true;
    }
    // 아이디 유효성 검사: 영어와 숫자만 포함되며 12자리 이하
    private boolean isValidId(String id) {
        String regex = "^[a-zA-Z0-9]{1,12}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(id);
        return matcher.matches();
    }

    // 비밀번호 유효성 검사: 최소 4글자 이상
    private boolean isValidPassword(String password) {
        return password.length() >= 4;
    }

}
