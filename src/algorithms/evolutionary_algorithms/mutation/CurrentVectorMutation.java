package algorithms.evolutionary_algorithms.mutation;

import algorithms.evolutionary_algorithms.ParameterSet;
import algorithms.problem.BaseIndividual;
import algorithms.problem.BaseProblemRepresentation;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of mutation with current genes.
 * Performs mutation on current genes with 2 random individuals
 */
public class CurrentVectorMutation extends BaseMutation<Double, BaseProblemRepresentation> {

  /**
   * Performs a mutation, where a base individual
   * is the current individual
   *
   * @param population population to process
   * @param f weight of a difference of two vectors
   *          used during the mutation
   * @param genesToMutate list of genes to mutate
   * @param current index of an individual to mutate
   * @param populationSize size of the populationCurrentVectorMutation
   * @return mutant vector
   */
  @Override
  public List<Double> mutate(List<BaseIndividual<Double, BaseProblemRepresentation>> population, double f, List<Double> genesToMutate,
                             int current, int populationSize, ParameterSet<Double, BaseProblemRepresentation> parameters) {
    int r2, r3;
    List<Double> currentGenes, x2, x3;

    int numGenes = population.get(current).getGenes().size();
    List<Double> mutant;
    currentGenes = population.get(current).getGenes();
    mutant = new ArrayList<Double>(currentGenes);

    for (int r = 0; r < parameters.mutationRank; ++r) {
      do {
        r2 = parameters.random.nextInt(populationSize);
      } while (r2 == current);
      do {
        r3 = parameters.random.nextInt(populationSize);
      } while (r3 == r2 || r3 == current);
      x2 = population.get(r2).getGenes();
      x3 = population.get(r3).getGenes();
      for (int i = 0; i < numGenes; ++i) {
        mutant.set(i, mutant.get(i) + f * (x2.get(i) - x3.get(i)));
      }
    }

    return mutant;
  }

}
