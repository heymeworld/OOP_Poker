import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    @Test
    public void test() {
        Game game = new Game();
        game.addPlayer("Alice");
        game.addPlayer("Bob");
        game.addPlayer("Charlie");
        game.addPlayer("Dance monkey Swarove");
        assertEquals(4, game.players.size());
//        game.addPlayer("Elvin");

        game.playGames();
        game.playGames();  // 재게임

        /* 만약에 같은 인원으로 게임을 다시 한다면? -> game.playGames(); 호출로 해결
        -> game.playGames(play(dealCards(shuffle()))) 이런 식으로 카드를 나눠주기 전 결국 shuffle()을 호출하기 때문에 게임 다시 시작할 때 신규는 shuffle()뿐이고 객체는 재활용됨
        -> 대신에 최종 결과 출력 시, players.sort()로 players 리스트에 순서 변형이 생기기 때문에 게임 재시작 시에도 변동된 순서로 객체 나열됨*/

        /* 같은 랭크에서 동점이 나온다면? -> findWinner()에 구현: 높은 점수로 다시 승패 가리고, 거기서도 동점이면 카드 무늬로 승패 결정
        카드 무늬당 숫자는 Unique 하기 때문에 중복이 더 이상 나올 수 없음         */
    }
}