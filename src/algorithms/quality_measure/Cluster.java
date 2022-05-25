package algorithms.quality_measure;

import algorithms.problem.BaseIndividual;

import java.util.List;

/**
 * Determines how clustered are the solutions. Number of all
 * solutions divided by number of distinct solutions. Assuming
 * all values are distinct - the measure is equal to 1. Higher
 * values mean more clustered solutions.
 */
public class Cluster extends BaseMeasure {

  /**
   * Number of all solutions divided by number of distinct (on genotype level) solutions.
   *
   * @param population population to measure
   * @return number of all solutions divided by number of distinct solutions.
   */
  @Override
  public <T extends BaseIndividual> double getMeasure(List<T> population) {
    return population.size() / population.stream().distinct().count();
  }

}
