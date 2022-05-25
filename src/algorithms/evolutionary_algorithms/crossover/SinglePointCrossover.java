package algorithms.evolutionary_algorithms.crossover;


import algorithms.evolutionary_algorithms.ParameterSet;
import algorithms.problem.BaseProblemRepresentation;

import java.util.ArrayList;
import java.util.List;

/**
 * Single point crossover implementation. Copies genes
 * from both parents around single random point.
 */
public class SinglePointCrossover<GENE extends Number> extends BaseCrossover<GENE, BaseProblemRepresentation> {

  /**
   * A single random point is chosen. Children are created by
   * coping genes from parents - left from the point from one parent
   * and right from the point from second parent.
   *
   * @param cr cross over rate
   * @param firstParent first parent's genes
   * @param secondParent second parent's genes
   * @param parameters set of parameters
   * @return resulting array of genes
   */
  @Override
  public List<List<GENE>> crossover(double cr, List<GENE> firstParent, List<GENE> secondParent, ParameterSet<GENE, BaseProblemRepresentation> parameters) {
    int point = parameters.random.nextInt(firstParent.size());

    List<GENE> firstChild = new ArrayList<>(firstParent);
    List<GENE> secondChild = new ArrayList<>(secondParent);

    if (parameters.random.nextDouble() < cr) {
      for (int i = 0; i < firstParent.size(); ++i) {
        if (i < point) {
          firstChild.set(i, firstParent.get(i));
          secondChild.set(i, secondParent.get(i));
        } else {
          firstChild.set(i, secondParent.get(i));
          secondChild.set(i, firstParent.get(i));
        }
      }
    }

    List<List<GENE>> result = new ArrayList<>();
    result.add(firstChild);
    result.add(secondChild);

    return result;
  }

}
