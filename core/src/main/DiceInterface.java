package main;

public interface DiceInterface {
    void reset();

    void rollDice();

    boolean jailCheck();

    int getValue();

    int getD1();

    int getD2();

    boolean wasItADouble();
}
