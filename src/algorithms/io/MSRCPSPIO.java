package algorithms.io;

import algorithms.problem.scheduling.Resource;
import algorithms.problem.scheduling.Schedule;
import algorithms.problem.scheduling.Skill;
import algorithms.problem.scheduling.Task;

import java.io.*;
import java.util.*;
import java.util.logging.Level;

/**
 * Handles loading .def files and saving solutions to the file for
 * Multi Skill Resource Constraint Project Scheduling Problem
 */
public class MSRCPSPIO extends BaseIO {

  /**
   * Reads a .def file and builds a problem from it.
   *
   * @param fileName path to the file
   * @return Schedule build from the file
   */
  public Schedule readDefinition(String fileName) {
    Schedule schedule;

    BufferedReader reader;
    try {
      reader = new BufferedReader(new FileReader(fileName));
    } catch (FileNotFoundException e) {
      LOGGER.log(Level.FINE, e.toString());
      return null;
    }
    String line;
    int numTasks;
    int numResources;
    Resource[] resources;
    Task[] tasks;

    try {
      line = reader.readLine();
      numTasks = readNumber(reader, line, "Tasks");
      numResources = readNumber(reader, line, "Resources");
      skipTo(reader, line, "ResourceID");
      resources = readResources(reader, numResources);
      skipTo(reader, line , "TaskID");
      tasks = readTasks(reader, numTasks);

      schedule = new Schedule(tasks, resources);

    } catch (IOException e) {
      LOGGER.log(Level.FINE, e.toString());
      return null;
    } finally {
      closeReader(reader);
    }
    return schedule;
  }

  /**
   * Reads the number of tasks or resources in the problem.
   *
   * @param reader used reader
   * @param line current line of the file
   * @param toRead determines what to readDefinition, either "Tasks" or "Resources"
   * @return number of tasks or resources
   * @throws IOException when there is no <code>toRead in the file</code>
   */
  private int readNumber(BufferedReader reader, String line, String toRead) throws IOException {
    line = skipTo(reader, line, toRead);
    if (null == line) {
      LOGGER.log(Level.FINE, "No number specified for given type");
      return -1;
    }
    return Integer.parseInt(line.substring(line.lastIndexOf(' ') + 1));
  }

  /**
   * Reads resources from the file.
   *
   * @param reader used reader
   * @param numResources number of resources to readDefinition
   * @return Array of resources
   * @throws IOException when file is in the wrong format
   */
  private Resource[] readResources(BufferedReader reader, int numResources) throws IOException {
    Resource[] resources = new Resource[numResources];
    String line;
    String[] parts;
    int id;
    double salary;
    Skill[] skills;

    for (int i = 0; i < numResources; ++i) {
      line = reader.readLine();

      parts = line.split("\\s+");
      id = Integer.parseInt(parts[0]);
      salary = Double.parseDouble(parts[1]);
      skills = new Skill[(parts.length - 1) / 2];
      skills = readSkills(skills, parts);

      resources[i] = new Resource(id ,salary, skills);
    }

    return resources;
  }

  /**
   * Creates an array of skills from parts containing types and levels.
   *
   * @param skills array of skills to fill
   * @param parts line readDefinition from the file containing types and levels of skills
   * @return array of skills
   */
  private Skill[] readSkills(Skill[] skills, String[] parts) {
    String type;
    for (int i = 0; i < skills.length; ++i) {
      type = parts[2 + (i*2)];
      skills[i] = new Skill(type.substring(0, type.length() -1),
                            Integer.parseInt(parts[3 + (i*2)]));
    }

    return skills;
  }

  /**
   * Reads tasks from the file.
   *
   * @param reader used reader
   * @param numTasks number of tasks
   * @return array of tasks
   * @throws IOException when file is in the wrong format
   */
  private Task[] readTasks(BufferedReader reader, int numTasks) throws IOException {
    Task[] tasks = new Task[numTasks];
    String line;
    String[] parts;
    int id;
    int duration;
    List<Skill> skills;
    Skill skill;
    int[] predecessors;

    for (int i = 0; i < numTasks; ++i) {
      line = reader.readLine();

      parts = line.split("\\s+");
      id = Integer.parseInt(parts[0]);
      duration = Integer.parseInt(parts[1]);

      skills = new ArrayList<>();
      int skillsIterator = 2;
      while (parts.length > skillsIterator && Character.isLetter(parts[skillsIterator].charAt(0))) {
        skill = new Skill(parts[skillsIterator].substring(0, parts[2].length() -1), Integer.parseInt(parts[skillsIterator + 1]));
        skills.add(skill);
        skillsIterator += 2;
      }
      predecessors = new int[parts.length - skillsIterator];
      predecessors = readPredecessors(predecessors, parts, skillsIterator);

      tasks[i] = new Task(id, skills.toArray(new Skill[skills.size()]), duration, predecessors);
    }

    return tasks;
  }

  /**
   * Creates an array of predecessors from parts containing the ids.
   *
   * @param predecessors array to fill
   * @param parts line readDefinition from the file containing ids of the predecessors
   * @param startingIndex index from which to start reading the predecessors
   * @return array of predecessors
   */
  private int[] readPredecessors(int[] predecessors, String[] parts, int startingIndex) {
    for (int i = 0; i < predecessors.length; ++i) {
      predecessors[i] = Integer.parseInt(parts[i + startingIndex]);
    }

    return predecessors;
  }

  /**
   * Reads a solution file saving all the assignments into
   * the given problem
   *
   * @param fileName solution file
   * @param schedule problem to fill
   * @return scheduled with assignments read from the file
   */
  public Schedule readSolution(String fileName, Schedule schedule) {
    BufferedReader reader;
    try {
      reader = new BufferedReader(new FileReader(fileName));
    } catch (FileNotFoundException e) {
      LOGGER.log(Level.FINE, e.toString());
      return null;
    }
    String line;

    try {
      // skip headers
      reader.readLine();
      line = reader.readLine();
      while (line != null) {
        String parts[] = line.split("\\s");
        int timestamp = Integer.parseInt(parts[0]);
        for (int i = 1; i < parts.length; ++i) {
          String[] idParts = parts[i].split("-");
          int resourceId = Integer.parseInt(idParts[0]);
          int taskId = Integer.parseInt(idParts[1]);
          schedule.assign(taskId, resourceId, timestamp);
        }
        line = reader.readLine();
      }

    } catch (IOException e) {
      LOGGER.log(Level.FINE, e.toString());
      return null;
    } finally {
      closeReader(reader);
    }
    return schedule;
  }

  /**
   * Saves a problem to the file.
   *
   * @param schedule problem to save
   * @param filename path to the file
   * @throws IOException
   */
  // TODO: taken from legacy code - refactor!
  public void write(Schedule schedule, String filename)
      throws IOException {
    PrintWriter writer = new PrintWriter(new FileWriter(filename));
    Map<Integer, List<Task>> map = new TreeMap<Integer, List<Task>>();
    writer.write("Hour \t Resource assignments (resource ID - task ID) \n");
    for (Task t : schedule.getTasks()) {
      if (!map.containsKey(t.getStart())) {
        map.put(t.getStart(), new LinkedList<Task>());
      }
      map.get(t.getStart()).add(t);
    }
    for (int i : map.keySet()) {
      writer.write(i + " ");
      for (Task t : map.get(i)) {
        writer.write(t.getResourceId() + "-" + t.getId() + " ");
      }
      writer.write("\n");
    }
    writer.close();

  }

}
