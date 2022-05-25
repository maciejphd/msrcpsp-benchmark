package algorithms.evolutionary_algorithms.genetic_algorithm;

import algorithms.evolutionary_algorithms.ParameterSet;
import algorithms.evolutionary_algorithms.genetic_algorithm.utils.Direction;
import algorithms.problem.BaseIndividual;
import algorithms.problem.BaseProblemRepresentation;

import java.util.*;
import java.util.stream.Collectors;

public class ThetaDEA<PROBLEM extends BaseProblemRepresentation> extends NondominatedSortingGA<PROBLEM> {

  private int numObjectives;
  private double theta;

  private List<Direction<PROBLEM>> referenceDirections;

  public ThetaDEA(PROBLEM problem, int populationSize, int generationLimit,
                  ParameterSet<Integer, PROBLEM> parameters, double mutationProbability,
                  double crossoverProbability, double theta) {
    super(problem, populationSize, generationLimit, parameters,
        mutationProbability, crossoverProbability);
    this.numObjectives = parameters.evaluator.getNumObjectives();
    referenceDirections = calculateReferenceDirections(populationSize);
    this.theta = theta;
  }

  public List<BaseIndividual<Integer, PROBLEM>> optimize() {
    int generation = 1;
    List<BaseIndividual<Integer, PROBLEM>> newPopulation;
    List<BaseIndividual<Integer, PROBLEM>> combinedPopulation;
    List<BaseIndividual<Integer, PROBLEM>> allIndividuals = new ArrayList<>();
    Map<Integer, List<BaseIndividual<Integer, PROBLEM>>> fronts;
    Map<Integer, List<BaseIndividual<Integer, PROBLEM>>> thetaFronts;
    List<BaseIndividual<Integer, PROBLEM>> thetaFront;
    BaseIndividual<Integer, PROBLEM> thetaIndividual;
    List<BaseIndividual<Integer, PROBLEM>> targetFront;
    List<Direction<PROBLEM>> nonEmptyDirections;

    population = parameters.initialPopulation.generate(problem, populationSize,
        parameters.evaluator, parameters);
    int rank;
    for (BaseIndividual<Integer, PROBLEM> individual : population) {
      individual.buildSolution(individual.getGenes(), parameters);
    }

    allIndividuals.addAll(population);
    allIndividuals = removeDuplicates(allIndividuals);
    allIndividuals = getNondominated(allIndividuals);

    while (generation < generationLimit) {

      fronts = nondominatedSorting(population);
      for (List<BaseIndividual<Integer, PROBLEM>> front : fronts.values()) {
        crowdingDistance(front); // TODO: remove after random selection
      }
      newPopulation = makeNewPop(population, allIndividuals);

      combinedPopulation = new ArrayList<>();
      combinedPopulation.addAll(population);
      combinedPopulation.addAll(newPopulation);

//      for (int i = 0; i < combinedPopulation.size(); ++i) {
//        final BaseIndividual<Integer, PROBLEM> ind = combinedPopulation.get(i);
//        if (combinedPopulation.stream().filter(in -> in.equalsPhenotype(ind) && in != ind).count() > 0) {
//          combinedPopulation.remove(ind);
//          --i;
//        }
//      }
      fronts = nondominatedSorting(combinedPopulation);
      normalize(combinedPopulation);

      population = new ArrayList<>();
      rank = 1;
      while (population.size() + fronts.get(rank).size() <= populationSize) {
        population.addAll(fronts.get(rank));
        ++rank;
      }
      targetFront = fronts.get(rank);
      createNiches(targetFront);
      nonEmptyDirections = referenceDirections.stream().filter(d -> !d.getSurrounding().isEmpty()).collect(Collectors.toList());
      thetaFronts = thetaSorting(nonEmptyDirections);
      rank = 1;
      while (population.size() + thetaFronts.get(rank).size() <= populationSize) {
        population.addAll(thetaFronts.get(rank));
        ++rank;
      }
      while (population.size() < populationSize) {
        thetaFront = thetaFronts.get(rank);
        thetaIndividual = thetaFront.get(parameters.random.nextInt(thetaFront.size()));
        population.add(thetaIndividual);
        thetaFront.remove(thetaIndividual);
      }

      for (BaseIndividual<Integer, PROBLEM> individual : population) {
        boolean dominated = false;
        boolean clone = false;
        BaseIndividual<Integer, PROBLEM> trial;
        for (int i = 0; i < allIndividuals.size() && !clone && !dominated; ++i) {
          trial = allIndividuals.get(i);
          if (trial.dominates(individual)) {
            dominated = true;
            continue;
          }
          if (trial.equalsPhenotype(individual)) {
            clone = true;
            continue;
          }
          if (individual.dominates(trial)) {
            allIndividuals.remove(trial);
            --i;
          }
        }
        if (!dominated && !clone) {
          allIndividuals.add(individual);
        }
      }

      if (generation < generationLimit * 0.8) {
        allIndividuals = selectMostEffective(allIndividuals);
      }

      ++generation;
    }

    allIndividuals = removeDuplicates(allIndividuals);
    List<BaseIndividual<Integer, PROBLEM>> pareto = getNondominated(allIndividuals);
    pareto.forEach(BaseIndividual::setNormalObjectives);

    return pareto;
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

  protected Map<Integer, List<BaseIndividual<Integer, PROBLEM>>> thetaSorting(
      List<Direction<PROBLEM>> clusters) {
    List<BaseIndividual<Integer, PROBLEM>> currentFront;
    Map<Integer, List<BaseIndividual<Integer, PROBLEM>>> fronts = new HashMap<>();
    BaseIndividual<Integer, PROBLEM> individual;
    Direction<PROBLEM> cluster;

    int rank = 1;
    while (!clusters.isEmpty()) {
      fronts.put(rank, new ArrayList<>());

      for (int i = 0; i < clusters.size(); ++i) {
        cluster = clusters.get(i);
        individual = cluster.getSurrounding().stream().min(Comparator.comparingDouble(BaseIndividual::getDistance)).orElse(null);
        fronts.get(rank).add(individual);
        cluster.removeFromSurrounding(individual);
        if (cluster.getSurrounding().isEmpty()) {
          clusters.remove(cluster);
          --i;
        }
      }

      ++rank;
    }

    return fronts;
  }

  private List<Direction<PROBLEM>> calculateReferenceDirections(int K) {
    List<Direction<PROBLEM>> result = new ArrayList<>();

    for (int j = 0; j < K; ++j) {
      List<Double> objectives = new ArrayList<>();
      double s = 0.0d;
      for (int k = 1; k <= numObjectives; ++k) {
        if (k < numObjectives) {
          double rand = parameters.random.nextDouble();
          double objective = (1.0 - s) * (1 - Math.pow(rand, 1.0 / (numObjectives - k) ));
          objectives.add(objective);
          s += objective;
        } else {
          double objective = 1.0 - s;
          objectives.add(objective);
        }
      }
      result.add(new Direction<>(objectives));
    }

    return result;
  }

  public void createNiches(List<BaseIndividual<Integer, PROBLEM>> population) {
    for (Direction<PROBLEM> d : referenceDirections) {
      d.clear();
    }

    for (BaseIndividual<Integer, PROBLEM> individual : population) {
      double clusteringDistance = Double.MAX_VALUE;
      Direction<PROBLEM> currentDirection = referenceDirections.get(0);
      for (Direction<PROBLEM> d : referenceDirections) {
        double distance = d.getClusteringDistance(individual);
        if (distance < clusteringDistance) {
          clusteringDistance = distance;
          currentDirection = d;
        }
      }
      double convergenceDistance = currentDirection.getConvergenceDistance(individual);
      individual.setDistance(convergenceDistance + theta * clusteringDistance);

      if (currentDirection.getSurrounding().stream().noneMatch(i -> i.equalsPhenotype(individual))) {
        currentDirection.incrementNicheCount();
      }
      currentDirection.addToSurrounding(individual);
    }
  }

  // TODO: extract to a Normalizer class
  private void normalize(List<BaseIndividual<Integer, PROBLEM>> population) {
    double[] mins = new double[numObjectives];
    double[] maxs = new double[numObjectives];
    for (int i = 0; i < numObjectives; ++i) {
      mins[i] = Double.MAX_VALUE;
      maxs[i] = Double.MIN_VALUE;
    }

    for (BaseIndividual<Integer, PROBLEM> individual : population) {
      for (int i = 0; i < numObjectives; ++i) {
        double value = individual.getObjectives()[i];
        if (value < mins[i]) {
          mins[i] = value;
        }
        if (value > maxs[i]) {
          maxs[i] = value;
        }
      }
    }

    for (BaseIndividual<Integer, PROBLEM> individual : population) {
      double[] objectives = individual.getObjectives();
      double[] normalObjectives = new double[numObjectives];
      for (int i = 0; i < numObjectives; ++i) {
        normalObjectives[i] = (objectives[i] - mins[i]) / (maxs[i] - mins[i]);
      }
      individual.setNormalObjectives(normalObjectives);
    }
  }

}
