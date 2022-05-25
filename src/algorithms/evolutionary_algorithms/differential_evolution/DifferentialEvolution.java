package algorithms.evolutionary_algorithms.differential_evolution;

import algorithms.evolutionary_algorithms.EvolutionaryAlgorithm;
import algorithms.evolutionary_algorithms.ParameterSet;
import algorithms.evolutionary_algorithms.util.NondominatedSorter;
import algorithms.problem.BaseIndividual;
import algorithms.problem.scheduling.Schedule;
import algorithms.quality_measure.BaseMeasure;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the differential evolution approach.
 */
public class DifferentialEvolution extends EvolutionaryAlgorithm<Double, Schedule> {

  private List<BaseIndividual<Double, Schedule>> population;

  // mutation parameter
  private double f;
  // crossover parameter
  private double cr;
  // limits max number of generations without improvement
  private int staleLimit;
  // limits max number of clones in population
  private int maxClones;

  private List<BaseMeasure> measures;

  private NondominatedSorter<BaseIndividual<Double, Schedule>> sorter;

  public DifferentialEvolution(Schedule schedule, int populationSize, double f, double cr,
                               int generationLimit, int staleLimit, int maxClones, ParameterSet<Double, Schedule> parameters,
                               List<BaseMeasure> measures) {
    this.problem = schedule;
    this.parameters = parameters;
    this.populationSize = populationSize;
    this.f = f;
    this.cr = cr;
    this.generationLimit = generationLimit;
    this.staleLimit = staleLimit;
    this.maxClones = maxClones;
    this.measures = measures;

    sorter = new NondominatedSorter<>();
  }

  /**
   * Runs single objective version of differential evolution.
   *
   * @return best found individual
   */
  public List<BaseIndividual<Double, Schedule>> optimize() {
    int generation = 0;
    int lastUpdate = 0;
    int numClones = 0;
    BaseIndividual<Double, Schedule> best;
    List<Double> donor;
    BaseIndividual<Double, Schedule> current;
    List<Double> trialGenes;
    BaseIndividual<Double, Schedule> trialIndividual;

    population = parameters.initialPopulation.generate(problem, populationSize, parameters.evaluator, parameters);
    for (BaseIndividual<Double, Schedule> individual : population) {
      buildSchedule(individual);
    }
    best = findBestIndividual(population);

    while ((generation < generationLimit) && (generation - lastUpdate < staleLimit) &&
        (numClones < maxClones)) {

      for (int i = 0; i < populationSize; ++i) {
        // Perform mutation
        donor = parameters.mutation.mutate(population, f, null, i, populationSize, parameters);

        // Perform crossover
        current = population.get(i);
        // there is only one child
        trialGenes = parameters.crossover.crossover(cr, current.getGenes(), donor, parameters).get(0);

        // Create and evaluate trialGenes individual and perform selections
        trialIndividual = new BaseIndividual<>(problem, trialGenes, parameters.evaluator);
        buildSchedule(trialIndividual);
        population.set(i, parameters.selection.select(population, null, i, current, trialIndividual, parameters));

        // Set current best individual
        if (trialIndividual.getEvalValue() < best.getEvalValue()) {
          best = trialIndividual;
          lastUpdate = generation;
        }
        // TODO: correct place?
        numClones = parameters.cloneHandler.handleClones(population);
      }
      ++generation;
    }

    this.problem = best.getProblem();
    List<BaseIndividual<Double, Schedule>> results = new ArrayList<>();
    results.add(best);
    return results;
  }

  /**
   * Builds individual of given individual, by converting
   * genotype to phenotype and using greedy individual builder
   * to place tasks on the timeline.
   *
   * @param individual individual with the individual to build
   * @return individual with built individual
   */
  private BaseIndividual<Double, Schedule> buildSchedule(BaseIndividual<Double, Schedule> individual) {
    Schedule schedule = individual.getProblem();
    parameters.converter.convertToInteger(schedule.getTasks(), individual.getGenes());
    parameters.constraintPreserver.repair(schedule);
    schedule.buildTaskResourceAssignments();
    individual.setProblem(parameters.scheduleBuilder.buildTimestamps(schedule));
    individual.setEvalValue(individual.evaluate());
    individual.setObjectives();
    individual.setNormalObjectives();
    return individual;
  }

  /**
   * Finds an individual with the lowest value of
   * evaluation function
   *
   * @param population population, in which we search
   * @return best individual
   */
  private BaseIndividual<Double, Schedule> findBestIndividual(List<BaseIndividual<Double, Schedule>> population) {
    BaseIndividual<Double, Schedule> best = population.get(0);
    double eval = best.getEvalValue();
    BaseIndividual<Double, Schedule> trial;
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
