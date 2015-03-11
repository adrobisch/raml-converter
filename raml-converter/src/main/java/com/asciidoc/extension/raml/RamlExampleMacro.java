package com.asciidoc.extension.raml;

import org.asciidoctor.ast.AbstractBlock;
import raml.tools.util.LogUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RamlExampleMacro extends RamlBlockMacro  {
  public RamlExampleMacro(String macroName, Map<String, Object> config) {
    super(macroName, config);
  }

  @Override
  protected Object process(AbstractBlock parent, String ramlFileName, Map<String, Object> attributes) {
    try {
      RamlMacroAttributes macroAttributes = new RamlMacroAttributes(attributes);

      String exampleHtml = htmlRenderer(parent, ramlFileName).renderExample(macroAttributes.path(),
        macroAttributes.method(),
        macroAttributes.status(),
        macroAttributes.mimeType());

      return createBlock(parent, "pass", Arrays.asList(exampleHtml), attributes, new HashMap<>());
    } catch (Exception e) {
      throw LogUtil.loggedException(new RuntimeException(e));
    }
  }

}
