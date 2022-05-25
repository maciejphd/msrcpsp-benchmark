package algorithms.validation;

import algorithms.problem.scheduling.Schedule;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base for all validators. Contains an array of error messages.
 */
abstract public class BaseValidator {

  protected List<String> errorMessages;

  public BaseValidator() {
    errorMessages = new ArrayList<>();
  }

  /**
   * Validates given optimize.
   * @param schedule optimize to validate
   * @return error message, empty if optimize is valid
   */
  abstract public ValidationResult validate(Schedule schedule);

  public List<String> getErrorMessages() {
    return errorMessages;
  }

}
