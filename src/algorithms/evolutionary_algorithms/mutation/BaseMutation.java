package algorithms.evolutionary_algorithms.mutation;

import algorithms.evolutionary_algorithms.ParameterSet;
import algorithms.problem.BaseIndividual;
import algorithms.problem.BaseProblemRepresentation;

import java.util.List;

/**
 * Enables dynamic choosing of mutation method.
 */
abstract public class BaseMutation<GENE extends Number, PROBLEM extends BaseProblemRepresentation> {

  /**
   * Performs a mutation on a current individual
   *
   * @param population population to process
   * @param f weight of a difference of two vectors
   *          used during the mutation
   * @param genesToMutate list of genes to mutate
   * @param current index of an individual to mutate
   * @param populationSize size of the population
   * @param parameters set of parameters
   * @return mutant vector
   */
  // TODO: find better solution for genesToMutate
  abstract public List<GENE> mutate(List<BaseIndividual<GENE, PROBLEM>> population, double f, List<GENE> genesToMutate,
                                    int current, int populationSize, ParameterSet<GENE, PROBLEM> parameters);

}
