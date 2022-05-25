package algorithms.quality_measure;

import algorithms.problem.BaseIndividual;

import java.util.List;

/**
 * Max value of Generational Distance and Inverted Generational Distance
 */
public class AveragedHausdorffDistance extends BaseMeasure {

  public AveragedHausdorffDistance(List<? extends BaseIndividual> optimalParetoFront) {
    this.referencePopulation = optimalParetoFront;
  }

  /**
   * Max value of {@link GenerationalDistance} and {@link InvertedGenerationalDistance}
   *
   * @param population population to measure
   * @return Averaged Hausdorff Distance
   */
  @Override
  public <T extends BaseIndividual> double getMeasure(List<T> population) {
    GenerationalDistance generationalDistance = new GenerationalDistance(referencePopulation);
    InvertedGenerationalDistance invertedGenerationalDistance = new InvertedGenerationalDistance(referencePopulation);

    return Math.max(generationalDistance.getMeasure(population), invertedGenerationalDistance.getMeasure(population));
  }

}
