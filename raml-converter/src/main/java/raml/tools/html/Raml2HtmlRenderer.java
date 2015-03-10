package raml.tools.html;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.StringTemplateSource;
import raml.tools.model.RamlContext;
import raml.tools.model.ResourceContext;
import raml.tools.util.IoUtil;

import java.io.IOException;

public class Raml2HtmlRenderer {

  private final Handlebars handlebars;
  private final RamlContext raml;

  public Raml2HtmlRenderer(RamlContext raml, Handlebars handlebars) {
    this.handlebars = handlebars;
    this.raml = raml;
  }

  public String renderFull() {
    return renderFull(null);
  }

  public String renderFull(String mainTemplate) {
    return renderHtml(orDefault(mainTemplate, "template.hbs"), raml);
  }


  public String renderResource(String uri) {
    return renderResource(uri, null);
  }

  public String renderResource(String uri, String resourceTemplate) {
    return renderHtml(orDefault(resourceTemplate, "resource.hbs"), getResourceContext(uri));
  }

  private ResourceContext getResourceContext(String uri) {
    for (ResourceContext resourceContext: raml.getResources()) {
      if (resourceContext.getUri().equals(uri)) {
        return resourceContext;
      }
    }
    throw new IllegalArgumentException("resource not found in RAML: " + uri);
  }

  String renderHtml(String templateName, Object context) {
    try {
      Template template = handlebars.compile(new StringTemplateSource(
          templateName,
          fileContent(templateName))
      );
      return template.apply(context);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  protected String fileContent(String templateLocation) {
    return IoUtil.contentFromFile(templateLocation);
  }

  <T> T orDefault(T nullable, T defaultValue) {
    if (nullable == null) {
      return defaultValue;
    }
    return nullable;
  }

}
