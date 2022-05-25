package algorithms.evolutionary_algorithms.genetic_algorithm;


import algorithms.evolutionary_algorithms.ParameterSet;
import algorithms.evolutionary_algorithms.initial_population.BaseInitialPopulation;
import algorithms.problem.BaseIndividual;
import algorithms.problem.BaseProblemRepresentation;

import java.util.*;

// TODO: refactor completely, it's copied from another project
public class NondominatedSortingGA<PROBLEM extends BaseProblemRepresentation> extends GeneticAlgorithm<PROBLEM> {

  public NondominatedSortingGA(PROBLEM problem, int populationSize, int generationLimit,
                               ParameterSet<Integer, PROBLEM> parameters,
                               double mutationProbability, double crossoverProbability) {
    super(problem, populationSize, generationLimit, parameters,
        mutationProbability, crossoverProbability);
  }

  public List<BaseIndividual<Integer, PROBLEM>> optimize() {
    int generation = 1;
    List<BaseIndividual<Integer, PROBLEM>> newPopulation;
    List<BaseIndividual<Integer, PROBLEM>> combinedPopulation;
    List<BaseIndividual<Integer, PROBLEM>> allIndividuals = new ArrayList<>();

    Comparator<BaseIndividual<Integer, PROBLEM>> c = new Comparator<BaseIndividual<Integer, PROBLEM>>() {
      public int compare(BaseIndividual<Integer, PROBLEM> o1, BaseIndividual<Integer, PROBLEM> o2) {
        if (o1.getRank() == o2.getRank()) {
          if (o1.getDistance() == o2.getDistance())
            return 0;
          return o1.getDistance() > o2.getDistance() ? -1 : 1;
        }
        return o1.getRank() < o2.getRank() ? -1 : 1;
      }
    };

    population = parameters.initialPopulation.generate(problem, populationSize,
        parameters.evaluator, parameters);
    for (BaseIndividual<Integer, PROBLEM> individual : population) {
      individual.buildSolution(individual.getGenes(), parameters);
    }
    nondominatedSorting(population);
    allIndividuals.addAll(population);
    newPopulation = makeNewPop(population, allIndividuals);

    allIndividuals.addAll(newPopulation);
    allIndividuals = removeDuplicates(allIndividuals);
    allIndividuals = getNondominated(allIndividuals);

//    ConvergenceMeasure ed = new ConvergenceMeasure(
//        parameters.evaluator.getPerfectPoint());
//    HyperVolume hv = new HyperVolume(parameters.evaluator.getNadirPoint());
//    ONVG pfs = new ONVG();

//    System.out.println(ed.getMeasure(allIndividuals) + ";" + hv.getMeasure(allIndividuals) + ";" + pfs.getMeasure(allIndividuals));
//    for (BaseIndividual i : allIndividuals) {
//      System.out.println(i.getObjectives()[0] + ";" + i.getObjectives()[1]);
//    }
//    System.out.println();

    while (generation < generationLimit - 1) {

      // R_t = P_t u Q_t
      combinedPopulation = new ArrayList<>();
      combinedPopulation.addAll(population);
      combinedPopulation.addAll(newPopulation);

      // F = fast-nondominated-sort(R_t)
      Map<Integer, List<BaseIndividual<Integer, PROBLEM>>> fronts = nondominatedSorting(combinedPopulation);
      population = new ArrayList<>();
      newPopulation = new ArrayList<>();

      // until |P_t+1| < N
      for (int i = 1; population.size() < populationSize; ++i) {
        // crowding-distance-assignment(F_i)
        crowdingDistance(fronts.get(i));
        // P_t+1 = P_t+1 u F_i
        population.addAll(fronts.get(i));
      }

      // Sort(P_t+1, >=_n)
      Collections.sort(population, c);

      // P_t+1 = P_t+1[0:N]
      population = population.subList(0, populationSize);

      // Q_t+1 = make-new-pop(P_t+1)
      newPopulation = makeNewPop(population, allIndividuals);
      allIndividuals.addAll(newPopulation);
      allIndividuals = removeDuplicates(allIndividuals);
      allIndividuals = getNondominated(allIndividuals);
      if (generation < generationLimit * 0.8) {
        allIndividuals = selectMostEffective(allIndividuals);
      }

//      System.out.println(ed.getMeasure(allIndividuals) + ";" + hv.getMeasure(allIndividuals) + ";" + pfs.getMeasure(allIndividuals));
//      for (BaseIndividual i : allIndividuals) {
//        System.out.println(i.getObjectives()[0] + ";" + i.getObjectives()[1] + ";" + i.getObjectives()[2]);
//      }
//      System.out.println();

      ++generation;
    }

    allIndividuals = removeDuplicates(allIndividuals);
    List<BaseIndividual<Integer, PROBLEM>> pareto = getNondominated(allIndividuals);

    return pareto;
  }

