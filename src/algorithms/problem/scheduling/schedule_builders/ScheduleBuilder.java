package algorithms.problem.scheduling.schedule_builders;

import algorithms.problem.scheduling.Resource;
import algorithms.problem.scheduling.Schedule;
import algorithms.problem.scheduling.Task;

import java.util.Arrays;
import java.util.List;

/**
 * Completes a individual in a greedy manner. Useful for when
 * representation of a individual is described only
 * by order of tasks. When it is described by assignments, use
 * either {@link ForwardScheduleBuilder} or
 * {@link BackwardScheduleBuilder}
 *
 * Designed to work with <code>assign()</code> methods
 * from <code>Schedule</code> class.
 */
public class ScheduleBuilder {

  /**
   * Builds a individual by assigning the cheapest resource to
   * each task and then setting an earliest available time
   * for it.
   *
   * @param schedule individual to complete
   * @return complete individual
   */
  public Schedule build(Schedule schedule) {
    for (Task task : schedule.getTasks()) {
      build(schedule, task);
    }

    return schedule;
  }

  /**
   * Sets timestamp of a task and assigns it to
   * a resource
   *
   * @param schedule individual to complete
   * @param task task to process
   * @return individual with task assigned to resource
   *         and timestamp
   */
  private Schedule build(Schedule schedule, Task task) {
    List<Resource> capableResources = schedule.getCapableResources(task);
    Resource candidateResource = schedule.findCheapestResource(capableResources);
    task.setResourceId(candidateResource.getId());
    int earliestTime = schedule.getEarliestTime(task);
    task.setStart(Math.max(candidateResource.getFinish(), earliestTime));
    candidateResource.setFinish(task.getStart() + task.getDuration());
    return schedule;
  }

  /**
   * Creates task / resource assignments, while leaving
   * order of tasks in place (timestamps might be moved a little bit
   * if no resources are available). Assumes that all start times
   * are set and predecessor constraint is not violated.
   *
   * @param schedule individual to complete
   * @return complete individual
   */
  public Schedule buildAssignments(Schedule schedule) {
    Arrays.sort(schedule.getTasks());
    for (Task task : schedule.getTasks()) {
      buildAssignments(schedule, task);
    }
    return schedule;
  }

  /**
   * Finds the cheapest resource available for the task and the point
   * of its start and assigns it. If there are none, assigns the first
   * available one and moves the start time.
   *
   * @param schedule individual with the <code>task</code>
   * @param task task to modify
   * @return assigned resource
   */
  public Resource buildAssignments(Schedule schedule, Task task) {
    task.setStart(schedule.getEarliestTime(task));
    List<Resource> capableResources = schedule.getCapableResources(task, task.getStart());
    Resource candidateResource = schedule.findCheapestResource(capableResources);
    if (null != candidateResource) {
      task.setResourceId(candidateResource.getId());
      candidateResource.setFinish(task.getStart() + task.getDuration());
    } else {
      capableResources = schedule.getCapableResources(task);
      candidateResource = schedule.findFirstFreeResource(capableResources);
      task.setResourceId(candidateResource.getId());
      task.setStart(candidateResource.getFinish());
      candidateResource.setFinish(task.getStart() + task.getDuration());
    }
    return candidateResource;
  }

  /**
   * Throws illegal state exception. Please use either
   * {@link ForwardScheduleBuilder} or {@link BackwardScheduleBuilder}
   *
   * @param schedule individual to complete
   * @return complete individual
   */
  public Schedule buildTimestamps(Schedule schedule) {
    throw new IllegalStateException("Choose whether to use forward or" +
        "backward builder (ForwardScheduleBuilder / BackwardScheduleBuilder");
  }

}
