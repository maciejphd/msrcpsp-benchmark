package algorithms.visualization;

import algorithms.problem.scheduling.Schedule;
import algorithms.problem.scheduling.Task;

import java.io.StringWriter;
import java.util.*;
import java.util.stream.Collectors;

public class TimeslotTable implements Renderer {
  private Palletizer palletizer = new Palletizer();

  @Override
  public String RenderHTML(Schedule schedule, Set<Integer> critical) {
    StringWriter table = new StringWriter();

    table.write("<tbody><tr><td class=\"first text\"></td>");
    int maxFinishTime = maximumFinishTime(schedule);
    for (int i = 0; i < maxFinishTime; i++) {
      table.write("<td class=\"cell\">" + i + "</td>");
    }

    int cellWidth = maxFinishTime >= 100 ? 17 : 12;
    int tableWidth = 93 + (cellWidth + 3) * (maxFinishTime + 1);
    int boxMargin = 10;

    StringWriter styles = new StringWriter();
    styles.write("table { width: " + tableWidth + "px; }\n");
    styles.write("#box { width: " + (tableWidth + 2 * boxMargin) + "px; " +
        "margin: " + boxMargin + "; }\n");

    Arrays.stream(schedule.getTasks())
        .collect(Collectors.groupingBy(Task::getResourceId, Collectors.toList()))
        .entrySet().forEach((e) -> processResourceEntry(e, critical, styles, table, schedule));

    table.write("</tbody>");

    if (critical != null && !critical.isEmpty()) {
      table.write("<tbody>");
      List<Task> criticalTasks = critical.stream().map(schedule::getTask)
          .collect(Collectors.toList());
      table.write(generateTableRow(criticalTasks, critical, null, schedule));
      table.write("</tbody>");
    }

    return embedHTML(table, cellWidth, styles);
  }

  private String embedHTML(StringWriter table, int cellWidth, StringWriter styles) {
    return "  <html>\n" +
    "  <head>\n" +
    "    <style>\n" +
    "      .cell {\n" +
    "        width: " + cellWidth + "px;\n" +
    "        text-align: center;\n" +
    "        font-size: 11px;\n" +
    "      }\n" +
    "      #box {\n" +
    "        margin: 10px;\n" +
    "      }\n" +
    "      .first {\n" +
    "        width: 90px;\n" +
    "      }\n" +
    "      .text {\n" +
    "        text-align: center;\n" +
    "        font-size: 16px;\n" +
    "      }\n" +
    "      .critical {\n" +
    "        background-color: #000;\n" +
    "        color: #fff;\n" +
    "        border-color: #000 #fff;\n" +
    "      }\n" +
    "      th, td {\n" +
    "        border: 1px solid black;\n" +
    "      }\n" +
    "      table {\n" +
    "        border-collapse: collapse;\n" +
    "        table-layout: fixed;\n" +
    "      }\n" +
    "      tbody:not(:first-child):before {\n" +
    "        content: '';\n" +
    "        display: table-row;\n" +
    "        height: 15px;\n" +
    "      }\n" + styles.toString() +
    "    </style>\n" +
    "  </head>\n" +
    "  <body>\n" +
    "    <div id=\"box\">\n" +
    "      <table>\n" + table.toString() +
    "      </table>\n" +
    "    </div>\n" +
    "  </body>\n" +
    "  </html>";
  }

  private void processResourceEntry(Map.Entry<Integer, List<Task>> entry,
                                    Set<Integer> critical,
                                    StringWriter styles, StringWriter table,
                                    Schedule schedule) {
    int resourceId = entry.getKey();
    List<Task> tasks = entry.getValue();
    styles.write(generateRowStyles(tasks, critical));
    table.write(generateTableRow(tasks, critical, resourceId, schedule));
  }

  private String generateRowStyles(List<Task> tasks, Set<Integer> critical) {
    StringWriter result = new StringWriter();
    for (Task task : tasks) {
      if (critical == null || !critical.contains(task.getId())) {
        result.write(".color" + task.getId() +
            " { background-color: #" + palletizer.getColor() + "; }\n");
      }
    }
    return result.toString();
  }

  private String generateTableRow(Collection<Task> tasks,
                                  Set<Integer> critical,
                                  Integer resourceId,
                                  Schedule schedule) {
    PriorityQueue<Task> queue = new PriorityQueue<>(tasks.size(),
        (a, b) -> a.getStart() - b.getStart());
    queue.addAll(tasks);

    StringWriter row = new StringWriter();
    row.write("<tr><td class=\"first text\">");
    if (resourceId != null) {
      row.write("Resource " + resourceId);
    } else {
      row.write("Critical path");
    }
    row.write("</td>\n");

    for (int i = 0; i < maximumFinishTime(schedule); i++) {
      row.write("<td class=\"cell");
      if (!queue.isEmpty() && queue.peek().getStart() == i) {
        Task task = queue.remove();
        row.write(" text ");
        if (critical != null && critical.contains(task.getId())) {
          row.write("critical");
        } else {
          row.write("color" + task.getId());
        }
        row.write("\" colspan=\"" + (task.getDuration()));
        i = task.getStart() + task.getDuration() - 1;
        row.write("\">Task " + task.getId() + " [" + task.getDuration() + "]</td>");
      } else {
        row.write("\"></td>");
      }
    }
    row.write("</tr>\n");
    return row.toString();
  }

  private int maximumFinishTime(Schedule schedule) {
    return Arrays.stream(schedule.getTasks())
        .mapToInt((t) -> t.getStart() + t.getDuration())
        .max().getAsInt();
  }
}
