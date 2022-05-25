package algorithms.problem.scheduling;

import algorithms.evolutionary_algorithms.ParameterSet;
import algorithms.problem.BaseProblemRepresentation;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Describes the project definition and also the solution itself - project
 * individual. Consists of tasks, resources and an evaluation. Able to build a
 * individual from provided representation and manage individual.
 */
public class Schedule extends BaseProblemRepresentation {

  private Task[] tasks;
  private Resource[] resources;

  public Schedule() { }

  /**
   * Initiates a individual with given tasks and resources.
   *
   * @param tasks tasks to store
   * @param resources resources to store
   */
  public Schedule(Task[] tasks, Resource[] resources) {
    this.tasks = tasks;
    this.resources = resources;
    clear(true);
  }

  @Override
  public Schedule buildSolution(List<? extends Number> genes, ParameterSet<? extends Number, ? extends BaseProblemRepresentation> parameters) {
    if (parameters.converter != null) {
      parameters.converter.convertToInteger(this.getTasks(), genes);
    }
    if (parameters.constraintPreserver != null) {
      parameters.constraintPreserver.repair(this);
    }
    buildTaskResourceAssignments();
    parameters.scheduleBuilder.buildTimestamps(this);
    this.setHashCode();
    return this;
  }

  /**
   * Returns a deep copy of this individual.
   */
  @Override
  public Schedule cloneDeep() {
    Schedule schedule = new Schedule();
    schedule.setTasks(new Task[this.getTasks().length]);
    for (int i = 0; i < tasks.length; i++) {
      Task[] tasks = schedule.getTasks();
      Task org = this.getTasks()[i];
      tasks[i] = new Task(org.getId(), org.getRequiredSkills(),
          org.getDuration(), org.getPredecessors());
    }
    schedule.setResources(new Resource[this.getResources().length]);
    for (int i = 0; i < resources.length; i++) {
      Resource[] resources = schedule.getResources();
      Resource org = this.getResources()[i];
      resources[i] = new Resource(org.getId(), org.getSalary(),
          org.getSkills());
    }
    for (Task t : schedule.getTasks()) {
      t.setStart(this.getTask(t.getId()).getStart());
      t.setResourceId(this.getTask(t.getId()).getResourceId());
    }
    for (Resource r : schedule.getResources()) {
      r.setFinish(this.getResource(r.getId()).getFinish());
    }
    return schedule;
  }

  /**
   * Number of genes required for this individual.
   *
   * @return number of tasks
   */
  @Override
  public int getNumGenes() {
    return tasks.length;
  }

  /**
   * Gets upper bounds of each task-resource assignment, which
   * is a number of capable resources for each respective task.
   */
  public int[] getUpperBounds() {
    int numTasks = tasks.length;
    int[] upperBounds = new int[numTasks];
    for (int i = 0; i < numTasks; ++i) {
      upperBounds[i] = this.getCapableResources(this.getTasks()[i]).size();
    }
    return upperBounds;
  }

  /**
   * Assigns resource <code>r</code> to the task <code>t</code>
   * at the time <code>timestamp</code>. Does not check if the assignment
   * violates the constraints.
   *
   * @param t task to assign
   * @param r resource to assign
   * @param timestamp start time of the task <code>t</code>
   */
  public void assign(Task t, Resource r, int timestamp) {
    t.setResourceId(r.getId());
    t.setStart(timestamp);
    r.setFinish(timestamp + t.getDuration());
  }

  /**
   * Assigns resource with id: <code>resourceId</code> to the task with id:
   * <code>taskId</code> at the time <code>timestamp</code>.
   * Does not check if the assignment violates the constraints.
   *
   * @param taskId task to assign
   * @param resourceId resource to assign
   * @param timestamp start time of the task <code>t</code>
   */
  public void assign(int taskId, int resourceId, int timestamp) {
    assign(getTask(taskId), getResource(resourceId), timestamp);
  }

