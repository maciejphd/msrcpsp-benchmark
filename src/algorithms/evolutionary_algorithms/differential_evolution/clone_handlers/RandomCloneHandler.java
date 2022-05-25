package algorithms.evolutionary_algorithms.differential_evolution.clone_handlers;

import algorithms.problem.BaseIndividual;
import algorithms.problem.scheduling.Schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Randomizes each found clone. Requires a random random and
 * an array containing upper bounds of assignment for each task.
 */
public class RandomCloneHandler extends BaseCloneHandler<Schedule> {

  private Random generator;
  private int[] upperBounds;

  /**
   * Randomizes each found process.
   *
   * @param population population to fix
   * @return number of clones
   */
  @Override
  public int handleClones(List<BaseIndividual<Double, Schedule>> population) {
    numClones = 0;
    int numGenes = population.get(0).getGenes().size();
    List<Double> genes = new ArrayList<>(numGenes);

    for (int i = 0; i < population.size(); ++i) {
      for (int j = i+1; j < population.size(); ++j) {
        if (isClone(population.get(i).getProblem().getTasks(),
            population.get(j).getProblem().getTasks())) {
          for (int k = 0; k < numGenes; ++k) {
            genes.set(k, generator.nextDouble() * upperBounds[k]);
          }
          population.get(j).setGenes(genes);

          ++numClones;
        }
      }
    }

    return numClones;
  }

  public void setGenerator(Random generator) {
    this.generator = generator;
  }

  public void setUpperBounds(int[] upperBounds) {
    this.upperBounds = upperBounds;
  }

}
