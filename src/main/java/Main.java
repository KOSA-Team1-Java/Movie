import controller.MainController;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

public class Main {

    private static final MainController controller = new MainController();

    public static void main(String[] args) throws UnsupportedEncodingException {
        Scanner scanner = new Scanner(System.in);
        boolean login = false;
        PrintStream printStream = new PrintStream(System.out, true, "UTF-8");
        printStream.println("안녕");
        printStream.println("KingSmile 영화관에 오신걸 환영합니다.");
        printStream.println("/ 입력 : 커맨드 보기");
        while(true) {
            String input = scanner.nextLine();
            if(input.equals("exit")) {
                break;
            } else controller.call(input);
        }
        System.out.println("안녕히 가세요");
    }
}
