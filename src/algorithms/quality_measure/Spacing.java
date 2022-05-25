package algorithms.quality_measure;

import algorithms.problem.BaseIndividual;

import java.util.Collections;
import java.util.List;

/**
 * Relative distance between consecutive points on the
 * potential Pareto front.
 */
public class Spacing extends BaseMeasure {

  /**
   * Calculates relative distance between consecutive points on the
   * potential Pareto front.
   *
   * @param population population to measure
   * @return average distance between consecutive
   *         points in the sorted <code>population</code>
   */
  @Override
  public <T extends BaseIndividual> double getMeasure(List<T> population) {
    Collections.sort(population);

    double averageDistance = 0.0d;
    double[] distances = new double[population.size() - 1];
    for (int i = 1; i < population.size(); ++i) {
      distances[i-1] = getDistance(population.get(i-1), population.get(i));
      averageDistance += distances[i-1];
    }
    averageDistance = averageDistance / (population.size() - 1);

    double spacing = 0.0d;
    for (double distance : distances) {
      spacing += Math.pow(distance - averageDistance, 2);
    }
    spacing = spacing / (population.size() - 1);
    return Math.sqrt(spacing);
  }

}
