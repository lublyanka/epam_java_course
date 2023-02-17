package com.epam.rd.autotasks.arrays;

import java.util.Arrays;

public class SumOfEvenNumbers {

    public static void main(String[] args) {
        int[] array = new int[]{1, 3, 2, 8, 15, 199};

        System.out.println(sum(array));
    }

    public static int sum(int[] array) {
        int sum = 0;
        if(array == null || array.length ==0)
            return 0;
        for (int curr : array) {
            if (curr % 2 == 0) sum += curr;
        }

        return sum ;
        //throw new UnsupportedOperationException();
    }
}
