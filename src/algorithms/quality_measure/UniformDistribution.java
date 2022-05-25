package algorithms.quality_measure;

import algorithms.problem.BaseIndividual;

import java.util.List;

/**
 * Determines how closely a set of non-dominated individuals
 * resembles uniform distribution.
 */
public class UniformDistribution extends BaseMeasure {

  private double sigma;

  public UniformDistribution(double sigma) {
    this.sigma = sigma;
  }

  /**
   * Calculates uniformity of a distribution based on
   * average niche count. Returns values between 0.0 and 1.0.
   *
   * @param population population to measure
   * @return higher values mean more similarity to uniform distribution,
   *         lower values mean very clustered points
   */
  @Override
  public <T extends BaseIndividual> double getMeasure(List<T> population) {
    double nicheCountStd = 0.0d;
    double[] nicheCounts = new double[population.size()];
    double nicheCountsAverage = 0.0d;

    for (int i = 0; i < population.size(); ++i) {
      nicheCounts[i] = 0.0d;
      for (int j = 0; j < population.size(); ++j) {
        if (i != j) {
          nicheCounts[i] += getDistance(population.get(i), population.get(j)) < sigma ? 1.0d : 0.0d;
        }
      }
      nicheCountsAverage += nicheCounts[i];
    }
    nicheCountsAverage /= population.size();

    for (double nicheCount : nicheCounts) {
      nicheCountStd += Math.pow(nicheCount - nicheCountsAverage, 2);
    }
    nicheCountStd /= (population.size() - 1);
    nicheCountStd = Math.sqrt(nicheCountStd);

    return 1.0d / (1.0d + nicheCountStd);
  }

}
