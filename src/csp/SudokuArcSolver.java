package csp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class SudokuArcSolver {
  private static int[][] initialSudoku = new int[9][9];
  private static boolean[][] isPartOfInput = new boolean[9][9];

  // solutions[i][j][k] means that k+1 is a possible solution for the cell in row i and column j
  private static boolean[][][] solutions= new boolean[9][9][9];

  public static void main(String[] args) {
    String input;
    try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))){
      // read Sudoku from stdin
      for (int i = 0; i < 9; i++) {
        input = bufferedReader.readLine();
        for (int j = 0; j < 9; j++) {
          initialSudoku[i][j] = input.charAt(j) - '0';
        }
      }

      // solve Sudoku by backtracking

      // initialize the input cells with input as only solution
      for (int i = 0; i < 9; i++) {
        for (int j = 0; j < 9; j++) {
          int tmp = initialSudoku[i][j];
          if (tmp != 0) {
            isPartOfInput[i][j] = true;
            solutions[i][j][tmp - 1] = true;
          }
        }
      }
      backtracking();

      // print solution
      for (int i = 0; i < 9; i++){
        String temp;
        for (int j = 0; j < 9; j++) {
          temp = "{";
          for (int k = 9; k > 0; k--) {
            if (solutions[i][j][k-1]) {
              temp += k + ",";
            }
          }
          temp = temp.substring(0, Math.max(temp.length() - 1, 1));
          temp += "}";
          System.out.print(temp);
        }

        System.out.print("\n");
      }

      /*for (int i = 0; i < 9; i++) {
        for (int j = 0; j < 9; j++) {
          System.out.print(Arrays.toString(solutions[i][j]));
        }
        System.out.println();
      }*/
    } catch (IOException ioException) {
      ioException.printStackTrace();
    }
  }

  private static void backtracking() {
    int[][] current = initialSudoku.clone();
    for (int i = 0; i < 9; i++) {

      for (int j = 1; j < 10; j++) {
        if (backtrackCell(current, j, i)) {
          solutions[0][0][j-1] = true;
        }
      }
    }
  }

  /**
   * Checks for current sudoku if it can apply the given integer
   */
  private static boolean backtrackCell(int[][] current, int number, int numberCode) {
    if (numberCode > 80) return true;

    int row = numberCode / 9;
    int column = numberCode % 9;

    if (isPartOfInput[row][column]) return initialSudoku[row][column] == number;

    if (!occursInRow(current, number, row) && !occursInColumn(current, number, column)) {
      current[row][column] = number;
      for (int j = numberCode + 1; j < 81; j++) {
        for (int i = 1; i < 10; i++){
          boolean tmp = backtrackCell(current, i, j);
          if (tmp) {
            solutions[row][column][number - 1] = true;
            return true;
          }
        }
      }
    }
    return false;
  }

  private static boolean occursInRow(int[][] current, int number, int row) {
    for (int i = 0; i < 9; i++) {
      if (current[row][i] == number) return true;
    }
    return false;
  }

  private static boolean occursInColumn(int[][] current, int number, int column) {
    for (int i = 0; i < 9; i++) {
      if (current[i][column] == number) return true;
    }
    return false;
  }
}
