package algorithms.evolutionary_algorithms.mutation;


import algorithms.evolutionary_algorithms.ParameterSet;
import algorithms.problem.BaseIndividual;
import algorithms.problem.BaseProblemRepresentation;

import java.util.List;

/**
 * Random mutation. Changes a gene to a random value.
 */
public class RandomBitMutation extends BaseMutation<Integer, BaseProblemRepresentation> {

  /**
   * With a probability equal to <code>mutationProbability</code>
   * divided by the number of genes changes each gene to a random
   * value within the boundaries.
   *
   * @param population population to process
   * @param mutationProbability probability that the gene should be mutated
   * @param genesToMutate list of genes to mutate
   * @param current index of an individual to mutate
   * @param populationSize size of the population
   * @param parameters set of parameters
   * @return mutated individual
   */
  @Override
  public List<Integer> mutate(List<BaseIndividual<Integer, BaseProblemRepresentation>> population, double mutationProbability,
                              List<Integer> genesToMutate, int current, int populationSize, ParameterSet<Integer, BaseProblemRepresentation> parameters) {

    for (int i = 0; i < genesToMutate.size(); ++i) {
      if (parameters.random.nextDouble() < mutationProbability) {
        genesToMutate.set(i, parameters.random.nextInt(parameters.upperBounds[i]));
      }
    }
    return genesToMutate;
  }
}
