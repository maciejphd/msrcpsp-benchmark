package algorithms.quality_measure;

import algorithms.problem.BaseIndividual;

import java.util.List;

public class EDMany extends BaseMeasure {

  public EDMany(BaseIndividual perfectPoint) {
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

  @Override
  protected double getDistance(BaseIndividual i1, BaseIndividual i2) {
    double sum = 0.0d;
    int numObjectives = i1.getNormalObjectives().length;
    for (int i = 0; i < numObjectives; ++i) {
      sum += Math.pow(i1.getNormalObjectives()[i] - i2.getNormalObjectives()[i], 2);
    }
    return Math.sqrt(sum);
  }
}
