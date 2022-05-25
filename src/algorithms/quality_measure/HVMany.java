package algorithms.quality_measure;

import algorithms.problem.BaseIndividual;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HVMany extends BaseMeasure {

  public HVMany(BaseIndividual nadirPoint) {
    this.referencePoint = nadirPoint;
  }


  @Override
  public <T extends BaseIndividual> double getMeasure(List<T> population) {
    double hyperVolume = 0.0;
    int numObjectives = population.get(0).getNormalObjectives().length;
    Collections.sort(population);

    List<Pair<Double, List<T>>> s = new ArrayList<>();
    s.add(new Pair<>(1.0, population));
    for (int k = 0; k < numObjectives - 1; ++k) {
      List<Pair<Double, List<T>>> sPrim = new ArrayList<>();
      for (Pair<Double, List<T>> pair : s) {
        for (Pair<Double, List<T>> pairPrim : slice(pair.getValue(), k)) {
          sPrim.add(new Pair<>(pair.getKey() * pairPrim.getKey(), pairPrim.getValue()));
        }
      }
      s = sPrim;
    }
    for (Pair<Double, List<T>> pair : s) {
      hyperVolume += pair.getKey() * Math.abs(pair.getValue().get(0).getNormalObjectives()[numObjectives - 1] - referencePoint.getNormalObjectives()[numObjectives - 1]);
    }

    return hyperVolume;
  }

  private <T extends BaseIndividual> List<Pair<Double, List<T>>> slice(List<T> pl, int k) {
    T p = pl.get(0);
    List<T> ql = new ArrayList<>();
    List<Pair<Double, List<T>>> s = new ArrayList<>();
    for (int i = 1; i < pl.size(); ++i) {
      ql = insert(p, k+1, ql);
      T pPrim = pl.get(i);
      s.add(new Pair<>(Math.abs(p.getNormalObjectives()[k] - pPrim.getNormalObjectives()[k]), ql));
      p = pPrim;
    }
    ql = insert(p, k+1, ql);
    s.add(new Pair<>(Math.abs(p.getNormalObjectives()[k] - referencePoint.getNormalObjectives()[k]), ql));
    return s;
  }

  private <T extends BaseIndividual> List<T> insert(T p, int k, List<T> pl) {
    List<T> ql = new ArrayList<>();
    for (int i = 0; i < pl.size() && pl.get(0).getNormalObjectives()[k] < p.getNormalObjectives()[k]; ++i) {
      ql.add(pl.get(i));
    }
    ql.add(p);
    for (int i = 0; i < pl.size(); ++i) {
      if (!dominates(p, pl.get(i), k) && !ql.contains(pl.get(i))) {
        ql.add(pl.get(i));
      }
    }
    return ql;
  }

  private <T extends BaseIndividual> boolean dominates(T p1, T p2, int k) {
    boolean better = false;
    int numObjectives = p1.getNormalObjectives().length;
    for (int i = k; i < numObjectives; ++i) {
      if (p2.getNormalObjectives()[i] < p1.getNormalObjectives()[i]) {
        return false;
      }
      better = p1.getNormalObjectives()[i] < p2.getNormalObjectives()[i];
    }
    return better;
  }

}
