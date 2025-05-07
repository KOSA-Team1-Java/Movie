package reservation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import member.Member;
import movie.Screening;

@Getter
@Builder
@AllArgsConstructor
public class Reservation {
    private final int id;
    private final Member member;
    private final Screening screening;
    private final int cash;  //결제 금액
    private final int credit;  //결제 금액
}
