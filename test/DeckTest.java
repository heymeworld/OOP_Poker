import org.junit.jupiter.api.Test;

public class DeckTest {
    // 카드 뽑기, 카드 섞기 테스트
    @Test public void Test1() {
        Deck d = new Deck();	   // 카드 한 벌(Deck)을 만든다.
        Card c = d.pick();         // 섞이기 전. 첫 카드로 SPADE, A 나온다
        System.out.println(c);     // System.out.println(c.toString());

        // 카드 뭉치 전체 출력
        for (int i=1; i<d.CARD_NUM; i++) {
            c = d.pick();
            System.out.println(c);
        }

        System.out.println();

        // 섞인 후. 카드 잘 섞이는지 확인 ok --> 게임 다시 할 때, Deck 객체 생성하지 말고, shuffle() 사용하면 될 듯
        d.shuffle();
        for (int i=1; i<d.CARD_NUM; i++) {
            c = d.pick();
            System.out.println(c);
        }
    }
}