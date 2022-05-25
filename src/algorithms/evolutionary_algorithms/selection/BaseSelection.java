package algorithms.evolutionary_algorithms.selection;

import algorithms.evolutionary_algorithms.ParameterSet;
import algorithms.problem.BaseIndividual;
import algorithms.problem.BaseProblemRepresentation;

import java.util.List;

/**
 * Enables dynamic choosing of selection method.
 */
abstract public class BaseSelection<GENE extends Number, PROBLEM extends BaseProblemRepresentation> {

  /**
   * Selects an individual for the next generation.
   *
   * @param population population with the individuals
   * @param nonDominated non dominated individuals
   * @param index index of the current individual
   * @param current current individual
   * @param trial individual that challenges the current one
   * @param parameters set of parameters
   * @return selected individual
   */
  abstract public BaseIndividual<GENE, PROBLEM> select(List<BaseIndividual<GENE, PROBLEM>> population, List<BaseIndividual<GENE, PROBLEM>> nonDominated,
                                                       int index, BaseIndividual<GENE, PROBLEM> current, BaseIndividual<GENE, PROBLEM> trial,
                                                       ParameterSet<GENE, PROBLEM> parameters);

}
