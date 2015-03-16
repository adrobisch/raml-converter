package com.asciidoc.extension.raml;

import org.asciidoctor.ast.AbstractBlock;
import raml.tools.util.LogUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RamlSchemaMacro extends RamlBlockMacro {
  public RamlSchemaMacro(String macroName, Map<String, Object> config) {
    super(macroName, config);
  }

  @Override
  protected Object process(AbstractBlock parent, String ramlFileName, Map<String, Object> attributes) {
    try {
      RamlMacroAttributes macroAttributes = new RamlMacroAttributes(attributes);

      String headersHtml = htmlRenderer(parent, ramlFileName).renderSchema(macroAttributes.name());

      return createBlock(parent, "pass", Arrays.asList(headersHtml), attributes, new HashMap<>());
    } catch (Exception e) {
      throw LogUtil.loggedException(new RuntimeException(e));
    }
  }
}
