package member;

import exception.MovieException;
import pay.Pay;

public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void signUp(String id, String password, String name, int age)  throws MovieException {
        Member member = new Member(id, password, name, age);
        memberRepository.save(member);
    }

    public Member login(String id, String password) throws MovieException {
        Member member = memberRepository.findById(id);
        if (member == null) {
            throw new MovieException("존재하지 않는 회원입니다.");
        }
        String hashPassword = PasswordHasher.hash(password);
        if (!member.getPassword().equals(hashPassword)) {
            throw new MovieException("비밀번호가 일치하지 않습니다."); // 로그인 성공
        }
        return member;
    }

    public void logout() {}

    public void bookMovie() {}

    public void viewReservation() {}

    public void veiwMyPage(){}

    public void updateBudget(Member member) {
        memberRepository.updateBudget(member);
    }

    public void processPayment(Member member, Pay payMethod, int amount) {
        if (member == null) {
            System.out.println("로그인 되어있지 않습니다.");
            return;
        }

        boolean success = payMethod.pay(member, amount);
        if (!success) {
            throw new MovieException("결제 실패: 잔액 부족");
        }

        member.addPaymentHistory(amount);
        updateBudget(member);

        System.out.println("✅ 결제가 완료되었습니다. 남은 예산: " + member.getBudget() + "원");
    }

}