  /**
   * Assigns time of start of work to the given task. If a task is
   * already linked with a resources, a finish time is set on
   * that resource.
   *
   * @param t task, on which start time is set
   * @param timestamp start of work
   */
  public void assign(Task t, int timestamp) {
    t.setStart(timestamp);
    int resourceId = t.getResourceId();
    if (-1 != resourceId) {
      getResource(resourceId).setFinish(timestamp + t.getDuration());
    }
  }

  /**
   * Assigns resource <code>r</code> to the task <code>t</code>. Does not
   * assign time. Does not check if the assignment violates the constraints.
   *
   * @param t task to assign
   * @param r resource to assign
   */
  public void assign(Task t, Resource r) {
    t.setResourceId(r.getId());
  }

  /**
   * For each task, sets its resource id to the corresponding capable resource.
   */
  public void buildTaskResourceAssignments() {
    for (Task task : this.getTasks()) {
      task.setResourceId(this.getCapableResources(task).get(task.getResourceId()-1).getId());
    }
  }

  /**
   * Creates successor table. True on the nth place
   * means that nth task has successors.
   */
  public boolean[] getSuccesors() {
    Task[] tasks = this.getTasks();
    boolean[] hasSuccesors = new boolean[tasks.length];
    Task predecessorTask;
    int tempId;
    for (Task task : tasks) {
      for (int predecessor : task.getPredecessors()) {
        predecessorTask = this.getTask(predecessor);
        tempId = getTaskIndex(predecessorTask);
        hasSuccesors[tempId] = true;
      }
    }
    return hasSuccesors;
  }

  /**
   * Finds index in the array of a given task.
   *
   * @param task task to look for
   * @return index of the <code>task</code>
   */
  private int getTaskIndex(Task task) {
    int index = 0;
    for (Task t : this.getTasks()) {
      if (t.getId() == task.getId()) {
        return index;
      }
      ++index;
    }
    return -1;
  }

  /**
   * Creates individual with given number of tasks and resources.
   *
   * @param numOfTasks number of tasks
   * @param numOfResources number of resources
   */
  public Schedule(int numOfTasks, int numOfResources) {
    tasks = new Task[numOfTasks];
    resources = new Resource[numOfResources];
  }

  /**
   * Clears timestamps from tasks and resources.
   * and optionaly task - resource assignments
   *
   * @param withAssignments determines whether to clear assignments
   */
  public void clear(boolean withAssignments) {
    if (withAssignments) {
      for (Task t : tasks) {
        t.setStart(-1);
        t.setResourceId(-1);
      }
    } else {
      for (Task t : tasks) {
        t.setStart(-1);
      }
    }
    for (Resource r : resources) {
      r.setFinish(-1);
    }
  }

  /**
   * Gets a task with given id.
   *
   * @param taskId task id
   * @return task with given id or null if such task
   * does not exist.
   */
  public Task getTask(int taskId) {
    for (Task t : tasks) {
      if (t.getId() == taskId) {
        return t;
      }
    }
    return null;
  }

  /**
   * Gets a resource with given id.
   *
   * @param resourceId resource id
   * @return resource with given id or null if such resource
   * does not exist.
   */
  public Resource getResource(int resourceId) {
    for (Resource r : resources) {
      if (r.getId() == resourceId) {
        return r;
      }
    }
    return null;
  }

  /**
   * Finds all resources capable of doing given task.
   *
   * @param t given task, for which we find capable resources
   * @return list of resources capable of doing task <code>t</code>
   */
  public List<Resource> getCapableResources(Task t) {
    List<Resource> result = new LinkedList<>();
    for (Resource r : resources) {
      if (canDoTask(t, r)) {
        result.add(r);
      }
    }
    return result;
  }

  /**
   * Finds all resources capable of doing given task and
   * available at the given timestamp
   *
   * @param t t given task, for which we find capable resources
   * @param timestamp time, at which <code>t</code> has
   *                  to be available
   * @return list of resources capable of doing task <code>t</code>
   * at the time <code>timestamp</code>
   */
  public List<Resource> getCapableResources(Task t, int timestamp) {
    List<Resource> result = new LinkedList<>();
    for (Resource r : resources) {
      if (canDoTask(t, r) && timestamp >= r.getFinish()) {
        result.add(r);
      }
    }
    return result;
  }

