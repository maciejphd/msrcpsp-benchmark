package algorithms.evaluation;

import algorithms.problem.BaseIndividual;
import algorithms.problem.scheduling.Schedule;

/**
 * Single objective evaluator for duration.
 */
public class DurationEvaluator<GENE extends Number> extends BaseScheduleEvaluator<GENE> {

  public DurationEvaluator() {
    super();
  }

  public DurationEvaluator(BaseIndividual<GENE, Schedule> individual) {
    super(individual);
  }

  /**
   * Duration of the individual
   *
   * @return duration of the individual
   */
  @Override
  public double evaluate() {
    return (double) super.getDuration();
  }

  @Override
  public BaseEvaluator getCopy(BaseIndividual<GENE, Schedule> individual) {
    return new DurationEvaluator<>(individual);
  }

  @Override
  public EvaluatorType getType() {
    return EvaluatorType.DURATION_EVALUATOR;
  }

  @Override
  public int getNumObjectives() {
    return 1;
  }

}
