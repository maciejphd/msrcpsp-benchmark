package algorithms.evolutionary_algorithms.mutation;

import algorithms.evolutionary_algorithms.ParameterSet;
import algorithms.problem.BaseIndividual;
import algorithms.problem.BaseProblemRepresentation;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of mutation with best genes.
 * Performs mutation on current best genes with 2 random individuals.
 */
public class BestVectorMutation extends BaseMutation<Double, BaseProblemRepresentation> {

  /**
   * Performs a mutation, where a base individual
   * is the best individual
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
  @Override
  public List<Double> mutate(List<BaseIndividual<Double, BaseProblemRepresentation>> population, double f, List<Double> genesToMutate,
                             int current, int populationSize, ParameterSet<Double, BaseProblemRepresentation> parameters) {
    int baseIndex = findBest(population);
    int r2, r3;
    List<Double> x2, x3;

    int numGenes = population.get(current).getGenes().size();
    List<Double> mutant;
    List<Double> best = population.get(baseIndex).getGenes();
    mutant = new ArrayList<>(best);

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

  /**
   * Finds index of an individual with the best
   * fitness value.
   *
   * @param population population to search through
   * @return index of the best individual
   */
  private int findBest(List<BaseIndividual<Double, BaseProblemRepresentation>> population) {
    double best = 1.0d;
    int bestIndex = 0;
    BaseIndividual individual;
    for (int i = 0; i < population.size(); ++i) {
      individual = population.get(i);
      if (individual.getEvalValue() < best) {
        best = individual.getEvalValue();
        bestIndex = i;
      }
    }
    return bestIndex;
  }

}
