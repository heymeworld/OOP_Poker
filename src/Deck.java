// Deck class
class Deck {
    final int CARD_NUM = 52;	          // (상수) 카드의 개수
    Card[] cardArr = new Card[CARD_NUM];  // 게임에 사용할  카드 묶음. READ 용이라 배열 사용
    int top;                              // 카드 배열에서 카드 뽑기를 위한 인덱스

    // Deck 생성자
    Deck () {	  // Deck의 카드를 초기화
        int i=0;  // 카드 배열 초기화를 위한 인덱스

        // 4(무늬) * 13(숫자) = 총 52장의 카드 객체들이 배열로 초기화
        for (int k = Card.KIND_MAX; k > 0; k--) {
            for (int n = 0; n < Card.NUM_MAX; n++) {
                cardArr[i++] = new Card(k, n + 1);  // n+1 : 이렇게 하면 numbers[1]부터 시작
            }
        }
        // shuffle();  // 초기화하고, 바로 섞기 --> Game.playGames(Game.play(Dealer.dealCards()))로 호출하면서 shuffle() 하기 때문에 필요 없을 듯
    }

    // 카드 섞기(위에서 초기화한 카드 배열을 섞는 것이다)
    void shuffle() {
        for(int i=0; i < cardArr.length; i++) {
            int r = (int)(Math.random() * CARD_NUM);

            Card temp = cardArr[i];
            cardArr[i] = cardArr[r];
            cardArr[r] = temp;
        }
        top = 0;  // 카드 뽑기 인덱스 0으로 초기화
    }

//    Card pick(int index) {	// 지정된 위치(index)에 있는 카드 하나를 꺼내서 반환
//        return cardArr[index];
//    }
//
//    Card pick() {			// Deck에서 카드 하나를 선택한다.
//        int index = (int)(Math.random() * CARD_NUM);
//        return pick(index);
//    }

    // 카드 뽑기
    Card pick() {
        return cardArr[top++];
    }

} // Deck class 끝