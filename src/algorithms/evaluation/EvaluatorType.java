package algorithms.evaluation;

/**
 * Enum containing all types of evaluators. Used mostly for
 * multi objective optimization, when multiple evaluators
 * are connected with a optimize. Allows to differentiate
 * which results came from which evaluator.
 */
public enum EvaluatorType {

  COST_EVALUATOR,
  DURATION_EVALUATOR,
  WEIGHTED_EVALUATOR,
  THREE_SCHEDULE_EVALUATOR,
  FOUR_SCHEDULE_EVALUATOR,
  FIVE_SCHEDULE_EVALUATOR,
  BASE_TSP_EVALUATOR,
  MAOP1_EVALUATOR,
  BASE_KNAPSACK_EVALUATOR,
  PENALTY_KNAPSACK_EVALUATOR,
  SINGLE_OBJECTIVE_TTP_EVALUATOR,
  MULTI_OBJECTIVE_TTP_EVALUATOR,
  COMPETITION_EVALUATOR,
  EXPERIMENTAL_SCHEDULE,

}