  /**
   * Finds a resource from the list <code>tempR</code> with the earliest
   * finish time of its work.
   *
   * @param tempR list of resources
   * @return Resource with the earliest finish time of work
   */
  public Resource findFirstFreeResource(List<Resource> tempR) {
    if (tempR == null) {
      return null;
    }
    Resource result = tempR.get(0);
    int firstFree = tempR.get(0).getFinish();
    for (Resource r : tempR) {
      if (r.getFinish() < firstFree) {
        result = r;
        firstFree = r.getFinish();
      }
    }
    return result;
  }

  /**
   * Finds the cheapest resource.
   *
   * @return the cheapest resource available
   */
  public Resource findCheapestResource() {
    double salary = resources[0].getSalary();
    Resource result = resources[0];
    for (Resource r : resources) {
      if (r.getSalary() < salary) {
        salary = r.getSalary();
        result = r;
      }
    }
    return result;
  }

  /**
   * Finds the cheapest resource in given list.
   *
   * @param tempR list of resources
   * @return cheapest resource from <code>tempR</code>
   */
  public Resource findCheapestResource(List<Resource> tempR) {
    Resource result;
    if (tempR.isEmpty()) {
      return null;
    }
    double salary = tempR.get(0).getSalary();
    result = tempR.get(0);
    for (Resource r : tempR) {
      if (r.getSalary() < salary) {
        salary = r.getSalary();
        result = r;
      }
    }
    return result;
  }

  /**
   * Calculates the earliest possible time, in which task with
   * <code>taskId</code>, can be started. It is the time, when
   * the last of its predecessors gets finished.
   *
   * @param task task, for which we want to find the
   *               earliest time of start
   * @return finish time of the last predecessor of <code>task</code>
   */
  public int getEarliestTime(Task task) {
    int earliest = 0;
    int[] pred = task.getPredecessors();
    for (int p : pred) {
      Task t = getTask(p);
      if (t.getStart() + t.getDuration() > earliest) {
        earliest = t.getStart() + t.getDuration();
      }
    }
    return earliest;
  }

  /**
   * Gets all tasks, which given resource can do.
   *
   * @param r resource, for which we look for tasks, that it can do
   * @return tasks doable by <code>r</code>
   */
  public List<Task> tasksCapableByResource(Resource r) {
    List<Task> tasks = new LinkedList<>();
    for (Task t : getTasks()) {
      if (canDoTask(t, r)) {
        tasks.add(t);
      }
    }
    return tasks;
  }

  /**
   * Checks whether resource <code>r</code> can do task <code>t</code>.
   *
   * @param t task for which we check capability
   * @param r resource for which we check capability
   * @return true if <code>r</code> can do <code>t</code>, false otherwise
   */
  public boolean canDoTask(Task t, Resource r) {
    if (null == r) {
      return false;
    }
    for (Skill skill : t.getRequiredSkills()) {
      if (!r.hasSkill(skill)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Get number of tasks that are done by resource with given id.
   *
   * @param resourceId id of the resource
   * @return number of assignements to resource with <code>resourceId</code>
   */
  public int getNumberOfAssignments(int resourceId) {
    int num = 0;
    for (Task t : tasks) {
      if (t.getResourceId() == resourceId) {
        num++;
      }
    }
    return num;
  }

  public void setHashCode() {
    this.hashCode = Arrays.hashCode(Arrays.stream(tasks).mapToInt(Task::getResourceId).toArray());
  }

  public Task[] getTasks() {
    return tasks;
  }

  public void setTasks(Task[] tasks) {
    this.tasks = tasks;
  }

  public Resource[] getResources() {
    return resources;
  }

  public void setResources(Resource[] resources) {
    this.resources = resources;
  }

}
