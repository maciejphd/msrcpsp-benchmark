package runners;

import algorithms.io.MSRCPSPIO;
import algorithms.problem.scheduling.Schedule;
import algorithms.visualization.TimeslotTable;
import algorithms.visualization.Visualizer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

/**
 * Runs visualization method. Designed to be used from the
 * console. Expects a filepath as a parameter.
 */
public class VisualizationRunner {

  /**
   * Runs visualization method. Takes three parameters
   * definition filepath, solution filepath and
   * visualization save filepath.
   *
   * @param args
   */
  public static void main(String[] args) {
    if (3 > args.length) {
      System.out.println("Please provide the following file paths: definition, solution, visualization destination.");
      return;
    }

    String definitionFile = args[0];
    String solutionFile = args[1];
    String visualizationFile = args[2];

    MSRCPSPIO reader = new MSRCPSPIO();
    Schedule schedule = reader.readDefinition(definitionFile);
    if (null == schedule) {
      System.out.println("Could not read the Definition " + definitionFile);
      return;
    }

    schedule = reader.readSolution(solutionFile, schedule);
    if (null == schedule) {
      System.out.println("Could not read the Solution " + solutionFile);
      return;
    }

    Arrays.stream(schedule.getTasks()).forEach((t) -> t.setStart(t.getStart() + 1));
    Visualizer visualizer = new Visualizer(new TimeslotTable());
    String html = visualizer.Visualize(schedule, true);
    try {
      FileWriter writer = new FileWriter(visualizationFile);
      writer.write(html);
      writer.close();
      System.out.println("Success!");
    } catch (IOException e) {
      System.out.println("Could not save Visualization file: " + visualizationFile);
      e.printStackTrace();
    }
  }

}
