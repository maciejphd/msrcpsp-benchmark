package algorithms.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Generic class for all IO classes.
 * Use specific classes to load specific problems. This class
 * is only to encapsulate common methods associated with
 * handling files.
 */
public class BaseIO {

  protected static final Logger LOGGER = Logger.getLogger( BaseIO.class.getName() );

  /**
   * Closes the reader. Should be used in <code>finally</code>
   * part of reading the file.
   *
   * @param reader closed reader
   */
  protected void closeReader(BufferedReader reader) {
    try {
      if (null != reader) {
        reader.close();
      }
    } catch (IOException e) {
      LOGGER.log(Level.FINE, e.toString());
    }
  }

  /**
   * Skips the reader to the line starting with the desired string
   *
   * @param reader used reader
   * @param line current line of the file
   * @param desired desired start of the line
   * @return line, that starts with <code>desired</code> String
   * @throws IOException exception during IO operation
   */
  protected String skipTo(BufferedReader reader, String line, String desired) throws IOException {
    while (null != line && !line.startsWith(desired)) {
      line = reader.readLine();
    }
    return line;
  }

}
