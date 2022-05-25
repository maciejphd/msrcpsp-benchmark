package algorithms.problem;

import algorithms.evaluation.BaseEvaluator;
import algorithms.evolutionary_algorithms.ParameterSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Single individual of a population.
 * Stores information about generic individual representation
 * and evaluation properties.
 * @param <GENE> type of genes
 */
public class BaseIndividual<GENE extends Number, PROBLEM extends BaseProblemRepresentation> implements Comparable<BaseIndividual> {

	protected PROBLEM problem;

  protected BaseEvaluator<GENE, PROBLEM> evaluator;
	protected double evalValue;
  private List<GENE> genes;
  private int hashCode;

  protected double[] objectives;
  protected double[] normalObjectives;

	private int numOfDominatingSolutions;
	private List<BaseIndividual<GENE, PROBLEM>> dominatedSolutions;
	private int rank;
	private double fitnessValue;
	private double distance;

	public BaseIndividual(PROBLEM problem, BaseEvaluator<GENE, PROBLEM> evaluator) {
		this.problem = (PROBLEM)problem.cloneDeep();
		this.setEvaluator(evaluator.getCopy(this));
    this.getEvaluator().setIndividual(this);
		this.setEvalValue(-1);
    this.setDistance(0.0);
    this.numOfDominatingSolutions = 0;
    this.dominatedSolutions = new ArrayList<>();
    this.rank = -1;
    this.fitnessValue = 0.0;
    this.genes = new ArrayList<>(problem.getNumGenes());
    this.setDistance(0.0);
	}

  public BaseIndividual(PROBLEM problem, List<GENE> genes, BaseEvaluator<GENE, PROBLEM> evaluator) {
    this(problem, evaluator);
    this.genes = new ArrayList<>(genes);
		setHashCode();
  }

  /**
   * Builds solution for given problem based on the genes.
   *
   * @return this individual with built solution
   */
  public BaseIndividual<GENE, PROBLEM> buildSolution(List<GENE> genes, ParameterSet<GENE, PROBLEM> parameters) {
    problem.buildSolution(genes, parameters);
    setEvalValue(evaluate()); // TODO: maybe skip for multi-objective?
    setObjectives();
    setNormalObjectives();
    setHashCode();
    return this;
  }

  /**
   * Returns an array of objective values. First objective
   * is the duration and the second is the cost.
   *
   * @return array with duration and cost of the individual
   */
  public double[] getObjectives() {
    return objectives;
  }

  /**
   * Returns an array of normalized objective values. First objective
   * is the duration and the second is the cost. Normalization is
   * done by dividing the values by the maximum possible values.
   *
   * @return array with duration and cost of the individual
   */
  public double[] getNormalObjectives() {
    return normalObjectives;
  }

	/**
	 * Sets objectives calculated by the evaluator
	 */
	public void setObjectives() {
    this.objectives = evaluator.getObjectives();
	}

	/**
	 * Sets normalized objectives calculated by the evaluator
	 */
	public void setNormalObjectives() {
    this.normalObjectives = evaluator.getNormalObjectives();
	}

  /**
   * Evaluates the individual for single objective problems.
   * For multi objective problems use <code>evaluateMultiObjective()</code>
   *
   * @return evaluation value of linked evaluator
   * @throws IllegalStateException if there is more than 1 evaluator linked
   */
  public double evaluate() throws IllegalStateException {
    return evaluator.evaluate();
  }

	/**
	 * Determines whether this individual dominates given
	 * individual. It means all its objectives value are not
	 * worse and at least one value is better.
	 *
	 * @param individual individual to compare
	 * @return true if this individual dominates the <code>individual</code>
	 */
	public boolean dominates(BaseIndividual individual) {
    boolean better = false;
    for (int i = 0; i < objectives.length; ++i) {
      if (individual.objectives[i] < this.objectives[i]) {
        return false;
      }
      better |= this.objectives[i] < individual.objectives[i];
    }
    return better;
	}

