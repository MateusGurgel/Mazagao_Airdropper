package me.waskjobe.mazagao_airdropper;

import java.util.Random;

public class ProbabilityUtils {
    static Random random = new Random();

    public static int getRandomInt(int min, int max) {
        if (min > max) {
            int temp = min;
            min = max;
            max = temp;
        }


        return random.nextInt(max - min) + min;
    }

    public static boolean getProbability(int chance) {
        int number = getRandomInt(1, 100);

        return number <= chance;
    }
}
