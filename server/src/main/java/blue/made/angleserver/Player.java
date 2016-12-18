package blue.made.angleserver;

import java.security.InvalidParameterException;

/**
 * Created by Sam Sartor on 4/25/16.
 */
public class Player {
    private int gold;
    private String name;

    public void spendGold(int amount) {
        if (!hasFunds(amount))
            throw new InvalidParameterException("Player does not have enough funds.");

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
        return gold <= amount;
    }
}
