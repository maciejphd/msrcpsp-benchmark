package algorithms.evolutionary_algorithms.util;

import algorithms.problem.BaseIndividual;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Helper to perform Non-dominated sorting as used in NSGA.
 */
public class NondominatedSorter<T extends BaseIndividual> {

  public void nondominatedSorting(List<T> population) {
    for (T individual : population) {
      individual.setNumOfDominatingSolutions(0);
      individual.getDominatedSolutions().clear();
    }

    int rank;
    for (T p : population) {
      for (T q : population) {
        if (!p.equals(q)) {
          if (p.dominates(q)) {
            p.getDominatedSolutions().add(q);
          } else if (q.dominates(p)) {
            p.setNumOfDominatingSolutions(p
                .getNumOfDominatingSolutions() + 1);
          }
        }
      }
      if (p.getNumOfDominatingSolutions() == 0) {
        p.setRank(1);
      }
    }
    boolean loop = true;
    rank = 1;
    while (loop) {
      loop = false;
      for (T individual : population) {
        if (individual.getRank() == rank) {
          List<BaseIndividual> dominatedSolutions = individual.getDominatedSolutions();
          for (BaseIndividual dominated : dominatedSolutions) {
            dominated.setNumOfDominatingSolutions(dominated
                .getNumOfDominatingSolutions() - 1);
            if (dominated.getNumOfDominatingSolutions() == 0) {
              dominated.setRank(rank + 1);
              loop = true;
            }
          }
        }
      }
      rank++;
    }
  }

  public void crowdingDistance(List<T> population) {

    Map<Integer, List<T>> fronts = getFronts(population);

    for (List<T> set : fronts.values()) {
      int l = set.size();

      for (T individual : set) {
        individual.setDistance(0.0);
      }

      Collections.sort(set, new Comparator<T>() {
        public int compare(T o1, T o2) {
          if (o1.getNormalObjectives()[0] == o2.getNormalObjectives()[0])
            return 0;
          return o1.getNormalObjectives()[0] < o2.getNormalObjectives()[0] ? -1 : 1;
        }
      });

      set.get(0).setDistance(Double.POSITIVE_INFINITY);
      set.get(l - 1).setDistance(Double.POSITIVE_INFINITY);

      for (int i = 1; i < l - 1; i++) {
        set.get(i).setDistance(
            set.get(i).getDistance() + set.get(i + 1).getNormalObjectives()[0]
                - set.get(i - 1).getNormalObjectives()[0]);
      }

      Collections.sort(set, new Comparator<T>() {
        public int compare(T o1, T o2) {
          if (o1.getNormalObjectives()[1] == o2.getNormalObjectives()[1])
            return 0;
          return o1.getNormalObjectives()[1] < o2.getNormalObjectives()[1] ? -1 : 1;
        }
      });

      set.get(0).setDistance(Double.POSITIVE_INFINITY);
      set.get(l - 1).setDistance(Double.POSITIVE_INFINITY);

      for (int i = 1; i < l - 1; i++) {
        set.get(i).setDistance(
            set.get(i).getDistance() + set.get(i + 1).getNormalObjectives()[1]
                - set.get(i - 1).getNormalObjectives()[1]);
      }
    }
  }

  private Map<Integer, List<T>> getFronts(
      List<T> population) {

    Map<Integer, List<T>> fronts = new HashMap<>();

    OptionalInt lastFront = population.stream()
        .mapToInt(T::getRank).max();

    for (int i = 0; i < lastFront.getAsInt(); i++) {
      fronts.put((i + 1), new ArrayList<>());
    }

    for (T individual : population) {
      fronts.get(individual.getRank()).add(individual);
    }
    return fronts;
  }

  public List<T> getFront(
      List<T> population, int frontNumber) {

    List<T> result = population.stream()
        .filter(line -> (frontNumber == line.getRank()))
        .collect(Collectors.toList());

    return result;
  }

}
