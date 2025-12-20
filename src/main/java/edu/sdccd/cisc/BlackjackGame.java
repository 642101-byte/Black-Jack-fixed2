package edu.sdccd.cisc;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;

public class BlackjackGame extends Application {

    private Player player;
    private Dealer dealer = new Dealer();
    private Deck deck = new Deck();

    private final File saveFile = new File("blackjack_save.txt");

    private TextArea gameLog = new TextArea();
    private TextField betField = new TextField();
    private Button hitButton = new Button("Hit");
    private Button standButton = new Button("Stand");

    private int currentBet = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        player = new Player("Player", loadChips());

        gameLog.setEditable(false);

        VBox root = new VBox(10);
        HBox controls = new HBox(10);
        controls.getChildren().addAll(
                new Label("Bet:"), betField, hitButton, standButton
        );
        root.getChildren().addAll(
                new Label("Blackjack Game"), gameLog, controls
        );

        hitButton.setDisable(true);
        standButton.setDisable(true);

        betField.setOnAction(e -> handleBetSubmission());
        hitButton.setOnAction(e -> handlePlayerHit());
        standButton.setOnAction(e -> handlePlayerStand());

        stage.setScene(new Scene(root, 600, 450));
        stage.setTitle("Blackjack GUI");
        stage.setOnCloseRequest(e -> saveChips());
        stage.show();

        appendLog("Welcome! You have " + player.getChips() + " chips. Enter a bet.");
    }

    private void handleBetSubmission() {
        String betText = betField.getText();
        
        if (betText.isEmpty() || !betText.matches("\\d+")) {
            appendLog("Enter a valid bet amount.");
            return;
        }

        currentBet = Integer.parseInt(betText);

        if (currentBet <= 0) {
            appendLog("Bet must be greater than zero.");
        } else if (currentBet > player.getChips()) {
            appendLog("You do not have enough chips.");
        } else {
            startNewRound();
            hitButton.setDisable(false);
            standButton.setDisable(false);
            betField.setDisable(true);
        }
    }

    private void startNewRound() {
        player.clearHand();
        dealer.clearHand();

        player.addCard(deck.dealCard());
        dealer.addCard(deck.dealCard());
        player.addCard(deck.dealCard());
        dealer.addCard(deck.dealCard());

        appendLog("Dealer shows: " + dealer.getCardAt(0) + " [Hidden]");
        logPlayerHand();
        
        // Check for blackjack
        if (player.handValue() == 21) {
            appendLog("Blackjack! You win 1.5x your bet!");
            player.adjustChips((int)(currentBet * 1.5));
            endRound();
        }
    }

    private void handlePlayerHit() {
        player.addCard(deck.dealCard());
        logPlayerHand();

        if (player.handValue() > 21) {
            appendLog("You busted!");
            player.adjustChips(-currentBet);
            endRound();
        }
    }

    private void handlePlayerStand() {
        // Dealer plays their turn
        while (dealer.handValue() < 17) {
            dealer.addCard(deck.dealCard());
        }
        
        // Show dealer's full hand
        appendLog("Dealer's hand:");
        for (int i = 0; i < dealer.getHandSize(); i++) {
            appendLog("  " + dealer.getCardAt(i));
        }
        
        resolveRound();
        endRound();
    }

    private void resolveRound() {
        int p = player.handValue();
        int d = dealer.handValue();

        appendLog("Your total: " + p);
        appendLog("Dealer total: " + d);

        if (d > 21 || p > d) {
            appendLog("You win!");
            player.adjustChips(currentBet);
        } else if (p < d) {
            appendLog("Dealer wins.");
            player.adjustChips(-currentBet);
        } else {
            appendLog("Push.");
        }
    }

    private void endRound() {
        appendLog("Chips: " + player.getChips());
        appendLog("Cards remaining in deck: " + deck.remainingCards());
        hitButton.setDisable(true);
        standButton.setDisable(true);
        betField.setDisable(false);
        betField.clear();
        
        if (player.getChips() <= 0) {
            appendLog("Game Over! You're out of chips.");
            betField.setDisable(true);
        }
    }

    private void logPlayerHand() {
        appendLog("Your hand value: " + player.handValue());
    }

    private void appendLog(String msg) {
        gameLog.appendText(msg + "\n");
    }

    private void saveChips() {
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

    @Override
    public void stop() {
        saveChips();
    }
}
