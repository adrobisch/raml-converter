package com.asciidoc.extension.raml;

import com.github.jknack.handlebars.Handlebars;
import org.asciidoctor.ast.AbstractBlock;
import org.asciidoctor.extension.BlockMacroProcessor;
import raml.tools.handlebars.HandlebarsFactory;
import raml.tools.html.Raml2HtmlRenderer;
import java.util.Map;
import java.util.logging.Logger;

import static raml.tools.util.LogUtil.loggedException;

public abstract class RamlBlockMacro extends BlockMacroProcessor {

  Logger logger = Logger.getLogger(getClass().getName());

  Handlebars handlebars = createHandleBars();

  RamlContextFactory ramlContextFactory = new RamlContextFactory();

  public RamlBlockMacro(String macroName, Map<String, Object> config) {
    super(macroName, config);
  }

  protected Handlebars createHandleBars() {
    return HandlebarsFactory.defaultHandlebars();
  }

  protected Raml2HtmlRenderer htmlRenderer(AbstractBlock parent, String ramlFileName) {
    try {
      return new Raml2HtmlRenderer(getRamlContextFactory().ramlContext(parent, ramlFileName), handlebars);
    } catch(Exception e) {
      throw loggedException(logger, new RuntimeException(e));
    }
  }

  public RamlContextFactory getRamlContextFactory() {
    return ramlContextFactory;
  }
}
