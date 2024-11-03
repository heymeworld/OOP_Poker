import java.util.*;
import java.util.stream.Collectors;

// Dealer class
public class Dealer {
    // 카드 뭉치 꺼내기 + 섞기까지 완료
    Deck deck = new Deck();

    // 카드 나눠주기
    void dealCards(List<Player> players) {
        deck.shuffle();  // Deck class 객체 생성할 때 섞이지만, 재게임시 다시 섞어야 함

        for (Player player : players) {
            player.clearCards();           // 카드패 비우기
            for (int i = 0; i < 5; i++) {  // player 마다 카드 나눠주기
                player.receiveCard(deck.pick());
            }
        }
    }

    // 카드 랭크에 따른 score 반환 (숫자로 비교할 수 있으니까)
    int evaluateCard(List<Card> cards) {
        // 13가지 Rule. 이 순서도 중요함. rank 가 높은 것부터 맞는 게 있는지 확인해야 제대로 걸리지
        if (isRoyalFlush(cards)) return 1000;
        if (isBackStraightFlush(cards)) return 900;
        if (isStraightFlush(cards)) return 800;
        if (isFourOfAKind(cards)) return 700;
        if (isFullHouse(cards)) return 600;
        if (isFlush(cards)) return 500;
        if (isMountain(cards)) return 400;
        if (isBackStraight(cards)) return 300;
        if (isStraight(cards)) return 200;
        if (isThreeOfAKind(cards)) return 100;
        if (isTwoPair(cards)) return 50;
        if (isOnePair(cards)) return 20;
        return highCardScore(cards); // 노 페어 점수
    }

    String myRank(int score) {
        if (score == 1000) return "RoyalStraightFlush";
        if (score == 900) return "BackStraightFlush";
        if (score == 800) return "StraightFlush";
        if (score == 700) return "FourCards";
        if (score == 600) return "FullHouse";
        if (score == 500) return "Flush";
        if (score == 400) return "Mountain";
        if (score == 300) return "BackStraight";
        if (score == 200) return "Straight";
        if (score == 100) return "Triple";
        if (score == 50) return "TwoPair";
        if (score == 20) return "OnePair";
        return "NoPair";
    }
    
    /* 각 페어 평가 메서드 */

    // 1. 로얄 스트레이트 플러쉬 (로티플) : 10, J, Q, K, A를 같은 무늬로 모은 경우
    boolean isRoyalFlush(List<Card> cards) {
        // 모양 전부 같은지 && { 10, J, Q, K, A } 가 card 리스트(카드패)에 있는지
        return isFlush(cards) && hasRanks(cards, 10, 11, 12, 13, 1);
    }

    // 2. 백스트레이트 플러쉬 : A, 2, 3, 4, 5를 같은 무늬로 모은 경우
    boolean isBackStraightFlush(List<Card> cards) {
        // 모양 전부 같은지 && { A, 2, 3, 4, 5 } 가 card 리스트(카드패)에 있는지
        return isFlush(cards) && hasRanks(cards, 1, 2, 3, 4, 5);
    }

    // 3. 스트레이트 플러쉬 (스티플) : 연속되는 숫자 5개를 같은 무늬로 모은 경우
    boolean isStraightFlush(List<Card> cards) {
        // 모양 전부 같은지 && 숫자들이 전부 연속되는지
        return isFlush(cards) && isStraight(cards);
    }

    // 4. 포커 : 무늬는 다르지만 같은 숫자 4개를 모은 경우
    boolean isFourOfAKind(List<Card> cards) {
        // cards 리스트 중에 같은 숫자 4개 가지고 있는지
        return hasSameNum(cards, 4);
    }

    // 5. 풀 하우스 : 무늬가 다른 같은 숫자 3개와 2개를 각각 모은 경우
    boolean isFullHouse(List<Card> cards) {
        /* 우선 getNumCounts(cards)으로 Map(card.num, numCount) 받아온 다음에,
        -> 개중에 Value 가 3 and 2 인게 있는지 */
        Map<Integer, Long> rankCounts = getNumCounts(cards);
        return rankCounts.containsValue(3L) && rankCounts.containsValue(2L);
    }

    // 6. 플러쉬 : 숫자에 관계 없이 같은 무늬의 카드를 5개 모은 경우
    boolean isFlush(List<Card> cards) {
        /* cards 리스트 -> 스트림 -> 중간연산 (allMatch : 주어진 조건을 만족하면 참)
        -> 람다식 : (Card card) { return card.kind == cards.getFirst().kind } 카드 무늬가 전부 일치하는지 */
        return cards.stream().allMatch(card -> card.kind == cards.getFirst().kind);
    }
    /* 스트림
        1. 스트림 생성 -> 2. 중간연산 -> 3. 최종연산
     */

