package algorithms.problem;

import algorithms.evolutionary_algorithms.ParameterSet;

import java.util.List;

/**
 * Generic class for individual representations.
 */
public abstract class BaseProblemRepresentation {

  protected long hashCode;

  public abstract BaseProblemRepresentation cloneDeep();

  public abstract int getNumGenes();

  /**
   * Performs tasks specific to the problem that are required to
   * build the solution.
   *
   * @param genes list of genes, converted to integer values if necessary
   * @param parameters set of parameters
   * @return this problem
   */
  public abstract BaseProblemRepresentation buildSolution(List<? extends Number> genes,
                                                          ParameterSet<? extends Number, ? extends BaseProblemRepresentation> parameters);

  public abstract void setHashCode();

  public long getHashCode() {
    return hashCode;
  }

}
