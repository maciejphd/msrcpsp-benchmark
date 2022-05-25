package algorithms.evolutionary_algorithms.initial_population;

import algorithms.evaluation.BaseEvaluator;
import algorithms.evolutionary_algorithms.ParameterSet;
import algorithms.problem.BaseIndividual;
import algorithms.problem.scheduling.Schedule;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of opposition based initial population generation
 * Opposition of x is defined in range (l, h) as l + h - x.
 */
public class OppositionInitialPopulation extends BaseInitialPopulation<Double, Schedule> {

  /**
   * Creates a population by randomizing half of it and then
   * creating the opposite second half.
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
    int breakPoint = populationSize / 2;
    List<Double> genes = new ArrayList<>(numTasks);
    List<Double> base;

    int i = 0;
    // Fill part of population with random individuals
    for ( ; i < breakPoint; ++i) {
      for (int j = 0; j < numTasks; ++j) {
        genes.set(j, parameters.random.nextDouble() * parameters.upperBounds[j]);
      }
      population.add(new BaseIndividual<>(problem, genes, evaluator));
    }

    // Fill the rest of population with opposite individuals
    for ( ; i < populationSize ; ++i) {
      base = population.get(i - breakPoint).getGenes();
      for (int j = 0; j < numTasks; ++j) {
        genes.set(j, parameters.upperBounds[j] - base.get(j));
      }
      population.add(new BaseIndividual<>(problem, genes, evaluator));
    }

    return population;
  }

}
