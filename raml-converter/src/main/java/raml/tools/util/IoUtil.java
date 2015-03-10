package raml.tools.util;

import java.io.*;

public class IoUtil {
  public static String contentFromFile(String fileName) {
    return convertStreamToString(IoUtil.class.getClassLoader().getResourceAsStream(fileName));
  }

  public static String convertStreamToString(java.io.InputStream is) {
    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
    return s.hasNext() ? s.next() : "";
  }

  public static void writeToFile(String content, String outputFilePath) {
    FileOutputStream fileOutput = null;
    try {
      fileOutput = new FileOutputStream(new File(outputFilePath));
      fileOutput.write(content.getBytes("UTF-8"));
    } catch (FileNotFoundException | UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException();
    } finally {
      if (fileOutput != null) {
        try {
          fileOutput.flush();
          fileOutput.close();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    }
  }
}
