package algorithms.quality_measure;

import algorithms.problem.BaseIndividual;

import java.util.List;

/**
 * Number of points that remain non-dominated after
 * comparison with points achieved with other approach.
 */
public class Purity extends BaseMeasure {

  public Purity(List<? extends BaseIndividual> referencePopulation) {
    this.referencePopulation = referencePopulation;
  }

  /**
   * Calculates how many points from the <code>population</code>,
   * remain non-dominated when compared with the reference population.
   *
   * @param population population to measure with
   * @return number of points non-dominated in <code>population</code>
   */
  @Override
  public <T extends BaseIndividual> double getMeasure(List<T> population) {
    double result = 0.0d;
    for (T individual : population) {
      if (individual.isNotDominatedBy(referencePopulation)) {
        ++result;
      }
    }
    return result / population.size();
  }

}
