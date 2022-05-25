package algorithms.factories;


import algorithms.evolutionary_algorithms.ParameterSet;
import algorithms.evolutionary_algorithms.differential_evolution.clone_handlers.*;

public class CloneHandlerFactory {

  private ParameterSet parameters;

  public CloneHandlerFactory(ParameterSet parameters) {
    this.parameters = parameters;
  }

  /**
   * Creates clone handler based on provided type.
   * Default: {@link AllowCloneHandler}
   * @param type type to use
   * @return chosen clone handler
   */
  public BaseCloneHandler createCloneHandler(CloneHandlerType type) {
    switch (type) {
      case ALLOW:
        return new AllowCloneHandler();
      case RANDOM:
        RandomCloneHandler randomCloneHandler = new RandomCloneHandler();
        randomCloneHandler.setGenerator(parameters.random.getRandom());
        randomCloneHandler.setUpperBounds(parameters.upperBounds);
        return randomCloneHandler;
      case OPPOSITE:
        OppositeCloneHandler oppositeCloneHandler = new OppositeCloneHandler();
        oppositeCloneHandler.setUpperBounds(parameters.upperBounds);
        return oppositeCloneHandler;
      default:
        return new AllowCloneHandler();
    }
  }

}
