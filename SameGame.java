import java.util.Scanner;
import java.io.*;
import java.util.*;

// Add your suitable imports here
// Remember your code should be using Java 11 features only without the use of var keyword

/**
 * @author: 24234958 (SID)24234958
 *
 *          For the instruction of the assignment please refer to the assignment
 *          GitHub.
 *
 *          Plagiarism is a serious offense and can be easily detected. Please
 *          don't share your code to your classmate even if they are threatening
 *          you with your friendship. If they don't have the ability to work on
 *          something that can compile, they would not be able to change your
 *          code to a state that we can't detect the act of plagiarism. For the
 *          first commit of plagiarism, regardless you shared your code or
 *          copied code from others, you will receive 0 with an addition of 5
 *          mark penalty. If you commit plagiarism twice, your case will be
 *          presented in the exam board and you will receive a F directly.
 *
 *          Terms about generative AI:
 *          You are not allowed to use any generative AI in this assignment.
 *          The reason is straight forward. If you use generative AI, you are
 *          unable to practice your coding skills. We would like you to get
 *          familiar with the syntax and the logic of the Java programming.
 *          We will examine your code using detection software as well as
 *          inspecting your code with our eyes. Using generative AI tool
 *          may fail your assignment.
 *
 *          If you cannot work out the logic of the assignment, simply contact
 *          us on Discord. The teaching team is more the eager to provide
 *          you help. We can extend your submission due if it is really
 *          necessary. Just please, don't give up.
 */
public class SameGame {

    static final char EMPTY = ' ';
    static final char SELECTED = '*';
    static final char[] SYMBOLS = {EMPTY, '@', '=', '^', '+'};

    //for testing purpose we might change to the following, or other arrays
    //static final char[] SYMBOLS = {EMPTY, '@', '='};

    static final int MAX_ROW = 10;
    static final int MAX_COL = 26;
    // Store the top score file in this file at your current path
    // Create such file if it does not exist

    static final String TOP_SCORE_FILE = "top_scores.txt";

    public static void main(String[] args) {
        new SameGame().startGame();
    }

