package com.asciidoc.extension.raml;

import com.github.jknack.handlebars.Handlebars;
import org.asciidoctor.ast.AbstractBlock;
import org.asciidoctor.extension.BlockMacroProcessor;
import org.raml.parser.visitor.RamlDocumentBuilder;
import raml.tools.handlebars.HandlebarsFactory;
import raml.tools.html.Raml2HtmlRenderer;
import raml.tools.model.RamlContext;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RamlBlockMacro extends BlockMacroProcessor {

  Map<String, RamlContext> loadedRamlContext = new HashMap<>();
  Handlebars handlebars = HandlebarsFactory.defaultHandlebars();

  public RamlBlockMacro(String macroName, Map<String, Object> config) {
    super(macroName, config);
  }

  @Override
  protected Object process(AbstractBlock parent, String ramlFileName, Map<String, Object> attributes) {
    Raml2HtmlRenderer htmlRenderer = new Raml2HtmlRenderer(ramlContext(ramlFileName), handlebars);

    String resourceHtml = htmlRenderer.renderResource(require("resource", attributes.get("resource")).toString());

    return createBlock(parent, "pass", Arrays.asList(resourceHtml), attributes, new HashMap<>());
  }

  RamlContext ramlContext(String ramlFileName) {
    if (!loadedRamlContext.containsKey(ramlFileName)) {
      loadedRamlContext.put(ramlFileName, new RamlContext(new RamlDocumentBuilder().build(getFileStream(ramlFileName))));
    }
    return loadedRamlContext.get(ramlFileName);
  }

  private InputStream getFileStream(String path) {
    return getClass().getClassLoader().getResourceAsStream(path);
  }

  <T> T require(String name, T value) {
    if (value == null) {
      throw new IllegalArgumentException("value is required for " + name);
    }
    return value;
  }

}
