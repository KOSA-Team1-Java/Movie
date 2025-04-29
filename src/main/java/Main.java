import controller.MainController;

import java.util.Scanner;

public class Main {

    private static final MainController controller = new MainController();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean login = false;
        System.out.println("KingSmile 영화관에 오신걸 환영합니다.");
        System.out.println("/ 입력 : 커맨드 보기");
        while(true) {
            String input = scanner.nextLine();
            if(input.equals("exit")) {
                break;
            } else controller.call(input);
        }
        System.out.println("안녕히 가세요");
    }
}
