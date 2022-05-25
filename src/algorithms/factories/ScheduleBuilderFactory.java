package algorithms.factories;


import algorithms.evolutionary_algorithms.ParameterSet;
import algorithms.problem.scheduling.schedule_builders.BackwardScheduleBuilder;
import algorithms.problem.scheduling.schedule_builders.ForwardScheduleBuilder;
import algorithms.problem.scheduling.schedule_builders.ScheduleBuilder;
import algorithms.problem.scheduling.schedule_builders.ScheduleBuilderType;

public class ScheduleBuilderFactory {

  private ParameterSet parameters;

  public ScheduleBuilderFactory(ParameterSet parameters) {
    this.parameters = parameters;
  }

  /**
   * Creates problem builder based on provided type.
   * Default: {@link ScheduleBuilder}
   *
   * @param type type to use
   * @return chosen problem builder method
   */
  public ScheduleBuilder createScheduleBuilder(ScheduleBuilderType type) {
    switch (type) {
      case SCHEDULE_BULDER:
        return new ScheduleBuilder();
      case FORWARD_SCHEDULE_BUILDER:
        return new ForwardScheduleBuilder(parameters.hasSuccesors);
      case BACKWARD_SCHEDULE_BUILDER:
        return new BackwardScheduleBuilder();
      default:
        return new ScheduleBuilder();
    }
  }

}
