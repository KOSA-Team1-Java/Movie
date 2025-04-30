package member;

import exception.MovieException;

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
        String hashPassword = PasswordHasher.hash(password);
        if (member != null && member.getPassword().equals(hashPassword)) {
            return member;  // 로그인 성공
        }
        return null;
    }

    public void logout() {}

    public void bookMovie() {}

    public void viewReservation() {}

    public void veiwMyPage(){}

}