package raml.tools.html;

import raml.tools.handlebars.HandlebarsFactory;
import raml.tools.model.RamlContext;
import raml.tools.util.RamlParser;

import java.io.*;

public class Raml2HtmlConverter {

  String ramlBasePath = null;
  String mainTemplate = null;

  public void convert(String ramlFilePath, String outputFilePath) {
    try {
      convert(new FileInputStream(new File(ramlFilePath)), new FileOutputStream(new File(outputFilePath)));
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  public <T extends OutputStream> T convert(InputStream ramlFilePath, T outputStream) {
    try {
      String ramlHtml = new Raml2HtmlRenderer(new RamlContext(new RamlParser().parseRaml(ramlBasePath, ramlFilePath)), HandlebarsFactory.defaultHandlebars())
        .renderFull(mainTemplate);
      outputStream.write(ramlHtml.getBytes());
      return outputStream;
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      if (outputStream != null) {
        try {
          outputStream.flush();
          outputStream.close();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    }
  }

  public Raml2HtmlConverter withMainTemplate(String mainTemplate) {
    this.mainTemplate = mainTemplate;
    return this;
  }

  public Raml2HtmlConverter withRamlBasePath(String ramlBasePath) {
    this.ramlBasePath = ramlBasePath;
    return this;
  }

}
