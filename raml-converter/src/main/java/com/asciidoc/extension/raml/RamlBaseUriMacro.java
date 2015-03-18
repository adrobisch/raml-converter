package com.asciidoc.extension.raml;

import org.asciidoctor.ast.AbstractBlock;
import org.asciidoctor.extension.InlineMacroProcessor;
import raml.tools.util.LogUtil;

import java.util.HashMap;
import java.util.Map;

public class RamlBaseUriMacro extends InlineMacroProcessor {
  public RamlBaseUriMacro(String macroName, Map<String, Object> config) {
    super(macroName, config);
  }

  @Override
  protected Object process(AbstractBlock parent, String ramlFileName, Map<String, Object> attributes) {
    try {
      String baseUri = new RamlContextFactory()
        .ramlContext(parent.document().attributes().get("docdir").toString(), ramlFileName)
        .getBaseUri();

      return createInline(parent, "pass", baseUri, attributes, new HashMap<String, Object>());
    } catch (Exception e) {
      throw LogUtil.loggedException(new RuntimeException(e));
    }
  }
}
