package algorithms.evolutionary_algorithms.initial_population;

import algorithms.evaluation.BaseEvaluator;
import algorithms.evolutionary_algorithms.ParameterSet;
import algorithms.problem.BaseIndividual;
import algorithms.problem.BaseProblemRepresentation;

import java.util.List;

/**
 * Enables dynamic choosing of initial population generation method.
 */
abstract public class BaseInitialPopulation<GENE extends Number, PROBLEM extends BaseProblemRepresentation> {

  /**
   * Generates an initial population.
   *
   * @param problem problem, for which to generate a population
   * @param populationSize size of the population
   * @param evaluator list of evaluators
   * @param parameters set of parameters
   * @return population - list of individuals
   */
  abstract public List<BaseIndividual<GENE, PROBLEM>> generate(PROBLEM problem, int populationSize,
                                                               BaseEvaluator<GENE, PROBLEM> evaluator, ParameterSet<GENE, PROBLEM> parameters);

}
