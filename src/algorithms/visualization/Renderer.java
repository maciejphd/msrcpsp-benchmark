package algorithms.visualization;

import algorithms.problem.scheduling.Schedule;

import java.util.Set;

public interface Renderer {
  String RenderHTML(Schedule schedule, Set<Integer> critical);
}
