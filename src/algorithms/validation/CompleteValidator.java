package algorithms.validation;

import algorithms.problem.scheduling.Schedule;

/**
 * Merges all validators, to thoroughly validate a individual.
 */
public class CompleteValidator extends BaseValidator{

  private BaseValidator[] validators;

  public CompleteValidator() {
    validators = new BaseValidator[4];
    validators[0] = new AssignmentValidator();
    validators[1] = new ConflictValidator();
    validators[2] = new RelationValidator();
    validators[3] = new SkillValidator();
  }

  @Override
  public ValidationResult validate(Schedule schedule) {
    errorMessages.clear();
    for (BaseValidator validator : validators) {
      validator.validate(schedule);
      errorMessages.addAll(validator.errorMessages);
    }
    if (errorMessages.isEmpty()) {
      return ValidationResult.SUCCESS;
    }
    return ValidationResult.FAILURE;
  }

}
