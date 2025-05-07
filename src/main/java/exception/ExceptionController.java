package exception;

public class ExceptionController {
    public void handle(Exception e) {
        if (e instanceof MovieException) {
            System.out.println("1조 영화관 오류 : " + e.getMessage());
        } else {
            System.out.println("오류 : " + e.getMessage());
        }
    }
}
