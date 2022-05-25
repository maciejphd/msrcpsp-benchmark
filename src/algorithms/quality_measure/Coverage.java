package algorithms.quality_measure;

import algorithms.problem.BaseIndividual;

import java.util.List;

/**
 * Measures the number of solutions found in potential pareto
 * front, that exist in optimal pareto front. Equivalent to
 * C1R measure.
 */
public class Coverage extends BaseMeasure {

  public Coverage(List<? extends BaseIndividual> optimalParetoFront) {
    this.referencePopulation = optimalParetoFront;
  }

  /**
   * Number of solutions found in potential pareto
   * front, that exist in optimal pareto front.
   *
   * @param population population to check
   * @return coverage measure
   */
  @Override
  public <T extends BaseIndividual> double getMeasure(List<T> population) {
    int result = 0;

    for (BaseIndividual individual : population) {
      if (existsInReferenceFront(individual)) {
        ++result;
      }
    }

    return result;
  }

}
