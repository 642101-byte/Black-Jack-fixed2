package edu.sdccd.cisc;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.TextFormatter;
import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.*;
import java.util.function.UnaryOperator;

public class BlackjackGame extends Application {

    private Player player;
    private Dealer dealer = new Dealer();
    private Deck deck = new Deck();
    private Path saveFile = Paths.get("blackjack_save.txt");
    private TextArea gameLog = new TextArea();
    private TextField betField = new TextField();
    private Button hitButton = new Button("Hit");
    private Button standButton = new Button("Stand");
    private int currentBet = 0;
    private ScheduledExecutorService autoSaveExecutor; // Track executor for proper shutdown
    private static final int AUTO_SAVE_INTERVAL = 10; // Seconds (configurable)

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        // Initialize player with saved chips or default
        player = new Player("Player", loadChips());
        setupAutoSave();

        // Configure UI components
        gameLog.setEditable(false);
        configureBetInputValidation();

        // Layout setup
        VBox root = new VBox(10);
        HBox controls = new HBox(10);
        controls.getChildren().addAll(new Label("Bet:"), betField, hitButton, standButton);
        root.getChildren().addAll(new Label("Blackjack Game"), gameLog, controls);

        // Disable action buttons initially (only enable after valid bet)
        hitButton.setDisable(true);
        standButton.setDisable(true);

        // Event handlers
        betField.setOnAction(e -> handleBetSubmission());
        hitButton.setOnAction(e -> handlePlayerHit());
        standButton.setOnAction(e -> handlePlayerStand());

        // Stage configuration
        stage.setScene(new Scene(root, 600, 450));
        stage.setTitle("Blackjack GUI");
        stage.setOnCloseRequest(e -> cleanupResources()); // Ensure proper exit
        stage.show();

