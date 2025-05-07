package command;

import controller.MainController;
import exception.CustomException;
import member.Member;
import member.MemberService;
import movie.Movie;
import movie.MovieService;
import movie.Screening;
import movie.SeatRequest;
import pay.PayService;
import reservation.ReservationService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class BookCommand implements Command, RequiredMember {
    private final MemberService memberService;
    private final MovieService movieService;
    private final ReservationService reservationService;
    private final Scanner scanner;
    private Member member;

    public BookCommand(MemberService memberService, MovieService movieService, ReservationService reservationService, Scanner scanner) {
        this.memberService = memberService;
        this.movieService = movieService;
        this.reservationService = reservationService;
        this.scanner = scanner;
    }

    @Override
    public void execute(MainController context) throws CustomException {
        // 1단계: 영화 목록 출력
        System.out.println("상영 중인 영화");
        movieService.showMovie(); // 영화 목록 출력 (영화 ID 포함)

        Movie selectedMovie = null;
        while (selectedMovie == null) {
            try {
                System.out.print("예매할 영화 번호 : ");
                int movieId = Integer.parseInt(scanner.nextLine());

                // 영화 정보(객체) 가져오기
                selectedMovie = movieService.getMovieById(movieId); // movie_id로 조회
                if (selectedMovie == null) {
                    System.out.println("❌ 해당 번호의 영화가 존재하지 않습니다. 다시 입력해주세요.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ 숫자로 입력해주세요.");
            }
        }

// 1.2 나이 제한 체크!
        if (member.getAge() < selectedMovie.getAge()) {
            System.out.println("❌ 청소년 관람불가 영화입니다. 예매하실 수 없습니다.");
            return;
        }

        // 2단계: 영화 번호에 따른 영화관 위치 출력(번호로 선택)
        List<Screening> screenings = movieService.getScreenings(selectedMovie.getId());
        List<String> locations = screenings.stream()
                .map(screening -> screening.getTheater().getLocation())
                .distinct()
                .toList();

        System.out.println("번호에 따라 장소를 선택하세요");
        for (int i = 0; i < locations.size(); i++) {
            System.out.println((i + 1) + ". " + locations.get(i));
        }
        System.out.print("장소 번호 입력 : ");
        int locationChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        if (locationChoice < 1 || locationChoice > locations.size()) {
            System.out.println("올바른 영화관 번호가 아닙니다.");
            return;
        }
        String selectedLocation = locations.get(locationChoice - 1);

        // 날짜 선택
        System.out.println("예매 가능한 날짜를 선택하세요: ");
        List<LocalDate> availableDates = screenings.stream()
                .map(Screening::getScreeningDate)
                .distinct()
                .sorted()  // 날짜 순으로 정렬
                .toList();

        for (int i = 0; i < availableDates.size(); i++) {
            System.out.println((i + 1) + ". " + availableDates.get(i));
        }

        System.out.print("날짜 번호 입력: ");
        int dateChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (dateChoice < 1 || dateChoice > availableDates.size()) {
            System.out.println("올바른 날짜 번호가 아닙니다.");
            return;
        }

        LocalDate selectedDate = availableDates.get(dateChoice - 1);
        System.out.println("선택한 날짜: " + selectedDate);

        // 3단계: 선택한 장소에 따른 상영 시간대 출력
        List<Screening> filterByLocationScreening = screenings.stream()
                .filter(screening -> screening.getTheater().getLocation().equals(selectedLocation))
                .toList();
        Set<LocalTime> times = filterByLocationScreening.stream()
                .map(screening -> screening.getStartTime())
                .collect(Collectors.toSet());

        System.out.println("시작 시간을 선택해주세요");
        for (Screening screening : filterByLocationScreening) {
            System.out.println(screening.getStartTime() + " ~ " + screening.getEndTime());
        }
        System.out.print("--:--와 같은 형식으로 시간 입력 : ");
        String inputTime = scanner.nextLine();
        LocalTime startTime = LocalTime.parse(inputTime);
        if (!times.contains(startTime)) {
            System.out.println("올바른 영화 시간대가 아닙니다.");
            return;
        }

        // 4단계: 선택된 상영 정보 출력
        Screening selectedScreening = new Screening();
        for (Screening screening : filterByLocationScreening) {
            if (screening.getStartTime().equals(startTime)) {
                selectedScreening = screening;
                break;
            }
        }
        System.out.println("✅ 선택하신 영화:");
        System.out.println("영화제목: " + selectedScreening.getMovie().getTitle());
        System.out.println("영화관: " + selectedScreening.getTheater().getLocation());
        System.out.println("날짜: " + selectedScreening.getScreeningDate());
        System.out.println("시간: " + selectedScreening.getStartTime() + " ~ " + selectedScreening.getEndTime());
        System.out.println("-------------------------------");

        //인원 선택
        System.out.print("인원: ");
        int peopleCount = scanner.nextInt();
        scanner.nextLine();

        //좌석표 출력
        int availableSeats = movieService.getAvailableSeatsByScreening(selectedScreening);
        System.out.println("이용가능좌석: " + availableSeats);
        movieService.printSeatMap(selectedScreening.getId());

        List<String> reservedSeats = movieService.getReservedSeatsByScreeningId(selectedScreening.getId());

        // 좌석 선택
        List<SeatRequest> seatList = new ArrayList<>();
        for (int i = 0; i < peopleCount; i++) {
            while (true) {
                System.out.print("예약할 좌석 번호를 입력하세요 (예: A1): ");
                String seatInput = scanner.nextLine().trim().toUpperCase();
                if (!seatInput.matches("^[A-J](10|[1-9])$")) {
                    System.out.println("좌석 번호는 A1 ~ J10 사이여야 합니다. 다시 입력하세요.");
                    continue;
                }
                if (reservedSeats.contains(seatInput)) {
                    System.out.println("이미 예약된 좌석입니다. 다른 좌석을 선택하세요.");
                    continue;
                }
                char row = seatInput.charAt(0);
                int col = Integer.parseInt(seatInput.substring(1));
                seatList.add(new SeatRequest(row, col));
                reservedSeats.add(seatInput);
                break;
            }
        }

        //결제
        PayService payService = new PayService(memberService);
        int totalPrice = selectedMovie.getPrice() * seatList.size();
        Map<String, Integer> payMap = payService.pay(member, totalPrice, scanner);

        //예매정보 db저장
        reservationService.save(member, selectedScreening, seatList,payMap.getOrDefault("cash", 0), payMap.getOrDefault("credit", 0));

        System.out.print("예매내역을 조회하시겠습니까? (1: 네 / 2: 나가기): ");
        String viewChoice = scanner.nextLine();

        if (viewChoice.equals("1")) {
            System.out.println("-------------예매내역 조회-------------");
            System.out.println("영화제목: " + selectedScreening.getMovie().getTitle());
            System.out.println("날짜: " + selectedScreening.getScreeningDate());
            System.out.println("시간: " + selectedScreening.getStartTime() + " ~ " + selectedScreening.getEndTime());
            System.out.println("영화관: " + selectedScreening.getTheater().getLocation());
            System.out.println("총 인원: " + peopleCount);

            // 좌석 정보 출력
            System.out.print("좌석: ");
            for (int i = 0; i < seatList.size(); i++) {
                SeatRequest seat = seatList.get(i);
                System.out.print(seat.getRow() + "열 " + seat.getCol() + "번");
                if (i < seatList.size() - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println(); // 줄바꿈
            System.out.println("-----------------------------------");
        }
    }

    @Override
    public void setMember(Member member) {
        this.member = member;
    }

    @Override
    public boolean requiresLogin() {
        return true;
    }

}

