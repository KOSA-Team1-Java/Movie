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
        return memberRepository.findById(id);
    }

    public void logout() {}

    public void bookMovie() {}

    public void viewReservation() {}

    public void veiwMyPage(){}

}