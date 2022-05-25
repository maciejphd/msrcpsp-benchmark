package algorithms.quality_measure;

import algorithms.problem.BaseIndividual;

import java.util.List;

/**
 * Measures spread of the population based on average
 * distances between its members and distances to extreme
 * points. Extreme point for each criteria is a point that
 * achieves an optimal value for that criteria, ignoring all else.
 */
public class SpreadAssessment extends BaseMeasure {

  public SpreadAssessment(List<? extends BaseIndividual> extremePopulation) {
    this.referencePopulation = extremePopulation;
  }

  /**
   * Spread of the population based on average
   * distances between its members and distances to extreme
   * points. Extreme point for each criteria is a point that
   * minimizes only that criteria, ignoring all else.
   *
   * @param population potential pareto front
   * @return generated spread
   */
  @Override
  public <T extends BaseIndividual> double getMeasure(List<T> population) {
    double extremeDistances = 0.0;
    for (BaseIndividual extreme : referencePopulation) {
      extremeDistances += getMinDistance(extreme, population);
    }
    // store inner distances to avoid calculating them again later on
    double[] innerDistances = new double[population.size()];
    double sum = 0.0;
    for (int i = 0; i < population.size(); ++i) {
      innerDistances[i] = getMinDistance(population.get(i), population);
      sum += innerDistances[i];
    }
    double meanInnerDistances = sum / (double) population.size();
    double innerSum = 0.0;
    for (int i = 0; i < population.size(); ++i) {
      innerSum += Math.abs(innerDistances[i] - meanInnerDistances);
    }

    return (extremeDistances + innerSum) /
        (extremeDistances + ((double)population.size() * meanInnerDistances));
  }

}
