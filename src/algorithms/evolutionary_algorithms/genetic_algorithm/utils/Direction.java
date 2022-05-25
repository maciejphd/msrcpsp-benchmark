package algorithms.evolutionary_algorithms.genetic_algorithm.utils;

import algorithms.problem.BaseIndividual;
import algorithms.problem.BaseProblemRepresentation;

import java.util.ArrayList;
import java.util.List;

public class Direction<PROBLEM extends BaseProblemRepresentation> {

  private List<Double> coordinates;
  private List<BaseIndividual<Integer, PROBLEM>> surrounding;
  private BaseIndividual<Integer, PROBLEM> nicheIndividual;
  private int nicheCount;

  public Direction(List<Double> coordinates) {
    this.coordinates = coordinates;
    surrounding = new ArrayList<>();
    nicheCount = 0;
  }

  public void clear() {
    nicheCount = 0;
    nicheIndividual = null;
    surrounding = new ArrayList<>();
  }

  public double getClusteringDistance(BaseIndividual<Integer, PROBLEM> individual) {
    double result = 0.0d;

    double[] objectives = individual.getNormalObjectives();
    double tNominator = 0.0d;
    double tDenominator = 0.0d;
    for (int i = 0; i < coordinates.size(); ++i) {
      tNominator += objectives[i] * coordinates.get(i);
      tDenominator += coordinates.get(i) * coordinates.get(i);
    }
    double t = tNominator / tDenominator;
    for (int i = 0; i < coordinates.size(); ++i) {
      result += Math.pow(objectives[i] - (t * coordinates.get(i)), 2);
    }

    result = Math.sqrt(result);
    return result;
  }

  public double getConvergenceDistance(BaseIndividual<Integer, PROBLEM> individual) {
    double result;

    double[] objectives = individual.getNormalObjectives();
    double tNominator = 0.0d;
    double tDenominator = 0.0d;
    for (int i = 0; i < coordinates.size(); ++i) {
      tNominator += objectives[i] * coordinates.get(i);
      tDenominator += coordinates.get(i) * coordinates.get(i);
    }
    tDenominator = Math.sqrt(tDenominator);
    result = tNominator / tDenominator;
    return result;
  }

  public void addToSurrounding(BaseIndividual<Integer, PROBLEM> individual) {
    surrounding.add(individual);
  }

  public void removeFromSurrounding(BaseIndividual<Integer, PROBLEM> individual) {
    surrounding.remove(individual);
  }

  public List<Double> getCoordinates() {
    return coordinates;
  }

  public void setCoordinates(List<Double> coordinates) {
    this.coordinates = coordinates;
  }

  public BaseIndividual<Integer, PROBLEM> getNicheIndividual() {
    return nicheIndividual;
  }

  public void setNicheIndividual(BaseIndividual<Integer, PROBLEM> nicheIndividual) {
    this.nicheIndividual = nicheIndividual;
  }

  public List<BaseIndividual<Integer, PROBLEM>> getSurrounding() {
    return surrounding;
  }

  public void setSurrounding(List<BaseIndividual<Integer, PROBLEM>> surrounding) {
    this.surrounding = surrounding;
  }

  public int getNicheCount() {
    return nicheCount;
  }

  public void setNicheCount(int nicheCount) {
    this.nicheCount = nicheCount;
  }

  /**
   * Increment the niche count and return the new value
   *
   * @return new niche count
   */
  public int incrementNicheCount() {
    return ++nicheCount;
  }

}
