package member;

import exception.MovieException;
import util.PasswordHasher;

public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void signUp(String id, String password, String name, int age) throws MovieException {
        Member member = new Member(id, password, name, age);
        memberRepository.save(member);
    }

    public Member login(String id, String password) {
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

    public void refundBudget(Member member, int refundAmount) {
        // 회원 객체의 cash/credit에 금액을 더해주기 (여기선 example로 cash에 환불)
//        member.setCash(member.getCash() + refundAmount);
    }

    public Member updateName(Member member, String newName) {
        member.updateName(newName);
        return memberRepository.updateName(member);
    }

    public void updatePassword(Member member, String newPassword) {
        member.updatePassword(PasswordHasher.hash(newPassword));
        memberRepository.updatePassword(member);
    }

    public Member findByLoginId(String loginId) {
        return memberRepository.findById(loginId);
    }
}