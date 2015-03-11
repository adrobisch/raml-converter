package com.asciidoc.extension.raml;

import org.asciidoctor.ast.AbstractBlock;
import raml.tools.util.LogUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RamlResourceMacro extends RamlBlockMacro {
  public RamlResourceMacro(String macroName, Map<String, Object> config) {
    super(macroName, config);
  }

  @Override
  protected Object process(AbstractBlock parent, String ramlFileName, Map<String, Object> attributes) {
    try {
      String resourceHtml = htmlRenderer(parent, ramlFileName).renderResource(new RamlMacroAttributes(attributes).path());
      return createBlock(parent, "pass", Arrays.asList(resourceHtml), attributes, new HashMap<>());
    } catch (Exception e) {
      throw LogUtil.loggedException(new RuntimeException(e));
    }
  }
}