        appendLog("Welcome! You have " + player.getChips() + " chips. Enter a bet to start.");
    }

    /**
     * Validates bet input to allow only positive integers
     */
    private void configureBetInputValidation() {
        UnaryOperator<TextFormatter.Change> numericFilter = change -> {
            String newText = change.getControlNewText();
            // Allow empty input or digits only
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        };
        betField.setTextFormatter(new TextFormatter<>(numericFilter));
    }

    /**
     * Handles bet submission with validation
     */
    private void handleBetSubmission() {
        String betText = betField.getText().trim();
        if (betText.isEmpty()) {
            appendLog("Please enter a bet amount.");
            return;
        }

        try {
            currentBet = Integer.parseInt(betText);
            if (currentBet <= 0) {
                appendLog("Bet must be greater than 0.");
            } else if (currentBet > player.getChips()) {
                appendLog("Bet cannot exceed your chips (" + player.getChips() + ").");
            } else {
                // Start round if bet is valid
                startNewRound();
                hitButton.setDisable(false);
                standButton.setDisable(false);
                betField.setDisable(true);
            }
        } catch (NumberFormatException ex) {
            appendLog("Invalid bet format. Please enter a number.");
        }
    }

    /**
     * Handles player's "Hit" action (request another card)
     */
    private void handlePlayerHit() {
        player.addCard(deck.dealCard());
        logPlayerHand();

        if (player.handValue() > 21) {
            appendLog("You bust! (Over 21)");
            player.adjustChips(-currentBet);
            endRound();
        }
    }

    /**
     * Handles player's "Stand" action (end turn, let dealer play)
     */
    private void handlePlayerStand() {
        appendLog("You stand. Dealer's turn...");
        dealerTurn();
        resolveRound();
        endRound();
    }

    /**
     * Starts a new round: resets hands, shuffles deck, deals initial cards
     */
    private void startNewRound() {
        // Reset hands
        player.clearHand();
        dealer.clearHand();
        
        // Deal initial cards (player gets 2, dealer gets 2)
        player.addCard(deck.dealCard());
        dealer.addCard(deck.dealCard());
        player.addCard(deck.dealCard());
        dealer.addCard(deck.dealCard());

        // Check for player blackjack (instant win)
        if (player.handValue() == 21) {
            appendLog("Blackjack! You win!");
            player.adjustChips(currentBet);
            endRound();
            return;
        }

        // Show initial hands (dealer hides one card)
        appendLog("Dealer shows: " + dealer.getHand().get(0) + " and [Hidden Card]");
        logPlayerHand();
    }

    /**
     * Executes dealer's turn (automatic play: hits until ≥17)
     */
    private void dealerTurn() {
        while (dealer.handValue() < 17) {
            Card newCard = deck.dealCard();
            dealer.addCard(newCard);
            appendLog("Dealer draws: " + newCard);
        }
        appendLog("Dealer stands at " + dealer.handValue());
    }

    /**
     * Determines round outcome and updates chips
     */
    private void resolveRound() {
        int playerValue = player.handValue();
        int dealerValue = dealer.handValue();

        appendLog("\nFinal results:");
        appendLog("Your hand: " + playerValue);
        appendLog("Dealer's hand: " + dealerValue);

        if (dealerValue > 21) {
            appendLog("Dealer busts! You win!");
            player.adjustChips(currentBet);
        } else if (playerValue > dealerValue) {
            appendLog("You win!");
            player.adjustChips(currentBet);
        } else if (playerValue < dealerValue) {
            appendLog("Dealer wins!");
            player.adjustChips(-currentBet);
        } else {
            appendLog("It's a push (tie). No chips changed.");
        }
    }

    /**
     * Ends the current round and prepares for next round
     */
    private void endRound() {
        appendLog("\nRound over. Your chips: " + player.getChips());

        if (player.getChips() <= 0) {
            appendLog("You've run out of chips! Game over.");
            betField.setDisable(true);
        } else {
            // Prepare for next round
            betField.setDisable(false);
            betField.clear();
        }

        // Disable action buttons until next bet
        hitButton.setDisable(true);
        standButton.setDisable(true);
    }

    /**
     * Helper method to log player's current hand and value
     */
    private void logPlayerHand() {
        appendLog("Your hand: " + player.getHand() + " (Value: " + player.handValue() + ")");
    }

    /**
     * Adds a message to the game log
     */
    private void appendLog(String message) {
        gameLog.appendText(message + "\n");
    }

    /**
     * Saves current chip count to file
     */
    private void saveChips() {
        try {
            Files.writeString(saveFile, String.valueOf(player.getChips()),
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            appendLog("Warning: Failed to save progress - " + e.getMessage());
        }
    }

    /**
     * Loads saved chip count or returns default (100)
     */
    private int loadChips() {
        if (Files.exists(saveFile)) {
            try {
                String content = Files.readString(saveFile).trim();
                return Integer.parseInt(content);
            } catch (IOException | NumberFormatException e) {
                appendLog("Failed to load saved data. Starting with 100 chips.");
            }
        }
        return 100;
    }

    /**
     * Sets up automatic save every X seconds
     */
    private void setupAutoSave() {
        autoSaveExecutor = Executors.newSingleThreadScheduledExecutor();
        autoSaveExecutor.scheduleAtFixedRate(
                this::saveChips,
                AUTO_SAVE_INTERVAL,
                AUTO_SAVE_INTERVAL,
                TimeUnit.SECONDS
        );
    }

    /**
     * Cleans up resources on application exit
     */
    private void cleanupResources() {
        // Shutdown auto-save executor
        if (autoSaveExecutor != null) {
            autoSaveExecutor.shutdown();
            try {
                if (!autoSaveExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                    autoSaveExecutor.shutdownNow();
                }
            } catch (InterruptedException e) {
                autoSaveExecutor.shutdownNow();
            }
        }
        // Final save before exit
        saveChips();
    }

    @Override
    public void stop() {
        cleanupResources(); // Ensure cleanup when app closes
    }
}