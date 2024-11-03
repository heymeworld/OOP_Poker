import java.util.ArrayList;
import java.util.List;

// Game class
public class Game {
    List<Player> players = new ArrayList<>();  // Players 묶음 생성
    Dealer dealer = new Dealer();              // Dealer 불러오기

    // 플레이어 입장
    void addPlayer(String nickName) {
        /* if문 조건이 'size >= 4'인 이유 : 선 조건, 후 실행(size++;)이라서. 'size == 4' 이면 그만 추가해야 하는데,
           'size > 4' 이렇게 적으면, size 가 4인 상태에서 일단 추가하고 '5'부터 조건에 걸리기 때문에.
           그렇다고 조건 문을 add() 뒤에 적으면, do ~ while 식으로 5번째 객체가 일단 추가된 다음에 문이 닫힐 것이기 때문에 안 됨 */
        if (players.size() >= 4) {
            throw new IllegalStateException("방에 인원이 가득 찼습니다. (현재 인원: 4명)");
        }
        players.add(new Player(nickName));
    }

    // '게임' 자체
    void play() {
        dealer.dealCards(players);  // 카드 나눠주기
        Player winner = dealer.findWinner(players);

        // 게임에서 승리한 플레이어는 상금 100원과 1승이 추가되고, 나머지 플레이어는 상금 0원과 1패가 추가된다
        for (Player player : players) {
            if (player == winner) {
                player.wins++;
                player.money += 100;
                System.out.printf("========================================================================= %s - You Win ", player.nickName);
            } else {
                player.loses++;
            }
        }
    }

    // 100번의 '게임'을 자동 반복
    void playGames() {
        System.out.println("Game Start !!!   ...");
        System.out.println();

        for (int i = 1; i <= 100; i++) {
            play();
            System.out.printf("round %d !! =========================================================================%n%n", i);
        }

        // *람다 함수 : (p1, p2) -> p2.wins - p1.wins
        // method(Player p1, Player p2) {
        //      return p2.wins - p1.wins; }
        // *sort() : 반환값을 통해 양수, 음수, 0을 기준으로 정렬한다
        // 알고리즘 자체는 위와 같지만, 피연산자의 순서에 따라 정렬 순서가 달라진다
        // p1.wins - p2.wins : 오름차순
        // p2.wins - p1.wins : 내림차순
        players.sort((p1, p2) -> p2.wins - p1.wins);  // 승리의 수가 많은 플레이어부터 내림차순
        System.out.println("========== Final Results =======================================================================================================================================================");
        for (Player player : players) {
            System.out.println(player);
        }
        System.out.println("================================================================================================================================================================================");
        System.out.printf("%n%n");
    }

} // Game class 끝