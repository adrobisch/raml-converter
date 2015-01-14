package raml.tools.html;

import org.raml.model.Raml;
import org.raml.parser.visitor.RamlDocumentBuilder;
import raml.tools.RamlTraversal;

import java.io.*;

public class Raml2HtmlConverter {

  public void convert(String ramlFilePath, String outputFilePath) {
    try {
      convert(new FileInputStream(new File(ramlFilePath)), new FileOutputStream(new File(outputFilePath)));
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  public void convert(InputStream ramlFilePath, OutputStream fileOutput) {
    try {
      HtmlGenerator htmlGenerator = new HtmlGeneratorBuilder().build();
      new RamlTraversal(parseRaml(ramlFilePath)).withListener(htmlGenerator).traverse();

      fileOutput.write(htmlGenerator.getOutput().getBytes());
    } catch (IOException e) {
      throw new RuntimeException(e);
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

  private Raml parseRaml(InputStream ramlStream) {
    return new RamlDocumentBuilder().build(ramlStream);
  }

}
