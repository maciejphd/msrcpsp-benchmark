package algorithms.evaluation;

import algorithms.problem.BaseIndividual;
import algorithms.problem.scheduling.Resource;
import algorithms.problem.scheduling.Schedule;
import algorithms.problem.scheduling.Skill;
import algorithms.problem.scheduling.Task;

import java.util.Arrays;

public class ThreeScheduleEvaluator<GENE extends Number> extends BaseScheduleEvaluator<GENE> {

  protected double maxAverageCashFlowDeviation;

  public ThreeScheduleEvaluator() {
    super();
  }

  public ThreeScheduleEvaluator(BaseIndividual<GENE, Schedule> individual) {
    super(individual);
    setMaxValues();
  }

  @Override
  public void setMaxValues() {
    super.setMaxValues();
    maxAverageCashFlowDeviation = getMaxAverageCashFlowDeviation();
  }

  @Override
  public double evaluate() {
    return 0.0d;
  }

  @Override
  public double[] getObjectives() {
    double[] objectives = new double[3];
    double duration = getDuration();
    objectives[0] = duration;
    objectives[1] = getCost();
    objectives[2] = getAverageCashFlowDeviation(duration);
    return objectives;
  }

  @Override
  public double[] getNormalObjectives() {
    // TODO: - get min cost / duration ?
    double[] objectives = new double[3];
    double duration = getDuration();
    objectives[0] = duration / maxDuration;
    objectives[1] = getCost() / maxCost;
    objectives[2] = getAverageCashFlowDeviation(duration) / maxAverageCashFlowDeviation;
    return objectives;
  }

  private double getAverageCashFlowDeviation(double duration) {
    Schedule schedule = individual.getProblem();
    double[] cashFlows = new double[(int)duration];
    Task[] tasks = schedule.getTasks();
    double cashFlow;
    for (Task t : tasks) {
      cashFlow = schedule.getResource(t.getResourceId()).getSalary();
      int finish = t.getStart() + t.getDuration();
      for (int i = t.getStart(); i < finish; ++i) {
        cashFlows[i] += cashFlow;
      }
    }

    double average = Arrays.stream(cashFlows).average().orElse(Double.NaN);
    double deviation = 0.0d;
    for (double flow : cashFlows) {
      deviation += Math.abs(flow - average);
    }
    return deviation;
  }

  public double getMaxAverageCashFlowDeviation() {
    return getMaxCost();
  }

  /**
   * Creates a Nadir point. It contains the worst possible values
   * of all criteria.
   *
   * @return Nadir point
   */
  @Override
  public BaseIndividual<GENE, Schedule> getNadirPoint() {
    Schedule schedule = individual.getProblem();
    BaseIndividual<GENE, Schedule> nadirPoint = new BaseIndividual<GENE, Schedule>(schedule, this);

    double[] objectives = new double[3];
    objectives[0] = getMaxDuration();
    objectives[1] = getMaxCost();
    objectives[2] = getMaxAverageCashFlowDeviation();
    nadirPoint.setObjectives(objectives);

    objectives = new double[3];
    objectives[0] = 1.0d;
    objectives[1] = 1.0d;
    objectives[2] = 1.0d;
    nadirPoint.setNormalObjectives(objectives);

    return nadirPoint;
  }

  /**
   * Creates a perfect point. It contains the best possible values
   * of all criteria.
   *
   * @return perfect point
   */
  @Override
  public BaseIndividual<GENE, Schedule> getPerfectPoint() {
    Schedule schedule = individual.getProblem();
    BaseIndividual<GENE, Schedule> perfectPoint = new BaseIndividual<GENE, Schedule>(schedule, this);

    double[] objectives = new double[3];
    objectives[0] = this.getMinDuration();
    objectives[1] = this.getMinCost();
    objectives[2] = 0.0d;
    perfectPoint.setObjectives(objectives);

    double[] normalObjectives = new double[3];
    normalObjectives[0] = objectives[0] / this.getMaxDuration();
    normalObjectives[1] = objectives[1] / this.getMaxCost();
    normalObjectives[2] = 0.0d;
    perfectPoint.setNormalObjectives(normalObjectives);

    return perfectPoint;
  }

  @Override
  public ThreeScheduleEvaluator getCopy(BaseIndividual<GENE, Schedule> individual) {
    return new ThreeScheduleEvaluator<>(individual);
  }

  @Override
  public EvaluatorType getType() {
    return EvaluatorType.THREE_SCHEDULE_EVALUATOR;
  }

  @Override
  public int getNumObjectives() {
    return 3;
  }

}