    void startGame() {
        char[][] gameboard = new char[MAX_ROW][MAX_COL];
        randomizeBoard(gameboard); // call random the gameboard
        printHelp(); // print instruction

        Scanner scanner = new Scanner(System.in);
        int score = 0;
        char[][] selectedBoard = null;

        while (!isGameOver(gameboard)) {
            printBoard(gameboard); // print the gameboard
            System.out.print("Enter your move in the format column-row, e.g. A-5, or press 'h' for help, 'q' to quit: ");
            String input = scanner.nextLine().trim();
            selectedBoard = null;

            switch (input) {
                case "h":
                    printHelp();
                    continue; // Skip the rest of the loop
                case "q":
                    System.out.println("Your score is: " + score);
                    System.out.println("Thank you for playing SameGame!");
                    return;
                case "r":
                    randomizeBoard(gameboard);
                    score = 0; // Reset score on restart
                    printBoard(gameboard);
                    continue; // Skip the rest of the loop
                case "t":
                    System.out.println("Tips: selected the biggest segment of blocks for you..");
                    selectedBoard = selectBiggestSegment(gameboard);

                    // Check if any segment was actually selected
                    boolean hasSelected = false;
                    for (int i = 0; i < selectedBoard.length; i++) {
                        for (int j = 0; j < selectedBoard[i].length; j++) {
                            if (selectedBoard[i][j] == SELECTED) {
                                hasSelected = true;
                                break;
                            }
                        }
                        if (hasSelected) break; // if have * end loop
                    }
                    if (!hasSelected) { // if have no *
                        selectedBoard = null;
                        continue;
                    }
                    break;
                default:
                    // Check if the input is in the format of "A-5" or similar
                    if ((input.length() == 3 && input.charAt(1) == '-' && input.charAt(0) >= 'A' && input.charAt(0) <= 'Z' && input.charAt(2) >= '0' && input.charAt(2) <= '9'))
                    {
                        char column = input.charAt(0);
                        int row = input.charAt(2) - '0'; // Convert char to int

                        // 1. Validate the selection if it is a valid selection
                        if (isValidSelection(gameboard, row, column)) {

                            // 2. If valid, print the number of blocks being selected and
                            //    copy the gameboard to the variable selectedBoard
                            selectedBoard = copyArray(gameboard);
                            int blocksSelected = select(selectedBoard, row, column);
                            System.out.println("You selected "+blocksSelected+" blocks.");

                        } else {
                            // 3. If it is invalid, print an error message and continue to the next iteration
                            System.out.println("Invalid selection. Please try again.");
                            continue; // Go to next iteration of while loop
                        }

                    } else
                    {
                        System.out.println("Invalid input, please try again.");
                        continue;
                    }
                    break;
            }

            // This part handles the removal confirmation for BOTH manual selection AND tip selection
            if (selectedBoard != null) {
                // 4. If the selectedBoard is set, either through the "t" command or a valid selection,
                //    print the selectedBoard and ask for confirmation to remove the selected blocks
                printBoard(selectedBoard);
                System.out.print("Please confirm if you want to remove the selected blocks (y/n): \n");
                String confirm = scanner.nextLine().trim();

                // is y or Y or not empty
                if (!confirm.isEmpty() && (confirm.charAt(0) == 'y' || confirm.charAt(0) == 'Y')){

                    int scoreEarned = computeScore(selectedBoard);
                    score += scoreEarned;
                    gameboard = removeSelected(selectedBoard);
                    System.out.println(" ");
                    System.out.println("Your current score: " + score);

                } else {
                    System.out.println("Selection cancelled.");
                }
                selectedBoard = null;
            }
        }
        // is Game over become True, then,
        System.out.println("\n\n");
        printBoard(gameboard); //after the game is over, print the final board again.

        System.out.println("Game over! Your final score is: " + score);
        topscorer(score); //to display the top scores and save the current score if applicable
    }

