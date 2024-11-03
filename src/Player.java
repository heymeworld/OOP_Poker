import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Player class
public class Player {
    // 중복 닉네임 검증용. 순서 필요없고, 중복 허용 X SET 사용
    private static final Set<String> NICKNAMES = new HashSet<>();

    String nickName;
    int money = 10000;  // 게임머니 10,000원
    int wins;           // 승리 기록
    int loses;          // 패배 기록
    List<Card> cards = new ArrayList<>();  // 객체 추가, 삭제가 많아서 List 사용

    // Player 생성자. 닉네임 필수
    Player(String nickname) {
        if (nickname.length() > 20) {
            throw new IllegalArgumentException("닉네임을 20글자 이하로 설정해주세요.");
        }
        if (NICKNAMES.contains(nickname)) {
            throw new IllegalArgumentException("이미 등록된 닉네임입니다.");
        }

        this.nickName = nickname;
        NICKNAMES.add(nickname);  // 고유 닉네임 저장
    }

    // 카드 받기
    void receiveCard(Card card) {
        cards.add(card);
    }
    
    // 카드패 비우기
    void clearCards() {
        cards.clear();
    }

    @Override
    public String toString() {
        return nickName + " - Wins: " + wins + ", Losses: " + loses + ", Money: " + money;
    }

    // 게임 종료 후 닉네임 초기화
    static void resetNicknames() {
        NICKNAMES.clear();
    }

}  // Player class 끝
