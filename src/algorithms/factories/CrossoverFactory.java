package algorithms.factories;


import algorithms.evolutionary_algorithms.crossover.*;

public class CrossoverFactory {

  /**
   * Creates crossover based on provided type.
   * Default: {@link BinomialCrossover}
   *
   * @param type type to use
   * @return chosen crossover method
   */
  public BaseCrossover createCrossover(CrossoverType type) {
    switch (type) {
      case BINOMIAL:
        return new BinomialCrossover();
      case EXPONENTIAL:
        return new ExponentialCrossover();
      case SINGLE_POINT:
        return new SinglePointCrossover();
      case ORDERED:
        return new OrderedCrossover();
      case UNIFORM:
        return new UniformCrossover();
      case COMPETITION:
        return new CompetitionCrossover();
      default:
        return new BinomialCrossover();
    }
  }

}
