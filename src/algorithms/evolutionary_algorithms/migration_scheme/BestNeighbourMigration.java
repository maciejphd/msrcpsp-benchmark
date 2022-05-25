package algorithms.evolutionary_algorithms.migration_scheme;


import algorithms.problem.BaseIndividual;
import algorithms.problem.BaseProblemRepresentation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BestNeighbourMigration<GENE extends Number, PROBLEM
    extends BaseProblemRepresentation> extends BaseMigration<GENE, PROBLEM> {

  /**
   * Every <code>migrationInterval</code> generations
   * migrates <code>migrationSize</code> best individuals
   * to neighbouring populations.
   *
   * @param populations list of all populations (islands)
   * @param generation current generation number
   * @return populations after migration
   */
  @Override
  public List<List<BaseIndividual<GENE, PROBLEM>>> migrate(List<List<BaseIndividual<GENE, PROBLEM>>> populations,
                                                           int generation) {
    if (generation % migrationInterval != 0) {
      return populations;
    }
    List<List<BaseIndividual<GENE, PROBLEM>>> migrants = initiateMigrants(populations.size());

    for (int i = 0; i < populations.size(); ++i) {
      List<BaseIndividual<GENE, PROBLEM>> population = populations.get(i);
      migrants.get(i).addAll(chooseNBest(population));
    }

    for (int i = 0; i < populations.size() - 1; ++i) {
      List<BaseIndividual<GENE, PROBLEM>> firstPopulation = populations.get(i);
      List<BaseIndividual<GENE, PROBLEM>> secondPopulation = populations.get(i+1);

      List<BaseIndividual<GENE, PROBLEM>> firstMigrants = migrants.get(i);
      List<BaseIndividual<GENE, PROBLEM>> secondMigrants = migrants.get(i+1);

      migrate(firstPopulation, secondMigrants);
      migrate(secondPopulation, firstMigrants);
    }

    return populations;
  }

  private void migrate(List<BaseIndividual<GENE, PROBLEM>> population,
                       List<BaseIndividual<GENE, PROBLEM>> migrants) {
    List<BaseIndividual<GENE, PROBLEM>> worst = new ArrayList<>();
    population.stream().sorted((i1, i2) -> Double.compare(i2.getEvalValue(), i1.getEvalValue())).limit(migrationSize)
        .forEach(individual -> worst.add(individual));

    int index = 0;
    for (BaseIndividual<GENE, PROBLEM> candidate : worst) {
      for (int i = 0; i < population.size(); ++i) {
        if (population.get(i) == candidate) {
          population.set(i, new BaseIndividual<>(migrants.get(index).getProblem(),
              migrants.get(index).getGenes(), population.get(i).getEvaluator()));
          ++index;
        }
      }
    }
  }

  private List<List<BaseIndividual<GENE, PROBLEM>>> initiateMigrants(int size) {
    List<List<BaseIndividual<GENE, PROBLEM>>> migrants = new ArrayList<>();
    for (int i = 0; i < size; ++i) {
      migrants.add(new ArrayList<>());
    }
    return migrants;
  }

  private List<BaseIndividual<GENE, PROBLEM>> chooseNBest(List<BaseIndividual<GENE, PROBLEM>> population) {
    List<BaseIndividual<GENE, PROBLEM>> best = new ArrayList<>();

    population.stream().sorted(Comparator.comparingDouble(BaseIndividual::getEvalValue)).limit(migrationSize)
        .forEach(individual -> best.add(individual));

    return best;
  }

}
