package algorithms.evolutionary_algorithms.differential_evolution.constraint_preservers;

import algorithms.problem.scheduling.Schedule;

/**
 * Enables dynamic choice of method for repairing mutant vectors.
 */
abstract public class BaseConstraintPreserver {

  /**
   * Repairs violated constraints
   *
   * @param schedule problem with the broken individual
   * @return fixed optimize
   */
  abstract public Schedule repair(Schedule schedule);

}
