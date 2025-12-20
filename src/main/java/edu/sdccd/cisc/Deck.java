package edu.sdccd.cisc;

import java.util.Random;

public class Deck {

    private Card[] cards = new Card[52];
    private int index = 0;

    public Deck() {
        initializeDeck();
    }

    private void initializeDeck() {
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
        String[] ranks = {
                "2", "3", "4", "5", "6", "7",
                "8", "9", "10", "Jack", "Queen", "King", "Ace"
        };
        int[] values = {2,3,4,5,6,7,8,9,10,10,10,10,11};

        int count = 0;

        for (int s = 0; s < suits.length; s++) {
            for (int r = 0; r < ranks.length; r++) {
                cards[count++] = new Card(suits[s], ranks[r], values[r]);
            }
        }

        shuffle();
    }

    public void shuffle() {
        Random rand = new Random();

        for (int i = cards.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            Card temp = cards[i];
            cards[i] = cards[j];
            cards[j] = temp;
        }
        index = 0;
    }

    public Card dealCard() {
        if (index >= cards.length) {
            shuffle();
        }
        return cards[index++];
    }

    public int remainingCards() {
        return cards.length - index;
    }
}
