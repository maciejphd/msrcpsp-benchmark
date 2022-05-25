package algorithms.problem.scheduling.schedule_builders;


import algorithms.problem.scheduling.Resource;
import algorithms.problem.scheduling.Schedule;
import algorithms.problem.scheduling.Task;

/**
 * Extension of ScheduleBuilder. Implements
 * forward problem building.
 */
public class ForwardScheduleBuilder extends ScheduleBuilder {

  private boolean[] hasSuccessors;

  public ForwardScheduleBuilder(boolean[] hasSuccesors) {
   setHasSuccessors(hasSuccesors);
  }

  /**
   * Determines order of tasks by setting their start
   * and finish. Does not change task / resource assignment.
   * Assumes that all assignments are set. Uses knowledge about
   * successors of each task to first place the tasks with successors
   * and then rest of the tasks.
   *
   * @param schedule problem to build
   * @return built problem
   */
  public Schedule buildTimestamps(Schedule schedule) {
    Resource[] resources = schedule.getResources();
    for (Resource r : resources) {
      r.setFinish(0);
    }
    Task[] tasks = schedule.getTasks();
    Resource res;
    int start;
    // Assign tasks with relation requirements
    for (int i = 0; i < tasks.length; ++i)  {
      if (hasSuccessors[i]) {
        res = resources[tasks[i].getResourceId()-1];
        start = Math.max(schedule.getEarliestTime(tasks[i]),
            res.getFinish());
        tasks[i].setStart(start);
        res.setFinish(start + tasks[i].getDuration());
        res.setWorkingTime(res.getWorkingTime() + tasks[i].getDuration());
      }
    }
    // Assign rest of the tasks
    for (int i = 0; i < tasks.length; ++i)  {
      if (!hasSuccessors[i]) {
        res = resources[tasks[i].getResourceId()-1];
        start = Math.max(schedule.getEarliestTime(tasks[i]),
            res.getFinish());
        tasks[i].setStart(start);
        res.setFinish(start + tasks[i].getDuration());
        res.setWorkingTime(res.getWorkingTime() + tasks[i].getDuration());
      }
    }
    return schedule;
  }

  public boolean[] getHasSuccessors() {
    return hasSuccessors;
  }

  public void setHasSuccessors(boolean[] hasSuccessors) {
    this.hasSuccessors = hasSuccessors;
  }

}
