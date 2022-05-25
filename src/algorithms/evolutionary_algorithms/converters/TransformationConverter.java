package algorithms.evolutionary_algorithms.converters;

import algorithms.problem.scheduling.Task;

import java.util.List;

/**
 * Implementation of transformation based converter
 */
public class TransformationConverter extends BaseConverter {

  private static final int scaleFactor = 100;
  private static final int p = 2;
  private int[] upperBounds;

  public TransformationConverter(int[] upperBounds) {
    this.upperBounds = upperBounds;
  }

  /**
   * Uses predefined transformation to convert
   * real number genes to integer
   * task-resource assignments.
   *
   * @param tasks tasks with assignments
   * @param genes real value genes
   */
  @Override
  public void convertToInteger(Task[] tasks, List<? extends Number> genes) {
    double x;
    int alpha;
    double beta;
    int resourceId;
    for (int i = 0; i < genes.size(); ++i) {
      x = ((genes.get(i).doubleValue() + 1) * (Math.pow(10, p+1) - 1)) / (scaleFactor * upperBounds[i]);
      alpha = (int)(x + 0.5);
      beta = alpha - x;
      resourceId = alpha - (beta > 0.5 ? 1 : 0);
      tasks[i].setResourceId(resourceId);
    }
  }

  /** Uses predefined tansformation to converts
   * integer task-resource assignments to
   * real number genes.
   *
   * @param tasks tasks with assignemnts
   * @param genes real value genes
   */
  @Override
  public void convertToReal(Task[] tasks, List<Double> genes) {
    for (int i = 0; i < genes.size(); ++i) {
      genes.set(i, -1 + (tasks[i].getResourceId() * scaleFactor * upperBounds[i]) / (Math.pow(10, p+1) - 1));
    }
  }

}
