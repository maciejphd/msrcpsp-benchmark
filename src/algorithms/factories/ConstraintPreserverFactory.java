package algorithms.factories;


import algorithms.evolutionary_algorithms.ParameterSet;
import algorithms.evolutionary_algorithms.differential_evolution.constraint_preservers.*;

public class ConstraintPreserverFactory {

  private ParameterSet parameters;

  public ConstraintPreserverFactory(ParameterSet parameters) {
    this.parameters = parameters;
  }

  /**
   * Creates constraint preserver based on provided type.
   * Default: {@link RandomConstraintPreserver}
   * @param type type to use
   * @return chosen constraint preserver
   */
  public BaseConstraintPreserver createConstraintPreserver(ConstraintPreserverType type) {
    BaseConstraintPreserver constraintPreserver;
    switch (type) {
      case LOAD_BASED:
        constraintPreserver = new LoadBasedConstraintPreserver();
        ((LoadBasedConstraintPreserver)constraintPreserver).setGenerator(parameters.random.getRandom());
        ((LoadBasedConstraintPreserver)constraintPreserver).setUpperBounds(parameters.upperBounds);
        break;
      case MODULO:
        constraintPreserver = new ModuloConstraintPreserver();
        ((ModuloConstraintPreserver)constraintPreserver).setUpperBounds(parameters.upperBounds);
        break;
      case RANDOM:
        constraintPreserver = new RandomConstraintPreserver();
        ((RandomConstraintPreserver)constraintPreserver).setGenerator(parameters.random.getRandom());
        ((RandomConstraintPreserver)constraintPreserver).setUpperBounds(parameters.upperBounds);
        break;
      default:
        constraintPreserver = new RandomConstraintPreserver();
        ((RandomConstraintPreserver)constraintPreserver).setGenerator(parameters.random.getRandom());
        ((RandomConstraintPreserver)constraintPreserver).setUpperBounds(parameters.upperBounds);
    }
    return constraintPreserver;
  }

}
