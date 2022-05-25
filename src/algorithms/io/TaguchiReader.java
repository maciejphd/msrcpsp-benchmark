package algorithms.io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

/**
 * Reader for Taguchi matrices
 */
public class TaguchiReader extends BaseIO {

  public ArrayList<ArrayList<String>> read(String filePath) {
    BufferedReader reader;
    try {
      reader = new BufferedReader(new FileReader(filePath));
    } catch (FileNotFoundException e) {
      LOGGER.log(Level.FINE, e.toString());
      return null;
    }
    ArrayList<ArrayList<String>> result = new ArrayList<>();
    String line;
    String[] parts;
    try {
      while ((line = reader.readLine()) != null) {
        ArrayList<String> row = new ArrayList<>();
        parts = line.split("\\s");
        Arrays.stream(parts).forEach(row::add);
        result.add(row);
      }

    } catch (IOException e) {
      LOGGER.log(Level.FINE, e.toString());
      return null;
    } finally {
      closeReader(reader);
    }
    return result;
  }

}
