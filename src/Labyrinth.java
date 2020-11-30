import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/*
Sheet 02 exercise 1 for DOMjudge.
 */
public class Labyrinth {

  private static char[][] labyrinth;
  private static LinkedBlockingQueue<Position> nodes = new LinkedBlockingQueue<>();
  private static Set<Position> visitablePositions = new HashSet<>();

  public static void main(String[] args) throws InterruptedException {
    String input;
    List<String> labyrinthStrings = new ArrayList<>();
    try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))){
      while (!((input = bufferedReader.readLine())/*.equals("")*/==null)) {
        labyrinthStrings.add(input);
      }
      int width;
      int height = labyrinthStrings.size();
      labyrinth = new char[height][];

      for (int i = 0; i < height; i++) {
        String rowString = labyrinthStrings.get(i);
        width = rowString.length();
        labyrinth[i] = new char[width];
        for (int j = 0; j < width; j++) {
          char positionCharacter = rowString.charAt(j);
          labyrinth[i][j] = positionCharacter;
          if (!(positionCharacter == '#')) {
            visitablePositions.add(new Position(i, j, 0));
          }
        }
      }
    } catch (IOException ioException) {
      ioException.printStackTrace();
    }
    Position start = findStartPosition();
    Position goal = findGoalPosition();


    // Breadth First Search
    nodes.put(start);
    visitablePositions.remove(start);
    int path = 0;
    while (true) {
      Position currentState = nodes.poll();
      path++;
      if (currentState.equals(goal)) {
        System.out.println(currentState.level);
        break;
      }
      List<Position> neighbors = findExpansions(currentState);
      for (int i = 0; i < neighbors.size(); i++) {
        Position neighbor = neighbors.get(i);
        nodes.put(neighbor);
        visitablePositions.remove(neighbor);
      }
    }
  }

  private static Position findStartPosition() {
    int width;
    int height = labyrinth.length;
    for (int i = 0; i < height; i++) {
      width = labyrinth[i].length;
      for (int j = 0; j < width; j++) {
        if (labyrinth[i][j] == '@') {
          return new Position(i, j, 0);
        }
      }
    }
    return new Position(-1,-1, 0);
  }

  /*
  Find goal
   */
  private static Position findGoalPosition() {
    int width;
    int height = labyrinth.length;
    for (int i = 0; i < height; i++) {
      width = labyrinth[i].length;
      for (int j = 0; j < width; j++) {
        if (labyrinth[i][j] == '.') {
          return new Position(i, j, 0);
        }
      }
    }
    return new Position(-1,-1, 0);
  }

  private static List<Position> findExpansions(Position position) {
    int row = position.getRow();
    int column = position.getColumn();
    int level = position.level;

    List<Position> neighbors = new ArrayList<>();
    // Up
    if (row > 0 && !(labyrinth[row - 1][column] == '#')) {
      Position tmp = new Position(row - 1, column, level + 1);
      if (visitablePositions.contains(tmp)) {
          neighbors.add(tmp);
      }
    }
    if (column + 1 < labyrinth[row].length
        && !(labyrinth[row][column + 1] == '#')) {
      Position tmp = new Position(row, column + 1, level + 1);
      if (visitablePositions.contains(tmp)) {
        neighbors.add(tmp);
      }
    }
    if (row + 1 < labyrinth.length
        && !(labyrinth[row + 1][column] == '#')) {
      Position tmp = new Position(row + 1, column, level + 1);
      if (visitablePositions.contains(tmp)) {
        neighbors.add(tmp);
      }
    }
    if (column > 0 && !(labyrinth[row][column - 1] == '#')) {
      Position tmp = new Position(row, column - 1, level + 1);
      if (visitablePositions.contains(tmp)) {
        neighbors.add(tmp);
      }
    }

    return neighbors;
  }
}

class Position {
  private final int row;
  private final int column;
  public int level;

  Position(int row, int column, int level) {
    this.row = row;
    this.column = column;
    this.level = level;
  }

  public int getRow() {
    return row;
  }

  public int getColumn() {
    return column;
  }

  @Override
  public String toString() {
    return "Position{" +
        "row=" + row +
        ", column=" + column +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Position position = (Position) o;
    return row == position.row &&
        column == position.column;
  }

  @Override
  public int hashCode() {
    return Objects.hash(row, column);
  }
}