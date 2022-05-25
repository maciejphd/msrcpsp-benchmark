package algorithms.evolutionary_algorithms.differential_evolution.constraint_preservers;

import algorithms.problem.scheduling.Schedule;
import algorithms.problem.scheduling.Task;

import java.util.Random;

/**
 * Implementation of random constraints preserver.
 * Randomizes each gene that violates constraints.
 * Requires a random random and an array containing upper bounds of assignment for each task.
 */
public class RandomConstraintPreserver extends BaseConstraintPreserver {

  private Random generator;
  private int[] upperBounds;

  /**
   * Repairs an individual by randomizing its broken
   * task-resource assignment.
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
        resourceId = generator.nextInt(upperBounds[i]);
        tasks[i].setResourceId(resourceId+1);
      }
    }

    return schedule;
  }

  public void setGenerator(Random generator) {
    this.generator = generator;
  }

  public void setUpperBounds(int[] upperBounds) {
    this.upperBounds = upperBounds;
  }

}
