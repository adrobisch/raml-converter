package raml.tools.util;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LogUtil {

  private static Logger logger = Logger.getLogger(LogUtil.class.getName());

  public static <T extends Exception> T loggedException(T e) {
    logger.log(Level.SEVERE, e.getMessage(), e);
    return e;
  }

  public static <T extends Exception> T loggedException(Logger logger, T e) {
    logger.log(Level.SEVERE, e.getMessage(), e);
    return e;
  }

}
