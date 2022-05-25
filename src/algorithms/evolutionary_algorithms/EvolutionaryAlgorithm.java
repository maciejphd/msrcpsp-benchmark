package algorithms.evolutionary_algorithms;

import algorithms.problem.BaseIndividual;
import algorithms.problem.BaseProblemRepresentation;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for evolutionary algorithms.
 */
public class EvolutionaryAlgorithm<GENE extends Number, PROBLEM extends BaseProblemRepresentation> {

  protected PROBLEM problem;
  protected ParameterSet<GENE, PROBLEM> parameters;

  protected int populationSize;
  // limits max number of generations
  protected int generationLimit;

  protected List<BaseIndividual<GENE, PROBLEM>> removeDuplicates(
      List<BaseIndividual<GENE, PROBLEM>> allIndividuals) {
    List<BaseIndividual<GENE, PROBLEM>> allIndividualsNoDuplicates = new ArrayList<>();
    for (BaseIndividual<GENE, PROBLEM> ind : allIndividuals) {
      if (allIndividualsNoDuplicates.stream().noneMatch(i -> i.equalsPhenotype(ind))) {
        allIndividualsNoDuplicates.add(ind);
      }
    }
    return allIndividualsNoDuplicates;
  }

  // TODO: probably should be in a more generic place, it is also used by measures and now by mutation
  protected List<BaseIndividual<GENE, PROBLEM>> getNondominated(
      List<BaseIndividual<GENE, PROBLEM>> population) {

    List<BaseIndividual<GENE, PROBLEM>> nondominatedSolutions = new ArrayList<>();

    for (BaseIndividual<GENE, PROBLEM> individual : population) {
      if (individual.isNotDominatedBy(population)) {
        nondominatedSolutions.add(individual);
      }
    }

    return nondominatedSolutions;
  }

}
