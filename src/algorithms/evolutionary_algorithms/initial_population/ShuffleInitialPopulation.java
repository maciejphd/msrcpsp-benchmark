package algorithms.evolutionary_algorithms.initial_population;

import algorithms.evaluation.BaseEvaluator;
import algorithms.evolutionary_algorithms.ParameterSet;
import algorithms.problem.BaseIndividual;
import algorithms.problem.BaseProblemRepresentation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of initial population generator that
 * randomly shuffles genes of each individual
 */
public class ShuffleInitialPopulation<PROBLEM extends BaseProblemRepresentation> extends BaseInitialPopulation<Integer, PROBLEM> {

  /**
   * Each genome is filled with ids and then its shuffled.
   * Designed for TSP.
   *
   * @param problem problem, for which to generate a population
   * @param populationSize size of the population
   * @param evaluator list of evaluators
   * @param parameters set of parameters
   * @return population of individuals with shuffled genomes
   */
  @Override
  public List<BaseIndividual<Integer, PROBLEM>> generate(PROBLEM problem, int populationSize, BaseEvaluator<Integer, PROBLEM> evaluator, ParameterSet<Integer, PROBLEM> parameters) {
    List<BaseIndividual<Integer, PROBLEM>> population = new ArrayList<>(populationSize);
    int numTasks = problem.getNumGenes();

    // Fill genes with random values and assign to an individual
    List<Integer> genes = new ArrayList<>(Collections.nCopies(numTasks, null));
    for (int i = 0; i < genes.size(); ++i) {
      genes.set(i, i);
    }

    for (int i = 0; i < populationSize; ++i) {
      Collections.shuffle(genes, parameters.random.getRandom());
      population.add(new BaseIndividual<>(problem, genes, evaluator));
    }

    return population;
  }

}
