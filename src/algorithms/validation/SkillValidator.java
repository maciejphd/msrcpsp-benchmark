package algorithms.validation;

import algorithms.problem.scheduling.Resource;
import algorithms.problem.scheduling.Schedule;
import algorithms.problem.scheduling.Task;

import java.util.List;

/**
 * Validates if any task has resource assigned that is not capable of performing
 * it. In other words, checks if a task has resource assigned that does not have skill of
 * required type or has skill on given type, but on the lower level.
 */
public class SkillValidator extends BaseValidator {

  /**
   * Validates if individual violates skill constraint.
   * @param schedule individual to validate
   * @return string with list of tasks ids that violate skill constraint
   * or null if individual is valid
   */
  @Override
  public ValidationResult validate(Schedule schedule) {
    errorMessages.clear();
    String errorMessage = "";
    for (Task t : schedule.getTasks()) {
      if (!schedule.canDoTask(t, schedule.getResource(t.getResourceId()))) {
        errorMessage += "Task id: "
            + t.getId()
            + ", assigned resource id: "
            + t.getResourceId()
            + ", resources capable: "
            + getResourceList(schedule.getCapableResources(t)) + "\n";
      }
    }
    if (errorMessage.isEmpty()) {
      return ValidationResult.SUCCESS;
    }
    errorMessages.add("Skill constraints violated: \n" + errorMessage);
    return ValidationResult.FAILURE;
  }

  private String getResourceList(List<Resource> resources) {
    String s = "";
    for (Resource r : resources) {
      s += r.getId() + " ; ";
    }
    return s;
  }

}
