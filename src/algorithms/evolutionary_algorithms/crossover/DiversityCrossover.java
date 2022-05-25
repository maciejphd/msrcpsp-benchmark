package algorithms.evolutionary_algorithms.crossover;

import algorithms.evolutionary_algorithms.ParameterSet;
import algorithms.problem.BaseProblemRepresentation;

import java.util.ArrayList;
import java.util.List;

public class DiversityCrossover<GENE extends Number> extends BaseCrossover<GENE, BaseProblemRepresentation> {

  @Override
  public List<List<GENE>> crossover(double cr, List<GENE> firstParent, List<GENE> secondParent, ParameterSet<GENE, BaseProblemRepresentation> parameters) {
    double random;
    List<GENE> firstChild = new ArrayList<>(firstParent);
    List<GENE> secondChild = new ArrayList<>(secondParent);

    if (parameters.random.nextDouble() < cr) {
      for (int i = 0; i < firstParent.size(); ++i) {
        random = parameters.random.nextDouble();
        if (random < 0.5) {
          firstChild.set(i, secondParent.get(i));
        }
        random = parameters.random.nextDouble();
        if (random < 0.5) {
          secondChild.set(i, firstParent.get(i));
        }
      }
    }

    List<List<GENE>> result = new ArrayList<>();
    result.add(firstChild);
    result.add(secondChild);

    return result;
  }

}
