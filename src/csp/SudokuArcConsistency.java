package csp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Sheet 4 exercise 1.1
 *
 * Given a Sudoku puzzle as input, we formulate a Constraint Satisfaction Problem and output domains for each cell.
 * These domains obtain arc consistency
 */
public class SudokuArcConsistency {

  private static int[][] inputSudoku = new int[9][9];
  private static Cell[][] sudokuDomains = new Cell[9][9];

  private static List<Integer> numberList() {
    List<Integer> result = new ArrayList<>(9);
    for (int i = 1; i < 10; i++) {
      result.add(i);
    }
    return result;
  }

  public static void main(String[] args) {

    // read input
    String input;
    try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))){
      for (int i = 0; i < 9; i++) {
        input = bufferedReader.readLine();
        for (int j = 0; j < 9; j++) {
          int tmp = input.charAt(j) - '0';
          inputSudoku[i][j] = tmp;
          if (tmp != 0) {
            sudokuDomains[i][j] = new Cell(tmp);
          } else {
            sudokuDomains[i][j] = new Cell(numberList());
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    int counter;
    do {
      counter = 0;
      for (int i = 0; i < 9; i++) {
        for (int j = 0; j < 9; j++) {
          Cell temp = sudokuDomains[i][j];
          if (temp.getNumbers().size() == 1) {
            counter += obtainRowConsistency(temp, i);
            counter += obtainColumnConsistency(temp, j);
            counter += obtainSquareConsistency(temp, i, j);
          }
        }
      }
      //counter--;
    } while (counter != 0);

    // print output
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < 9; i++) {
      for (int j = 0; j < 9; j++) {
        builder.append("{");
        TreeSet<Integer> tmp = sudokuDomains[i][j].getNumbers();
        builder.append(tmp.pollFirst());
        while (!tmp.isEmpty()) {
          builder.append(",");
          builder.append(tmp.pollFirst());
        }
        builder.append("}");
      }
      builder.append("\n");
    }
    System.out.println(builder.toString());
  }

  /*
   * constraints for Sudoku
   */

  // row constraints: only one integer per row. Counts how many violations were found.
  private static int obtainRowConsistency(Cell currentCell, int row) {
    int result = 0;
    int number = currentCell.getNumbers().first();
    for (int i = 0; i < 9; i++) {
      Cell tmp = sudokuDomains[row][i];
      if (tmp != currentCell) {
        if (tmp.getNumbers().remove(number)) result++;
      }
    }
    return result;
  }

  // column constraints: only one integer per column. Counts how many violations were found.
  private static int obtainColumnConsistency(Cell currentCell, int column) {
    int result = 0;
    int number = currentCell.getNumbers().first();
    for (int i = 0; i < 9; i++) {
      Cell tmp = sudokuDomains[i][column];
      if (tmp != currentCell) {
        if (tmp.getNumbers().remove(number)) result++;
      }
    }

    return result;
  }

  // square constraint: only one integer per 3x3 square. Counts how many violations were found.
  private static int obtainSquareConsistency(Cell currentCell, int row, int column) {
    int result = 0;
    int number = currentCell.getNumbers().first();
    Square currentSquare = Square.fromCell(row, column);
    for (int i = 0; i < 9; i++) {
      for (int j = 0; j < 9; j++) {
        Cell temp = sudokuDomains[i][j];
        if (currentSquare == Square.fromCell(i, j) && currentCell != temp) {
          if (temp.getNumbers().remove(number)) result++;
        }
      }
    }

    return result;
  }
}

class Cell {
  private TreeSet<Integer> numbers;

  Cell(List<Integer> integerList) {
    numbers = new TreeSet<>(integerList);
  }

  Cell(int number) {
    numbers = new TreeSet<>();
    numbers.add(number);
  }

  TreeSet<Integer> getNumbers() {
    return numbers;
  }
}

enum Square {
  SQUARE11, SQUARE12, SQUARE13,
  SQUARE21, SQUARE22, SQUARE23,
  SQUARE31, SQUARE32, SQUARE33;

  static Square fromCell(int row, int column) {
    if (row < 3) {
      if (column < 3) return SQUARE11;
      else if (column < 6) return SQUARE12;
      else return SQUARE13;
    }

    if (row < 6) {
      if (column < 3) return SQUARE21;
      else if (column < 6) return SQUARE22;
      else return SQUARE23;
    }

    if (column < 3) return SQUARE31;
    else if (column < 6) return SQUARE32;
    else return SQUARE33;
  }
}
