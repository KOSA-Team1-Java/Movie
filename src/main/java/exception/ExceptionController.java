package exception;

public class ExceptionController {
    public void handle(Exception e) {
        if (e instanceof CustomException) {
            System.out.println("서비스 오류 : " + e.getMessage());
        } else {
            System.out.println("오류 : " + e.getMessage());
        }
    }
}
