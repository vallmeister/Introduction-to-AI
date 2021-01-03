package csp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Sheet 4 exercise 1.2
 *
 * Based on the arc-consistent Sudoku search space, we find a solution via backtrack search.
 */
public class SudokuSolver {
  private static SudokuCell[][] sudokuCells = new SudokuCell[9][9];
  private static Set<SudokuCell> visitedCells = new HashSet<>();

  public static void main(String[] args) {
    readInput();
    backtrackSearch2();
    printOutput();
  }

  private static boolean backtrackSearch2() {
    obtainConsistency(sudokuCells);

    if (hasConflict(sudokuCells)) return false;
    else if (isSolved(sudokuCells)) return true;

    SudokuCell nextVariable = firstCellWithSmallestDomain(sudokuCells);
    int row = nextVariable.row;
    int column = nextVariable.column;
    SudokuCell[][] backup = copyOfSudoku(sudokuCells);

    TreeSet<Integer> numbers = nextVariable.getDomain();

    for (int number : numbers) {
      SudokuCell temp = new SudokuCell(number, row, column);
      sudokuCells[row][column] = temp;
      visitedCells.add(temp);

      boolean search = backtrackSearch2();
      if (search) return true;
      else sudokuCells = backup;
    }
    return false;
  }

  private static void readInput() {
    try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))) {
      String input;
      for (int i = 0; i < 9; i++) {
        input = bufferedReader.readLine();
        for (int j = 0; j < 9; j++) {
          int tmp = input.charAt(j) - '0';
          if (tmp != 0) {
            sudokuCells[i][j] = new SudokuCell(tmp, i, j);
          } else {
            sudokuCells[i][j] = new SudokuCell(i, j);
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void obtainConsistency(SudokuCell[][] cells) {
    int counter;
    do {
      counter = 0;
      for (int i = 0; i < 9; i++) {
        for (int j = 0; j < 9; j++) {
          SudokuCell temp = cells[i][j];
          if (temp.getDomain().size() == 1) {
            counter += obtainRowConsistency(cells, temp, i);
            counter += obtainColumnConsistency(cells, temp, j);
            counter += obtainQuadrantConsistency(cells, temp, i, j);
          }
        }
      }
    } while (counter != 0);
  }

  private static int obtainRowConsistency(SudokuCell[][] cells, SudokuCell currentCell, int row) {
    int result = 0;
    int number = currentCell.getDomain().first();
    for (int i = 0; i < 9; i++) {
      SudokuCell tmp = cells[row][i];
      if (tmp != currentCell) if (tmp.getDomain().remove(number)) result++;
    }
    return result;
  }

  // column constraints: only one integer per column. Counts how many violations were found.
  private static int obtainColumnConsistency(SudokuCell[][] cells, SudokuCell currentCell, int column) {
    int result = 0;
    int number = currentCell.getDomain().first();
    for (int i = 0; i < 9; i++) {
      SudokuCell tmp = cells[i][column];
      if (tmp != currentCell) if (tmp.getDomain().remove(number)) result++;
    }
    return result;
  }

  // square constraint: only one integer per 3x3 square. Counts how many violations were found.
  private static int obtainQuadrantConsistency(SudokuCell[][] cells, SudokuCell currentCell, int row, int column) {
    int result = 0;
    int number = currentCell.getDomain().first();
    Quadrant currentQuadrant = Quadrant.fromCell(row, column);
    for (int i = 0; i < 9; i++) {
      for (int j = 0; j < 9; j++) {
        SudokuCell temp = cells[i][j];
        if (currentQuadrant == Quadrant.fromCell(i, j) && currentCell != temp) {
          if (temp.getDomain().remove(number)) result++;
        }
      }
    }
    return result;
  }

  private static boolean isSolved(SudokuCell[][] cells) {
    for (int i = 0; i < 9; i++) {
      for (int j = 0; j < 9; j++) {
        if (cells[i][j].getDomain().size() != 1) return false;
      }
    }
    return true;
  }

  private static boolean hasConflict(SudokuCell[][] cells) {
    for (int i = 0; i < 9; i++) {
      for (int j = 0; j < 9; j++) {
        if (cells[i][j].getDomain().size() == 0) {
          return true;
        }
      }
    }
    return false;
  }

  private static SudokuCell[][] copyOfSudoku(SudokuCell[][] cells) {
    SudokuCell[][] result = new SudokuCell[9][9];
    for (int i = 0; i < 9; i++) {
      for (int j = 0; j < 9; j++) {
        result[i][j] = new SudokuCell(cells[i][j]);
      }
    }
    return result;
  }

  private static SudokuCell firstCellWithSmallestDomain(SudokuCell[][] cells) {
    SudokuCell result = null;
    int minimum = 10;
    for (int i = 0; i < 9; i++) {
      for (int j = 0; j < 9; j++) {
        SudokuCell temp = cells[i][j];
        int size = temp.getDomain().size();
        if (!visitedCells.contains(temp) && size > 1 && size < minimum) {
          minimum = size;
          result = temp;
        }
      }
    }
    return result;
  }

  private static void printOutput() {
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < 9; i++) {
      for (int j = 0; j < 9; j++) {
        stringBuilder.append(sudokuCells[i][j].getDomain().first());
      }
      stringBuilder.append("\n");
    }
    System.out.println(stringBuilder.toString());
  }


}

class SudokuCell {
  private TreeSet<Integer> domain;
  public int row;
  public int column;

  /**
   * Constructor for empty cells.
   */
  SudokuCell(int row, int column) {
    domain = new TreeSet<>(numberList());
    this.row = row;
    this.column = column;
  }

  SudokuCell(int number, int row, int column) {
    domain = new TreeSet<>();
    domain.add(number);
    this.row = row;
    this.column = column;
  }

  SudokuCell(SudokuCell origin) {
    domain = (TreeSet<Integer>) origin.getDomain().clone();
    row = origin.row;
    column = origin.column;
  }

  public void setDomain(TreeSet<Integer> domain) {
    this.domain = domain;
  }

  SudokuCell(TreeSet<Integer> set, int row, int column) {
    domain = set;
    this.row = row;
    this.column = column;
  }

  public TreeSet<Integer> getDomain() {
    return domain;
  }

  private static List<Integer> numberList() {
    List<Integer> result = new ArrayList<>(9);
    for (int i = 1; i < 10; i++) {
      result.add(i);
    }
    return result;
  }
}

enum Quadrant {
  QUADRANT11, QUADRANT12, QUADRANT13,
  QUADRANT21, QUADRANT22, QUADRANT23,
  QUADRANT31, QUADRANT32, QUADRANT33;

  static Quadrant fromCell(int row, int column) {
    if (row < 3) {
      if (column < 3) return QUADRANT11;
      else if (column < 6) return QUADRANT12;
      else return QUADRANT13;
    }

    if (row < 6) {
      if (column < 3) return QUADRANT21;
      else if (column < 6) return QUADRANT22;
      else return QUADRANT23;
    }

    if (column < 3) return QUADRANT31;
    else if (column < 6) return QUADRANT32;
    else return QUADRANT33;
  }
}