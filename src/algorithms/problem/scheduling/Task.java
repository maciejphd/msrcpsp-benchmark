package algorithms.problem.scheduling;


import java.util.Arrays;

/**
 * Defines a task in a project. Task is an atomic element of any project -
 * project consists of tasks that have to be performed to achieve given goal.
 * Task is described by skill required, duration (in hours) and predecessors
 * (precedence relations).
 *
 * For easier implementation, task also stores
 * information about resource assigned to it, its start time in a project. If id
 * of assigned resource and start time is -1, it means that task is not assigned
 * to any resource and not placed anywhere in the timeline.
 */
public class Task implements Comparable {

  private int id;
  private Skill[] requiredSkills;
  private int duration;
  private int start;
  private int[] predecessors;
  private int resourceId;

  public Task(int id, Skill[] requiredSkills, int duration, int start,
              int[] predecessors, int resourceId) {
    this.id = id;
    this.requiredSkills = requiredSkills;
    this.duration = duration;
    this.start = start;
    this.predecessors = predecessors;
    this.resourceId = resourceId;
  }

  public Task(int id, Skill[] skills, int duration, int[] predecessors) {
    this(id, skills, duration, -1, predecessors, -1);
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Skill[] getRequiredSkills() {
    return requiredSkills;
  }

  public void setRequiredSkills(Skill[] requiredSkills) {
    this.requiredSkills = requiredSkills;
  }

  public int getDuration() {
    return duration;
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }

  public int getStart() {
    return start;
  }

  public void setStart(int start) {
    this.start = start;
  }

  public int[] getPredecessors() {
    return predecessors;
  }

  public void setPredecessors(int[] predecessors) {
    this.predecessors = predecessors;
  }

  public int getResourceId() {
    return resourceId;
  }

  public void setResourceId(int resourceId) {
    this.resourceId = resourceId;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(id);
    sb.append(", duration: ");
    sb.append(duration);
    sb.append(", start: ");
    sb.append(start);
    sb.append(", required skills: ");
    for (Skill skill : requiredSkills) {
      sb.append(skill);
      sb.append(" ");
    }
    sb.append(", predecessors: ");
    for (int i : predecessors) {
      sb.append(i);
      sb.append(" ");
    }
    return sb.toString();
  }

  /**
   * Compare two tasks.
   *
   * @param t task to compare to
   * @return true if this task is equal to task t
   */
  @Override
  public boolean equals(Object t) {
    if (!(t instanceof Task)) {
      return false;
    }
    Task task = (Task) t;
    return duration == task.duration &&
        id == task.id &&
        Arrays.equals(predecessors, task.predecessors) &&
        Arrays.equals(requiredSkills, task.requiredSkills);
  }

  /**
   * Compares start times.
   *
   * @param o object to compare
   * @return -1 if this task start earlier, 1 if
   * <code>o</code> start earlier, 0 if they start
   * at the same time
   */
  @Override
  public int compareTo(Object o) {
    if (!(o instanceof Task)) {
      throw new IllegalArgumentException("Parameter is not a Task");
    }
    return Integer.compare(start, ((Task) o).start);
  }

}
