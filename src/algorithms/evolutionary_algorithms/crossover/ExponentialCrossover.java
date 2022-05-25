package algorithms.evolutionary_algorithms.crossover;

import algorithms.evolutionary_algorithms.ParameterSet;
import algorithms.problem.BaseProblemRepresentation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of exponential crossover.
 * Creates trial vector by randomly choosing starting point,
 * and then replacing following elements with probability cr
 */
public class ExponentialCrossover extends BaseCrossover<Double, BaseProblemRepresentation> {

  /**
   * Performs an exponential crossover.
   *
   * @param cr cross over rate
   * @param target target genes
   * @param donor donor genes
   * @param parameters set of parameters
   * @return resulting array of genes
   */
  @Override
  public List<List<Double>> crossover(double cr, List<Double> target, List<Double> donor, ParameterSet<Double, BaseProblemRepresentation> parameters) {
    List<Double> trial = new ArrayList<>(Collections.nCopies(target.size(), 0.0d));
    int startingPoint = parameters.random.nextInt(target.size());
    int L = 0;
    do {
      ++L;
    } while(parameters.random.nextDouble() < cr && L < target.size());
    for (int i = 0; i < target.size(); ++i) {
      // Condition checks whether i falls into (n, n+L) range
      // 2 middle lines subtract donor.length if n+L exceeds donor.length
      if (startingPoint + L -
          ((startingPoint > (startingPoint + L)
              % target.size()) ? 1 : 0) * target.size() > i
          && startingPoint < i) {
        trial.set(i, donor.get(i));
      } else {
        trial.set(i, target.get(i));
      }
    }
    // Ensure that at least one gene will be taken from mutant
    int single = parameters.random.nextInt(target.size());
    trial.set(single, donor.get(single));

    List<List<Double>> result = new ArrayList<>();
    result.add(trial);

    return result;
  }

}
