package movie;

import member.Member;

public class Reservation {
    private int id;
    private Screening screening;
    private int peopleCount;
    private Member member;

    public Reservation(int id, Screening screening, int peopleCount, Member member) {
        this.id = id;
        this.screening = screening;
        this.peopleCount = peopleCount;
        this.member = member;
    }

    public int getId() {
        return id;
    }

    public Screening getScreening() {
        return screening;
    }

    public int getPeopleCount() {
        return peopleCount;
    }

    public Member getMember() {
        return member;
    }
}
