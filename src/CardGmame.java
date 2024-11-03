public class CardGmame {
    public static void main(String[] args) {
        Game game = new Game();
        game.addPlayer("Alice");
        game.addPlayer("Bob");
        game.addPlayer("Charlie");
        game.addPlayer("Dance monkey Swarove");
//        game.addPlayer("Elvin");

        game.playGames();
    }
}
