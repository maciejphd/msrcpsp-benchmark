package util.random;

import java.util.Random;

/**
 * Abstract Base Class to unify procuring or random numbers
 * independent of type
 */
abstract public class RandomBase<T extends Number> {

  protected Random random;

  public RandomBase(long seed) {
    random = new Random(seed);
  }

  public int nextInt() {
    return random.nextInt();
  }

  public int nextInt(int bound) {
    return random.nextInt(bound);
  }

  public double nextDouble() {
    return random.nextDouble();
  }

  abstract public T next(int bound);

  public Random getRandom() {
    return random;
  }

}
