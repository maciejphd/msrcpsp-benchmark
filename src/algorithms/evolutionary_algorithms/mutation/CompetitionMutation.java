package algorithms.evolutionary_algorithms.mutation;

import algorithms.evolutionary_algorithms.ParameterSet;
import algorithms.problem.BaseIndividual;
import algorithms.problem.BaseProblemRepresentation;

import java.util.Collections;
import java.util.List;

public class CompetitionMutation extends BaseMutation<Integer, BaseProblemRepresentation> {

  /**
   * Swaps
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
  public List<Integer> mutate(List<BaseIndividual<Integer, BaseProblemRepresentation>> population, double mutationProbability, List<Integer> genesToMutate,
                             int current, int populationSize, ParameterSet<Integer, BaseProblemRepresentation> parameters) {
    int splitPoint = parameters.geneSplitPoint;

    if (parameters.random.nextDouble() < mutationProbability) {

      int firstGene = parameters.random.nextInt(splitPoint - 1) + 1;
      int secondGene = parameters.random.nextInt(splitPoint - 1) + 1;
      while (firstGene == secondGene) {
        secondGene = parameters.random.nextInt(splitPoint - 1) + 1;
      }
//      Collections.swap(genesToMutate, firstGene, secondGene);
      if (firstGene < secondGene) {
        Collections.reverse(genesToMutate.subList(firstGene, secondGene));
      } else {
        Collections.reverse(genesToMutate.subList(secondGene, firstGene));
      }

    }
    if (parameters.random.nextDouble() < mutationProbability) {
      int random = parameters.random.next(genesToMutate.size() - splitPoint) + splitPoint;
      genesToMutate.set(random, genesToMutate.get(random) ^ 1);
    }
    return genesToMutate;
  }

}
