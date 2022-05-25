package algorithms.evaluation;

import algorithms.problem.BaseIndividual;
import algorithms.problem.scheduling.Schedule;

/**
 * Single objective evaluator for cost.
 */
public class CostEvaluator<GENE extends Number> extends BaseScheduleEvaluator<GENE> {

  public CostEvaluator() {
    super();
  }

  public CostEvaluator(BaseIndividual<GENE, Schedule> individual) {
    super(individual);
  }

  /**
   * Cost of the individual.
   *
   * @return cost of the individual
   */
  @Override
  public double evaluate() {
    return super.getCost();
  }

  @Override
  public CostEvaluator getCopy(BaseIndividual<GENE, Schedule> individual) {
    return new CostEvaluator<>(individual);
  }

  @Override
  public EvaluatorType getType() {
    return EvaluatorType.COST_EVALUATOR;
  }

  @Override
  public int getNumObjectives() {
    return 1;
  }

}
