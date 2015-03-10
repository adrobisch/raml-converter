package raml.tools.html;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.StringTemplateSource;
import org.raml.model.Action;
import org.raml.model.ActionType;
import org.raml.model.Response;
import raml.tools.model.RamlContext;
import raml.tools.model.ResourceContext;
import raml.tools.util.IoUtil;

import java.io.IOException;
import java.util.Map;

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

  public String renderFull(String mainTemplateFile) {
    return renderClassPathTemplate(orDefault(mainTemplateFile, "template.hbs"), raml);
  }

  public String renderResource(String uri) {
    return renderResource(uri, null);
  }

  public String renderResource(String uri, String resourceTemplateFile) {
    return renderClassPathTemplate(orDefault(resourceTemplateFile, "resource.hbs"), getResourceContext(uri));
  }

  public String renderHeaderList(String uri, String method, String status) {
    Action action = getResourceContext(uri).getAction(ActionType.valueOf(method.toUpperCase()));
    if (status != null) {
      return renderResponseHeaders(uri, method, status, action);
    }
    return renderClassPathTemplate("header_list.hbs", action.getHeaders());
  }

  protected String renderResponseHeaders(String uri, String method, String status, Action action) {
    for (Map.Entry<String, Response> response: action.getResponses().entrySet()) {
      if (response.getKey().equals(status)) {
        return renderClassPathTemplate("header_list.hbs", response.getValue().getHeaders());
      }
    }
    throw new IllegalArgumentException(String.format("status %s does not exist for uri %s and method %s", status, uri, method));
  }

  public String renderRamlContext(String templateContent) {
    return renderHandlebars(templateContent, raml);
  }

  private ResourceContext getResourceContext(String uri) {
    for (ResourceContext resourceContext: raml.getResources()) {
      if (resourceContext.getUri().equals(uri)) {
        return resourceContext;
      }
    }
    throw new IllegalArgumentException("resource not found in RAML: " + uri);
  }

  String renderClassPathTemplate(String classPathTemplate, Object context) {
    return renderHandlebars(fileContent(classPathTemplate), context);
  }

  private String renderHandlebars(String templateContent, Object context) {
    try {
      Template template = handlebars.compile(new StringTemplateSource(
          "raml_template", templateContent)
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
