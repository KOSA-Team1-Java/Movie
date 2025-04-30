package exception;

public class ExceptionController {
    public void signUpError(Exception e) {
        System.out.println("회원가입 오류 " + e.getMessage());
    }
    
    public void loginError(Exception e) {
        System.out.println("로그인 오류 " + e.getMessage());
    }
}
