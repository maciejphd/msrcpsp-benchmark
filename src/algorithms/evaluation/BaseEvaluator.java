package algorithms.evaluation;

import algorithms.problem.BaseIndividual;
import algorithms.problem.BaseProblemRepresentation;

/**
 * Base evaluator class.
 */
abstract public class BaseEvaluator<GENE extends Number, PROBLEM extends BaseProblemRepresentation> {

  protected BaseIndividual<GENE, PROBLEM> individual;

  public BaseEvaluator() { }

  public BaseEvaluator(BaseIndividual<GENE, PROBLEM> individual) {
    this.individual = individual;
  }

  /**
   * Abstract <code>evaluate()</code>. Each evaluator
   * must define a body for this function.
   *
   * @return evaluation value
   */
  abstract public double evaluate();

  /**
   * Creates a copy of this evaluator with a new individual
   *
   * @param individual individual to use
   * @return new evaluator of the same type, but
   * with the new individual
   */
  abstract public BaseEvaluator<GENE, PROBLEM> getCopy(BaseIndividual<GENE, PROBLEM> individual);

  /**
   * Returns an array of objective values.
   *
   * @return array with objectives values of the solution
   */
  abstract public double[] getObjectives();

  /**
   * Returns an array of normalized objective values. Normalization is
   * done by dividing the values by the maximum possible values.
   *
   * @return array with duration and cost of the individual
   */
  abstract public double[] getNormalObjectives();

  /**
   * Allows to differentiate evaluators
   *
   * @return type from <code>EvaluatorType</code>
   */
  abstract public EvaluatorType getType();

  /**
   * Creates a Nadir point. It contains the worst possible values
   * of all criteria.
   *
   * @return Nadir point
   */
  public abstract BaseIndividual getNadirPoint();

  /**
   * Creates a perfect point. It contains the best possible values
   * of all criteria.
   *
   * @return perfect point
   */
  public abstract BaseIndividual getPerfectPoint();

  public BaseIndividual<GENE, PROBLEM> getIndividual() {
    return individual;
  }

  public void setIndividual(BaseIndividual<GENE, PROBLEM> individual) {
    this.individual = individual;
  }

  public abstract int getNumObjectives();

}
