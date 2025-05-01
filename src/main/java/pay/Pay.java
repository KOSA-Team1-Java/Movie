package pay;

import member.Member;

public interface Pay {
    boolean pay(Member member, int amount);
}
