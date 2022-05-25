package algorithms.validation;

import algorithms.problem.scheduling.Schedule;
import algorithms.problem.scheduling.Task;

/**
 * Checks if all tasks have at least one resource assigned.
 */
public class AssignmentValidator extends BaseValidator {

  /**
   * Check if all tasks have at least one resource assigned.
   * @param schedule individual to validate
   * @return ValidationResult, additionally stores error message in an array
   */
  @Override
  public ValidationResult validate(Schedule schedule) {
    errorMessages.clear();
    String errorMessage = "";
    for (Task t : schedule.getTasks()) {
      if (t.getResourceId() == -1) {
        errorMessage += t.getId() + ", ";
      }
    }
    if (errorMessage.isEmpty()) {
      return ValidationResult.SUCCESS;
    }
    errorMessages.add("Assignment constraint violated by tasks: " + errorMessage);
    return ValidationResult.FAILURE;
  }

}
