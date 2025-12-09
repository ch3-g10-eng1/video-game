package com.team3._8.game;

import java.util.Random;

/**
 * Generates a math problem to solve for the puzzle hidden event
 * The puzzle is in the format:
 *    a * b +- c
 * We generate 3 options that look reasonable (enough) alongside the correct answer
 *
 * @author Oliver
 */
public class PuzzleEventLogic {
    private final int a, b, c;
    private final char operator;
    private final int answer;
    private final int[] options;
    private final Random random;

    /**
     * Creates puzzle event object
     */
    public PuzzleEventLogic(){
        this.random = new Random();

        this.a = random.nextInt(2, 10);
        this.b = random.nextInt(2, 10);
        this.c = random.nextInt(2, ((a*b)/2) + 1);

        this.operator = random.nextBoolean() ? '+' : '-';

        // too lazy to ternary notation this one rn
        if (operator == '+'){ answer = a * b + c; }
        else { answer = a * b - c; }

        options = generateOptions();
    }

    /**
     * Generates a shuffled array of 4 answers, including the correct answer
     *
     * @return int array of options
     */
    private int[] generateOptions(){
        int[] tempArray = new int[4];
        tempArray[0] = answer;

        if (operator == '+'){ tempArray[1] = a * (b + c); }
        else { tempArray[1] = a * (b - c); }

        for (int i = 2; i < 4; i++){
            int option;
            do{
                option = answer + random.nextInt(11) - 5;
            } while (option == answer);
            tempArray[i] = option;
        }

        // shuffle options
        for (int i = 3; i > 0; i--){
            int index = random.nextInt(i + 1);
            int temp = tempArray[index];
            tempArray[index] = tempArray[i];
            tempArray[i] = temp;
        }

        return tempArray;
    }

    /**
     * Gets the event's equation in string format
     *
     * @return puzzle equation as string
     */
    public String getPrintableExpression(){
        return a + " * " + b + " " + operator + " " + c;
    }


    /**
     * Get the list of answers the player can choose
     *
     * @return int array of options
     */
    public int[] getOptions(){
        return options;
    }


    /**
     * Get the correct answer
     *
     * @return answer
     */
    public int getAnswer(){
        return answer;
    }
}
