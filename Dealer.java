package edu.sdccd.cisc;

import java.util.List;

public class Dealer extends Participant {

    /**
     * Returns a string showing only the dealer's first card (hides the "hole card" initially)
     * This follows standard Blackjack rules where the dealer's second card is hidden.
     */
    public String getVisibleHand() {
        List<Card> hand = getHand();
        if (hand.isEmpty()) {
            return "No cards";
        }
        // Show first card, hide the rest with [Hidden]
        StringBuilder visibleHand = new StringBuilder();
        visibleHand.append(hand.get(0).toString()); // First card is visible
        if (hand.size() > 1) {
            visibleHand.append(" and [Hidden Card]");
        }
        return visibleHand.toString();
    }

    /**
     * Automatically plays the dealer's turn according to standard rules:
     * Hits until hand value is at least 17, then stands.
     * @param deck The deck to draw cards from
     */
    public void playTurn(Deck deck) {
        while (handValue() < 17) {
            Card drawnCard = deck.dealCard();
            addCard(drawnCard);
        }
    }
}