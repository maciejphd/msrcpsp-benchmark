package algorithms.quality_measure;

import algorithms.problem.BaseIndividual;

import java.util.List;

/**
 * Measures the size of pareto front in given population.
 * It is a number of individuals that are not dominated by
 * any other individual. This measure is equivalent to
 * Overall Non-dominated Vector Generation when
 * all known points are given or to Generational
 * Non-dominated Vector Generation when current generation is given.
 */
public class ONVG extends BaseMeasure {

  /**
   * Calculates number of individuals that are not dominated by
   * any other individual.
   *
   * @param population population to measure
   * @return ONVG or GNVG of given population
   */
  @Override
  public <T extends BaseIndividual> double getMeasure(List<T> population) {
    int sum = 0;
    for (BaseIndividual individual : population) {
      if (individual.isNotDominatedBy(population)) {
        ++sum;
      }
    }
    return sum;
  }

}
