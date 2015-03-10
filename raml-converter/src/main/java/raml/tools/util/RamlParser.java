package raml.tools.util;

import org.raml.model.Raml;
import org.raml.parser.loader.DefaultResourceLoader;
import org.raml.parser.visitor.RamlDocumentBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class RamlParser {

  public Raml parseRaml(InputStream ramlStream) {
    return new RamlDocumentBuilder().build(ramlStream);
  }

  public Raml parseRaml(String basePath, InputStream ramlStream) {
    if (basePath == null) {
      return parseRaml(ramlStream);
    } else {
      return new RamlDocumentBuilder(RamlParser.basePathLoader(basePath)).build(ramlStream);
    }
  }

  public static DefaultResourceLoader basePathLoader(final String basePath) {
    return new DefaultResourceLoader() {
      @Override
      public InputStream fetchResource(String resourceName) {
        if (basePath == null) {
          return super.fetchResource(resourceName);
        }

        File ramlFileAtBasePath = new File(basePath + File.separatorChar + resourceName);
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
}
