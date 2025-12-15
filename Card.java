package edu.sdccd.cisc;

public class Card {
    // Make fields private and final for immutability (cards shouldn't change after creation)
    private final String suit;
    private final String rank;
    private final int value;

    // Constructor with validation to ensure valid card data
    public Card(String suit, String rank, int value) {
        // Basic validation to prevent null/invalid inputs
        if (suit == null || suit.trim().isEmpty()) {
            throw new IllegalArgumentException("Suit cannot be null or empty");
        }
        if (rank == null || rank.trim().isEmpty()) {
            throw new IllegalArgumentException("Rank cannot be null or empty");
        }
        if (value < 2 || value > 11) { // Valid blackjack card values range
            throw new IllegalArgumentException("Invalid card value (must be 2-11)");
        }

        this.suit = suit;
        this.rank = rank;
        this.value = value;
    }

    // Getters (no setters - cards are immutable)
    public String getSuit() {
        return suit;
    }

    public String getRank() {
        return rank;
    }

    public int getValue() {
        return value;
    }

    // Improved toString() for better readability
    @Override
    public String toString() {
        return String.format("%s of %s", rank, suit);
    }

    // Override equals() and hashCode() to properly compare cards
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return value == card.value &&
                suit.equals(card.suit) &&
                rank.equals(card.rank);
    }

    @Override
    public int hashCode() {
        int result = suit.hashCode();
        result = 31 * result + rank.hashCode();
        result = 31 * result + value;
        return result;
    }
}