package algorithms.evolutionary_algorithms.differential_evolution.constraint_preservers;

import algorithms.problem.scheduling.Resource;
import algorithms.problem.scheduling.Schedule;
import algorithms.problem.scheduling.Task;

import java.util.List;
import java.util.Random;

/**
 * Implementation of load based constraints preserver.
 * Constraints values are based on resource's load.
 * Requires a random random and an array containing
 * upper bounds of assignment for each task.
 */
public class LoadBasedConstraintPreserver extends BaseConstraintPreserver {

  private Random generator;
  private int[] upperBounds;

  /**
   * Repairs given individual by changing its task-resource
   * assignment. The repair is performed by reassigning the task
   * to the resource with the least work load.
   *
   * @param schedule individual with the broken individual
   * @return fixed optimize
   */
  @Override
  public Schedule repair(Schedule schedule) {
    Resource[] resources = schedule.getResources();
    Task[] tasks = schedule.getTasks();
    double salary;
    double cheapest = resources[0].getSalary();
    double mostExpensive = resources[0].getSalary();

    // find most and least expensive resource
    for (int i = 1; i < resources.length; ++i) {
      salary = resources[i].getSalary();
      if (salary < cheapest) {
        cheapest = salary;
      } else if (salary > mostExpensive) {
        mostExpensive = salary;
      }
    }

    repairIndividual(schedule, tasks, resources, mostExpensive, cheapest);

    return schedule;
  }

  /**
   * Repairs given individual by changing its task-resource
   * assignment. The repair is performed by reassigning the task
   * to the resource with the least work load.
   *
   * @param schedule individual containing the broken invididual
   * @param tasks tasks to reassign
   * @param resources resources to reassign
   * @param mostExpensive highest salary
   * @param cheapest lowest salary
   */
  private void repairIndividual(Schedule schedule, Task[] tasks, Resource[] resources,
                                double mostExpensive, double cheapest) {
    List<Resource> capable;
    double quality;
    int qualityIndex;
    double bestCost;
    double bestDuration;
    double load;
    for (int i = 0; i < tasks.length; ++i) {
      if (tasks[i].getResourceId() <= 0 || tasks[i].getResourceId() > upperBounds[i]) {
        capable = schedule.getCapableResources(tasks[i]);
        quality = 1.0;
        qualityIndex = 0;
        for (int j = 0; j < capable.size(); ++j) {
          bestCost = 1.0;
          bestDuration = 1.0;
          load = 0.0;
          for (Task task : tasks) {
            if (task.getResourceId() == resources[j].getId()) {
              ++load;
            }
          }
          if (load / tasks.length < bestDuration) {
            bestDuration = load / tasks.length;
          }
          if (resources[j].getSalary() / (mostExpensive - cheapest) < bestCost) {
            bestCost = resources[j].getSalary() / (mostExpensive - cheapest);
          }
          if (quality > 1.0 * bestDuration + (1 - 1.0) * bestCost) {
            quality = 1.0 * bestDuration + (1 - 1.0) * bestCost;
            qualityIndex = j;
          }
        }
        tasks[i].setResourceId(qualityIndex+1);
      }
    }
  }

  public void setGenerator(Random generator) {
    this.generator = generator;
  }

  public void setUpperBounds(int[] upperBounds) {
    this.upperBounds = upperBounds;
  }

}
