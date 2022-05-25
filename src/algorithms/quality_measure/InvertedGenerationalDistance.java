package algorithms.quality_measure;

import algorithms.problem.BaseIndividual;

import java.util.List;

/**
 * Measures the average distance between the solutions
 * in optimal pareto front from the closest solution
 * in potential pareto front
 */
public class InvertedGenerationalDistance extends BaseMeasure {

  public InvertedGenerationalDistance(List<? extends BaseIndividual> optimalParetoFront) {
    this.referencePopulation = optimalParetoFront;
  }

  /**
   * Average distance between the solutions in the
   * optimal pareto front and closest solutions from
   * potential pareto front.
   *
   * @return inverted generational distance
   */
  @Override
  public <T extends BaseIndividual> double getMeasure(List<T> population) {
    double sum = 0.0;
    for (BaseIndividual individual : referencePopulation) {
      sum += Math.pow(getMinDistance(individual, population), 2);
    }
    return Math.sqrt(sum) / (double) referencePopulation.size();
  }

}
