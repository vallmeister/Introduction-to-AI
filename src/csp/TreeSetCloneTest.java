package csp;

import java.util.TreeSet;

public class TreeSetCloneTest {
  public static void main(String[] args) {
    TreeSet<Integer> treeSet = new TreeSet<>();
    treeSet.add(1);
    treeSet.add(2);
    treeSet.add(3);
    System.out.println("TreeSet before cloning: " + treeSet.toString());

    TreeSet<Integer> copy = (TreeSet<Integer>) treeSet.clone();
    copy.add(4);
    treeSet.remove(2);
    System.out.println("TreeSet after cloning: " + treeSet);
    System.out.println("Copy after modification: " + copy);
  }
}
