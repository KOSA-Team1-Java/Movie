package reservation;

import member.Member;
import movie.Movie;
import movie.SeatRequest;
import pay.CashPay;
import pay.CreditPay;
import pay.Pay;
import pay.PayService;

import java.util.List;
import java.util.Scanner;

public class ReservationService {
    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }
}
