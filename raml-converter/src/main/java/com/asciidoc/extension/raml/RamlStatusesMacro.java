package com.asciidoc.extension.raml;

import org.asciidoctor.ast.AbstractBlock;
import raml.tools.util.LogUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RamlStatusesMacro extends RamlBlockMacro {
  public RamlStatusesMacro(String macroName, Map<String, Object> config) {
    super(macroName, config);
  }

  @Override
  protected Object process(AbstractBlock parent, String ramlFileName, Map<String, Object> attributes) {
    try {
      RamlMacroAttributes macroAttributes = new RamlMacroAttributes(attributes);

      String statusesHtml = htmlRenderer(parent, ramlFileName).renderStatuses(macroAttributes.path(), macroAttributes.method());

      return createBlock(parent, "pass", Arrays.asList(statusesHtml), attributes, new HashMap<>());
    } catch (Exception e) {
      throw LogUtil.loggedException(new RuntimeException(e));
    }
  }
}
