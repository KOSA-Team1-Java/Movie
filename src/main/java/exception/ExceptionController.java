package exception;

public class ExceptionController {
    public void handle(Exception e) {
        if (e instanceof CustomException) {
            System.out.println("1조 영화관 오류 : " + e.getMessage() + e.getMessage());
        } else {
            System.out.println("오류 : " + e.getMessage());
        }
    }
}
