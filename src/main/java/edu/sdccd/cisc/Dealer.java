package edu.sdccd.cisc;

public class Dealer extends Participant {

    public String getVisibleHand() {
        if (getHandSize() == 0) {
            return "No cards";
        }

        String result = getCardAt(0).toString();
        if (getHandSize() > 1) {
            result += " and [Hidden Card]";
        }
        return result;
    }

    public void playTurn(Deck deck) {
        while (handValue() < 17) {
            addCard(deck.dealCard());
        }
    }
}