    // 7. 마운틴 : 무늬에 관계 없이 10, J, Q, K, A를 모은 경우
    boolean isMountain(List<Card> cards) {
        // hasRanks: 카드들 중에 { 10, J, Q, K, A } 있는지 확인
        return hasRanks(cards, 10, 11, 12, 13, 1);
    }

    // 8. 백스트레이트 : 무늬에 관계 없이 A, 2, 3, 4, 5를 모은 경우
    boolean isBackStraight(List<Card> cards) {
        // hasRanks: 카드들 중에 { A, 2, 3, 4, 5 } 있는지 확인
        return hasRanks(cards, 1, 2, 3, 4, 5);
    }

    // 9. 스트레이트 : 무늬에 관계없이 연속되는 숫자 5개를 모은 경우
    boolean isStraight(List<Card> cards) {
        // List : 순서, 중복 o -> ranks : 내가 찾고자 하는 숫자들의 List
        /* cards 리스트 -> 스트림 -> 중간연산 (map: cards 리스트의 card 객체에서 number 만 빼다가 새로운 스트림 만듦)
        -> 중간연산 (sorted: 새로운 card.number 스트림을 오름차순 정렬)
        -> Collectors.toList(): 데이터 집합을 List 로 수집해서 -> 최종연산 (collect: 그룹별 reduce) -> */
        List<Integer> ranks = cards.stream().map(card -> card.number).sorted().collect(Collectors.toList());

        // 위 숫자 리스트 ranks 에서 하나씩 꺼내가지고 연속되는지 확인
        for (int i = 0; i < ranks.size() - 1; i++) {
            if (ranks.get(i) + 1 != ranks.get(i + 1))  // 어떤 숫자(ranks.get(i))를 꺼냈는데, 그 숫자에 +1을 더한 게, 다음 차례(NextValue) 숫자(ranks.get(i + 1))랑 같아??
                return false;  // 다르면 바로 false;
        }
        return true;           // 전부 통과하면 true;
    }

    // 10. 트리플 (봉) : 무늬에 관계 없이 같은 숫자를 3장 모은 경우
    boolean isThreeOfAKind(List<Card> cards) {
        // cards 리스트 중에 같은 숫자 3개 가지고 있는지
        return hasSameNum(cards, 3);
    }

    // 11. 투 페어 : 같은 숫자 두 개(원페어)가 두 쌍이 있는 패
    boolean isTwoPair(List<Card> cards) {
        /* cards 리스트 -> Map(card.number, numCount)으로 변환 -> values(): value 값만 가져와서 리스트로 변환 -> 스트림으로 만듦
        -> 중간연산 (filter: 조건에 맞게 걸러내기) -> 그 조건은 (int count) { return count == 2 } : 어떤 card.number 가 두 번 있는지(= 원페어가 있냐는 말)
        -> 이걸 filter 하면, 원페어인 요소(values 의 value, numCount 값)들만 남게됨 -> .count() == 2 : 그래서 그 원페어인 요소가 총 두 쌍임? 두 쌍이면 True */
        return getNumCounts(cards).values().stream().filter(count -> count == 2).count() == 2;
    }

    // 12. 원 페어 : 무늬에 관계 없이 같은 숫자 2장을 모은 경우
    boolean isOnePair(List<Card> cards) {
        // cards 리스트 중에 같은 숫자 2개 가지고 있는지
        return hasSameNum(cards, 2);
    }

    // 13. 노 페어 (하이 카드) : 그 누구도 족보를 완성하지 못했다면 가지고 있는 카드 중 가장 높은 등급의 카드를 가진 사람이 이김
    int highCardScore(List<Card> cards) {
        /* cards 리스트 -> 스트림 -> card.number 숫자만 뽑아서 IntStream 으로 변환 -> 그 card.number 숫자들중에 max 값 찾기
        -> .orElse(0) : 최대값이 없으면 0으로 처리해주세요 (위에 랭크들이 그렇게 많았는데, 노 페어까지 올 정도면 들고있는 카드가 있긴 하거냐? 확인해봐라, 카드가 아예 없을 수도 있다 */
        return cards.stream().mapToInt(card -> card.number).max().orElse(0);
    }

    /* 랭크 평가에 필요한 공통 메서드들 */
    
