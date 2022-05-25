package algorithms.quality_measure;

import algorithms.problem.BaseIndividual;

import java.util.Collections;
import java.util.List;

/**
 * Average standard deviation between consecutive solutions
 * in the non-dominated front
 */
public class DeltaMetric extends BaseMeasure {

  /**
   * Average standard deviation between consecutive solutions
   * in the non-dominated front
   *
   * @param population population to measure
   * @return Delta Metric
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
    double sum = 0.0d;
    for (double distance : distances) {
      sum += Math.abs(distance - averageDistance);
    }

    return sum / (population.size() - 1);
  }

}
