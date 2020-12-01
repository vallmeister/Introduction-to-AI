import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Sheet 2 exercise 2. A program that solves Sokoban instances by using Breadth First Search.
 */
public class SokobanBFS {
  private static char[][] sokobanCharTable;

  public static void main(String[] args) {
    String input;
    List<String> sokobanTable = new ArrayList<>();

    try (BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in))){
      while (!((input = inputReader.readLine())/*.equals("")*/ == null/**/)) {
        sokobanTable.add(input);
      }
    } catch (IOException ioException) {
      ioException.printStackTrace();
      System.exit(0);
    }
    int height = sokobanTable.size();
    int maximumWidth = -1;
    for (String row : sokobanTable) {
      if (row.length() > maximumWidth) maximumWidth = row.length();
    }
    System.out.println(maximumWidth);

  }

  private static boolean isGoalState() {
    int openGoalPositions = 0;
    for (int i = 0; i < sokobanCharTable.length; i++) {
      for (int j = 0; j < sokobanCharTable[0].length; j++) {
        if (sokobanCharTable[i][j] == '.') {
          openGoalPositions++;
        }
      }
    }
    return openGoalPositions == 0;
  }
}
class Tree {
  private PositioNode root;
}

class PositioNode {
  public enum Directions {
    UP,
    RIGHT,
    DOWN,
    LEFT
  }

  private sokobanPositionB position;
  private Map<Directions, sokobanPositionB> children;
  private int level;
}

class sokobanPositionB {
  private int row;
  private int column;

  sokobanPositionB(int row, int column) {
    this.row = row;
    this.column = column;
  }
}