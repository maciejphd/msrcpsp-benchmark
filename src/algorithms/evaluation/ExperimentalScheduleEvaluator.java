package algorithms.evaluation;

import algorithms.problem.BaseIndividual;
import algorithms.problem.scheduling.Resource;
import algorithms.problem.scheduling.Schedule;
import algorithms.problem.scheduling.Skill;
import algorithms.problem.scheduling.Task;

import java.util.Arrays;

public class ExperimentalScheduleEvaluator<GENE extends Number> extends BaseScheduleEvaluator<GENE> {

  protected double maxSkillOveruse;

  public ExperimentalScheduleEvaluator() {
    super();
  }

  public ExperimentalScheduleEvaluator(BaseIndividual<GENE, Schedule> individual) {
    super(individual);
    setMaxValues();
  }

  @Override
  public void setMaxValues() {
    super.setMaxValues();
    maxSkillOveruse = getMaxSkillOveruse();
  }

  @Override
  public double evaluate() {
    return 0.0d;
  }

  @Override
  public double[] getObjectives() {
    double[] objectives = new double[3];
    objectives[0] = getDuration();
    objectives[1] = getCost();
    objectives[2] = getSkillOveruse();
    return objectives;
  }

  @Override
  public double[] getNormalObjectives() {
    // TODO: - get min cost / duration ?
    double[] objectives = new double[3];
    objectives[0] = getDuration() / maxDuration;
    objectives[1] = getCost() / maxCost;
    objectives[2] = getSkillOveruse() / maxSkillOveruse;
    return objectives;
  }

  private double getSkillOveruse() {
    Schedule schedule = individual.getProblem();
    double overuse = 0.0d;
    Task[] tasks = schedule.getTasks();
    for (Task t : tasks) {
      Resource r = schedule.getResource(t.getResourceId());
      for (Skill tSkill : t.getRequiredSkills()) {
        Skill rSkill = Arrays.stream(r.getSkills()).filter(s -> s.getType().equals(tSkill.getType())).findFirst().orElse(null);
        if (rSkill == null) {
          throw new IllegalStateException("Incorrect task - resource assignment");
        }
        overuse += rSkill.getLevel() - tSkill.getLevel();
      }

    }
    return  overuse;
  }

  public double getMaxSkillOveruse() {
    Schedule schedule = individual.getProblem();
    Task[] tasks = schedule.getTasks();
    double maxOveruse = 0.0d;
    for (Task t : tasks) {
      for (Skill tSkill : t.getRequiredSkills()) {

        int maxLevel = 0;
        for (Resource r : schedule.getResources()) {
          Skill rSkill = Arrays.stream(r.getSkills()).filter(s -> s.getType().equals(tSkill.getType())).findFirst().orElse(null);
          if (rSkill != null && rSkill.getLevel() > maxLevel) {
            maxLevel = rSkill.getLevel();
          }
        }
        maxOveruse += maxLevel - tSkill.getLevel();

      }
    }
    return maxOveruse;
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
    objectives[0] = maxDuration;
    objectives[1] = maxCost;
    objectives[2] = maxSkillOveruse;
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
  public ExperimentalScheduleEvaluator getCopy(BaseIndividual<GENE, Schedule> individual) {
    return new ExperimentalScheduleEvaluator<>(individual);
  }

  @Override
  public EvaluatorType getType() {
    return EvaluatorType.EXPERIMENTAL_SCHEDULE;
  }

  @Override
  public int getNumObjectives() {
    return 3;
  }

}
