package algorithms.evolutionary_algorithms.selection;

import algorithms.evolutionary_algorithms.ParameterSet;
import algorithms.problem.BaseIndividual;
import algorithms.problem.BaseProblemRepresentation;

import java.util.List;

public class DiversitySelection<GENE extends Number> extends BaseSelection<GENE, BaseProblemRepresentation> {

  protected int tournamentSize;

  /**
   * If it's the first parent selects two best individuals from n random
   * candidates from the population with probability proportionate to fitness values.
   * If it's the second parent selects the farthest non-dominated individual in a
   * similar manner.
   *
   * @param population population to choose from
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
//    if (current == null) {
    int size = nonDominated.size();
    parent = choose(nonDominated.get((int) (parameters.random.nextDouble() * size)),
        nonDominated.get((int) (parameters.random.nextDouble() * size)));
    for (int i = 0; i < tournamentSize - 2; ++i) {
      BaseIndividual potentialInd = nonDominated.get((int) (parameters.random.nextDouble() * nonDominated.size()));
//        while (potentialInd.getDistance() == Double.POSITIVE_INFINITY || potentialInd.getDistance() == Double.NEGATIVE_INFINITY) {
        potentialInd = nonDominated.get((int) (parameters.random.nextDouble() * size));
//        }
      parent = choose(parent, potentialInd);
    }

    return parent;
  }

  private BaseIndividual<GENE, BaseProblemRepresentation> choose(BaseIndividual<GENE, BaseProblemRepresentation> firstCandidate,
                                                                 BaseIndividual<GENE, BaseProblemRepresentation> secondCandidate) {

    if (firstCandidate.getDistance() > secondCandidate.getDistance()) {
      return firstCandidate;
    }

    else if (firstCandidate.getDistance() < secondCandidate.getDistance()) {
      return secondCandidate;
    }

    return secondCandidate;
  }

  public int getTournamentSize() {
    return tournamentSize;
  }

  public void setTournamentSize(int tournamentSize) {
    this.tournamentSize = tournamentSize;
  }

}
