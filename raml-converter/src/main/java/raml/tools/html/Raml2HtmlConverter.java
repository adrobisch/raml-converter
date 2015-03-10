package raml.tools.html;

import org.raml.model.Raml;
import org.raml.parser.loader.DefaultResourceLoader;
import org.raml.parser.visitor.RamlDocumentBuilder;
import raml.tools.handlebars.HandlebarsFactory;
import raml.tools.model.RamlContext;

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
      String ramlHtml = new Raml2HtmlRenderer(new RamlContext(parseRaml(ramlFilePath)), HandlebarsFactory.defaultHandlebars())
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

  protected Raml parseRaml(InputStream ramlStream) {
    return new RamlDocumentBuilder(basePathLoader()).build(ramlStream);
  }

  private DefaultResourceLoader basePathLoader() {
    return new DefaultResourceLoader() {
      @Override
      public InputStream fetchResource(String resourceName) {
        if (ramlBasePath == null) {
          return super.fetchResource(resourceName);
        }

        File ramlFileAtBasePath = new File(ramlBasePath + File.separatorChar + resourceName);
        if (ramlFileAtBasePath.exists()) {
          return getFileInputStream(ramlFileAtBasePath);
        } else {
          return super.fetchResource(resourceName);
        }
      }

      private InputStream getFileInputStream(File ramlFileAtBasePath) {
        try {
          return new FileInputStream(ramlFileAtBasePath);
        } catch (FileNotFoundException e) {
          throw new RuntimeException(e);
        }
      }
    };
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
