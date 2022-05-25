package algorithms.evolutionary_algorithms.selection;

import algorithms.evolutionary_algorithms.ParameterSet;
import algorithms.problem.BaseIndividual;
import algorithms.problem.BaseProblemRepresentation;

import java.util.List;

public class NondominatedSortingNoCrowdingTournament<GENE extends Number> extends BaseSelection<GENE, BaseProblemRepresentation> {

  protected int tournamentSize;

	/**
	 * Selects two best individuals from n random candidates from the population with
	 * probability proportionate to fitness values.
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

    // TODO: refactor
    BaseIndividual<GENE, BaseProblemRepresentation> parent;
    BaseIndividual<GENE, BaseProblemRepresentation> pretender;
    int size = nonDominated.size() + population.size();
    parent = randomizeIndividual(population, nonDominated, size, parameters);
    pretender = randomizeIndividual(population, nonDominated, size, parameters);
    parent = choose( parent, pretender);
    for (int i = 0; i < tournamentSize - 2; ++i) {
      pretender = randomizeIndividual(population, nonDominated, size, parameters);
      parent = choose(parent, pretender);
    }

    return parent;
  }

  private BaseIndividual<GENE, BaseProblemRepresentation> randomizeIndividual(List<BaseIndividual<GENE, BaseProblemRepresentation>> population,
                                                                              List<BaseIndividual<GENE, BaseProblemRepresentation>> nonDominated,
                                                                              int size,
                                                                              ParameterSet<GENE, BaseProblemRepresentation> parameters) {
    BaseIndividual<GENE, BaseProblemRepresentation> pretender;
    int random = parameters.random.next(size).intValue();
    if (random >= nonDominated.size()) {
      random -= nonDominated.size();
      pretender = population.get(random);
    } else {
      pretender = nonDominated.get(random);
    }
    return pretender;
  }

	private BaseIndividual<GENE, BaseProblemRepresentation> choose(BaseIndividual<GENE, BaseProblemRepresentation> firstCandidate,
                                                                 BaseIndividual<GENE, BaseProblemRepresentation> secondCandidate) {

    if (firstCandidate.getRank() < secondCandidate.getRank()) {
			return firstCandidate;
		}

		else if (firstCandidate.getRank() > secondCandidate.getRank()) {
			return secondCandidate;
		}

		return firstCandidate;
	}

  public int getTournamentSize() {
    return tournamentSize;
  }

  public void setTournamentSize(int tournamentSize) {
    this.tournamentSize = tournamentSize;
  }

}
