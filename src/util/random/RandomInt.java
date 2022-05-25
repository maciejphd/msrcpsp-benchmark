package util.random;


public class RandomInt extends RandomBase<Integer> {

  public RandomInt(long seed) {
    super(seed);
  }

  @Override
  public Integer next(int bound) {
    return random.nextInt(bound);
  }

}
