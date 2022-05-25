package algorithms.evolutionary_algorithms.initial_population;

import algorithms.evaluation.BaseEvaluator;
import algorithms.evolutionary_algorithms.ParameterSet;
import algorithms.evolutionary_algorithms.converters.BaseConverter;
import algorithms.problem.BaseIndividual;
import algorithms.problem.scheduling.Resource;
import algorithms.problem.scheduling.Schedule;
import algorithms.problem.scheduling.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of naive swaps population generation.
 * Swaps task/resource assignments until desired diversity is achieved.
 * Requires number of swaps and time limit parameters.
 */
public class DiversityInitialPopulation extends BaseInitialPopulation<Double, Schedule> {

  private double numSwaps;
  private long timeLimit;
  private BaseConverter converter;

  public DiversityInitialPopulation(BaseConverter converter) {
    this.converter = converter;
  }

  /**
   * Swaps task/resource assignments until desired diversity is achieved.
   *
   * @param problem problem, for which to generate a population
   * @param populationSize size of the population
   * @param evaluator list of evaluators
   * @param parameters set of parameters
   * @return population - list of individuals
   */
  @Override
  public List<BaseIndividual<Double, Schedule>> generate(Schedule problem, int populationSize,
                                                         BaseEvaluator<Double, Schedule> evaluator, ParameterSet<Double, Schedule> parameters) {
    List<BaseIndividual<Double, Schedule>> population = new ArrayList<>(populationSize);
    int numTasks = problem.getTasks().length;
    int nswaps = (int)(numSwaps * populationSize);
    Task swapper;
    Task swappee;
    int tempId;
    long startTime;

    BaseIndividual<Double, Schedule> individual;
    Schedule indSchedule;
    Task[] tasks;
    Resource[] resources;

    for (int i = 0; i < populationSize; ++i) {
      individual = new BaseIndividual<>(problem, new ArrayList<>(numTasks), evaluator);
      indSchedule = individual.getProblem();
      tasks = indSchedule.getTasks();
      resources = indSchedule.getResources();
      for (int swap = 0; swap < nswaps; ++swap) {
        swapper = tasks[parameters.random.nextInt(numTasks)];
        swappee = tasks[parameters.random.nextInt(numTasks)];
        startTime = System.nanoTime();
        while ( !problem.canDoTask(swapper, resources[swappee.getResourceId()-1]) ||
            !problem.canDoTask(swappee, resources[swapper.getResourceId()-1]) &&
                (swapper.getResourceId() != swappee.getResourceId()) &&
                System.nanoTime() - startTime < timeLimit) {
          swappee = tasks[parameters.random.nextInt(numTasks)];
        }
        tempId = swappee.getResourceId();
        swappee.setResourceId(swapper.getResourceId());
        swapper.setResourceId(tempId);
      }

      population.add(individual);
    }

    for (int i = 0; i < populationSize; ++i) {
      converter.convertToReal(population.get(i).getProblem().getTasks(),
          population.get(i).getGenes());
    }

    return population;
  }

  public void setNumSwaps(double numSwaps) {
    this.numSwaps = numSwaps;
  }

  public void setTimeLimit(long timeLimit) {
    this.timeLimit = timeLimit;
  }

}
