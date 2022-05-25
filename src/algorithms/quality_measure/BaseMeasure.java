package algorithms.quality_measure;

import algorithms.problem.BaseIndividual;

import java.util.List;

/**
 * Base class for implementing quality measures.
 */
// TODO: for now all measure assume 2 criteria - generalize!
abstract public class BaseMeasure {

  public List<? extends BaseIndividual> referencePopulation;
  public BaseIndividual referencePoint;
  public double referenceValue;

  abstract public <T extends BaseIndividual> double getMeasure(List<T> population);

  /**
   * Gets an euclidean distance between 2 individuals. It is
   * calculated based on its normalized values of objectives.
   *
   * @param i1 first individual
   * @param i2 second individual
   * @return
   */
  protected double getDistance(BaseIndividual i1, BaseIndividual i2) {
    double distance = 0.0d;
    for (int i = 0; i < i1.getNormalObjectives().length; ++i) {
      distance += Math.pow(i1.getNormalObjectives()[i] - i2.getNormalObjectives()[i], 2);
    }
    return Math.sqrt(distance);
  }

  /**
   * Calculates minimal euclidean distance between given individual
   * and given list of individuals. Distance is calculated based
   * on the cost and time of appropriate schedules.
   *
   * @param individual individual to compare
   * @param optimalParetoFront list of individuals to compare
   * @return minimal distance
   */
  protected double getMinDistance(BaseIndividual individual, List<? extends BaseIndividual> optimalParetoFront) {
    double min = Double.MAX_VALUE;
    BaseIndividual closestIndividual = optimalParetoFront.get(0);
    double distance;
    for (BaseIndividual refIndividual : optimalParetoFront) {
      // We avoid calculating powers and sqrt to speed up the algorithm
      distance = 0.0d;
      for (int i = 0; i < individual.getNormalObjectives().length; ++i) {
        distance += Math.abs(individual.getNormalObjectives()[i] - refIndividual.getNormalObjectives()[i]);
      }
      if (distance < min) {
        closestIndividual = refIndividual;
        min = distance;
      }
    }
    return getDistance(individual, closestIndividual);
  }

  /**
   * Checks whether given individual exist in an optimal
   * pareto front.
   *
   * @param individual individual to check
   * @return true if individual exist in optimal pareto front
   */
  protected boolean existsInReferenceFront(BaseIndividual individual) {
    for (BaseIndividual optimalIndividual : referencePopulation) {
      for (int i = 0; i < individual.getNormalObjectives().length; ++i) {
        if (optimalIndividual.getObjectives()[i] != individual.getObjectives()[i]) {
          return false;
        }
      }
    }
    return true;
  }

}
