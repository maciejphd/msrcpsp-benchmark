package algorithms.evolutionary_algorithms.converters;

import algorithms.problem.scheduling.Task;

import java.util.List;

/**
 * Implementation of truncating converter.
 * Chooses resource id by casting continuous value to integer.
 */
public class TruncatingConverter extends BaseConverter {

  /**
   * Converts real number genes to integer
   * task-resource assignments by casting
   * real numbers to integers.
   *
   * @param tasks tasks with assignments
   * @param genes real value genes
   */
  @Override
  public void convertToInteger(Task[] tasks, List<? extends Number> genes) {
    for (int i = 0; i < genes.size(); ++i) {
      tasks[i].setResourceId(genes.get(i).intValue()+1);
    }
  }

  /**
   * Converts integer task-resource assignments to
   * real number genes by casting integers
   * to real numbers.
   *
   * @param tasks tasks with assignemnts
   * @param genes real value genes
   */
  @Override
  public void convertToReal(Task[] tasks, List<Double> genes) {
    for (int i = 0; i < genes.size(); ++i) {
      genes.set(i, (double)(tasks[i].getResourceId()-1));
    }
  }

}
