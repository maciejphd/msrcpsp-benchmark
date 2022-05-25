package algorithms.factories;


import algorithms.evolutionary_algorithms.ParameterSet;
import algorithms.evolutionary_algorithms.initial_population.*;
import algorithms.problem.scheduling.Schedule;

public class InitialPopulationGeneratorFactory {

  private ParameterSet parameters;

  public InitialPopulationGeneratorFactory(ParameterSet parameters) {
    this.parameters = parameters;
  }

  /**
   * Create initial population generation method based
   * on provided type. Default: {@link RandomInitialPopulation}
   *
   * @param type type to use
   * @return chosen initial population generation method
   */
  public BaseInitialPopulation createInitialPopulation(InitialPopulationType type) {
    BaseInitialPopulation<? extends Number, Schedule> initialPopulation;
    switch (type) {
      case RANDOM:
        initialPopulation = new RandomInitialPopulation<>();
        ((RandomInitialPopulation) initialPopulation)
            .setMultiplicationFactor(parameters.populationMultiplicationFactor);
        return initialPopulation;
      case NAIVE_SWAPS:
        initialPopulation = new NaiveSwapsInitialPopulation(parameters.converter);
        ((NaiveSwapsInitialPopulation)initialPopulation).setNumSwaps(parameters.numSwaps);
        break;
      case DIVERSITY:
        initialPopulation = new DiversityInitialPopulation(parameters.converter);
        ((DiversityInitialPopulation)initialPopulation).setNumSwaps(parameters.numSwaps);
        ((DiversityInitialPopulation)initialPopulation).setTimeLimit(parameters.swapsTime);
        break;
      case OPPOSITION:
        return new OppositionInitialPopulation();
      case OPPOSITION_INT:
        initialPopulation = new OppositionIntInitialPopulation<>();
        ((OppositionIntInitialPopulation) initialPopulation).setMultiplicationFactor(parameters.populationMultiplicationFactor);
        return initialPopulation;
      case EVEN:
        return new EvenInitialPopulation();
      case SHUFFLE:
        return new ShuffleInitialPopulation();
      default:
        return new RandomInitialPopulation();
    }
    return initialPopulation;
  }

}
