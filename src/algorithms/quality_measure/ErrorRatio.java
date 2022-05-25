package algorithms.quality_measure;

import algorithms.problem.BaseIndividual;

import java.util.List;

/**
 * Number of points in given population that are not part of
 * the true Pareto Front.
 */
public class ErrorRatio extends BaseMeasure {

  public ErrorRatio(List<? extends BaseIndividual> optimalParetoFront) {
    this.referencePopulation = optimalParetoFront;
  }

  /**
   * Number of solutions found in potential pareto
   * front, that dont exist in optimal pareto front.
   *
   * @param population population to check
   * @return coverage measure
   */
  @Override
  public <T extends BaseIndividual> double getMeasure(List<T> population) {
    int result = 0;

    for (BaseIndividual individual : population) {
      if (!existsInReferenceFront(individual)) {
        ++result;
      }
    }

    return result;
  }

}
