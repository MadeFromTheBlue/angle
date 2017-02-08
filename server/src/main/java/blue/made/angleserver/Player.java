package blue.made.angleserver;

import blue.made.angleshared.exceptions.AngleException;

/**
 * Created by Sam Sartor on 4/25/16.
 */
public class Player {
    private int gold = 100;
    private String name;

    public Player(String name) {
        this.name = name;
    }

    public void spendGold(int amount) {
        if (!hasFunds(amount))
            throw new AngleException("InsufficientFunds");

        gold -= amount;
    }

    public void receiveGold(int amount) {
        gold += amount;
    }

    public void sendGold(Player recipient, int amount) {
        spendGold(amount);
        recipient.receiveGold(amount);
    }

    public boolean hasFunds(int amount) {
        return gold >= amount;
    }
}
