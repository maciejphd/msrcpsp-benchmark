package algorithms.factories;


import algorithms.evaluation.*;

public class EvaluatorFactory {

  /**
   * Creates evaluator based on provided type.
   * Default: Throws error if no correct type is provided,
   * default value would make no sense, as there are different
   * evaluators for different problems
   *
   * @param type type to use
   * @return chosen crossover method
   */
  public BaseEvaluator createEvaluator(EvaluatorType type, double evalRate) {
    switch (type) {
      case COST_EVALUATOR:
        return new CostEvaluator();
      case DURATION_EVALUATOR:
        return new DurationEvaluator();
      case WEIGHTED_EVALUATOR:
        return new WeightedEvaluator(evalRate);
      case THREE_SCHEDULE_EVALUATOR:
        return new ThreeScheduleEvaluator();
      case FOUR_SCHEDULE_EVALUATOR:
        return new FourScheduleEvaluator();
      case FIVE_SCHEDULE_EVALUATOR:
        return new FiveScheduleEvaluator();
      case EXPERIMENTAL_SCHEDULE:
        return new ExperimentalScheduleEvaluator();
      default:
        throw new IllegalArgumentException("Please provide the correct enum value from EvaluatorType enum");
    }
  }

}
