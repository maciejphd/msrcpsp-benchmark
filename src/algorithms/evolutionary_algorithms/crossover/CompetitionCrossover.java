package algorithms.evolutionary_algorithms.crossover;

import algorithms.evolutionary_algorithms.ParameterSet;
import algorithms.problem.BaseProblemRepresentation;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CompetitionCrossover extends BaseCrossover<Integer, BaseProblemRepresentation> {

  /**
   * Crossover specialized for TTP.
   * EDGE for TSP. Single-Point for KP.
   *
   * @param cr cross over rate
   * @param firstParent first parent's genes
   * @param secondParent second parent's genes
   * @param parameters set of parameters
   * @return array of children genes
   */
  @Override
  public List<List<Integer>> crossover(double cr, List<Integer> firstParent, List<Integer> secondParent, ParameterSet<Integer, BaseProblemRepresentation> parameters) {
    List<Integer> firstChild = new ArrayList<>(firstParent);
    List<Integer> secondChild = new ArrayList<>(secondParent);

    if (parameters.random.nextDouble() < cr) {
      // EDGE
      List<Set<Integer>> firstNeighbourhood = generateNeighbourhood(firstParent, secondParent, parameters);
      List<Set<Integer>> secondNeighbourhood = copyNeighbourhood(firstNeighbourhood);
      firstChild = getChild(firstChild, firstParent, secondParent, firstNeighbourhood, parameters);
      secondChild = getChild(secondChild, secondParent, firstParent, secondNeighbourhood, parameters);
    }

    int numGenes = parameters.geneSplitPoint;
    if (parameters.random.nextDouble() < cr) {
      for (int i = numGenes; i < firstParent.size(); ++i) {
        int point = parameters.random.nextInt(firstParent.size() - numGenes) + numGenes;

        if (i < point) {
          firstChild.set(i, firstParent.get(i));
          secondChild.set(i, secondParent.get(i));
        } else {
          firstChild.set(i, secondParent.get(i));
          secondChild.set(i, firstParent.get(i));
        }
      }
    }

    List<List<Integer>> result = new ArrayList<>();
    result.add(firstChild);
    result.add(secondChild);

    return result;
  }

  private List<Integer> getChild(List<Integer> child, List<Integer> firstParent, List<Integer> secondParent,
                                 List<Set<Integer>> neighbourhood, ParameterSet<Integer, BaseProblemRepresentation> parameters) {
    int numGenes = parameters.geneSplitPoint;
    List<Integer> availableGenes = IntStream.rangeClosed(0, numGenes - 1)
        .boxed().collect(Collectors.toList());
    int x = firstParent.get(0);
    availableGenes.remove(new Integer(x));
    for (int i = 0; i < numGenes - 1; ++i) {
      child.set(i, x);

      removeFromNeighbourhood(x, neighbourhood);

      if (neighbourhood.get(x).isEmpty()) {
        x = availableGenes.get(parameters.random.nextInt(availableGenes.size()));
      } else {
        Set<Integer> neighbourhoodOfX = neighbourhood.get(x);
        int minSize = neighbourhoodOfX.stream().mapToInt(neighbour -> neighbourhood.get(neighbour).size()).min().orElse(0);
        neighbourhoodOfX = neighbourhoodOfX.stream().filter(neighbour -> neighbourhood.get(neighbour).size() == minSize).collect(Collectors.toSet());
        int random = parameters.random.next(neighbourhoodOfX.size());
        Iterator<Integer> iter = neighbourhoodOfX.iterator();
        for (int j = 0; j < random; j++) {
          iter.next();
        }
        x = iter.next();

        availableGenes.remove(new Integer(x));
      }
    }
    child.set(numGenes - 1, x);

    return child;
  }

  private void removeFromNeighbourhood(final Integer toRemove, List<Set<Integer>> neighbourhood) {
    neighbourhood.forEach(set -> set.remove(toRemove));
  }

  private List<Set<Integer>> generateNeighbourhood(List<Integer> firstParent, List<Integer> secondParent, ParameterSet<Integer, BaseProblemRepresentation> parameters) {
    int numGenes = parameters.geneSplitPoint;
    List<Set<Integer>> neighbourhood = new ArrayList<>();
    for (int i = 0; i < numGenes; ++i) {
      neighbourhood.add(new HashSet<>());
    }
    neighbourhood.get(firstParent.get(0)).add(firstParent.get(1));
    neighbourhood.get(firstParent.get(0)).add(firstParent.get(numGenes - 1));
    neighbourhood.get(secondParent.get(0)).add(secondParent.get(1));
    neighbourhood.get(secondParent.get(0)).add(secondParent.get(numGenes - 1));

    for (int i = 1; i < numGenes - 1; ++i) {
      neighbourhood.get(firstParent.get(i)).add(firstParent.get(i - 1));
      neighbourhood.get(firstParent.get(i)).add(firstParent.get(i + 1));
      neighbourhood.get(secondParent.get(i)).add(secondParent.get(i - 1));
      neighbourhood.get(secondParent.get(i)).add(secondParent.get(i + 1));
    }

    neighbourhood.get(firstParent.get(numGenes - 1)).add(firstParent.get(numGenes - 2));
    neighbourhood.get(firstParent.get(numGenes - 1)).add(firstParent.get(0));
    neighbourhood.get(secondParent.get(numGenes - 1)).add(secondParent.get(numGenes - 2));
    neighbourhood.get(secondParent.get(numGenes - 1)).add(secondParent.get(0));

    return neighbourhood;
  }

  private List<Set<Integer>> copyNeighbourhood(List<Set<Integer>> neighbourhood) {
    List<Set<Integer>> copy = new ArrayList<>();
    for (Set<Integer> set : neighbourhood) {
      Set<Integer> setCopy = new HashSet<>(set);
      copy.add(setCopy);
    }
    return copy;
  }

}