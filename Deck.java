package edu.sdccd.cisc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private final List<Card> cards = new ArrayList<>();
    private int index = 0; // Tracks the next card to deal

    public Deck() {
        initializeDeck();
    }

    /**
     * Initializes the deck with 52 standard Blackjack cards (4 suits × 13 ranks)
     * and shuffles them immediately after creation.
     */
    private void initializeDeck() {
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};
        int[] values = {2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10, 11}; // Ace starts as 11

        // Clear any existing cards before reinitializing
        cards.clear();

        // Populate the deck with all combinations of suits and ranks
        for (String suit : suits) {
            for (int i = 0; i < ranks.length; i++) {
                cards.add(new Card(suit, ranks[i], values[i]));
            }
        }

        // Shuffle the deck to randomize card order
        shuffle();
    }

    /**
     * Shuffles the deck and resets the deal index to the start.
     * Uses Collections.shuffle for efficient randomization.
     */
    public void shuffle() {
        Collections.shuffle(cards);
        index = 0; // Reset to the first card after shuffling
    }

    /**
     * Deals the next card from the deck. Automatically reshuffles when the deck is exhausted.
     * @return The next card in the deck
     */
    public Card dealCard() {
        // Reshuffle if all cards have been dealt
        if (index >= cards.size()) {
            shuffle();
        }
        return cards.get(index++); // Return current card and increment index
    }

    /**
     * Returns the number of remaining cards in the deck (for debugging/information)
     * @return Remaining card count
     */
    public int remainingCards() {
        return cards.size() - index;
    }
}