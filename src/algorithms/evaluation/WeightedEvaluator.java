package algorithms.evaluation;

import algorithms.problem.BaseIndividual;
import algorithms.problem.scheduling.Schedule;

/**
 * Weighted evaluator for cost and duration.
 */
public class WeightedEvaluator<GENE extends Number> extends BaseScheduleEvaluator<GENE> {

  private double evalRate;

  public WeightedEvaluator(double evalRate) {
    super();
    this.evalRate = evalRate;
  }

  /**
   * Constructor. Sets evaluation rate to given value.
   *
   * @param individual individual to validate
   * @param evalRate evaluation rate linked to duration, 1 - evaluation rate
   *                 is used with cost
   */
  public WeightedEvaluator(BaseIndividual<GENE, Schedule> individual, double evalRate) {
    super(individual);
    this.evalRate = evalRate;
  }

  @Override
  public double evaluate() {
    if (evalRate > 1 || evalRate < 0) {
      throw new IllegalArgumentException(
          "Cannot provide the evalRate smaller than 0 or bigger than 1!");
    }
    double durationPart = getDuration() / maxDuration;
    double costPart = getCost() / maxCost;
    return durationPart * evalRate + costPart * (1 - evalRate);
  }

  @Override
  public BaseEvaluator getCopy(BaseIndividual<GENE, Schedule> individual) {
    return new WeightedEvaluator<GENE>(individual, evalRate);
  }

  @Override
  public EvaluatorType getType() {
    return EvaluatorType.WEIGHTED_EVALUATOR;
  }

  public double getEvalRate() {
    return evalRate;
  }

  public void setEvalRate(double evalRate) {
    this.evalRate = evalRate;
  }

  @Override
  public int getNumObjectives() {
    return 2;
  }

}
