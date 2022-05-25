package algorithms.evolutionary_algorithms.differential_evolution.constraint_preservers;

import algorithms.problem.scheduling.Schedule;
import algorithms.problem.scheduling.Task;

/**
 * Implementation of modulo constraints preserver.
 * Takes absolute value of every vector elements modulo max possible value.
 * Requires an array containing upper bounds of assignment for each task.
 */
public class ModuloConstraintPreserver extends BaseConstraintPreserver {

  private int[] upperBounds;

  /**
   * Repairs an individual by using modulo operator modify
   * its task-resource assignment.
   *
   * @param schedule problem with the broken individual
   * @return fixed optimize
   */
  @Override
  public Schedule repair(Schedule schedule) {
    Task[] tasks = schedule.getTasks();
    int resourceId;
    for (int i = 0; i < tasks.length; ++i) {
      if (tasks[i].getResourceId() <= 0 || tasks[i].getResourceId() > upperBounds[i]) {
        resourceId = Math.abs(tasks[i].getResourceId() % upperBounds[i]);
        tasks[i].setResourceId(resourceId+1);
      }
    }
    return schedule;
  }

  public void setUpperBounds(int[] upperBounds) {
    this.upperBounds = upperBounds;
  }

}
