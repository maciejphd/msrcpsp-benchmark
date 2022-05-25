package algorithms.quality_measure;

import algorithms.problem.BaseIndividual;

import java.util.Collections;
import java.util.List;

/**
 * Measures the HyperVolume of dominated space. It is a volume between
 * non-dominated solutions in current population and a point created
 * by taking the worst values from each criteria (Nadir Point).
 */
public class HyperVolume extends BaseMeasure {

  public HyperVolume(BaseIndividual nadirPoint) {
    this.referencePoint = nadirPoint;
  }

  /**
   * Volume between non-dominated solutions in current population
   * and a point created  by taking the worst values from
   * each criteria (Nadir Point).
   *
   * @param population population to measure
   * @return HyperVolume of given population
   */
  @Override
  public <T extends BaseIndividual> double getMeasure(List<T> population) {
    double hyperVolume = 0.0;
    Collections.sort(population);

    double previousCost = referencePoint.getNormalObjectives()[1];
    for (BaseIndividual individual : population) {
      hyperVolume += Math.abs(referencePoint.getNormalObjectives()[0] - individual.getNormalObjectives()[0]) *
          Math.abs(previousCost - individual.getNormalObjectives()[1]);
      previousCost = individual.getNormalObjectives()[1];
    }

    return hyperVolume;
  }

}
