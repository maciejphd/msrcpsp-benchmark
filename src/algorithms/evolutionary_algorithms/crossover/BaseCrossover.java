package algorithms.evolutionary_algorithms.crossover;

import algorithms.evolutionary_algorithms.ParameterSet;
import algorithms.problem.BaseProblemRepresentation;

import java.util.List;

/**
 * Enables dynamic choosing of crossover method.
 */
abstract public class BaseCrossover<GENE extends Number, PROBLEM extends BaseProblemRepresentation> {

  /**
   * Performs a crossover operator.
   *
   * @param cr cross over rate
   * @param firstParent first parent's genes
   * @param secondParent second parent's genes
   * @param parameters set of parameters
   * @return resulting array of genes
   */
  abstract public List<List<GENE>> crossover(double cr, List<GENE> firstParent, List<GENE> secondParent, ParameterSet<GENE, PROBLEM> parameters);

}
