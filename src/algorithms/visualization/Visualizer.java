package algorithms.visualization;

import algorithms.problem.scheduling.Schedule;

import java.util.Set;

public class Visualizer {
  private CriticalPathBuilder builder = new CriticalPathBuilder();
  private Renderer renderer;

  public Visualizer(Renderer renderer) {
    this.renderer = renderer;
  }

  public String Visualize(Schedule schedule, boolean is_critical) {
    Set<Integer> critical = null;
    if (is_critical) {
      critical = builder.BuildCriticalPath(schedule);
    }
    return renderer.RenderHTML(schedule, critical);
  }
}
