// Card class
class Card {
    static final int KIND_MAX = 4;	// 카드 무늬의 수 : "CLOVER", "HEART", "DIAMOND", "SPADE"
    static final int NUM_MAX  = 13;	// 무늬별 카드 수 : "A23456789XJQK"

    // 무늬 순서 : S > D > H > C
//    static final int SPADE   = 4;
//    static final int DIAMOND = 3;
//    static final int HEART   = 2;
//    static final int CLOVER  = 1;
    
    // 카드 한 장의 속성
    int kind;
    int number;
    
    // 카드 생성자. 카드 배열 초기화할 때 사용
    Card(int kind, int number) {
        this.kind = kind;
        this.number = number;
    }

    public String toString() {
        // 빈 공백 요소는 kinds 배열이 kind 값을 그대로 인덱스로 사용할 수 있게 하려는 의도
        // 이 배열 구조 덕분에 kind 값이 1이면 kinds[1]인 "CLOVER", kind 값이 4이면 kinds[4]인 "SPADE"로 간단히 접근할 수 있음
        // numbers 배열의 맨 앞 '0'도 같은 의도
        String[] kinds = {"", "CLOVER", "HEART", "DIAMOND", "SPADE"};
        String numbers = "0A23456789XJQK"; // 숫자 10은 X로 표현, A는 1로 고정

        return "kind : " + kinds[this.kind] + ", number : " + numbers.charAt(this.number);
    }

    public static String getKind(int kind) {
        String[] kinds = {"", "CLOVER", "HEART", "DIAMOND", "SPADE"};

        // kind가 1에서 4 사이일 때 해당하는 무늬를 반환, 범위 바깥 값은 "Unknown" 반환
        if (kind >= 1 && kind <= 4) {
            return kinds[kind];
        } else {
            return "Unknown";
        }
    }

} // Card class 끝
