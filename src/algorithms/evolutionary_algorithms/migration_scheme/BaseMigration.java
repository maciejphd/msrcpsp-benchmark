package algorithms.evolutionary_algorithms.migration_scheme;

import algorithms.problem.BaseIndividual;
import algorithms.problem.BaseProblemRepresentation;

import java.util.List;

/**
 * Enables dynamic choosing of migration scheme.
 */
abstract public class BaseMigration<GENE extends Number, PROBLEM extends BaseProblemRepresentation> {

  protected int migrationSize;
  protected int migrationInterval;

  /**
   * Migrates individuals between populations.
   *
   * @param populations list of all populations (islands)
   * @param generation current generation number
   * @return populations after migration
   */
  abstract public List<List<BaseIndividual<GENE, PROBLEM>>> migrate(List<List<BaseIndividual<GENE, PROBLEM>>> populations,
                                                                    int generation);

  public int getMigrationSize() {
    return migrationSize;
  }

  public void setMigrationSize(int migrationSize) {
    this.migrationSize = migrationSize;
  }

  public int getMigrationInterval() {
    return migrationInterval;
  }

  public void setMigrationInterval(int migrationInterval) {
    this.migrationInterval = migrationInterval;
  }

}
