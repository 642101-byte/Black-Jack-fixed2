How to Play

Enter a bet amount and press Enter
Click "Hit" to draw another card
Click "Stand" to end your turn
Dealer draws cards until they reach 17 or higher
Your chips are automatically saved when you close the game

Features

Chip persistence across sessions
Automatic deck reshuffling
Blackjack detection (21 with 2 cards)
Dealer AI follows standard casino rules (must hit until 17)


Topic Integration in Blackjack JavaFX
Topic 1: Java overview, JVM, OOP concepts

Where: Throughout the project, especially in Card.java, Deck.java, Player.java, Dealer.java, and BlackjackGame.java
Demonstrated: Object-Oriented Programming: The project models real-world entities like cards, decks, players, and dealers. Encapsulation: Private fields with public getters/setters. Abstraction: Common behavior for participants is represented in the Participant abstract class, extended by Player and Dealer.
Example: Player class with private fields name and chips and public methods adjustChips() and getChips().

Topic 2: Variables, types, input/output

Where: BlackjackGame.java, Player.java, Dealer.java
Demonstrated: Primitive types: int for chip amounts and bet values. Reference types: String, Card[], TextArea, TextField. Input: Text input for bets via JavaFX TextField. Output: JavaFX TextArea to display hand values, chips, and game results.
Example: private int chips; in Player.java, TextField betField = new TextField(); in BlackjackGame.java.

Topic 3: Control flow: if, switch, loops

Where: BlackjackGame.java, Player.java, Dealer.java, Deck.java
Demonstrated: If-else statements: Checking busts, blackjack, or win conditions. Loops: for loops to iterate over card arrays and calculate hand totals, while loop for dealer AI hitting until 17.
Example:

javawhile (dealer.handValue() < 17) {
    dealer.addCard(deck.dealCard());
}
Topic 4: Exceptions (intro), debugging

Where: BlackjackGame.java
Demonstrated: Handling invalid input with try-catch blocks for file I/O. Input validation prevents NumberFormatException by using regex matching.
Example:

javatry (BufferedReader in = new BufferedReader(new FileReader(saveFile))) {
    return Integer.parseInt(in.readLine());
} catch (Exception e) {
    return 100;
}
Topic 5: Methods, parameters, blocks, scope

Where: All class files
Demonstrated: Methods with parameters like addCard(Card card), handValue(), and adjustChips(int amount). Scope is demonstrated with local variables inside methods and instance variables at the class level. Methods are structured with clear blocks to perform single responsibilities.
Example:

javapublic int handValue() {
    int total = 0;
    int aceCount = 0;
    for (int i = 0; i < handSize; i++) {
        total += hand[i].getValue();
        if (hand[i].getRank().equals("Ace")) {
            aceCount++;
        }
    }
    while (total > 21 && aceCount > 0) {
        total -= 10;
        aceCount--;
    }
    return total;
}
Topic 6: Arrays & ArrayLists

Where: Participant.java, Dealer.java, Deck.java
Demonstrated: Array initialization and manipulation (Card[] hand, Card[] cards). Iteration over arrays to calculate totals or display cards. Adding cards dynamically during gameplay using array indexing.
Example:

javaprotected Card[] hand = new Card[12];
protected int handSize = 0;

public void addCard(Card card) {
    hand[handSize++] = card;
}
Topic 7: Objects & classes

Where: All Java files
Demonstrated: Class declarations for each entity (Card, Deck, Player, Dealer, Participant). Object instantiation like Player player = new Player("Player", 100);. Constructors set up decks, players, and initial hands.
Example:

javaDealer dealer = new Dealer();
Deck deck = new Deck();
Player player = new Player("Player", 500);
Topic 8: Abstract classes & interfaces

Where: Participant.java, Player.java, and Dealer.java
Demonstrated: Participant is an abstract class with common behavior for both players and dealers. Dealer and Player extend Participant, reusing methods like addCard() and handValue(). Demonstrates inheritance and method reuse.
Example:

javapublic abstract class Participant {
    protected Card[] hand = new Card[12];
    protected int handSize = 0
    
public void addCard(Card card) 
{ hand[handSize++] = card; } 
public int handValue() 
{ // implementation }
}

public class Dealer extends Participant {
    public void playTurn(Deck deck) {
        while (handValue() < 17) {
            addCard(deck.dealCard());
        }
    }
}
Topic 9: Files

Where: BlackjackGame.java
Demonstrated: Saving and loading player chips using BufferedReader and PrintWriter with FileReader and FileWriter. Handling file I/O exceptions with try-catch blocks. File persistence allows the player to continue their chips after closing the game.
Example:

javaprivate void saveChips() {
    try (PrintWriter out = new PrintWriter(new FileWriter(saveFile))) {
        out.println(player.getChips());
    } catch (IOException ignored) {}
}

private int loadChips() {
    if (!saveFile.exists()) return 100;
    try (BufferedReader in = new BufferedReader(new FileReader(saveFile))) {
        return Integer.parseInt(in.readLine());
    } catch (Exception e) {
        return 100;
    }
}
Topic 10: JavaFX

Where: BlackjackGame.java
Demonstrated: JavaFX Application lifecycle using start(Stage stage). UI components include Buttons, Labels, TextFields, TextArea, HBox/VBox layouts. Event handling for player actions like "Hit", "Stand", and bet submission. Scene and Stage management with dynamic updates to the UI.
Example:

javahitButton.setOnAction(e -> handlePlayerHit());
standButton.setOnAction(e -> handlePlayerStand());
betField.setOnAction(e -> handleBetSubmission());
Topic 11: Robustness & coding standards

Where: All files
Demonstrated: Input validation for bets using regex matching, proper exception handling for invalid input or file errors. Clear variable names and consistent indentation. Separation of concerns: GUI class handles interface, game logic resides in Player/Dealer/Deck classes.
Example:

javaif (betText.isEmpty() || !betText.matches("\\d+")) {
    appendLog("Enter a valid bet amount.");
    return;
}
if (currentBet > player.getChips()) {
    appendLog("You do not have enough chips.");
}
Topic 12: Multithreading

Where: Removed from final implementation
Demonstrated: Originally included background autosave using ScheduledExecutorService, but was removed to comply with course requirements (no java.util.concurrent until CISC191). Chips are now saved only when the game closes via stop() method.
Note: Multithreading feature was intentionally removed to meet course guidelines.
