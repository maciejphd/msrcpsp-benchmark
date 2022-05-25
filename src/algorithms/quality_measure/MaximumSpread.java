package algorithms.quality_measure;

import algorithms.problem.BaseIndividual;

import java.util.List;

/**
 * Distance between 2 farthest points - maximum extent in
 * each dimension. In case of 2 criteria it would be the
 * distance between the two outer solutions.
 */
public class MaximumSpread extends BaseMeasure {

  /**
   * Distance between 2 farthest points - maximum extent in each dimension
   *
   * @param population population to measure
   * @return distance between 2 farthest points
   */
  @Override
  public <T extends BaseIndividual> double getMeasure(List<T> population) {
    T individual = population.get(0);
    T trialIndividual;
    double minCost = individual.getNormalObjectives()[1];
    double maxCost = individual.getNormalObjectives()[1];
    double minDuration = individual.getNormalObjectives()[0];
    double maxDuration = individual.getNormalObjectives()[0];

    for (int i = 1; i < population.size(); ++i) {
      trialIndividual = population.get(i);
      if (trialIndividual.getNormalObjectives()[1] < minCost) {
        minCost = trialIndividual.getNormalObjectives()[1];
      }
      if (trialIndividual.getNormalObjectives()[1] > maxCost) {
        maxCost = trialIndividual.getNormalObjectives()[1];
      }
      if (trialIndividual.getNormalObjectives()[0] < minDuration) {
        minDuration = trialIndividual.getNormalObjectives()[0];
      }
      if (trialIndividual.getNormalObjectives()[0] > maxDuration) {
        maxDuration = trialIndividual.getNormalObjectives()[0];
      }
    }
    return Math.max(maxDuration - minDuration, maxCost - minCost);
  }

}
