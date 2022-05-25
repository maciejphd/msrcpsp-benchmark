package algorithms.evolutionary_algorithms.genetic_algorithm;

import algorithms.evolutionary_algorithms.ParameterSet;
import algorithms.evolutionary_algorithms.crossover.BaseCrossover;
import algorithms.evolutionary_algorithms.crossover.DiversityCrossover;
import algorithms.evolutionary_algorithms.mutation.BaseMutation;
import algorithms.evolutionary_algorithms.mutation.SwapBitMutation;
import algorithms.evolutionary_algorithms.selection.BaseSelection;
import algorithms.evolutionary_algorithms.selection.DiversitySelection;
import algorithms.evolutionary_algorithms.util.NondominatedSorter;
import algorithms.problem.BaseIndividual;
import algorithms.problem.BaseProblemRepresentation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NondominatedTournamentGA<PROBLEM extends BaseProblemRepresentation> extends GeneticAlgorithm<PROBLEM> {

  private NondominatedSorter<BaseIndividual<Integer, PROBLEM>> sorter;
  private boolean enhanceDiversity;
  private double diversityThreshold;


	public NondominatedTournamentGA(PROBLEM problem, int populationSize, int generationLimit,
                                  ParameterSet<Integer, PROBLEM> parameters, double mutationProbability,
                                  double crossoverProbability, double diversityTreshold, boolean enhanceDiversity) {
		super(problem, populationSize, generationLimit, parameters,
        mutationProbability, crossoverProbability);
    sorter = new NondominatedSorter<>();
    this.diversityThreshold = diversityTreshold;
    this.enhanceDiversity = enhanceDiversity;
	}

	public List<BaseIndividual<Integer, PROBLEM>> optimize() {
		int generation = 1;
    int generationDiversityThreshold = (int)(this.populationSize * diversityThreshold);
		BaseIndividual<Integer, PROBLEM> best;
		List<BaseIndividual<Integer, PROBLEM>> newPopulation;
		List<BaseIndividual<Integer, PROBLEM>> combinedPopulations = new ArrayList<>();

		BaseIndividual<Integer, PROBLEM> firstParent;
		BaseIndividual<Integer, PROBLEM> secondParent;
		BaseIndividual<Integer, PROBLEM> firstChild;
		BaseIndividual<Integer, PROBLEM> secondChild;
		List<List<Integer>> children;

    BaseSelection<Integer, PROBLEM> selection;
    selection = new DiversitySelection();
    ((DiversitySelection<Integer>) selection).setTournamentSize(20);
    BaseCrossover<Integer, PROBLEM> crossover;
    crossover = new DiversityCrossover();
    BaseMutation mutation;
    mutation = new SwapBitMutation();

		population = parameters.initialPopulation.generate(problem, populationSize, parameters.evaluator, parameters);
		for (BaseIndividual<Integer, PROBLEM> individual : population) {
      individual.buildSolution(individual.getGenes(), parameters);
		}
		sorter.nondominatedSorting(population);
		sorter.crowdingDistance(population);
		best = findBestIndividual(population);
    combinedPopulations.addAll(population);
    combinedPopulations = removeDuplicates(combinedPopulations);
    combinedPopulations = getNondominated(combinedPopulations);

//    ConvergenceMeasure ed = new ConvergenceMeasure(
//        parameters.evaluator.getPerfectPoint());
//    HyperVolume hv = new HyperVolume(parameters.evaluator.getNadirPoint());
//    ONVG pfs = new ONVG();
//
//    System.out.println(ed.getMeasure(combinedPopulations) + ";" + hv.getMeasure(combinedPopulations) + ";" + pfs.getMeasure(combinedPopulations));
//    for (BaseIndividual i : combinedPopulations) {
//      System.out.println(i.getObjectives()[0] + ";" + i.getObjectives()[1]);
//    }
//    System.out.println();

		while (generation < generationLimit) {
			newPopulation = new ArrayList<>();
      sorter.nondominatedSorting(population);
//      crowdingDistance(population);

			while (newPopulation.size() < populationSize) {

        if (!enhanceDiversity || /*newPopulation.size() < generationDiversityThreshold*/ generation % 100 < 50) {
          firstParent = parameters.selection.select(population, combinedPopulations, newPopulation.size(), null, null, parameters);
          secondParent = parameters.selection.select(population, combinedPopulations, newPopulation.size(), firstParent, null, parameters);
          children = parameters.crossover.crossover(crossoverProbability, firstParent.getGenes(), secondParent.getGenes(), parameters);
          children.set(0, parameters.mutation.mutate(population, mutationProbability, children.get(0), 0, populationSize, parameters));
          children.set(1, parameters.mutation.mutate(population, mutationProbability, children.get(1), 0, populationSize, parameters));
        } else {
          combinedPopulations = gapDistanceRevised(combinedPopulations);
//          crowdingDistance(combinedPopulations);

//          java.util.Collections.sort(combinedPopulations, new java.util.Comparator<BaseIndividual<Integer, PROBLEM>>() {
//            public int compare(BaseIndividual<Integer, PROBLEM> o1, BaseIndividual<Integer, PROBLEM> o2) {
//              return Double.compare(o2.getDistance(), o1.getDistance()); // descending
//            }
//          });
          firstParent = selection.select(population, combinedPopulations, newPopulation.size(), null, null, parameters);
//          secondParent = selection.select(population, combinedPopulations, newPopulation.size(), firstParent, null, parameters);
          int index = combinedPopulations.indexOf(firstParent);
          int random = parameters.random.nextInt() % 2;
          if (random == 0) {
            index = index - 1;
          } else {
            index = index + 1;
          }
          if (index < 0 || index > combinedPopulations.size() - 1) {
            secondParent = selection.select(population, combinedPopulations, newPopulation.size(), null, null, parameters);
          } else {
            secondParent = combinedPopulations.get(index);
          }
          children = parameters.crossover.crossover(crossoverProbability, firstParent.getGenes(), secondParent.getGenes(), parameters);
          // TODO: mutation type and mutation probability (0.1 vs 0.005)
          children.set(0, parameters.mutation.mutate(population, 0.005, children.get(0), 0, populationSize, parameters));
          children.set(1, parameters.mutation.mutate(population, 0.005, children.get(1), 0, populationSize, parameters));
        }


				firstChild = new BaseIndividual<>(problem, children.get(0), parameters.evaluator);
				secondChild = new BaseIndividual<>(problem, children.get(1), parameters.evaluator);


				// TODO: refactor after I get well :(
        for (int i = 0; newPopulation.contains(firstChild) && i < 20; i++) {
          firstChild.setGenes(parameters.mutation.mutate(population, 1.0, firstChild.getGenes(), 0, populationSize, parameters));
        }
        if (!newPopulation.contains(firstChild)) {
          firstChild.buildSolution(firstChild.getGenes(), parameters);
          newPopulation.add(firstChild);
        }
        for (int i = 0; newPopulation.contains(secondChild) && i < 20; i++) {
          secondChild.setGenes(parameters.mutation.mutate(population, 1.0, secondChild.getGenes(), 0, populationSize, parameters));
        }
        if (!newPopulation.contains(secondChild)) {
          secondChild.buildSolution(secondChild.getGenes(), parameters);
          newPopulation.add(secondChild);
        }

//        buildSchedule(firstChild);
//        buildSchedule(secondChild);
//        newPopulation.add(firstChild);
//        newPopulation.add(secondChild);

        if (firstChild.getEvalValue() < best.getEvalValue()) {
          best = firstChild;
        }
        if (secondChild.getEvalValue() < best.getEvalValue()) {
          best = secondChild;
        }

			}
			population = newPopulation;
//      long start = System.nanoTime();
//      combinedPopulations.addAll(population);
//			combinedPopulations = removeDuplicates(combinedPopulations);
//			combinedPopulations = getNondominated(combinedPopulations);

      for (BaseIndividual<Integer, PROBLEM> individual : population) {
        boolean dominated = false;
        boolean clone = false;
        BaseIndividual<Integer, PROBLEM> trial;
        for (int i = 0; i < combinedPopulations.size() && !clone && !dominated; ++i) {
          trial = combinedPopulations.get(i);
          if (trial.dominates(individual)) {
            dominated = true;
            continue;
          }
          if (trial.equalsPhenotype(individual)) {
            clone = true;
            continue;
          }
          if (individual.dominates(trial)) {
            combinedPopulations.remove(trial);
            --i;
          }
        }
        if (!dominated && !clone) {
          combinedPopulations.add(individual);
        }
      }


//			long end = System.nanoTime();
//      System.out.println(generation + " " + (end - start));
			if (generation < generationLimit * 0.8) {
        combinedPopulations = selectMostEffective(combinedPopulations);
      }

//      System.out.println(ed.getMeasure(combinedPopulations) + ";" + hv.getMeasure(combinedPopulations) + ";" + pfs.getMeasure(combinedPopulations));
//			for (BaseIndividual i : combinedPopulations) {
//        System.out.println(i.getObjectives()[0] + ";" + i.getObjectives()[1] + ";" + i.getObjectives()[2]);
//      }
//      System.out.println();

			// TODO: refactor
//      int numGenes = population.get(0).getGenes().size();
//      List<Integer> genes = new ArrayList<>(Collections.nCopies(numGenes, 0));
//      for (int i = 0; i < population.size(); ++i) {
//        for (int j = i+1; j < population.size(); ++j) {
//          if (isClone(population.get(i).getSchedule().getTasks(),
//              population.get(j).getSchedule().getTasks())) {
//            for (int k = 0; k < numGenes; ++k) {
//              genes.set(k, parameters.random.nextInt(parameters.upperBounds[k]));
//            }
//            population.get(j).setGenes(genes);
//          }
//        }
//      }

			++generation;
		}

		combinedPopulations = removeDuplicates(combinedPopulations);
		List<BaseIndividual<Integer, PROBLEM>> pareto = getNondominated(combinedPopulations);

		this.problem = best.getProblem();

		return pareto;
	}

  // TODO: move to parent
  private List<BaseIndividual<Integer, PROBLEM>> selectMostEffective(List<BaseIndividual<Integer, PROBLEM>> population) {
    int size = 2000;

    BaseIndividual<Integer, PROBLEM> worst = population.get(0);
    while (population.size() > size) {
      gapDistance(population);
      double worstDistance = Double.POSITIVE_INFINITY;
      for (BaseIndividual<Integer, PROBLEM> ind : population) {
        if (ind.getDistance() < worstDistance) {
          worstDistance = ind.getDistance();
          worst = ind;
        }
      }
      population.remove(worst);

    }
    return population;
  }

  protected void gapDistance(List<BaseIndividual<Integer, PROBLEM>> front) {

    if (front.isEmpty()) {
      return;
    }
    int l = front.size();
    double currentValue;
    double distance;

    for (BaseIndividual<Integer, PROBLEM> individual : front) {
      individual.setDistance(Double.MAX_VALUE);
    }

    for (int objective = 0; objective < numObjectives; ++objective) {
      final int obj = objective;
      front = front.stream().sorted(((o1, o2) -> o1.compareTo(o2, obj))).collect(Collectors.toList());

      front.get(0).setDistance(Double.POSITIVE_INFINITY);
      front.get(l - 1).setDistance(Double.POSITIVE_INFINITY);

      for (int i = 1; i < l - 1; i++) {
        currentValue = front.get(i).getNormalObjectives()[objective];
        if (front.get(i).getDistance() == Double.POSITIVE_INFINITY) {
          continue;
        }
        distance = Double.max(front.get(i + 1).getNormalObjectives()[objective] - currentValue,
                              currentValue - front.get(i - 1).getNormalObjectives()[objective]);
        if (distance < front.get(i).getDistance()) {
          front.get(i).setDistance(distance);
        }
      }
    }
  }

  // TODO: check and reconcile
  protected List<BaseIndividual<Integer, PROBLEM>> gapDistanceRevised(List<BaseIndividual<Integer, PROBLEM>> front) {

    if (front.isEmpty()) {
      return front;
    }
    int l = front.size();

    for (BaseIndividual<Integer, PROBLEM> individual : front) {
      individual.setDistance(0.0);
    }

    int objective = parameters.random.nextInt(numObjectives);
    final int obj = objective;
    front = front.stream().sorted(((o1, o2) -> o1.compareTo(o2, obj))).collect(Collectors.toList());

    front.get(0).setDistance(Double.POSITIVE_INFINITY);
    front.get(l - 1).setDistance(Double.POSITIVE_INFINITY);

//    for (int i = 1; i < l - 1; i++) {
//      front.get(i).setDistance(
//          front.get(i).getDistance()
//              + front.get(i + 1).getNormalObjectives()[objective]
//              - front.get(i - 1).getNormalObjectives()[objective]);
//    }
    for (int i = 1; i < l - 1; i++) {
      double value = front.get(i).getNormalObjectives()[objective];
      front.get(i).setDistance(Double.max(value - front.get(i - 1).getNormalObjectives()[objective],
                                          front.get(i + 1).getNormalObjectives()[objective] - value));
    }
    return front;
  }

	protected BaseIndividual<Integer, PROBLEM> findBestIndividual(
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
