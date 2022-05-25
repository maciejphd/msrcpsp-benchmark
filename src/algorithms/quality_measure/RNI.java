package algorithms.quality_measure;

import algorithms.problem.BaseIndividual;

import java.util.List;

/**
 * Measures the ratio of non-dominated individuals
 * to the size of true Pareto front. This measure is equivalent to
 * Overall Non-dominated Vector Generation and Ratio when
 * all known points are given or to Generational Non-dominated
 * Vector Generation and Ratio when current generation is given.
 * Can be used to calculate RNI (C2R) with population size and current population
 * as parameters.
 */
public class RNI extends BaseMeasure  {

  public RNI(double referenceValue) {
    this.referenceValue = referenceValue;
  }

  /**
   * Measures the ratio of non-dominated individuals
   * to the size of true Pareto front.
   *
   * @param population population to measure
   * @return RNI, GNVGR, RNI or C2R of given population
   */
  @Override
  public <T extends BaseIndividual> double getMeasure(List<T> population) {
    int sum = 0;
    for (BaseIndividual individual : population) {
      if (individual.isNotDominatedBy(population)) {
        ++sum;
      }
    }
    return sum / referenceValue;
  }

}
