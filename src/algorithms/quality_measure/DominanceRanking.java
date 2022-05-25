package algorithms.quality_measure;

import algorithms.evolutionary_algorithms.util.NondominatedSorter;
import algorithms.problem.BaseIndividual;

import java.util.List;

/**
 * Number of subsets in the set that dominate given point
 * (similar to the ranking used during NSGA-II). Returns an average value
 * unless a <code>reference_value</code> is not set, returns a value
 * for chosen individual otherwise.
 */
public class DominanceRanking extends BaseMeasure {

  /**
   * Calculates number of fronts in the population
   *
   * @param population population to measure
   * @return average number of subsets in that population
   *         or measure for chosen individual (<code>reference_value</code>)
   */
  @Override
  public <T extends BaseIndividual> double getMeasure(List<T> population) {
    NondominatedSorter<T> sorter = new NondominatedSorter<>();
    sorter.nondominatedSorting(population);
    if (referenceValue < 0 || referenceValue > population.size()) {
      return population.stream().mapToDouble(BaseIndividual::getRank).average().getAsDouble();
    } else {
      return population.get((int)referenceValue).getRank();
    }
  }
}
