package algorithms.evolutionary_algorithms.selection;


import algorithms.evolutionary_algorithms.ParameterSet;
import algorithms.problem.BaseIndividual;
import algorithms.problem.BaseProblemRepresentation;

import java.util.List;

// TODO: should be removed I think, due to existance of <code>NondominatedSortingTournanemt</code>
// or better even, remake it to classic tournament selection
public class TournamentSelection<GENE extends Number> extends BaseSelection<GENE, BaseProblemRepresentation> {

  /**
   * Selects the best individual from 2 random individuals.
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

    BaseIndividual<GENE, BaseProblemRepresentation> parent;
    // TODO: tournament size ?
		parent = choose(
				population.get(parameters.random.nextInt(population.size())),
				population.get(parameters.random.nextInt(population.size()))
    );

		return parent;
	}

	private BaseIndividual<GENE, BaseProblemRepresentation> choose(BaseIndividual<GENE, BaseProblemRepresentation> firstCandidate,
                                                                 BaseIndividual<GENE, BaseProblemRepresentation> secondCandidate) {

		if (firstCandidate.getEvalValue() < secondCandidate.getEvalValue()) {
			return firstCandidate;
		} else {
			return secondCandidate;
		}
	}
}
