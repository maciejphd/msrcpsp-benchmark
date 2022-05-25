package algorithms.evolutionary_algorithms.converters;

import algorithms.problem.scheduling.Task;

import java.util.List;

/**
 * Enables dynamic choosing of genotype / phenotype converter.
 */
abstract public class BaseConverter {

  /**
   * Converts real number genes to integer
   * task-resource assignments.
   *
   * @param tasks tasks with assignments
   * @param genes real value genes
   */
  abstract public void convertToInteger(Task[] tasks, List<? extends Number> genes);

  /**
   * Converts integer task-resource assignments to
   * real number genes.
   *
   * @param tasks tasks with assignemnts
   * @param genes real value genes
   */
  abstract public void convertToReal(Task[] tasks, List<Double> genes);

}
