package algorithms.quality_measure;

import algorithms.problem.BaseIndividual;

import java.util.List;

/**
 * Ratio of hypervolumes (hyperareas) of potential Pareto
 * Front and true Pareto Front.
 */
public class HyperAreaRatio extends BaseMeasure {

  public HyperAreaRatio(List<? extends BaseIndividual> optimalParetoFront) {
    this.referencePopulation = optimalParetoFront;
  }

  /**
   * Calculates {@link HyperVolume} of potential Pareto front
   * and true Pareto front and calculates their ratio
   *
   * @param population population to measure
   * @return ratio of hyperareas of potential and
   *         true Pareto front
   */
  @Override
  public <T extends BaseIndividual> double getMeasure(List<T> population) {
    BaseIndividual nadirPoint = population.get(0).getEvaluator().getNadirPoint();
    HyperVolume hypervolume = new HyperVolume(nadirPoint);

    return hypervolume.getMeasure(population) / hypervolume.getMeasure(referencePopulation);
  }

}
