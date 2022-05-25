package algorithms.evolutionary_algorithms.mutation;

import algorithms.evolutionary_algorithms.ParameterSet;
import algorithms.problem.BaseIndividual;
import algorithms.problem.BaseProblemRepresentation;

import java.util.Collections;
import java.util.List;

/**
 * Swaps two random genes.
 */
public class SwapBitMutation extends BaseMutation<Number, BaseProblemRepresentation> {

  /**
   * Swaps two genes
   *
   * @param population population to process
   * @param mutationProbability probability that the gene should be mutated
   * @param genesToMutate list of genes to mutate
   * @param current index of an individual to mutate
   * @param populationSize size of the population
   * @param parameters set of parameters
   * @return
   */
  @Override
  public List<Number> mutate(List<BaseIndividual<Number, BaseProblemRepresentation>> population, double mutationProbability, List<Number> genesToMutate,
                             int current, int populationSize, ParameterSet<Number, BaseProblemRepresentation> parameters) {
    int splitPoint = parameters.geneSplitPoint;
    if (parameters.random.nextDouble() < mutationProbability) {
      int firstGene = parameters.random.nextInt(genesToMutate.size());
      int secondGene = parameters.random.nextInt(genesToMutate.size());
      while (firstGene == secondGene ||
          ((firstGene < splitPoint && secondGene >= splitPoint) || (firstGene >= splitPoint && secondGene < splitPoint))
      ) {
        secondGene = parameters.random.nextInt(genesToMutate.size());
      }
      Collections.swap(genesToMutate, firstGene, secondGene);
    }
    return genesToMutate;
  }

}
