package algorithms.evolutionary_algorithms.crossover;

import algorithms.evolutionary_algorithms.ParameterSet;
import algorithms.problem.BaseProblemRepresentation;

import java.util.ArrayList;
import java.util.List;

/**
 * Uniform crossover implementation. Selects every gene from parents
 * with equal probability.
 */
public class UniformCrossover<GENE extends Number> extends BaseCrossover<GENE, BaseProblemRepresentation> {

  /**
   * Iterates over all genes and selects the value
   * from parents with equal probability
   *
   * @param cr cross over rate
   * @param firstParent first parent's genes
   * @param secondParent second parent's genes
   * @param parameters set of parameters
   * @return array of children genes
   */
  @Override
  public List<List<GENE>> crossover(double cr, List<GENE> firstParent, List<GENE> secondParent, ParameterSet<GENE, BaseProblemRepresentation> parameters) {
    double random;
    List<GENE> firstChild = new ArrayList<>(firstParent);
    List<GENE> secondChild = new ArrayList<>(secondParent);

    if (parameters.random.nextDouble() < cr) {
      for (int i = 0; i < firstParent.size(); ++i) {
        random = parameters.random.nextDouble();
        if (random < 0.5) {
          firstChild.set(i, secondParent.get(i));
        }
        random = parameters.random.nextDouble();
        if (random < 0.5) {
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
