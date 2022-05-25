package algorithms.evolutionary_algorithms.initial_population;

import algorithms.evaluation.BaseEvaluator;
import algorithms.evolutionary_algorithms.ParameterSet;
import algorithms.problem.BaseIndividual;
import algorithms.problem.scheduling.Schedule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of initial population generation
 * with evenly distributed genes.
 */
public class EvenInitialPopulation extends BaseInitialPopulation<Double, Schedule> {

  /**
   * Creates a population of individuals with
   * evenly distributed genes.
   *
   * @param problem problem, for which to generate a population
   * @param populationSize size of the population
   * @param evaluator list of evaluators
   * @param parameters set of parameters
   * @return population - list of individuals
   */
  @Override
  public List<BaseIndividual<Double, Schedule>> generate(Schedule problem, int populationSize, BaseEvaluator<Double, Schedule> evaluator,
                                                         ParameterSet<Double, Schedule> parameters) {
    List<BaseIndividual<Double, Schedule>> population = new ArrayList<>(populationSize);
    int numTasks = problem.getTasks().length;

    List<List<Double>> values = new ArrayList<>(numTasks);
    for (int i = 0; i < numTasks; ++i) {
      values.add(new ArrayList<>());
      for (int j = 0; j < populationSize; ++j) {
        values.get(i).add((double) (j % parameters.upperBounds[i]));
      }
    }

    List<Double> genes = new ArrayList<>(numTasks);
    for (int i = 0; i < populationSize; ++i) {
      population.add(new BaseIndividual<Double, Schedule>(problem, genes, evaluator));
    }

    for (int j = 0; j < numTasks; ++j) {
      Collections.shuffle(values.get(j), parameters.random.getRandom());
      for (int i = 0; i < populationSize; ++i) {
        population.get(i).getGenes().set(j, values.get(j).get(i));
      }
    }

    return population;
  }

}
