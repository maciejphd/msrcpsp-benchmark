package algorithms.factories;


import algorithms.evolutionary_algorithms.ParameterSet;
import algorithms.evolutionary_algorithms.selection.*;

public class SelectionFactory {

  private ParameterSet parameters;

  public SelectionFactory(ParameterSet parameters) {
    this.parameters = parameters;
  }

  /**
   * Creates selection based on provided type.
   * Default: {@link OneToOneSelection}
   *
   * @param type type to use
   * @return chosen selection method
   */
  public BaseSelection createSelection(SelectionType type) {
    BaseSelection selection;
    switch (type) {
      case DOMINATION:
        return new DominationSelection();
      case NONDOMINATED_SORTING_ROULETTE:
        return new NondominatedSortingRoulette();
      case NONDOMINATED_SORTING_TOURNAMENT:
        selection = new NondominatedSortingTournament();
        ((NondominatedSortingTournament)selection).setTournamentSize(parameters.tournamentSize);
        return selection;
      case ONE_TO_ONE:
        return new OneToOneSelection();
      case PROBABILITY:
        selection = new ProbabilitySelection();
        ((ProbabilitySelection)selection).setGenerator(parameters.random.getRandom());
        break;
      case RANK:
        return new RankSelection();
      case ROULETTE_WHEEL:
        return new RouletteWheelSelection();
      case TOURNAMENT:
        return new TournamentSelection();
      case NONDOMINATED_SORTING_NO_CROWDING_TOURNAMENT:
        selection = new NondominatedSortingNoCrowdingTournament();
        ((NondominatedSortingNoCrowdingTournament)selection).setTournamentSize(parameters.tournamentSize);
        break;
      case DIVERSITY_SELECTION:
        selection = new DiversitySelection();
        ((DiversitySelection) selection).setTournamentSize(parameters.tournamentSize);
        break;
      default:
        return new OneToOneSelection();
    }
    return selection;
  }

}