	/**
	 * Determines whether this individual is not dominated by any
	 * individual in a given population.
	 *
	 * @param population population to check
	 * @return true if this individual isNotDominatedBy every
	 *         individual in a given population
	 */
	public boolean isNotDominatedBy(List<? extends BaseIndividual> population) {
		for (BaseIndividual individual : population) {
			if (individual.dominates(this)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Determines whether given individual isNotDominatedBy this individual.
	 * It means all its objectives value are not worse.
	 *
	 * @param individual
	 *            individual to compare
	 * @return true if given individual isNotDominatedBy this individual
	 */
	public boolean isDominated(BaseIndividual individual) {
		return individual.dominates(this);
	}

	public PROBLEM getProblem() {
		return problem;
	}

  public BaseEvaluator<GENE, PROBLEM> getEvaluator() {
    return evaluator;
  }

  public void setEvaluator(BaseEvaluator<GENE, PROBLEM> evaluator) {
    this.evaluator = evaluator;
  }

	public void setProblem(PROBLEM problem) {
		this.problem = problem;
	}

	public double getEvalValue() {
		return evalValue;
	}

	public void setEvalValue(double evalValue) {
		this.evalValue = evalValue;
	}

  public List<GENE> getGenes() {
    return genes;
  }

  public void setGenes(List<GENE> genes) {
    this.genes = genes;
    setHashCode();
  }

  public void setObjectives(double[] objectives) {
    this.objectives = objectives;
  }

  public void setNormalObjectives(double[] normalObjectives) {
    this.normalObjectives = normalObjectives;
  }

  public int getHashCode() {
    return hashCode;
  }

  public void setHashCode() {
    this.hashCode = genes.hashCode();
  }

  public int getNumOfDominatingSolutions() {
    return numOfDominatingSolutions;
  }

  public void setNumOfDominatingSolutions(int numOfDominatingSolutions) {
    this.numOfDominatingSolutions = numOfDominatingSolutions;
  }

  public List<BaseIndividual<GENE, PROBLEM>> getDominatedSolutions() {
    return dominatedSolutions;
  }

  public void setDominatedSolutions(List<BaseIndividual<GENE, PROBLEM>> dominatedSolutions) {
    this.dominatedSolutions = dominatedSolutions;
  }

  public int getRank() {
    return rank;
  }

  public void setRank(int rank) {
    this.rank = rank;
  }

  public double getFitnessValue() {
    return fitnessValue;
  }

  public void setFitnessValue(double fitnessValue) {
    this.fitnessValue = fitnessValue;
  }

  public double getDistance() {
    return distance;
  }

  public void setDistance(double distance) {
    this.distance = distance;
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    if (objectives != null) {
      for (double objective : objectives) {
        result.append(objective).append("\t");
      }
    }
    result.append(rank).append("\t").append(fitnessValue);
    return result.toString();
  }

  public boolean equalsPhenotype(BaseIndividual individual) {
	  return this.getProblem().getHashCode() == individual.getProblem().getHashCode();
  }

	/**
	 * Compares individuals in the solution space. Returns true
	 * if both cost and duration are equal. Task-resource
	 * assignments may differ.
	 *
	 * @param o object to compare
	 * @return true if objects are equal
	 */
  @Override
	public boolean equals(Object o) {
		if (!(o instanceof BaseIndividual)) {
			return false;
		}

		BaseIndividual individual = (BaseIndividual)o;
		return this.hashCode == individual.getHashCode();
	}

	@Override
	public int compareTo(BaseIndividual o) {
    int result;
    for (int i = 0; i < this.normalObjectives.length; ++i) {
      result = Double.compare(this.normalObjectives[i], o.normalObjectives[i]);
      if (0 != result) {
        return result;
      }
    }
    return 0;
	}

	public int compareTo(BaseIndividual o, int k) {
    if (this.getNormalObjectives()[k] == o.getNormalObjectives()[k])
      return 0;
    return this.getNormalObjectives()[k] < o.getNormalObjectives()[k] ? -1 : 1;
  }
}
