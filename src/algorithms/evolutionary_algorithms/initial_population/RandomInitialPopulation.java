package algorithms.evolutionary_algorithms.initial_population;

import algorithms.evaluation.BaseEvaluator;
import algorithms.evolutionary_algorithms.ParameterSet;
import algorithms.problem.BaseIndividual;
import algorithms.problem.BaseProblemRepresentation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of random initial population generation
 */
public class RandomInitialPopulation<GENE extends Number, PROBLEM extends BaseProblemRepresentation> extends BaseInitialPopulation<GENE, PROBLEM> {

  private int multiplicationFactor = 1;

  /**
   * Creates a a population by randomizing
   * each individual
   *
   * @param problem problem, for which to generate a population
   * @param populationSize size of the population
   * @param evaluator list of evaluators
   * @param parameters set of parameters
   * @return population - list of individuals
   */
  @Override
  public List<BaseIndividual<GENE, PROBLEM>> generate(PROBLEM problem, int populationSize,
                                                      BaseEvaluator<GENE, PROBLEM> evaluator, ParameterSet<GENE, PROBLEM> parameters) {
    populationSize *= multiplicationFactor;
    List<BaseIndividual<GENE, PROBLEM>> population = new ArrayList<>(populationSize);
    int numGenes = problem.getNumGenes();

    // Fill genes with random values and assign to an individual
    List<GENE> genes = new ArrayList<>(Collections.nCopies(numGenes, null));
    for (int i = 0; i < populationSize; ++i) {
      for (int j = 0; j < numGenes; ++j) {
        genes.set(j, parameters.random.next(parameters.upperBounds[j]));
      }
      population.add(new BaseIndividual<>(problem, genes, evaluator));
    }

    return population;
  }

  public void setMultiplicationFactor(int multiplicationFactor) {
    this.multiplicationFactor = multiplicationFactor;
  }
}
