package ch1_fundamentals.analysis_of_algorithms;

import java.security.InvalidParameterException;

public class Building {
  private final int N;
  private final int F;

  public Building(int floors, int eggCrackFloor) {
    if (eggCrackFloor > floors) {
      throw new InvalidParameterException("Egg crack floor can't be larger than the total number of floors");
    }

    N = floors;
    F = eggCrackFloor;
  }

  public int floors() {
    return N;
  }

  /**
   * Try throwing an egg from the given floor of the building.
   * 
   * @param floor
   * @return whether the egg cracked or not
   */
  public boolean throwEgg(int floor) {
    return floor >= F;
  }
}
