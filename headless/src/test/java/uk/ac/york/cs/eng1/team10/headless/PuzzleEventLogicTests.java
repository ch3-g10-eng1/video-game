package uk.ac.york.cs.eng1.team10.headless;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.team3._8.game.PuzzleEventLogic;

public class PuzzleEventLogicTests {
  private PuzzleEventLogic puzzle;

  @BeforeEach
  public void createPuzzleEventLogic() {
    puzzle = new PuzzleEventLogic();
  }

  @Test
    void testCorrectAnswerMatchesPrintableExpression() {

        String expression = puzzle.getPrintableExpression();
        int answer = puzzle.getAnswer();
        int expected;

        // Parse expression: "a * b + c" or "a * b - c"
        String[] parts = expression.split(" ");
        int a = Integer.parseInt(parts[0]);
        int b = Integer.parseInt(parts[2]);
        char operator = parts[3].charAt(0);
        int c = Integer.parseInt(parts[4]);

        if (operator == '+') {
            expected = a * b + c;
        } else {
            expected = a * b - c;
        }

        assertEquals(expected, answer);
    }

    @Test
    void testOptionsContainCorrectAnswer() {
        int answer = puzzle.getAnswer();
        int[] options = puzzle.getOptions();

        assertEquals(Arrays.stream(options).anyMatch(o -> o == answer), true);
    }

    @Test
    void testOptionsHaveCorrectSize() {
        assertEquals(4, puzzle.getOptions().length);
    }

    @Test
    void testOptionsDoNotContainDuplicateCorrectAnswer() {
        int answer = puzzle.getAnswer();
        int[] options = puzzle.getOptions();

        long count = Arrays.stream(options).filter(o -> o == answer).count();

        assertEquals(1, count);
    }

    @Test
    void testPrintableExpressionHasExpectedFormat() {
        String expression = puzzle.getPrintableExpression();

        assertEquals(true, expression.matches("\\d+ \\* \\d+ [+-] \\d+"));
    }
}

