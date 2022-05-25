package algorithms.evolutionary_algorithms.selection;


import algorithms.evolutionary_algorithms.ParameterSet;
import algorithms.problem.BaseIndividual;
import algorithms.problem.BaseProblemRepresentation;

import java.util.List;

public class RouletteWheelSelection<GENE extends Number> extends BaseSelection<GENE, BaseProblemRepresentation> {

  /**
   * Selects two individuals from the population with
   * probability proportionate to fitness values.
   *
   * @param population  population to choose from
   * @param nonDominated non dominated individuals
   * @param index index of the current individual
   * @param current current individual
   * @param trial individual that challenges the current one
   * @param parameters set of parameters
   * @return <code>IndividualPair</code> with two individuals
   */
  @Override
  public BaseIndividual<GENE, BaseProblemRepresentation> select(List<BaseIndividual<GENE, BaseProblemRepresentation>> population,
                                                                List<BaseIndividual<GENE, BaseProblemRepresentation>> nonDominated, int index,
                                                                BaseIndividual<GENE, BaseProblemRepresentation> current,
                                                                BaseIndividual<GENE, BaseProblemRepresentation> trial,
                                                                ParameterSet<GENE, BaseProblemRepresentation> parameters) {
    BaseIndividual<GENE, BaseProblemRepresentation> parent;
    double sum = 0.0;
    for (BaseIndividual<GENE, BaseProblemRepresentation> individual : population) {
      sum += 1.0 - individual.getEvalValue(); // due to minimization
    }
    double value = parameters.random.nextDouble() * sum;
    parent = chooseParent(population, value);

    return parent;
  }

  private BaseIndividual<GENE, BaseProblemRepresentation> chooseParent(List<BaseIndividual<GENE, BaseProblemRepresentation>> population, double value) {
    double currentSum = 0.0;
    for (BaseIndividual<GENE, BaseProblemRepresentation> individual : population) {
      currentSum += 1.0 - individual.getEvalValue();
      if (currentSum > value) {
        return individual;
      }
    }
    return null;
  }

}
