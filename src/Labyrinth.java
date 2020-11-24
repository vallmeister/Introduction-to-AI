import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/*
Sheet 02 exercise 1 for DOMjudge.
 */
public class Labyrinth {

  public static void main(String[] args) {
    String input;
    List<String> labyrinthStrings = new ArrayList<>();
    try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))){
      while (!((input = bufferedReader.readLine()).equals(""))) {
        labyrinthStrings.add(input);
      }
      int width = labyrinthStrings.get(0).length();
      int height = labyrinthStrings.size();
      char[][] labyrinth = new char[width][height];

      for (int i = 0; i < width; i++) {
        for (int j = 0; j < height - 1; j++) {
          labyrinth[i][j] = labyrinthStrings.get(i).charAt(j);
        }
      }
    } catch (IOException ioException) {
      ioException.printStackTrace();
    }
  }
}
