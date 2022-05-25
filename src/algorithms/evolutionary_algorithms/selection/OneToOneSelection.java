package algorithms.evolutionary_algorithms.selection;

import algorithms.evolutionary_algorithms.ParameterSet;
import algorithms.problem.BaseIndividual;
import algorithms.problem.BaseProblemRepresentation;

import java.util.List;

/**
 * Implementation of one-to-one selection.
 * Selects better of current and trial vector.
 */
public class OneToOneSelection<GENE extends Number> extends BaseSelection<GENE, BaseProblemRepresentation> {

  /**
   * Selects better of current and trial vector.
   * Returns true if trial was selected.
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
    if (trial.getEvalValue() <= current.getEvalValue()) {
      return trial;
    }
    return current;
  }

}
