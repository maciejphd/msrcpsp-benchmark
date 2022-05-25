package algorithms.factories;


import algorithms.evolutionary_algorithms.ParameterSet;
import algorithms.evolutionary_algorithms.migration_scheme.BaseMigration;
import algorithms.evolutionary_algorithms.migration_scheme.BestNeighbourMigration;
import algorithms.evolutionary_algorithms.migration_scheme.MigrationSchemeType;

public class MigrationSchemeFactory {

  private ParameterSet parameters;

  public MigrationSchemeFactory(ParameterSet parameters) {
    this.parameters = parameters;
  }

  /**
   * Creates migration scheme based on provided type.
   * Default: {@link BestNeighbourMigration}
   *
   * @param type type to use
   * @return chosen migratino scheme
   */
  public BaseMigration createMigrationScheme(MigrationSchemeType type) {
    BaseMigration migration;
    switch (type) {
      case NEIGHBOUR:
        migration = new BestNeighbourMigration();
        migration.setMigrationSize(parameters.migrationSize);
        migration.setMigrationInterval(parameters.migrationInterval);
        return migration;
      default:
        migration = new BestNeighbourMigration();
        migration.setMigrationSize(parameters.migrationSize);
        migration.setMigrationInterval(parameters.migrationInterval);
        return migration;
    }
  }

}
