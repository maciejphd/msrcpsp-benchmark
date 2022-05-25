package runners.msrcpsp;

import algorithms.evaluation.EvaluatorType;
import algorithms.evolutionary_algorithms.ParameterSet;
import algorithms.evolutionary_algorithms.converters.ConverterType;
import algorithms.evolutionary_algorithms.crossover.CrossoverType;
import algorithms.evolutionary_algorithms.differential_evolution.DifferentialEvolution;
import algorithms.evolutionary_algorithms.differential_evolution.clone_handlers.CloneHandlerType;
import algorithms.evolutionary_algorithms.differential_evolution.constraint_preservers.ConstraintPreserverType;
import algorithms.evolutionary_algorithms.initial_population.InitialPopulationType;
import algorithms.evolutionary_algorithms.mutation.MutationType;
import algorithms.evolutionary_algorithms.selection.SelectionType;
import algorithms.factories.*;
import algorithms.io.MSRCPSPIO;
import algorithms.problem.BaseIndividual;
import algorithms.problem.scheduling.Schedule;
import algorithms.problem.scheduling.schedule_builders.ScheduleBuilderType;
import util.random.RandomDouble;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DEGRRunner {

  private static final Logger LOGGER = Logger.getLogger(DEGRRunner.class.getName());
  private static String definitionFile = "assets/definitions/MSRCPSP/100_10_26_15.def";

  public static void main(String[] args) {
    run();
  }

  private static List<BaseIndividual<Double, Schedule>> run() {
    ParameterSet<Double, Schedule> parameters = new ParameterSet<>();
    parameters.random = new RandomDouble(System.currentTimeMillis());
    parameters.populationMultiplicationFactor = 1;
    parameters.evalRate = 1.0;
    int populationSize = 500;
    int generationLimit = 10000;

    parameters.initialPopulation = new InitialPopulationGeneratorFactory(parameters).createInitialPopulation(InitialPopulationType.RANDOM);
    parameters.selection = new SelectionFactory(parameters).createSelection(SelectionType.ONE_TO_ONE);
    parameters.crossover = new CrossoverFactory().createCrossover(CrossoverType.BINOMIAL);
    parameters.mutation = new MutationFactory(parameters).createMutation(MutationType.RANDOM_VECTOR);
    parameters.converter = new ConverterFactory(parameters).createConverter(ConverterType.TRUNCATING);
    parameters.cloneHandler = new CloneHandlerFactory(parameters).createCloneHandler(CloneHandlerType.ALLOW);
    parameters.evaluator = new EvaluatorFactory().createEvaluator(EvaluatorType.WEIGHTED_EVALUATOR, parameters.evalRate);

    File assets = new File("assets/definitions/MSRCPSP");
    File[] files = assets.listFiles();
    Arrays.sort(files);
    for (File file : files) {

      MSRCPSPIO reader = new MSRCPSPIO();
      Schedule schedule = reader.readDefinition(file.getPath());
      if (null == schedule) {
        LOGGER.log(Level.WARNING, "Could not read the Definition "
            + file);
        return null;
      }
      parameters.upperBounds = schedule.getUpperBounds();
      parameters.hasSuccesors = schedule.getSuccesors();
      parameters.evaluator.setIndividual(new BaseIndividual<>(schedule, parameters.evaluator));
      parameters.scheduleBuilder = new ScheduleBuilderFactory(parameters).createScheduleBuilder(ScheduleBuilderType.FORWARD_SCHEDULE_BUILDER);
      parameters.constraintPreserver = new ConstraintPreserverFactory(parameters).createConstraintPreserver(ConstraintPreserverType.MODULO);

      System.out.println(file);
      for (int i = 0; i < 1; ++i) {
        long startTime;
        long endTime;

        DifferentialEvolution de = new DifferentialEvolution(schedule,
            populationSize, 0.1, 0.1, generationLimit, 10000, 500,
            parameters, null);

        List<BaseIndividual<Double, Schedule>> resultIndividuals;
        startTime = System.nanoTime();
        resultIndividuals = de.optimize();
        endTime = System.nanoTime();
//        System.out.println("Time elapsed: " + ((endTime - startTime) / Math.pow(10, 9)));
        System.out.println(((endTime - startTime) / Math.pow(10, 9)));

//        System.out.println(resultIndividuals.get(0));
      }


    }

    return null;
  }

}
