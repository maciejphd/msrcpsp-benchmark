package algorithms.quality_measure;

import algorithms.problem.BaseIndividual;

import java.util.List;

/**
 * Minimum distance to transform every point from potential
 * Pareto Front to dominate true Pareto Front
 */
public class Epsilon extends BaseMeasure {

  public Epsilon(List<? extends BaseIndividual> referencePopulation) {
    this.referencePopulation = referencePopulation;
  }

  /**
   * Minimum distance to move <code>population</code>, required
   * for it to dominate reference population
   *
   * @param population population to measure
   * @return epsilon metric
   */
  @Override
  public <T extends BaseIndividual> double getMeasure(List<T> population) {
    double[] minDistances = new double[population.size()];
    double min;
    for (int i = 0; i < referencePopulation.size(); ++i) {
      minDistances[i] = getMinDistance(referencePopulation.get(i), population);
    }
    double max = 0.0d;
    for (int i = 0; i < minDistances.length; ++i) {
      if (minDistances[i] > max) {
        max = minDistances[i];
      }
    }
    return max;
  }

}
