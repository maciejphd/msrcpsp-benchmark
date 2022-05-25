package algorithms.evolutionary_algorithms.selection;

import algorithms.evolutionary_algorithms.ParameterSet;
import algorithms.problem.BaseIndividual;
import algorithms.problem.BaseProblemRepresentation;

import java.util.List;

/**
 * Selects new individual if it isNotDominatedBy the old one
 * or if both are non-dominated.
 */
public class DominationSelection<GENE extends Number> extends BaseSelection<GENE, BaseProblemRepresentation> {

  /**
   * Inserts trial individual to the population if it
   * isNotDominatedBy current one or if both are non-dominated.
   *
   * @param population population with the individuals
   * @param nonDominated non dominated individuals
   * @param index index of the current individual
   * @param current current individual
   * @param trial individual that challenges the current one
   * @param parameters set of parameters
   * @return selected individual
   */
  @Override
  public BaseIndividual<GENE, BaseProblemRepresentation> select(List<BaseIndividual<GENE, BaseProblemRepresentation>> population,
                                                                List<BaseIndividual<GENE, BaseProblemRepresentation>> nonDominated, int index,
                                                                BaseIndividual<GENE, BaseProblemRepresentation> current,
                                                                BaseIndividual<GENE, BaseProblemRepresentation> trial,
                                                                ParameterSet<GENE, BaseProblemRepresentation> parameters) {
    if (!current.dominates(trial)) {
      return trial;
    }
    return current;
  }
}
