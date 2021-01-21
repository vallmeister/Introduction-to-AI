package games;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class Euclid {
  private static int largerNumber;
  private static int smallerNumber;
  private static Tree gameTree;

  public static void main(String[] args) throws InterruptedException {
    readInput();
    buildTree();
    calculateGameValue(gameTree.root);
    printOutput();
  }

  private static void calculateGameValue(Node node) {
    if (node.isGoalState) {
      node.gameValue = node.minimax ? 1 : -1;
    } else {
      List<Node> successors = node.successors;
      List<Integer> gameValues = new ArrayList<>();
      for (Node successor : successors) {
        calculateGameValue(successor);
        if (node.minimax && successor.gameValue == 1) {
          node.gameValue = 1;
          return;
        } else if (!node.minimax && successor.gameValue == -1) {
          node.gameValue = -1;
          return;
        }
        gameValues.add(successor.gameValue);
      }
      int size = gameValues.size();
      Collections.sort(gameValues);
      node.gameValue = node.minimax ? gameValues.get(size - 1) : gameValues.get(0);
    }
  }

  private static void printTree() throws InterruptedException {
    StringBuilder stringBuilder = new StringBuilder();
    LinkedBlockingQueue<Node> nodes = new LinkedBlockingQueue<>();

    System.out.println(stringBuilder.toString());
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

  private static void buildTree() throws InterruptedException {
    Node root = new Node(largerNumber, smallerNumber, true);
    gameTree = new Tree(root);

    LinkedBlockingQueue<Node> nodeQueue = new LinkedBlockingQueue<>();
    nodeQueue.put(root);
    while (nodeQueue.size() > 0) {
      Node currentNode = nodeQueue.poll();
      int larger = currentNode.largerNumber;
      int smaller = currentNode.smallerNumber;
      if (!goalTest(currentNode)) {
        int div = larger / smaller;
        for (int i = 1; i <= div; i++) {
          Node temp = new Node(larger - i * smaller, smaller, !currentNode.minimax);
          currentNode.successors.add(temp);
          nodeQueue.put(temp);
        }
      } else {
       currentNode.isGoalState = true;
      }
    }
  }

  private static boolean goalTest(Node node) {
    return node.smallerNumber == 0;
  }

  private static void printOutput() {
    if (gameTree.root.gameValue == 1) {
      System.out.println("Stan wins");
    } else {
      System.out.println("Ollie wins");
    }
  }
}

class Node {
  public int largerNumber;
  public int smallerNumber;
  public List<Node> successors;
  public boolean isGoalState;

  // 0 represents min-player, 1 is for max-player
  public boolean minimax;

  public int gameValue;

  public Node(int first, int second, boolean player) {
    largerNumber = Math.max(first, second);
    smallerNumber = Math.min(first, second);
    successors = new ArrayList<>();
    minimax = player;
    isGoalState = false;
    gameValue = 0;
  }

  @Override
  public String toString() {
    return "{" +
        largerNumber +
        " " + smallerNumber +
        '}';
  }
}

class Tree {
  public final Node root;

  public Tree(Node node) {
    root = node;
  }
}