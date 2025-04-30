package exception;

public class ExceptionController {
    public void signUpError(Exception e) {
        System.out.println("회원가입 오류 " + e.getMessage());
    }
    
    public void loginError(Exception e) {
        System.out.println("로그인 오류 " + e.getMessage());
    }
    
    public void reserveError(Exception e) {
        System.out.println("예약 중 오류" + e.getMessage());
    }
    
    public void paymentError(Exception e) {
        System.out.println("결제 중 오류" + e.getMessage());
    }
}
