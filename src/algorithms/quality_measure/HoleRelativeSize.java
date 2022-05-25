package algorithms.quality_measure;

import algorithms.problem.BaseIndividual;

import java.util.Collections;
import java.util.List;

/**
 * Size of the largest gap in the PF divided
 * by the size of the average gap
 */
public class HoleRelativeSize extends BaseMeasure {

  /**
   * Calculates hole relative size of the PF
   *
   * @param population population to measure
   * @return ratio of the largest to the average gap in the PF
   */
  @Override
  public <T extends BaseIndividual> double getMeasure(List<T> population) {
    Collections.sort(population);

    double averageDistance = 0.0d;
    double maxDistance = 0.0d;
    double distance;
    for (int i = 1; i < population.size(); ++i) {
      distance = getDistance(population.get(i-1), population.get(i));
      averageDistance += distance;
      if (distance > maxDistance) {
        maxDistance = distance;
      }
    }
    return maxDistance / averageDistance;
  }

}
