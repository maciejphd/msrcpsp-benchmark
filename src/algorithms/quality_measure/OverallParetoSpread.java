package algorithms.quality_measure;

import algorithms.evaluation.BaseEvaluator;
import algorithms.problem.BaseIndividual;

import java.util.Collections;
import java.util.List;

/**
 * Volume ratio of two hyper-rectangles. One defined by extreme points
 * in the set, with respect to each criteria. Second
 * defined by best / worst possible points.
 */
public class OverallParetoSpread extends BaseMeasure {

  /**
   * Volume ratio of extreme and best/worst hyper-rectangles.
   *
   * @param population population to measure
   * @return ratio of extreme and best/worst hyper-rectangle
   */
  @Override
  public <T extends BaseIndividual> double getMeasure(List<T> population) {
    Collections.sort(population);
    T leftExtreme = population.get(0);
    T rightExtreme = population.get(population.size()-1);
    double extremeVolume = Math.abs(leftExtreme.getNormalObjectives()[1] - rightExtreme.getNormalObjectives()[1]) *
        Math.abs(leftExtreme.getNormalObjectives()[0] - rightExtreme.getNormalObjectives()[0]);

    BaseEvaluator evaluator = leftExtreme.getEvaluator();
    BaseIndividual best = evaluator.getPerfectPoint();
    BaseIndividual worst = evaluator.getNadirPoint();
    double bestWorstVolume = Math.abs(best.getNormalObjectives()[1] - worst.getNormalObjectives()[1]) *
        Math.abs(best.getNormalObjectives()[0] - worst.getNormalObjectives()[0]);

    return extremeVolume / bestWorstVolume;
  }

}
