package member;

import exception.MovieException;
import pay.Pay;
import reservation.Reservation;
import util.PasswordHasher;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static util.ConnectionConst.*;

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
            throw new MovieException("존재하지 않는 회원입니다.");
        }
        return member;
    }

    public void updateBudget(Member member) {
        memberRepository.updateBudget(member);
    }


    public void refundBudget(Member member, int refundCash, int refundCredit) {
        // 회원 객체의 cash/credit에 금액을 더해주기 (여기선 example로 cash에 환불)
        member.setCash(member.getCash() + refundCash);
        member.setCredit(member.getCredit() + refundCredit);

        this.updateBudget(member);
    }

    public void processPayment(Member member, Pay payMethod, int amount) {


        member.addPaymentHistory(amount); // 회원 결제 내역 저장
    }
}