package algorithms.evolutionary_algorithms.selection;

import algorithms.evolutionary_algorithms.ParameterSet;
import algorithms.problem.BaseIndividual;
import algorithms.problem.BaseProblemRepresentation;

import java.util.List;

public class NondominatedSortingRoulette<GENE extends Number> extends BaseSelection<GENE, BaseProblemRepresentation> {

	/**
	 * Selects two individuals from the population with
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
																																BaseIndividual<GENE, BaseProblemRepresentation> current, BaseIndividual<GENE, BaseProblemRepresentation> trial,
																																ParameterSet<GENE, BaseProblemRepresentation> parameters) {

		BaseIndividual<GENE, BaseProblemRepresentation> parent;

		// TODO: refactor, only needed once per selection
		evaluateDomination(population);

		double denominator = 1.0;
		double worstRank = -1;
		for (BaseIndividual<GENE, BaseProblemRepresentation> individual : population) {
			denominator += individual.getRank();
			if (individual.getRank() > worstRank) {
				worstRank = individual.getRank();
			}
		}
		double sum = 0.0;

		for (BaseIndividual<GENE, BaseProblemRepresentation> individual : population) {

			double fitVal = ((double) 1 + worstRank - individual.getRank())
					/ denominator;
			individual.setFitnessValue(fitVal);
			sum += fitVal;
		}

		double value = parameters.random.nextDouble() * sum;
		parent = chooseParent(population, value);

		return parent;
	}

	private void evaluateDomination(List<BaseIndividual<GENE, BaseProblemRepresentation>> population) {

		int rank;
		for (BaseIndividual<GENE, BaseProblemRepresentation> p : population) {
			for (BaseIndividual<GENE, BaseProblemRepresentation> q : population) {
				if (!p.equals(q)) {
					if (p.dominates(q)) {
						p.getDominatedSolutions().add(q);
					} else if (q.dominates(p)) {
						p.setNumOfDominatingSolutions(p
								.getNumOfDominatingSolutions() + 1);
					}
				}
			}
			if (p.getNumOfDominatingSolutions() == 0) {
				p.setRank(1);
			}
		}
		boolean loop = true;
		rank = 1;
		while (loop) {
			loop = false;
			for (BaseIndividual<GENE, BaseProblemRepresentation> individual : population) {
				if (individual.getRank() == rank) {
					for (BaseIndividual dominated : individual
							.getDominatedSolutions()) {
						dominated.setNumOfDominatingSolutions(dominated
								.getNumOfDominatingSolutions() - 1);
						if (dominated.getNumOfDominatingSolutions() == 0) {
							dominated.setRank(rank + 1);
							loop = true;
						}
					}
				}
			}
			rank++;
		}

	}

	private BaseIndividual<GENE, BaseProblemRepresentation> chooseParent(List<BaseIndividual<GENE, BaseProblemRepresentation>> population, double value) {

		double currentSum = 0.0;
		for (BaseIndividual<GENE, BaseProblemRepresentation> individual : population) {
			currentSum += individual.getFitnessValue();
			if (currentSum > value) {
				return individual;
			}
		}
		return null;
	}
}
