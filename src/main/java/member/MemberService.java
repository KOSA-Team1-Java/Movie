package member;

public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void signUp() {}

    public void login(String id, String password) {}

    public void logout() {}

    public void bookMovie() {}

    public void viewReservation() {}

    public void veiwMyPage(){}

}
