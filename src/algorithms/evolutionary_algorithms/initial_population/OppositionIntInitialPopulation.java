package algorithms.evolutionary_algorithms.initial_population;

import algorithms.evaluation.BaseEvaluator;
import algorithms.evolutionary_algorithms.ParameterSet;
import algorithms.problem.BaseIndividual;
import algorithms.problem.BaseProblemRepresentation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of opposition based initial population generation
 * Opposition of x is defined in range (l, h) as l + h - x.
 */
public class OppositionIntInitialPopulation<PROBLEM extends BaseProblemRepresentation> extends BaseInitialPopulation<Integer, PROBLEM> {

  private int multiplicationFactor = 1;

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
  public List<BaseIndividual<Integer, PROBLEM>> generate(PROBLEM problem, int populationSize, BaseEvaluator<Integer, PROBLEM> evaluator,
                                                         ParameterSet<Integer, PROBLEM> parameters) {
    populationSize *= multiplicationFactor;
    List<BaseIndividual<Integer, PROBLEM>> population = new ArrayList<>(populationSize);
    int numGenes = problem.getNumGenes();
    int breakPoint = populationSize / 2;
    List<Integer> genes = new ArrayList<>(Collections.nCopies(numGenes, 0));
    List<Integer> base;

    int i = 0;
    // Fill part of population with random individuals
    for ( ; i < breakPoint; ++i) {
      for (int j = 0; j < numGenes; ++j) {
        genes.set(j, parameters.random.nextInt(parameters.upperBounds[j]));
      }
      population.add(new BaseIndividual<>(problem, genes, evaluator));
    }

    // Fill the rest of population with opposite individuals
    for ( ; i < populationSize ; ++i) {
      base = population.get(i - breakPoint).getGenes();
      for (int j = 0; j < numGenes; ++j) {
        genes.set(j, parameters.upperBounds[j] - base.get(j) - 1);
      }
      population.add(new BaseIndividual<>(problem, genes, evaluator));
    }

    return population;
  }

  public void setMultiplicationFactor(int multiplicationFactor) {
    this.multiplicationFactor = multiplicationFactor;
  }
}
