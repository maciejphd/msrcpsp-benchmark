package algorithms.evolutionary_algorithms.differential_evolution.clone_handlers;

import algorithms.problem.BaseIndividual;
import algorithms.problem.BaseProblemRepresentation;
import algorithms.problem.scheduling.Task;

import java.util.List;

/**
 * Enables dynamic choosing of method for handling clones.
 * Additionally retrieves info about clones in current population.
 */
abstract public class BaseCloneHandler<PROBLEM extends BaseProblemRepresentation> {

  protected int numClones = 0;

  abstract public int handleClones(List<BaseIndividual<Double, PROBLEM>> population);

  public int getNumClones() {
    return numClones;
  }

  /**
   * Checks whether an BaseIndividual is a clonen of another individual
   *
   * @param tasks list of tasks of first individual
   * @param tasks2 list of tasks of second individual
   * @return true if individuals are clones, false otherwise
   */
  protected boolean isClone(Task[] tasks, Task[] tasks2) {
    for (int i = 0; i < tasks.length; ++i) {
      if (tasks[i].getResourceId() != tasks2[i].getResourceId()) {
        return false;
      }
    }
    return true;
  }

}
