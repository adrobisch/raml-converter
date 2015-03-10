package com.asciidoc.extension.raml;

import org.asciidoctor.ast.AbstractBlock;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RamlResourceMacro extends RamlBlockMacro {
  private static final String PATH_ATTRIBUTE = "path";

  public RamlResourceMacro(String macroName, Map<String, Object> config) {
    super(macroName, config);
  }

  @Override
  protected Object process(AbstractBlock parent, String ramlFileName, Map<String, Object> attributes) {
    String pathAttribute = requireAttribute(PATH_ATTRIBUTE, attributes.get(PATH_ATTRIBUTE), String.class);
    String resourceHtml = htmlRenderer(parent, ramlFileName).renderResource(pathAttribute);
    return createBlock(parent, "pass", Arrays.asList(resourceHtml), attributes, new HashMap<>());
  }
}
