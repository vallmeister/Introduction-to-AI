package games;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Euclid {
  private static int largerNumber;
  private static int smallerNumber;

  public static void main(String[] args) {
    readInput();
    Node root = new Node(largerNumber, smallerNumber, null);

  }

  private static void readInput() {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))){
      String input = reader.readLine();
      String[] inputNumbers = input.split(" ");
      int[] numbers = {Integer.parseInt(inputNumbers[0]), Integer.parseInt(inputNumbers[1])};
      largerNumber = Math.max(numbers[0], numbers[1]);
      smallerNumber = Math.min(numbers[0], numbers[1]);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

class Node {
  public int largerNumber;
  public int smallerNumber;
  public List<Node> successors;
  public Node predecessor;

  public Node(int first, int second, Node parent) {
    largerNumber = Math.max(first, second);
    smallerNumber = Math.min(first, second);
    successors = new ArrayList<>();
    predecessor = parent;
  }
}