package algorithms.quality_measure;

import algorithms.problem.BaseIndividual;

import java.util.List;

/**
 * Measures the average distance between the solutions
 * in potential pareto front from the perfect point.
 * Perfect point is created by taking the best possible
 * values for each criteria. Similar to epsilon measure.
 */
public class ConvergenceMeasure extends BaseMeasure {

  public ConvergenceMeasure(BaseIndividual perfectPoint) {
    this.referencePoint = perfectPoint;
  }

  /**
   * Average distance between the solutions in the optimize
   * and a perfect point created by taking the best possible
   * values for each criteria.
   *
   * @param population potential pareto front
   * @return euclidean distance
   */
  @Override
  public <T extends BaseIndividual> double getMeasure(List<T> population) {
    double distance = 0.0;
    for (BaseIndividual individual : population) {
      distance += getDistance(individual, referencePoint);
    }
    return distance / (double) population.size();
  }
}
