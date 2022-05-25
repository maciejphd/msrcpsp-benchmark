package util.random;


public class RandomDouble extends RandomBase<Double> {

  public RandomDouble(long seed) {
    super(seed);
  }

  @Override
  public Double next(int bound) {
    return random.nextDouble() * (double)bound;
  }

}
