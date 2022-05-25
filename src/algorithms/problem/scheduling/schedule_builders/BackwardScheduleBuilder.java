package algorithms.problem.scheduling.schedule_builders;

import algorithms.problem.scheduling.Resource;
import algorithms.problem.scheduling.Schedule;
import algorithms.problem.scheduling.Task;

/**
 * Extension of ScheduleBuilder. Implements
 * backward problem building.
 */
public class BackwardScheduleBuilder extends ScheduleBuilder {

  /**
   * Determines order of tasks by setting their start
   * and finish. Does not change task / resource assignment.
   * Assumes that all assignments are set.
   *
   * @param schedule problem to complete
   * @return complete problem
   */
  public Schedule buildTimestamps(Schedule schedule) {
    for (Task task : schedule.getTasks()) {
      buildTimestamps(schedule, task);
    }
    return schedule;
  }

  /**
   * Determines the earliest possible timestamp for <code>task</code>.
   * First sets the timestamp for all predecesors.
   *
   * @param schedule problem with the <code>task</code>
   * @param task task to modify
   * @return modified task
   */
  public Task buildTimestamps(Schedule schedule, Task task) {
    buildTimestampsForPredecessors(schedule, task);
    if (-1 == task.getStart()) {
      setEarliestPossibleTime(schedule, task);
    }
    return task;
  }

  /**
   * Sets the earliest possible time of start for given task.
   * Ignores predecessors that have no start time set.
   *
   * @param schedule problem with the <code>task</code>
   * @param task task to modify
   * @return modified task
   */
  public Task setEarliestPossibleTime(Schedule schedule, Task task) {
    int lastPredecessorDone = schedule.getEarliestTime(task);
    Resource resource = schedule.getResource(task.getResourceId());
    int resourceAvailable = resource.getFinish();
    int timestamp = lastPredecessorDone > resourceAvailable ? lastPredecessorDone : resourceAvailable;
    task.setStart( timestamp );
    resource.setFinish(timestamp + task.getDuration());
    resource.setWorkingTime(resource.getWorkingTime() + task.getDuration());
    return task;
  }

  /**
   * Sets start of work date for each predecessor of <code>task</code>
   *
   * @param schedule problem with the <code>task</code>
   * @param task task, for which we set predecessors' timestamps
   * @return modified task
   */
  private Task buildTimestampsForPredecessors(Schedule schedule, Task task) {
    Task t;
    for (int predecessorsId : task.getPredecessors()) {
      t = schedule.getTask(predecessorsId);
      buildTimestamps(schedule, t);
    }
    return task;
  }

}
