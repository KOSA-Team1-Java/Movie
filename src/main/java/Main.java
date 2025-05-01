import controller.MainController;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

public class Main {

    private static final MainController mainController = new MainController();

    public static void main(String[] args) throws UnsupportedEncodingException {
        Scanner scanner = new Scanner(System.in);
        boolean login = false;
        PrintStream printStream = new PrintStream(System.out, true, "UTF-8");
        printStream.println("KingSmile 영화관에 오신걸 환영합니다");
        while(true) {
            printStream.print("입력 : ");
            String input = scanner.nextLine();
            if(input.equals("/exit")) {
                System.out.println("이용해주셔서 감사합니다!");
                break;
            }
            if (login == false && input.equals("/signup")) {
                mainController.call(input);
                printStream.println("회원가입 완료");
                continue;
            }
            if (login == false && input.equals("/login")) {
                mainController.call(input);
                login = true;
                continue;
            }
            if (login == true && input.equals("/logout")) {
                mainController.call(input);
                login = false;
                printStream.println("로그아웃 성공");
                continue;
            }
            else if (login == false && input.equals("/logout")) {
                printStream.println("로그인 상태가 아닙니다");
                continue;
            }
            if (login == true && input.equals("/res")) {
                mainController.call(input);
                continue;
            }
            else if (login == false && input.equals("/res")) {
                printStream.println("로그인이 필요한 기능입니다");
                continue;
            }

            // 잘못된 입력 처리
            printStream.println("올바른 명령어를 입력해주세요.");
        }
        printStream.close();
    }
}
