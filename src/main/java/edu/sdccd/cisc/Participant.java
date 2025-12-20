package edu.sdccd.cisc;

public abstract class Participant {

    protected Card[] hand = new Card[12]; // Max cards before bust is unlikely
    protected int handSize = 0;

    public void addCard(Card card) {
        hand[handSize++] = card;
    }

    public void clearHand() {
        handSize = 0;
    }

    public int getHandSize() {
        return handSize;
    }

    public Card getCardAt(int index) {
        if (index < 0 || index >= handSize) {
            return null;
        }
        return hand[index];
    }

    public int handValue() {
        int total = 0;
        int aceCount = 0;

        for (int i = 0; i < handSize; i++) {
            total += hand[i].getValue();
            if (hand[i].getRank().equals("Ace")) {
                aceCount++;
            }
        }

        // Adjust for aces
        while (total > 21 && aceCount > 0) {
            total -= 10;
            aceCount--;
        }

        return total;
    }
}
