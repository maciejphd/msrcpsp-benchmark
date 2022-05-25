package algorithms.visualization;

import algorithms.problem.scheduling.Schedule;
import algorithms.problem.scheduling.Task;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CriticalPathBuilder {
  public Set<Integer> BuildCriticalPath(Schedule schedule) {
    Map<Integer, Integer> minFinishTime = new HashMap<>(schedule.getTasks().length);
    for (Task task : schedule.getTasks()) {
      computeFinishTime(task, schedule, minFinishTime);
    }
    int maxMinFinishTime = Collections.max(minFinishTime.values());

    Map<Integer, Boolean> critical = new HashMap<>(schedule.getTasks().length);
    for (Task task : schedule.getTasks()) {
      if (minFinishTime.get(task.getId()) == maxMinFinishTime &&
          checkCriticalPath(task, schedule, critical, minFinishTime)) {
        break;
      }
    }

    Set<Integer> critical_set = critical.entrySet().stream()
        .filter(Map.Entry::getValue).map(Map.Entry::getKey)
        .collect(Collectors.toSet());

    return critical_set;
  }

  private void computeFinishTime(Task task, Schedule schedule,
                                Map<Integer, Integer> minFinishTime) {
    int taskId = task.getId();
    if (minFinishTime.containsKey(taskId)) return;

    minFinishTime.put(taskId, task.getDuration());
    for (int predecessorId : task.getPredecessors()) {
      Task predecessor = schedule.getTask(predecessorId);
      computeFinishTime(predecessor, schedule, minFinishTime);
      if (minFinishTime.get(taskId) < minFinishTime.get(predecessorId) + task.getDuration()) {
        minFinishTime.put(taskId, minFinishTime.get(predecessorId) + task.getDuration());
      }
    }
  }

  private boolean checkCriticalPath(Task task, Schedule schedule, Map<Integer, Boolean> critical,
                                    Map<Integer, Integer> minFinishTime) {
    if (critical.containsKey(task.getId())) {
      return critical.get(task.getId());
    }

    if (task.getPredecessors().length == 0) {
      critical.put(task.getId(), true);
      return true;
    }
    critical.put(task.getId(), false);

    int taskFinishTime = minFinishTime.get(task.getId());
    for (int predecessorId : task.getPredecessors()) {
      Task predecessor = schedule.getTask(predecessorId);
      if (taskFinishTime == minFinishTime.get(predecessorId) + task.getDuration() &&
          checkCriticalPath(predecessor, schedule, critical, minFinishTime)) {
        critical.put(task.getId(), true);
        return true;
      }
    }

    return false;
  }
}