    /**
     * This method is to load the top score and update the top score to a file.
     *
     * The method shall read from the file TOP_SCORE_FILE and load the top score
     * display the top five scores including the score from the current game on screen.
     * Then it shall save the top five scores to the file TOP_SCORE_FILE.
     *
     * The format of the file shall be
     * KEV 1100
     * JON 900
     * SAN 884
     * JIM 700
     * CH 500
     *
     * Each line contains an upper case name with at least 1 character and at most 3 characters,
     * followed by a space and then the score which is an integer. The file shall be sorted
     * by the score in descending order. It is possible that the file contains less than five lines.
     *
     * When the player's score is in the top five, the player will be prompted to enter their name
     * to save the score. The name must be an upper case string with at least 1 character and
     * at most 3 characters or it will be rejected. If the name is valid, the score will
     * be saved to the file.
     *
     *
     * @param score is the score to be saved.
     */
    void topscorer(int score) {

        // Read existing scores into array
        String[] names = new String[5];
        int[] scores = new int[5];
        int count = 0;

        // Read from file
        try (Scanner fileScanner = new Scanner(new File(TOP_SCORE_FILE))) {
            // If top_scores.txt exists and contains:
            // AAA 11974
            // KEV 900
            while (fileScanner.hasNextLine() && count < 5) {
                String line = fileScanner.nextLine().trim();
                // Read "AAA 11974", then "KEV 900"
                if (!line.isEmpty()) {
                    String[] parts = line.split(" ");
                    // parts = ["AAA", "11974"]
                    names[count] = parts[0];
                    // names[0] = "AAA"
                    scores[count] = Integer.parseInt(parts[1]);
                    // scores[0] = 11974
                    count++;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading top scores file: " + TOP_SCORE_FILE);
        }

        // Check if current score is a high score
        boolean highScore = (count < 5); //highScore = (count eg. 2 < 5) = true
        for (int i = 0; i < count && !highScore; i++) {
            if (score > scores[i]) { // Not executed since highScore is already true
                highScore = true; // highScore = true (because we have less than 5 scores)
            }
        }

        // If high score, get player name
        if (highScore) { // true, so enter this block
            Scanner scanner = new Scanner(System.in);
            String name;
            do {
                System.out.print("Enter your name (1-3 uppercase letters): ");
                name = scanner.nextLine().trim().toUpperCase(); // User types
            } while (name.length() < 1 || name.length() > 3 || !name.matches("[A-Z]+"));

            // Add new score to arrays
            if (count < 5) { // this part run if not full
                names[count] = name;
                scores[count] = score;
                count++;
            } else { // This part would run if we already had 5 scores
                // Replace lowest score
                int minIndex = 0;
                for (int i = 1; i < count; i++) {
                    if (scores[i] < scores[minIndex]) {
                        minIndex = i;
                    }
                }
                names[minIndex] = name;
                scores[minIndex] = score;
            }
        }

        // Sort scores in descending order (bubble sort)
        for (int i = 0; i < count - 1; i++) {
            for (int j = 0; j < count - 1 - i; j++) {
                if (scores[j] < scores[j + 1]) { // Check if we need to swap
                    // Swap scores
                    int tempScore = scores[j];
                    scores[j] = scores[j + 1];
                    scores[j + 1] = tempScore;

                    // Swap names
                    String tempName = names[j];
                    names[j] = names[j + 1];
                    names[j + 1] = tempName;
                }
            }
        }

        // Write to file
        try (FileWriter writer = new FileWriter(TOP_SCORE_FILE)) {
            for (int i = 0; i < count; i++) {
                writer.write(names[i] + " " + scores[i] + "\n");
                // writes in temple
            }
        } catch (IOException e) {}

        // Display top scores
        System.out.println("Top Scores:");
        for (int i = 0; i < count; i++) {
            System.out.println(names[i] + " " + scores[i]);
        }

    }

    /**
     * This method is to compute the score based on the number of blocks removed.
     *
     * The score is computed based on the number of blocks removed. The score is
     * calculated as follows:
     *  Let n be the number of blocks removed. The base score is n * (n + 1). For
     * each column of blocks that is completely removed, an additional 10 points
     * are added to the score.
     *
     *
     *
     * @param gameboard is a 2D char array that is always non-null and pointed to a
     *                   rectangular area.
     * @return the score
     */
    int computeScore(char[][] gameboard) {
        int rows = gameboard.length;
        int cols = gameboard[0].length;

        // Count selected blocks (n)
        int n = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (gameboard[i][j] == SELECTED) {
                    n++;
                }
            }
        }

        // Base score: n * (n + 1)
        int score = n * (n + 1);

        // Count completely selected columns and add bonus
        for (int j = 0; j < cols; j++) {
            boolean allSelected = true;
            for (int i = 0; i < rows; i++) {
                if (gameboard[i][j] != SELECTED) { // is not *
                    allSelected = false;
                    break;
                }
            }
            if (allSelected) {
                score += 10; //empty col
            }
        }
        return score;
    }

