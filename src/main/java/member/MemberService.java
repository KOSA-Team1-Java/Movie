package member;

import exception.MovieException;
import pay.Pay;
import util.PasswordHasher;

public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void signUp(String id, String password, String name, int age, String email)  throws MovieException {
        Member member = new Member(id, password, name, age, email);
        memberRepository.save(member);
    }

    public Member login(String id, String password) throws MovieException {
        Member member = memberRepository.findById(id);
        if (member == null) {
            throw new MovieException("존재하지 않는 회원입니다.");
        }
        String hashPassword = PasswordHasher.hash(password);
        if (!member.getPassword().equals(hashPassword)) {
            throw new MovieException("존재하지 않는 회원입니다.");
        }
        return member;
    }

    public void updateBudget(Member member) {
        memberRepository.updateBudget(member);
    }

    public void processPayment(Member member, Pay payMethod, int amount) {


        member.addPaymentHistory(amount); // 회원 결제 내역 저장
    }
}