    // 카드패의 숫자별로(중복X) 총 몇회씩 나오는지 카운팅한 것을 Map 으로 반환해주는 메서드
    private Map<Integer, Long> getNumCounts(List<Card> cards) {
        /* cards 리스트 -> 스트림 -> Collectors.groupingBy(Integer(Key: 그룹화 기준), Long(Value: 집계작업)): 주어진 Key 에 맞게 그룹화 -> 집계 함수나 연산 들어오면 그룹별로 집계 작업해서 -> Map<Integer, Long> 형태의 결과로 반환
        -> card -> card.number: card 객체에서 number 만 빼서 그룹화 기준 삼고(Key, 중복X),
           Collectors.counting(): number 별 등장 횟수를 Key당 Value로 적음(사실상 리스트를 두 번 도는거지) -> 최종연산 (collect: 그룹별 reduce) */
        return cards.stream().collect(Collectors.groupingBy(card -> card.number, Collectors.counting()));
    }

    // 같은 숫자가 뭐든, count 만큼 가지고 있으면 True, 없으면 False 반환해주는 메서드
    private boolean hasSameNum(List<Card> cards, int count) {
        /* getNumCounts(cards): Map(card.number, numberCount)
        -> containsValue((long) count): 매개변수로 받은 count가 위 Map의 Value 중에 있으면 True, 없으면 False */
        return getNumCounts(cards).containsValue((long) count);
    }

    // hasRanks() : rank 별로 찾고자 하는 숫자들을 해당 카드패(cards 리스트)가 가지고 있는지 확인하는 메서드
    // int... ranks : 여러 개의 정수를 받을 수 있는 가변 인자. 찾고자 하는 숫자를 입력한다
    private boolean hasRanks(List<Card> cards, int... ranks) {
        // Set : 순서, 중복 X -> rankSet : rank 별로 찾고자 하는 숫자들의 Set
        /* 가변 인자 ranks 배열 -> 스트림 -> 중간연산 (boxed: 기본 타입 int -> 객체 타입 Integer 로 변환)
        -> Collectors.toSet(): 데이터 집합을 Set 으로 수집해서) -> 최종연산 (collect: 그룹별 reduce) -> */
        Set<Integer> rankSet = Arrays.stream(ranks).boxed().collect(Collectors.toSet());
        /* cards 리스트 -> 스트림 -> 중간연산 (allMatch : 주어진 조건을 만족하면 참)
        -> 람다식 : (Card card) { return rankSet.contains(card.number) } 현재 카드의 숫자가 rankSet 에 포함되어 있는지 */
        return cards.stream().allMatch(card -> rankSet.contains(card.number));
    }

    // 가장 높은 숫자(highCardNum) 카드의 무늬(kind)를 찾는 메서드
    private int getHighCardKind(List<Card> cards, int highCardNumber) {
        /* cards 리스트 -> 스트림 -> 중간연산 (filter: 조건에 맞는 요소만 걸러내기) -> 그 조건은 (Card card) { return card.number == highCardNumber } 똑같으면 True
        -> 중간연산 (mapToInt: 남은 cards 리스트 요소(card 객체)들중 card.kind(변수) 만 반환해서 Int로 변환) -> 중간연산 (max: 개중 가장 큰 수 뽑기)
        -> orElse: 리스트가 비어 있는 경우 0 반환 (사실상 발생하지 않음) */
        return cards.stream().filter(card -> card.number == highCardNumber).mapToInt(card -> card.kind).max().orElse(0);
    }


    Player findWinner(List<Player> players) {
        // 일단 첫 번째 Player 로 지역변수 초기화
        Player winner = players.getFirst();
        int maxScore = evaluateCard(winner.cards);
        int maxCardNum = highCardScore(winner.cards);
        int maxCardKind = getHighCardKind(winner.cards, maxCardNum);

        for (Player player : players) {  // 반복문으로 플레이어 바꿔가며 점수 비교
            int score = evaluateCard(player.cards);  // player 마다 갖고 있는 카드패에 점수 매김
            int highCardNum = highCardScore(player.cards);
            int highCardKind = getHighCardKind(player.cards, highCardNum);

            // 첫 번째 Player 의 점수인 maxScore 와 비교 시작
            // 1. 점수가 더 높은 경우 즉시 우승자 갱신
            if (score > maxScore ||
                // 2. 플레이어 간의 랭크가 같다면, 가장 큰 숫자 카드를 가진 경우 우승자 결정
                (score == maxScore && (highCardNum > maxCardNum ||
                // 3. 가장 큰 숫자마저 같다면, 가장 큰 무늬 비교
                (highCardNum == maxCardNum && highCardKind > maxCardKind)))) {
                winner = player;
                maxScore = score;
                maxCardNum = highCardNum;
                maxCardKind = highCardKind;
            }
            System.out.printf("%-20s => %s(%d:%s) %s%n", player.nickName, myRank(score), highCardNum, Card.getKind(highCardKind), player.cards);
        }
        return winner;  // 최종 winner 반환
    }

} // Dealer class 끝