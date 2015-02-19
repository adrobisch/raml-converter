package raml.tools.html;

import org.raml.model.Raml;
import org.raml.parser.loader.DefaultResourceLoader;
import org.raml.parser.visitor.RamlDocumentBuilder;
import raml.tools.RamlTraversal;

import java.io.*;

public class Raml2HtmlConverter {

  String ramlBasePath = null;

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

  public Raml2HtmlConverter withRamlBasePath(String ramlBasePath) {
    this.ramlBasePath = ramlBasePath;
    return this;
  }
}
