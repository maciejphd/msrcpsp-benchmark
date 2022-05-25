package algorithms.evolutionary_algorithms.genetic_algorithm;

import algorithms.evolutionary_algorithms.EvolutionaryAlgorithm;
import algorithms.evolutionary_algorithms.ParameterSet;
import algorithms.problem.BaseIndividual;
import algorithms.problem.BaseProblemRepresentation;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of the genetic algorithm.
 */
public class GeneticAlgorithm<PROBLEM extends BaseProblemRepresentation> extends EvolutionaryAlgorithm<Integer, PROBLEM> {

	protected List<BaseIndividual<Integer, PROBLEM>> population;
  protected double crossoverProbability;
	protected double mutationProbability;
	protected int numObjectives;

	public GeneticAlgorithm(PROBLEM problem, int populationSize, int generationLimit, ParameterSet<Integer, PROBLEM> parameters,
													double mutationProbability, double crossoverProbability) {
		this.problem = problem;
    this.parameters = parameters;
		this.populationSize = populationSize;
		this.generationLimit = generationLimit;
		this.mutationProbability = mutationProbability;
    this.crossoverProbability = crossoverProbability;
    this.numObjectives = parameters.evaluator.getNumObjectives();
	}

	public List<BaseIndividual<Integer, PROBLEM>> optimize() {
		int generation = 0;
		BaseIndividual<Integer, PROBLEM> best;
		List<BaseIndividual<Integer, PROBLEM>> newPopulation;
		BaseIndividual<Integer, PROBLEM> firstParent;
		BaseIndividual<Integer, PROBLEM> secondParent;
		BaseIndividual<Integer, PROBLEM> firstChild;
		BaseIndividual<Integer, PROBLEM> secondChild;
		List<List<Integer>> children;

		population = parameters.initialPopulation.generate(problem, populationSize, parameters.evaluator, parameters);
		for (BaseIndividual<Integer, PROBLEM> individual : population) {
      individual.buildSolution(individual.getGenes(), parameters);
		}
		best = findBestIndividual(population);

		while (generation < generationLimit) {
			newPopulation = new ArrayList<>();
			for (int i = 0; i < populationSize / 2; ++i) { // 2 children every iteration

				firstParent = parameters.selection.select(population, null, i, null, null, parameters);
				secondParent = parameters.selection.select(population, null, i, null, null, parameters);

        children = parameters.crossover.crossover(crossoverProbability, firstParent.getGenes(), secondParent.getGenes(), parameters);

        children.set(0, parameters.mutation.mutate(population, mutationProbability, children.get(0), i, populationSize, parameters));
        children.set(1, parameters.mutation.mutate(population, mutationProbability, children.get(1), i, populationSize, parameters));

				firstChild = new BaseIndividual<>(problem, children.get(0), parameters.evaluator);
				secondChild = new BaseIndividual<>(problem, children.get(1), parameters.evaluator);

				firstChild.buildSolution(firstChild.getGenes(), parameters);
				secondChild.buildSolution(secondChild.getGenes(), parameters);
				newPopulation.add(firstChild);
				newPopulation.add(secondChild);

				if (firstChild.getEvalValue() < best.getEvalValue()) {
					best = firstChild;
				}
				if (secondChild.getEvalValue() < best.getEvalValue()) {
					best = secondChild;
				}

			}
			population.add(best);
			population = newPopulation;
			++generation;
		}

		this.problem = best.getProblem();
		List<BaseIndividual<Integer, PROBLEM>> results = new ArrayList<>();
		results.add(best);
		return results;
	}

	protected Map<Integer, List<BaseIndividual<Integer, PROBLEM>>> nondominatedSorting(
			List<BaseIndividual<Integer, PROBLEM>> population) {
		for (BaseIndividual<Integer, PROBLEM> individual : population) {
			individual.setNumOfDominatingSolutions(0);
			individual.getDominatedSolutions().clear();
			individual.setRank(-1);
		}

		Map<Integer, List<BaseIndividual<Integer, PROBLEM>>> fronts = new HashMap<>();
		int rank = 1;
		fronts.put(rank, new ArrayList<>());

		for (BaseIndividual<Integer, PROBLEM> p : population) {
			for (BaseIndividual<Integer, PROBLEM> q : population) {
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
				fronts.get(rank).add(p);
			}
		}

		List<BaseIndividual<Integer, PROBLEM>> currentFront;
		currentFront = fronts.get(1);

		rank = 2;
		while (!currentFront.isEmpty()) {
			fronts.put(rank, new ArrayList<>());
			for (BaseIndividual<Integer, PROBLEM> individual : currentFront) {
				for (BaseIndividual<Integer, PROBLEM> dominated : individual
						.getDominatedSolutions()) {
					dominated.setNumOfDominatingSolutions(dominated
							.getNumOfDominatingSolutions() - 1);
					if (dominated.getNumOfDominatingSolutions() == 0) {
						fronts.get(rank).add(dominated);
					}
				}
			}
			currentFront = fronts.get(rank);
			rank++;
		}
		fronts.remove(fronts.size());
		for (rank = 1; rank <= fronts.size(); rank++) {
			for (BaseIndividual<Integer, PROBLEM> individual : fronts.get(rank)) {
				individual.setRank(rank);
			}
		}
		return fronts;
	}

	protected void crowdingDistance(List<BaseIndividual<Integer, PROBLEM>> front) {

		if (front.isEmpty()) {
			return;
		}
		int l = front.size();

		for (BaseIndividual<Integer, PROBLEM> individual : front) {
			individual.setDistance(0.0);
		}

		for (int objective = 0; objective < numObjectives; ++objective) {
			final int obj = objective;
			front = front.stream().sorted(((o1, o2) -> o1.compareTo(o2, obj))).collect(Collectors.toList());

			front.get(0).setDistance(Double.POSITIVE_INFINITY);
			front.get(l - 1).setDistance(Double.POSITIVE_INFINITY);

			for (int i = 1; i < l - 1; i++) {
				front.get(i).setDistance(
						front.get(i).getDistance()
								+ front.get(i + 1).getNormalObjectives()[objective]
								- front.get(i - 1).getNormalObjectives()[objective]);
			}
		}
	}

	/**
	 * Finds an individual with the lowest value of
	 * evaluation function
	 *
	 * @param population
	 *            population, in which we search
	 * @return best individual
	 */
  // TODO: move to more generic spot
	private BaseIndividual<Integer, PROBLEM> findBestIndividual(
			List<BaseIndividual<Integer, PROBLEM>> population) {
    BaseIndividual<Integer, PROBLEM> best = population.get(0);
    double eval = best.getEvalValue();
    BaseIndividual<Integer, PROBLEM> trial;
    for (int i = 1; i < population.size(); ++i) {
      trial = population.get(i);
      if (trial.getEvalValue() < eval) {
        best = trial;
        eval = trial.getEvalValue();
      }
    }

    return best;
  }

}
