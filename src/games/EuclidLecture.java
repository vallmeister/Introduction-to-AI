package games;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EuclidLecture {

  public static void main(String[] args) {
    int[] numbers = readInput();
    State initialState = State.initialStateSingleton(numbers);
    int gameValue = calculateGameValue(initialState);
    if (gameValue == 1) {
      System.out.println("Stan wins");
    } else if (gameValue == -1){
      System.out.println("Ollie wins");
    }
  }

  private static int calculateGameValue(State state) {
    if (state.goalTest()) {
      return state.player ? 1 : -1;
    }
    int large = state.largerNumber;
    int small = state.smallerNumber;
    int div = large / small;

    List<Integer> successorValues = new ArrayList<>();

    for (int i = div; i >= 1; i--) {
      State temp = new State(large - i * small, small, !state.player);
      //state.addSuccessor(temp);
      int value = calculateGameValue(temp);
      if (state.player && value == 1) {
        return 1;
      } else if (!state.player && value == -1) {
        return -1;
      }
      successorValues.add(value);
    }
    Collections.sort(successorValues);
    return state.player ? successorValues.get(successorValues.size() - 1) : successorValues.get(0);
  }

  /**
   * Reads the two input numbers
   * @return {smaller, larger}
   */
  private static int[] readInput() {
    int[] result = new int[2];
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))){
      String input = reader.readLine();
      String[] inputNumbers = input.split(" ");
      int[] numbers = {Integer.parseInt(inputNumbers[0]), Integer.parseInt(inputNumbers[1])};
      result[0] = Math.min(numbers[0], numbers[1]);
      result[1] = Math.max(numbers[0], numbers[1]);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return result;
  }
}

class State {
  /* true is MAX, false is MIN player*/
  public boolean player;

  // Numbers of that state
  public int smallerNumber;
  public int largerNumber;

  //private List<State> successors;

  private static State initialState;

  public State(int first, int second, boolean minimax) {
    smallerNumber = Math.min(first, second);
    largerNumber = Math.max(first, second);
    player = minimax;
    //successors = new ArrayList<>();
  }

  public boolean goalTest() {
    return smallerNumber == 0;
  }

  public static State initialStateSingleton(int[] numbers) {
    if (initialState == null) {
      initialState = new State(numbers[0], numbers[1], true);
    }
    return initialState;
  }

  /*public void addSuccessor(State successor) {
    successors.add(successor);
  }

  public List<State> getSuccessors() {
    return successors;
  }*/
}