    /**
     * This method is to select the biggest segment of blocks that can be selected.
     *
     * The method will iterate through the gameboard and find the biggest segment of
     * blocks that can be selected. It will return a new gameboard with the selected
     * blocks marked as SELECTED. If there are multiple segments with the same size,
     * any one of them can be returned.
     *
     * @param gameboard is a 2D char array that is always non-null and pointed to a
     *                   rectangular area.
     * @return a new gameboard with the selected blocks marked as SELECTED.
     */
    char[][] selectBiggestSegment(char[][] gameboard) {
        int rows = gameboard.length;
        int cols = gameboard[0].length;

        char[][] resultBoard = copyArray(gameboard);
        int maxSize = 0;
        int bestStartRow = -1;
        int bestStartCol = -1;

        boolean[][] globalVisited = new boolean[rows][cols];

        // Find the biggest segment
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (resultBoard[i][j] == EMPTY || resultBoard[i][j] == SELECTED || globalVisited[i][j]) {
                    continue;
                }

                // Count segment size using BFS
                char targetSymbol = resultBoard[i][j];
                int segmentSize = 0;
                int[][] queue = new int[rows * cols][2];
                int front = 0, rear = 0;
                boolean[][] localVisited = new boolean[rows][cols];

                queue[rear][0] = i;
                queue[rear][1] = j;
                rear++;
                localVisited[i][j] = true;
                globalVisited[i][j] = true;

                while (front < rear) {
                    int r = queue[front][0];
                    int c = queue[front][1];
                    front++;
                    segmentSize++;

                    int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
                    for (int[] dir : directions) {
                        int newRow = r + dir[0];
                        int newCol = c + dir[1];

                        if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols &&
                                !localVisited[newRow][newCol] && resultBoard[newRow][newCol] == targetSymbol) {
                            queue[rear][0] = newRow;
                            queue[rear][1] = newCol;
                            rear++;
                            localVisited[newRow][newCol] = true;
                            globalVisited[newRow][newCol] = true;
                        }
                    }
                }

