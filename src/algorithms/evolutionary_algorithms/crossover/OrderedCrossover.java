package algorithms.evolutionary_algorithms.crossover;

import algorithms.evolutionary_algorithms.ParameterSet;
import algorithms.problem.BaseProblemRepresentation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Ordered crossover. Copies a sublist from one parent
 * and fills the rest of the genome from the second parent.
 * Best suited for TSP.
 */
public class OrderedCrossover extends BaseCrossover<Integer, BaseProblemRepresentation> {

  /**
   * Creates two children by swapping their n first genes.
   *
   * @param cr cross over rate
   * @param firstParent first parent's genes
   * @param secondParent second parent's genes
   * @param parameters set of parameters
   * @return resulting array of genes
   */
  @Override
  public List<List<Integer>> crossover(double cr, List<Integer> firstParent, List<Integer> secondParent, ParameterSet<Integer, BaseProblemRepresentation> parameters) {
    int size = firstParent.size();
    int splitPlace = parameters.random.next(size / 4) + size / 2;

    List<Integer> firstChild = new ArrayList<>(firstParent);
    List<Integer> secondChild = new ArrayList<>(secondParent);

    List<Integer> swapGenome = new ArrayList<>(firstChild);

    if (parameters.random.nextDouble() < cr) {

      for (int i = 0; i < splitPlace; i++) {
        changeOrder(firstChild, secondChild.get(i), i);
      }

      for (int i = 0; i < splitPlace; i++) {
        changeOrder(secondChild, swapGenome.get(i), i);
      }

    }

    List<List<Integer>> result = new ArrayList<>();
    result.add(firstChild);
    result.add(secondChild);

    return result;
  }

  private void changeOrder(List<Integer> genome, int newGene, int position) {
    int newCityPosition = findGenePosition(genome, newGene);
    if (position != newCityPosition) {
      Collections.swap(genome, position, newCityPosition);
    }
  }

  private int findGenePosition(List<Integer> genome, int gene) {
    for (int i = 0; i < genome.size(); i++) {
      if (genome.get(i) == gene) {
        return i;
      }
    }
    return -1;
  }

}
