import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;


/**
 * This file is used for testing your code. You can ignore the entire file during your development
 * You can click the button next to TestClass to test your code.
 *
 * This is also how we are going to grade your work! Of course, there will be more test cases
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestClass {
    private ByteArrayOutputStream out;
    private SameGame sameGame;

    @BeforeEach
    public void setup() {
        out = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(out));
        PrintStream console = System.out;
        PrintStream dualStream = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                console.write(b);
                out.write(b);
            }
        });
        System.setOut(dualStream);
        sameGame = new SameGame();
    }


    @Test
    void testPrintBoard() {


        char[][] gameboard = {
                {'+', '@', '+', '@', '=', '@', '^', '^', '=', '+', '=', '+', '^', '@', '@', '@', '^', '^', '+', '=', '=', '@', '^', '=', '+', '=',},
                {'+', '@', '+', '=', '+', '@', '+', '^', '@', '@', '^', '=', '+', '=', '=', '@', '+', '+', '=', '=', '=', '=', '+', '^', '=', '+'},
                {'+', '+', '=', '+', '^', '+', '+', '@', '+', '^', '^', '+', '+', '^', '^', '@', '+', '=', '^', '=', '@', '@', '+', '+', '@', '='},
                {'=', '@', '@', '^', '@', '^', '=', '@', '=', '@', '=', '=', '+', '+', '@', '@', '=', '@', '@', '^', '=', '+', '@', '@', '^', '='},
                {'=', '^', '@', '+', '+', '@', '^', '=', '=', '+', '@', '+', '+', '^', '@', '@', '^', '^', '@', '+', '+', '@', '+', '=', '=', '@'},
                {'=', '=', '=', '^', '+', '+', '=', '+', '^', '+', '+', '^', '@', '^', '@', '@', '@', '^', '+', '^', '+', '=', '+', '^', '@', '+'},
                {'^', '+', '=', '@', '@', '+', '@', '+', '=', '@', '+', '@', '@', '@', '=', '^', '+', '=', '@', '=', '^', '^', '@', '+', '^', '='},
                {'=', '+', '@', '=', '=', '+', '@', '@', '=', '@', '^', '+', '@', '=', '@', '@', '=', '^', '+', '=', '=', '@', '+', '+', '^', '^'},
                {'+', '^', '@', '@', '@', '^', '=', '^', '@', '=', '+', '=', '=', '^', '^', '@', '=', '@', '=', '@', '@', '@', '+', '^', '=', '^'},
                {'@', '+', '+', '^', '+', '+', '+', '=', '@', '@', '+', '=', '^', '+', '=', '^', '^', '+', '^', '=', '=', '=', '+', '^', '@', '@'}
        };

        sameGame.printBoard(gameboard);

        String expectedOutput = " ABCDEFGHIJKLMNOPQRSTUVWXYZ\n" +
                "0+@+@=@^^=+=+^@@@^^+==@^=+=0\n" +
                "1+@+=+@+^@@^=+==@++====+^=+1\n" +
                "2++=+^++@+^^++^^@+=^=@@++@=2\n" +
                "3=@@^@^=@=@==++@@=@@^=+@@^=3\n" +
                "4=^@++@^==+@++^@@^^@++@+==@4\n" +
                "5===^++=+^++^@^@@@^+^+=+^@+5\n" +
                "6^+=@@+@+=@+@@@=^+=@=^^@+^=6\n" +
                "7=+@==+@@=@^+@=@@=^+==@++^^7\n" +
                "8+^@@@^=^@=+==^^@=@=@@@+^=^8\n" +
                "9@++^+++=@@+=^+=^^+^===+^@@9\n" +
                " ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        String actualOutput = out.toString().replaceFirst("\\s+$", "");

        actualOutput = actualOutput.replace("\r\n", "\n"); // Normalize line endings for Windows compatibility
        assertEquals(expectedOutput, actualOutput, "The printed board does not match the expected output.");
    }

    @Test
    void testIsValidSelection() {
        SameGame sameGame = new SameGame();
        char[][] gameboard = {
                {'A', 'A', 'B', 'C'},
                {'A', 'A', 'C', 'E'},
                {'D', 'D', 'B', 'E'},
                {'D', 'C', 'B', 'E'}
        };

        // Valid selections
        assertTrue(sameGame.isValidSelection(gameboard, 0, 'A'), "Expected valid selection at (0, A)");
        assertTrue(sameGame.isValidSelection(gameboard, 2, 'D'), "Expected valid selection at (2, D)");

        // Invalid selections
        assertFalse(sameGame.isValidSelection(gameboard, 0, 'C'), "Expected invalid selection at (0, C)");
        assertFalse(sameGame.isValidSelection(gameboard, 3, 'B'), "Expected invalid selection at (3, B)");

    }

    @Test
    void testCopyArray() {
        SameGame sameGame = new SameGame();

        // Original array
        char[][] original = {
                {'A', 'B', 'C'},
                {'D', 'E', 'F'},
                {'G', 'H', 'I'}
        };

        // Copy the array
        char[][] copied = sameGame.copyArray(original);

        // Verify the copied array is identical to the original
        assertArrayEquals(original, copied, "The copied array should be identical to the original.");

        // Verify the copied array is a different object
        assertNotSame(original, copied, "The copied array should not share the same memory reference as the original.");
    }

    @Test
    void testSelect() {
        SameGame sameGame = new SameGame();

        // Sample gameboard
        char[][] gameboard = {
                {'A', 'A', 'B', 'C'},
                {'A', 'A', 'B', 'C'},
                {'D', 'D', 'B', 'C'},
                {'D', 'D', 'E', 'E'}
        };

        // Expected result after selection
        char[][] expectedGameboard = {
                {'*', '*', 'B', 'C'},
                {'*', '*', 'B', 'C'},
                {'D', 'D', 'B', 'C'},
                {'D', 'D', 'E', 'E'}
        };

        // Call the select method
        int selectedCount = sameGame.select(gameboard, 0, 'A');

        // Assertions
        assertEquals(4, selectedCount, "The number of selected blocks should be 4.");
        assertArrayEquals(expectedGameboard, gameboard, "The gameboard should match the expected result.");
    }

    @Test
    void testSelectBiggestSegment() {
        SameGame sameGame = new SameGame();

        // Sample gameboard
        char[][] gameboard = {
                {'A', 'A', 'B', 'C'},
                {'A', 'A', 'B', 'C'},
                {'D', 'D', 'B', 'C'},
                {'D', 'D', 'E', 'E'}
        };

        // Expected result after selecting the biggest segment
        char[][] expectedGameboard = {
                {'*', '*', 'B', 'C'},
                {'*', '*', 'B', 'C'},
                {'D', 'D', 'B', 'C'},
                {'D', 'D', 'E', 'E'}
        };

        // Call the method
        char[][] result = sameGame.selectBiggestSegment(gameboard);

        // Assertions
        assertNotNull(result, "The result should not be null.");
        assertArrayEquals(expectedGameboard, result, "The biggest segment should be selected correctly.");

        // Ensure the original gameboard is unchanged
        char[][] unchangedGameboard = {
                {'A', 'A', 'B', 'C'},
                {'A', 'A', 'B', 'C'},
                {'D', 'D', 'B', 'C'},
                {'D', 'D', 'E', 'E'}
        };
        assertArrayEquals(unchangedGameboard, gameboard, "The original gameboard should remain unchanged.");
    }
}
