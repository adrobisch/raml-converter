package com.asciidoc.extension.raml;

import org.asciidoctor.ast.AbstractBlock;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RamlHeadersMacro extends RamlBlockMacro {
  private static final String PATH_ATTRIBUTE = "path";
  private static final String METHOD_ATTRIBUTE = "method";
  private static final String STATUS_ATTRIBUTE = "status";

  public RamlHeadersMacro(String macroName, Map<String, Object> config) {
    super(macroName, config);
  }

  @Override
  protected Object process(AbstractBlock parent, String ramlFileName, Map<String, Object> attributes) {
    String path = requireAttribute(PATH_ATTRIBUTE, attributes.get(PATH_ATTRIBUTE), String.class);
    String method = requireAttribute(METHOD_ATTRIBUTE, attributes.get(METHOD_ATTRIBUTE), String.class);
    String status = getAttribute(attributes.get(STATUS_ATTRIBUTE), String.class);

    String resourceHtml = htmlRenderer(parent, ramlFileName).renderHeaderList(path, method, status);
    return createBlock(parent, "pass", Arrays.asList(resourceHtml), attributes, new HashMap<>());
  }

}