  protected List<BaseIndividual<Integer, PROBLEM>> makeNewPop(
      List<BaseIndividual<Integer, PROBLEM>> population,
      List<BaseIndividual<Integer, PROBLEM>> nonDominated) {
    List<BaseIndividual<Integer, PROBLEM>> newPopulation = new ArrayList<>();
    BaseIndividual<Integer, PROBLEM> firstParent;
    BaseIndividual<Integer, PROBLEM> secondParent;
    BaseIndividual<Integer, PROBLEM> firstChild;
    BaseIndividual<Integer, PROBLEM> secondChild;
    List<List<Integer>> children;
    while (newPopulation.size() < populationSize) {

      firstParent = parameters.selection.select(population, nonDominated, 0, null, null, parameters);
      secondParent = parameters.selection.select(population, nonDominated, 0, firstParent, null, parameters);

      children = parameters.crossover.crossover(crossoverProbability, firstParent.getGenes(), secondParent.getGenes(), parameters);
      children.set(0, parameters.mutation.mutate(population, mutationProbability, children.get(0), 0, populationSize, parameters));
      children.set(1, parameters.mutation.mutate(population, mutationProbability, children.get(1), 0, populationSize, parameters));

      firstChild = new BaseIndividual<>(problem, children.get(0), parameters.evaluator);
      secondChild = new BaseIndividual<>(problem, children.get(1), parameters.evaluator);

      // TODO: refactor after I get well :(
//      for (int i = 0; newPopulation.contains(firstChild) && i < 20; i++) {
//        firstChild.setGenes(parameters.mutation.mutate(population, 1.0, firstChild.getGenes(), 0, populationSize, parameters));
//      }
//      if (!newPopulation.contains(firstChild)) {
//        firstChild.buildSolution(firstChild.getGenes(), parameters);
//        newPopulation.add(firstChild);
//      }
//      for (int i = 0; newPopulation.contains(secondChild) && i < 20; i++) {
//        secondChild.setGenes(parameters.mutation.mutate(population, 1.0, secondChild.getGenes(), 0, populationSize, parameters));
//      }
//      if (!newPopulation.contains(secondChild)) {
//        secondChild.buildSolution(secondChild.getGenes(), parameters);
//        newPopulation.add(secondChild);
//      }

      firstChild.buildSolution(firstChild.getGenes(), parameters);
      secondChild.buildSolution(secondChild.getGenes(), parameters);

      newPopulation.add(firstChild);
      newPopulation.add(secondChild);

    }

    return newPopulation;
  }

  // TODO: move to parent
  private List<BaseIndividual<Integer, PROBLEM>> selectMostEffective(List<BaseIndividual<Integer, PROBLEM>> population) {
    int size = 2000;

    BaseIndividual<Integer, PROBLEM> worst = population.get(0);
    while (population.size() > size) {
      crowdingDistance(population);
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

  protected List<BaseIndividual<Integer, PROBLEM>> removeDuplicates(
      List<BaseIndividual<Integer, PROBLEM>> allIndividuals) {
    List<BaseIndividual<Integer, PROBLEM>> allIndividualsNoDuplicates = new ArrayList<>();
    for (BaseIndividual<Integer, PROBLEM> ind : allIndividuals) {
      if (allIndividualsNoDuplicates.stream().noneMatch(i ->
          i.getObjectives()[0] == ind.getObjectives()[0] &&
              i.getObjectives()[1] == ind.getObjectives()[1])
      ) {
        allIndividualsNoDuplicates.add(ind);
      }
    }
    return allIndividualsNoDuplicates;
  }

  public BaseIndividual getNadirPoint(List<BaseIndividual<Integer, PROBLEM>> paretoAll) {
    BaseIndividual nadirPoint = new BaseIndividual<>(problem, parameters.evaluator);
    double maxCost = -1.0;
    double maxDur = -1.0;

    for (BaseIndividual<Integer, PROBLEM> baseIntIndividual : paretoAll) {
      if (baseIntIndividual.getObjectives()[1] > maxCost) {
        maxCost = baseIntIndividual.getObjectives()[1];
      }
      if (baseIntIndividual.getObjectives()[0] > maxDur) {
        maxDur = baseIntIndividual.getObjectives()[0];
      }
    }

    double[] objectives = new double[2];
    objectives[0] = maxDur;
    objectives[1] = maxCost;
    nadirPoint.setObjectives(objectives);

    objectives = new double[2];
    objectives[0] = 1.0;
    objectives[1] = 1.0;
    nadirPoint.setNormalObjectives(objectives);

    return nadirPoint;
  }

  public BaseIndividual getPerfectPoint(List<BaseIndividual<Integer, PROBLEM>> paretoAll) {
    BaseIndividual nadirPoint = new BaseIndividual<>(problem, parameters.evaluator);
    double minCost = Double.POSITIVE_INFINITY;
    double minDur = Double.POSITIVE_INFINITY;

    for (BaseIndividual<Integer, PROBLEM> baseIntIndividual : paretoAll) {
      if (baseIntIndividual.getObjectives()[1] < minCost) {
        minCost = baseIntIndividual.getObjectives()[1];
      }
      if (baseIntIndividual.getObjectives()[0] < minDur) {
        minDur = baseIntIndividual.getObjectives()[0];
      }
    }

    double[] objectives = new double[2];
    objectives[0] = minDur;
    objectives[1] = minCost;
    nadirPoint.setObjectives(objectives);

    objectives = new double[2];
    objectives[0] = 0.0;
    objectives[1] = 0.0;
    nadirPoint.setNormalObjectives(objectives);

    return nadirPoint;
  }

}
