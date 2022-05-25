package algorithms.evolutionary_algorithms.selection;

import algorithms.evolutionary_algorithms.ParameterSet;
import algorithms.problem.BaseIndividual;
import algorithms.problem.BaseProblemRepresentation;

import java.util.List;
import java.util.Random;

/**
 * Implementation of probability based selection.
 * Selects from current and trial individual with probabilities
 * proportionate to their evaluation values. Requires a random random.
 */
public class ProbabilitySelection<GENE extends Number> extends BaseSelection<GENE, BaseProblemRepresentation> {

  private Random generator;

  /**
   * Selects from current and trial individual with probabilities
   * proportionate to their evaluation values.
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
    double max = population.get(0).getEvalValue();
    double min = population.get(0).getEvalValue();
    for (BaseIndividual<GENE, BaseProblemRepresentation> ind : population) {
      if (ind.getEvalValue() > max) {
        max = ind.getEvalValue();
      } else if (ind.getEvalValue() < min) {
        min = ind.getEvalValue();
      }
    }
    double trialValue = (trial.getEvalValue() - min) / (max - min);
    double currentValue = (current.getEvalValue() - min) / (max - min);
    double sum = trialValue + currentValue;

    if (generator.nextDouble() > trialValue / sum) {
      return trial;
    }
    return current;
  }

  public void setGenerator(Random generator) {
    this.generator = generator;
  }

}
