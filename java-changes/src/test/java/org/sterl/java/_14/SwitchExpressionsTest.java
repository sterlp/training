package org.sterl.java._14;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SwitchExpressionsTest {
    enum Strategy {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    @Test
    void test() {
        String movement = "a";
        
        Strategy preJava14;
        switch (movement) {
            case "up":
            case "w": 
                preJava14 = Strategy.UP;
                break;
            case "down":
            case "s":
                preJava14 = Strategy.DOWN;
                break;
            case "d":
                preJava14 = Strategy.RIGHT;
                break;
            case "left":
            case "a": {
                    System.out.println("going left ...");
                    preJava14 = Strategy.LEFT;
                }
                break;
            default: throw new IllegalArgumentException("Unexpected movement: " + movement);
        }
        System.out.println("Java 14: " + preJava14);
        
        // new Java 14
        final Strategy movementStrategy = switch (movement) {
            case "up", "w" -> Strategy.UP;
            case "down", "s" -> Strategy.DOWN;
            case "d" -> Strategy.RIGHT;
            case "left", "a" -> {
                System.out.println("going left ...");
                yield Strategy.LEFT;
            }
            default -> throw new IllegalArgumentException("Unexpected movement: " + movement);
        };
        System.out.println("Java 15: " + movementStrategy);
    }

}
