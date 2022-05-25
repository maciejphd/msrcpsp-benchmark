package algorithms.evolutionary_algorithms;

import algorithms.evaluation.BaseEvaluator;
import algorithms.evolutionary_algorithms.converters.BaseConverter;
import algorithms.evolutionary_algorithms.crossover.BaseCrossover;
import algorithms.evolutionary_algorithms.differential_evolution.clone_handlers.BaseCloneHandler;
import algorithms.evolutionary_algorithms.differential_evolution.constraint_preservers.BaseConstraintPreserver;
import algorithms.evolutionary_algorithms.initial_population.BaseInitialPopulation;
import algorithms.evolutionary_algorithms.migration_scheme.BaseMigration;
import algorithms.evolutionary_algorithms.mutation.BaseMutation;
import algorithms.evolutionary_algorithms.selection.BaseSelection;
import algorithms.problem.BaseProblemRepresentation;
import algorithms.problem.scheduling.schedule_builders.ScheduleBuilder;
import util.random.RandomBase;

/**
 * A helper class containing a set of parameters required
 * for evolutionary algorithms.
 */
public class ParameterSet<GENE extends Number, PROBLEM extends BaseProblemRepresentation> {

  // numbers of capable resources for each task
  // defines upper bound of each gene
  public int[] upperBounds;
  // number of swap in naive swap initial population
  public double numSwaps;
  // max time of creating individual in diversity initial population
  public long swapsTime;
  // size of the tournament selection
  public int tournamentSize;
  // true if n'th individual has successors
  public boolean[] hasSuccesors;
  // number of times to apply difference of vectors in DE mutations
  public int mutationRank;
  // weight of a criteria used in weighted evaluators
  public double evalRate;
  // number of individuals to migrate in island based approaches
  public int migrationSize;
  // number of generations between migrations
  public int migrationInterval;
  // factor to multiply the initial size of the population
  public int populationMultiplicationFactor;
  // used in compound problems, point where genotype divides the problem into two representations
  public int geneSplitPoint;
  // number of the neighbour solutions to consider, used in local search
  public int neighbourhoodSize;

  // Random used for various purposes, i.e. mutation
  public RandomBase<GENE> random;

  public BaseEvaluator<GENE, PROBLEM> evaluator;
  public BaseCloneHandler<PROBLEM> cloneHandler;
  public BaseConstraintPreserver constraintPreserver;
  public BaseConverter converter;
  public BaseCrossover<GENE, PROBLEM> crossover;
  public BaseMutation<GENE, PROBLEM> mutation;
  public BaseInitialPopulation<GENE, PROBLEM> initialPopulation;
  public BaseSelection<GENE, PROBLEM> selection;
  public BaseMigration<GENE, PROBLEM> migration;
  public ScheduleBuilder scheduleBuilder;

}
