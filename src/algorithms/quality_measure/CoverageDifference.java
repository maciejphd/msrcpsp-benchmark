package algorithms.quality_measure;

import algorithms.problem.BaseIndividual;

import java.util.ArrayList;
import java.util.List;

/**
 * Size of the space dominated by one set but not dominated
 by another set (regarding the space objective).
 */
public class CoverageDifference extends BaseMeasure {

  public CoverageDifference(List<? extends BaseIndividual> referencePopulation) {
    this.referencePopulation = referencePopulation;
  }

  /**
   * {@link HyperVolume} of two populations minus
   * {@link HyperVolume} of measured population.
   *
   * @param population population to measure
   * @return Coverage difference
   */
  @Override
  public <T extends BaseIndividual> double getMeasure(List<T> population) {
    BaseIndividual nadirPoint = population.get(0).getEvaluator().getNadirPoint();
    HyperVolume hyperVolume = new HyperVolume(nadirPoint);

    List<BaseIndividual> bothPopulations = new ArrayList<>();
    bothPopulations.addAll(population);
    bothPopulations.addAll(referencePopulation);

    return hyperVolume.getMeasure(bothPopulations) / hyperVolume.getMeasure(population);
  }

}
