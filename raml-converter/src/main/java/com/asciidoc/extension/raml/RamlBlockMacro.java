package com.asciidoc.extension.raml;

import com.github.jknack.handlebars.Handlebars;
import org.asciidoctor.ast.AbstractBlock;
import org.asciidoctor.extension.BlockMacroProcessor;
import raml.tools.handlebars.HandlebarsFactory;
import raml.tools.html.Raml2HtmlRenderer;
import raml.tools.model.RamlContext;
import raml.tools.util.RamlParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

public abstract class RamlBlockMacro extends BlockMacroProcessor {

  Handlebars handlebars = createHandleBars();

  public RamlBlockMacro(String macroName, Map<String, Object> config) {
    super(macroName, config);
  }

  protected Handlebars createHandleBars() {
    return HandlebarsFactory.defaultHandlebars();
  }

  protected Raml2HtmlRenderer htmlRenderer(AbstractBlock parent, String ramlFileName) {
    return new Raml2HtmlRenderer(ramlContext(parent, ramlFileName), handlebars);
  }

  RamlContext ramlContext(AbstractBlock parent, String ramlFileName) {
    try {
      File docfile = new File(parent.document().attributes().get("docdir").toString() + File.separatorChar + ramlFileName);
      return new RamlContext(new RamlParser().parseRaml(docfile.getParent(), new FileInputStream(docfile)));
    } catch (Exception e) {
      return new RamlContext(new RamlParser().parseRaml(classPathStream(ramlFileName)));
    }
  }

  private InputStream classPathStream(String path) {
    return getClass().getClassLoader().getResourceAsStream(path);
  }

  protected <T> T requireAttribute(String name, Object value, Class<T> clazz) {
    if (value == null) {
      throw new IllegalArgumentException("value is required for " + name);
    }
    return getAttribute(value, clazz);
  }

  protected <T> T getAttribute(Object value, Class<T> clazz) {
    return clazz.cast(value);
  }

}
