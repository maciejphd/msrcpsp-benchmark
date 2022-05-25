package algorithms.quality_measure;

import algorithms.problem.BaseIndividual;

import java.util.List;

/**
 * Measures the average distance between the solutions
 * in potential pareto front from the closest solution
 * in the optimal pareto front
 */
public class GenerationalDistance extends BaseMeasure {

  public GenerationalDistance(List<? extends BaseIndividual> optimalParetoFront) {
    this.referencePopulation = optimalParetoFront;
  }

  /**
   * Average distance between the solutions in the potential
   * pareto front and closest solutions from optimal pareto front.
   *
   * @return generational distance
   */
  @Override
  public <T extends BaseIndividual> double getMeasure(List<T> population) {
    double sum = 0.0;
    for (BaseIndividual individual : population) {
      sum += Math.pow(getMinDistance(individual, referencePopulation), 2);
    }
    return Math.sqrt(sum) / (double) population.size();
  }

}