                if (segmentSize > maxSize && segmentSize > 1) {
                    maxSize = segmentSize;
                    bestStartRow = i;
                    bestStartCol = j;
                }
            }
        }

        // Mark the biggest segment
        if (maxSize > 0) {
            char targetSymbol = resultBoard[bestStartRow][bestStartCol];
            int[][] queue = new int[rows * cols][2];
            int front = 0, rear = 0;
            boolean[][] visited = new boolean[rows][cols];

            queue[rear][0] = bestStartRow;
            queue[rear][1] = bestStartCol;
            rear++;
            visited[bestStartRow][bestStartCol] = true;

            while (front < rear) {
                int r = queue[front][0];
                int c = queue[front][1];
                front++;

                resultBoard[r][c] = SELECTED;

                int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
                for (int[] dir : directions) {
                    int newRow = r + dir[0];
                    int newCol = c + dir[1];

                    if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols &&
                            !visited[newRow][newCol] && resultBoard[newRow][newCol] == targetSymbol) {
                        queue[rear][0] = newRow;
                        queue[rear][1] = newCol;
                        rear++;
                        visited[newRow][newCol] = true;
                    }
                }
            }
        }

        return resultBoard;
    }

    /**
     * Copy the 2D char array to a new 2D char array.
     *
     * This method will create a new 2D char array that is the same size as the
     * parameter src. It will copy the content of the src to the new array.
     *
     * @param src is a 2D char array that is always non-null and pointed to a
     *             rectangular area.
     * @return a new 2D char array that is a copy of the src.
     */
    char[][] copyArray(char[][] src) {

        int rows = src.length;
        int cols = src[0].length;

        char[][] copy = new char[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                copy[i][j] = src[i][j]; // copy one by one
            }
        }
        return copy;
    }

    /**
     * This method is to print the help menu.
     *
     * By referring to the startGame method, create a proper printHelp method
     */
    void printHelp() {
        System.out.print("This is the Help menu: \n");
        System.out.print("h \t Print this help menu.\n");
        System.out.print("q \t Quit the game.\n");
        System.out.print("r \t Restart the game with a new random board.\n");
        System.out.print("t \t Get a tip which selects the biggest available segment.\n");

    }

    /**
     * This method is to print the game board with the coordinate labels.
     *
     * The first column and the last column should be the y-coordinate labeled from "0" to "9".
     * The first row and the last row should be the x-coordinate labeled from "A" to "?" where "?"
     * is the n-th character counts from "A" and n is the total number of column of the gameboard.
     *
     * At the beginning, the game board has MAX_COL (which is 26) columns. However, when the
     * game keep playing, the game board may shrink horizontally. The game board will never shrink
     * vertically, i.e., it will always display MAX_ROW (which is 10) rows of blocks even if some
     * rows are empty.
     *
     * The content of the game board should be printed inside the coordinate borders. The symbols of the label
     * should be referred to the constant SYMBOLS.
     *
     * We can assume that the size of the gameboard will never be null and is always pointed
     * to a rectangular char array.
     *
     * If the player plays very well, the game board may be empty at the end of the game, i.e.,
     * it has no columns. In this case, the method should print a message saying "Gameboard is empty."
     */
    void printBoard(char[][] gameboard) {

        int rows = gameboard.length;
        int cols = gameboard[0].length;

        // Print top coordinates (A-Z)
        System.out.print(" ");
        for (int j = 0; j < cols; j++) {
            System.out.print((char)('A' + j));
        }
        System.out.println();

        // Print rows with numbers on both sides
        for (int i = 0; i < rows; i++) {
            System.out.print(i);
            for (int j = 0; j < cols; j++) {
                System.out.print(gameboard[i][j]);
            }
            System.out.println(i);
        }

        // Print bottom coordinates (A-Z)
        System.out.print(" ");
        for (int j = 0; j < cols; j++) {
            System.out.print((char)('A' + j));
        }
        System.out.println();

        // End the game
        if (cols == 0) {
            System.out.println("Gameboard is empty.");
            return;
        }
    }

    /**
     * This method determine if the position that being selected is a valid selection
     *
     * - If the position being selected is out of the board, it is invalid. i.e., it must be a valid
     * coordinate with row between 0 to 9, and column between 'A' to 'Z'.
     * - If the position being selected does not contain a block, it is invalid.
     * - If the block on the selected position does not have the same type of block in its immediate
     * up/down/left/right position, it is invalid. That means, this is an isolated block that cannot
     * be cancelled with another block.
     * - Otherwise, it is a valid selection.
     *
     *
     * @param gameboard is a 2D char array that is always non-null and pointed to a
     *                   rectangular area.
     * @param row is the row of the selected block, which should be between 0 and 9.
     * @param column is the column of the selected block, which should be between 'A' and 'Z'.
     * @return The method should return true if it is a valid selection, false if it is not.
     *
     */
    boolean isValidSelection(char[][] gameboard, int row, char column) {
        int rows = gameboard.length;
        int cols = gameboard[0].length;

        // A=0 , B=1 ,...
        int colIndex = column - 'A'; //E.g. ASClII: 'A' is 65 -65 =0

        //Check if position is out of the board
        if (row < 0 || row >= rows || colIndex < 0 || colIndex >= cols) {
            return false;
        }

        //Check if position is not a block ( empty)
        char selectedBlock = gameboard[row][colIndex];
        if (selectedBlock == EMPTY || selectedBlock == SELECTED) {
            return false;
        }

        //Check if block has at least one adjacent same-colored block

        //UP
        if (row > 0 && gameboard[row-1][colIndex] == selectedBlock) {
            return true;
        }

        // DOWN
        if (row < rows - 1 && gameboard[row+1][colIndex] == selectedBlock) {
            return true;
        }

        // LEFT
        if (colIndex > 0 && gameboard[row][colIndex-1] == selectedBlock) {
            return true;
        }

        //RIGHT
        if (colIndex < cols - 1 && gameboard[row][colIndex+1] == selectedBlock) {
            return true;
        }

        // isolated block
        return false;
    }

    /**
     * This method change the gameboard by turning the selected blocks to the symbol SELECT and
     * returns the number of blocks that are selected.
     *
     * We assume that the selected position is valid when we call this method. (Valid, please refer to
     * the description of is ValidSelection). This method will create a new 2D char array that is the
     * same size as the parameter gameboard. It turns the selected blocks and its adjacent blocks that
     * share the same type to the symbol SELECT.
     *
     * You are expected to implement this method without using recursion.
     *
     *
     * @param gameboard the input gameboard
     * @param row the row of the selected block
     * @param column the column of the selected block
     *
     * @return the number of blocks that are selected, which is the number of blocks that are turned to SELECTED.
     */
    int select(char[][] gameboard, int row, char column) {

        // A=0 , B=1 ,...
        int colIndex = column - 'A';//E.g. ASClII: 'A' is 65 -65 =0
        char targetSymbol = gameboard[row][colIndex]; // Get the symbol which get selected
        int rows = gameboard.length;
        int cols = gameboard[0].length;
        int count = 0;

        // create queue array for ready to check the near position is same symbol
        int[][] queue = new int[rows * cols][2]; // column 0 for position x , column 1 for position y
        // create visited array for the near position is same symbol
        boolean[][] visited = new boolean[rows][cols];
        int front = 0, rear = 0;

        // Start with the initial selected block
        queue[rear][0] = row;
        queue[rear][1] = colIndex;
        rear++; // store in next queue one
        visited[row][colIndex] = true;

        // Process all connected blocks
        // until the queue all have been checked
        while (front < rear) {
            // Dequeue the selected, from queue array to visited array
            int currentRow = queue[front][0]; // selected row
            int currentCol = queue[front][1];
            front++; //check the next one in queue

            //Mark as SELECTED and count
            gameboard[currentRow][currentCol] = SELECTED;
            count++;

            // Check all four directions for adjacent same-colored blocks
            // Up
            // not out the board and not had been visit (check) and same symbol
            if (currentRow > 0 && !visited[currentRow - 1][currentCol] && gameboard[currentRow - 1][currentCol] == targetSymbol) {
                queue[rear][0] = currentRow - 1;
                queue[rear][1] = currentCol;
                rear++;
                visited[currentRow - 1][currentCol] = true; // add in visited array and become true
            }

            // Down
            //  not out the board and not had been visit (check) and same symbol
            if (currentRow < rows - 1 && !visited[currentRow + 1][currentCol] && gameboard[currentRow + 1][currentCol] == targetSymbol) {
                queue[rear][0] = currentRow + 1;
                queue[rear][1] = currentCol;
                rear++;
                visited[currentRow + 1][currentCol] = true;
            }

            // Left
            // not out the board and not had been visit (check) and same symbol
            if (currentCol > 0 && !visited[currentRow][currentCol - 1] &&
                    gameboard[currentRow][currentCol - 1] == targetSymbol) {
                queue[rear][0] = currentRow;
                queue[rear][1] = currentCol - 1;
                rear++;
                visited[currentRow][currentCol - 1] = true;
            }

            // Right
            // not out the board and not had been visit (check) and same symbol
            if (currentCol < cols - 1 && !visited[currentRow][currentCol + 1] &&
                    gameboard[currentRow][currentCol + 1] == targetSymbol) {
                queue[rear][0] = currentRow;
                queue[rear][1] = currentCol + 1;
                rear++;
                visited[currentRow][currentCol + 1] = true;
            }
        }

        return count;
    }

    /**
     * This method is to remove the selected blocks from the gameboard. Please refer to the rule
     * of the game on how blocks are removed and the subsequent shrinking of the gameboard, if any.
     *
     * After the method, the content of the original gameboard (pointed by the parameter) is
     * not important anymore. You are free to modify the content of the original gameboard.
     *
     * @param gameboard
     * @return the new gameboard after removing the selected blocks.
     */
    char[][] removeSelected(char[][] gameboard) {
        int rows = gameboard.length;
        int cols = gameboard[0].length;

        // change the selected blocks to empty
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (gameboard[i][j] == SELECTED) {
                    gameboard[i][j] = EMPTY;
                }
            }
        }

        // Apply gravity - blocks move UP in each column
        for (int j = 0; j < cols; j++) { // look in each column
            int writeIndex = 0; // Position to write next non-empty block
            for (int i = 0; i < rows; i++) {
                if (gameboard[i][j] != EMPTY) { // is a block, if empty skip
                    if (writeIndex != i) { // If the above of the block have empty or the block is not at right position
                        gameboard[writeIndex][j] = gameboard[i][j]; // Move block to top or right position
                        gameboard[i][j] = EMPTY; // the original is empty, for after blocks move after it
                    }
                    writeIndex++;
                }
            }
        }


        // Identify which columns are completely empty
        boolean[] emptyColumn = new boolean[cols];
        int nonEmptyCols = 0;

        for (int j = 0; j < cols; j++) { // column first
            boolean columnIsEmpty = true;
            for (int i = 0; i < rows; i++) { //each blocks
                if (gameboard[i][j] != EMPTY) { //is not empty column
                    columnIsEmpty = false; // do nothing
                    break; // just check the row 0 in each column
                }
            }
            emptyColumn[j] = columnIsEmpty; //emptyColumn[j] to store T/F in each column
            if (!columnIsEmpty) { //columnIsEmpty = false
                nonEmptyCols++;  // there is n column is non-empty column
            }
        }
        // If all columns are empty, return empty board
        if (nonEmptyCols == 0) {
            return new char[rows][0];
        }
        // If no empty columns, return the original board (no shrinking needed)
        if (nonEmptyCols == cols) {
            return gameboard; //unchange
        }


        // Create new board with only non-empty columns (shift left)
        char[][] newBoard = new char[rows][nonEmptyCols];
        int newCol = 0;

        for (int j = 0; j < cols; j++) {
            if (!emptyColumn[j]) { // This column has blocks
                for (int i = 0; i < rows; i++) {
                    newBoard[i][newCol] = gameboard[i][j]; //reset the number of columns, and copy the one already move up
                }
                newCol++; // next column
            }
        }

        return newBoard; // already move up and left
    }

    /**
     * This method is to check if the game is over.
     *
     * The game is over when there is no valid selection left on the gameboard.
     *
     * @param gameboard
     * @return true if the game is over, false otherwise.
     */
    boolean isGameOver(char[][] gameboard) {
        int rows = gameboard.length;
        int cols = gameboard[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                char current = gameboard[i][j];

                // Skip empty cells
                if (current == EMPTY) {
                    continue;
                }

                // Check right neighbor
                if (j < cols - 1 && gameboard[i][j + 1] == current) { // cols != 0
                    return false;
                }

                // Left
                if (j > 0 && gameboard[i][j - 1] == current) { // cols != 0
                    return false;
                }

                // Up
                if (i > 0 && gameboard[i - 1][j] == current) {
                    return false;
                }

                // Check bottom neighbor
                if (i < rows - 1 && gameboard[i + 1][j] == current) {
                    return false;
                }
            }
        }
        // all empty or not same elements next to it
        return true;
    }


    /**
     * This method is to randomize the gameboard with the symbols defined in SYMBOLS.
     *
     * The method will fill the gameboard with random symbols from SYMBOLS except for the EMPTY symbol.
     * @param gameboard is a 2D char array that is always non-null and pointed to a
     *                   rectangular area.
     */
    void randomizeBoard(char[][] gameboard) {

        for (int i = 0; i < gameboard.length; i++) { // A-Z
            for (int j = 0; j < gameboard[i].length; j++) { // 0-9
                // Generate random index from 1 to SYMBOLS.length-1 (skip EMPTY at index 0)
                int randomIndex = 1 + (int) (Math.random() * (SYMBOLS.length - 1));
                //If SYMBOLS.length = 5, then SYMBOLS.length - 1 = 4
                //
                //Math.random() * 4 gives values from 0.0 to 3.999... and make it become integer
                //add 1 to skip the EMPTY symbol at index 0
                gameboard[i][j] = SYMBOLS[randomIndex];

            }
        }

    }
}