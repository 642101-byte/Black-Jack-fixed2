package edu.sdccd.cisc;

public class Player extends Participant {
    private final String name;
    private int chips;

    // Constructor initializes player with name and starting chips
    public Player(String name, int chips) {
        this.name = name;
        this.chips = chips;
    }

    // Returns the player's name
    public String getName() {
        return name;
    }

    // Returns the current number of chips the player has
    public int getChips() {
        return chips;
    }

    // Adjusts the player's chip count by a positive (win) or negative (loss) amount
    public void adjustChips(int amount) {
        chips += amount;
    }

    // Checks if the player can place a bet (has enough chips)
    public boolean canBet(int betAmount) {
        return betAmount > 0 && betAmount <= chips;
    }
}
