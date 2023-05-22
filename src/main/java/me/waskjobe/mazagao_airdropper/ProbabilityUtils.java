package me.waskjobe.mazagao_airdropper;

import java.util.Random;

public class ProbabilityUtils {
    public static int getRandomInt(int min, int max) {
        if (min > max) {
            int temp = min;
            min = max;
            max = temp;
        }

        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    public static boolean getProbability(int chance) {
        int number = getRandomInt(1, 100);

        if (number <= chance) {
            return true; // The probability occurred
        }

        return false; // The probability did not occur
    }
}
