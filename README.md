# Topic Integration in Blackjack JavaFX

## Topic 1: Java overview, JVM, OOP concepts
- **Where**: Throughout the project, especially in `Card.java`, `Deck.java`, `Player.java`, `Dealer.java`, and `BlackjackGUI.java`  
- **Demonstrated**: Object-Oriented Programming: The project models real-world entities like cards, decks, players, and dealers. Encapsulation: Private fields with public getters/setters. Abstraction: Common behavior for players is represented in the `Player` class, extended by `Dealer`.  
- **Example**: `Player` class with private fields `hand` and `chips` and public methods `addCard()`, `handValue()`, and `getChips()`.  

## Topic 2: Variables, types, input/output
- **Where**: `BlackjackGUI.java`, `Player.java`, `Dealer.java`  
- **Demonstrated**: Primitive types: `int` for chip amounts, `boolean` for game state flags. Reference types: `String`, `ArrayList<Card>`, `List<Card>`. Input: Text input for bets via JavaFX TextFields. Output: JavaFX labels and console logs to display hand values, chips, and game results.  
- **Example**: `private int chips;` in `Player.java`, `TextField betInput = new TextField();` in `BlackjackGUI.java`.  

## Topic 3: Control flow: if, switch, loops
- **Where**: `BlackjackGUI.java`, `Player.java`, `Dealer.java`  
- **Demonstrated**: If-else statements: Checking busts, blackjack, or win conditions. Switch statements: Optional, e.g., handling player choices like “hit” or “stand”. Loops: Iterating over hands in arrays/ArrayLists to calculate totals and display cards.  
- **Example**: `for (Card c : hand) total += c.getValue();` in `Player.handValue()`.  

## Topic 4: Exceptions (intro), debugging
- **Where**: `BlackjackGUI.java`  
- **Demonstrated**: Throwing exceptions for invalid bet amounts. Handling invalid input with try-catch blocks.  
- **Example**: 
try {
    int bet = Integer.parseInt(betInput.getText());
} catch (NumberFormatException e) {
    showAlert("Invalid bet amount");
}

## Topic 5: Methods, parameters, blocks, scope
- **Where**: All class files  
- **Demonstrated**: Methods with parameters like `addCard(Card c)`, `handValue()`, and `placeBet(int amount)`. Scope is demonstrated with local variables inside methods and instance variables at the class level. Methods are structured with clear blocks to perform single responsibilities.  
- **Example**: 
public int handValue() {
    int total = 0;
    for (Card c : hand) {
        total += c.getValue();
    }
    return total;
}
## Topic 6: Arrays & ArrayLists
- **Where**: Player.java, Dealer.java, Deck.java  
- **Demonstrated**: ArrayList initialization and manipulation (ArrayList<Card> hand). Iteration over hands to calculate totals or display cards. Adding and removing cards dynamically during gameplay.  
- **Example**: 
hand.add(newCard);
for (Card c : hand) {
    System.out.println(c);
}
## Topic 7: Objects & classes
- **Where**: All Java files  
- **Demonstrated**: Class declarations for each entity (Card, Deck, Player, Dealer). Object instantiation like `Player player = new Player(100);`. Constructors set up decks, players, and initial hands.  
- **Example**: 
Dealer dealer = new Dealer();
Deck deck = new Deck();
Player player = new Player(500);
## Topic 8: Abstract classes & interfaces
- **Where**: Player.java and Dealer.java  
- **Demonstrated**: `Dealer` extends `Player`, reusing player methods and overriding behavior such as automatic hits until hand value reaches 17. Demonstrates method overriding and polymorphism.  
- **Example**: 
public class Dealer extends Player {
    @Override
    public void playTurn(Deck deck) {
        while (handValue() < 17) {
            addCard(deck.draw());
        }
    }
}
## Topic 9: Files
- **Where**: BlackjackGUI.java  
- **Demonstrated**: Saving and loading player chips using `BufferedReader` and `BufferedWriter`. Handling file I/O exceptions with try-catch blocks. File persistence allows the player to continue their chips after closing the game.  
- **Example**: 
Path saveFile = Paths.get("playerChips.txt");
try (BufferedWriter writer = Files.newBufferedWriter(saveFile)) {
    writer.write(String.valueOf(player.getChips()));
} catch (IOException e) {
    e.printStackTrace();
}
## Topic 10: JavaFX
- **Where**: BlackjackGUI.java  
- **Demonstrated**: JavaFX Application lifecycle using `start(Stage primaryStage)`. UI components include Buttons, Labels, TextFields, HBox/VBox, and Alerts. Event handling for player actions like "Hit", "Stand", and "Bet". Scene and Stage management with dynamic updates to the UI.  
- **Example**: 
hitButton.setOnAction(e -> playerHits());
standButton.setOnAction(e -> dealerTurn());
betButton.setOnAction(e -> placeBet());
## Topic 11: Robustness & coding standards
- **Where**: All files  
- **Demonstrated**: Input validation for bets, proper exception handling for invalid input or file errors. Clear variable names and consistent indentation. Separation of concerns: GUI class handles interface, game logic resides in Player/Dealer/Deck classes.  
- **Example**: 
if (bet > player.getChips()) {
    showAlert("Not enough chips");
    return;
}
## Topic 12: Multithreading
- **Where**: Optional background autosave in BlackjackGUI.java  
- **Demonstrated**: Background thread periodically saves chips to file without freezing the UI. Uses `Platform.runLater()` to update UI safely from threads. Thread runs continuously while the application is active.  
- **Example**: 
Thread autosaveThread = new Thread(() -> {
    while (running) {
        saveChips();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
});
autosaveThread.setDaemon(true);
autosaveThread.start();
