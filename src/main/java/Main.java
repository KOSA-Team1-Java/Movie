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
                break;
            }
            if(login==true) {
                mainController.call(input);
                continue;
            }
            if (login==true && input.equals("/logout")) {
                mainController.call(input);
                login = false;
                continue;
            }
            if (login==false && input.equals("/signup")) {
                mainController.call(input);
                continue;
            }
            if (login==false && input.equals("/login")) {
                mainController.call(input);
                login = true;
                continue;
            }
            else if (login==false) {
                printStream.println("로그인 해주세요");
            }
        }
        System.out.println("감사합니다!");
        printStream.println("안녕히 가세요");
        printStream.close();
    }
}
