package member;

import exception.CustomException;
import util.PasswordHasher;

public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void signUp(String id, String password, String name, int age)  throws CustomException {
        Member member = new Member(id, password, name, age);
        memberRepository.save(member);
    }

    public Member login(String id, String password) throws CustomException {
        Member member = memberRepository.findById(id);
        if (member == null) {
            throw new CustomException("존재하지 않는 회원입니다.");
        }
        String hashPassword = PasswordHasher.hash(password);
        if (!member.getPassword().equals(hashPassword)) {
            throw new CustomException("존재하지 않는 회원입니다.");
        }
        return member;
    }

    public void updateBudget(Member member) {
        memberRepository.updateBudget(member);
    }

    public Member updateName(Member member, String newName) {
        return memberRepository.updateName(member, newName);
    }

    public Member updatePassword(Member member, String newPassword) {
        return memberRepository.updatePassword(member, newPassword);
    }

}