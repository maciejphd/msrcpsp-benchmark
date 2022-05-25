package algorithms.evolutionary_algorithms.differential_evolution.clone_handlers;

import algorithms.problem.BaseIndividual;
import algorithms.problem.scheduling.Schedule;

import java.util.List;

/**
 * Allows existence of clones in population.
 */
public class AllowCloneHandler extends BaseCloneHandler<Schedule> {

  /**
   * Allows clone to exist, only calculates number of them
   * in the population
   *
   * @param population  population to check
   * @return number of clones
   */
  @Override
  public int handleClones(List<BaseIndividual<Double, Schedule>> population) {
    numClones = 0;
    boolean foundClone;

    for (int i = 0; i < population.size(); ++i) {
      foundClone = false;
      for (int j = i+1; j < population.size() && !foundClone; ++j) {
        if (isClone(population.get(i).getProblem().getTasks(),
            population.get(j).getProblem().getTasks())) {
          foundClone = true;
          ++numClones;
        }
      }
    }

    return numClones;
  }

}
