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

import static java.lang.String.format;

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

  public String renderExample(String uri, String method, String status, String mimeType) {
    Action action = getResourceContext(uri).getAction(ActionType.valueOf(method.toUpperCase()));

    String exampleFormat = "<div class=\"listingblock\">" +
      "<div class=\"content\">" +
      "<pre class=\"CodeRay highlight raml_example\">%s</pre>" +
      "</div>" +
      "</div>";

    if (status != null) {
      String exampleForResponse = getResponseForAction(action, status).getBody().get(mimeType).getExample();
      return String.format(exampleFormat, exampleForResponse);
    } else {
      return String.format(exampleFormat, action.getBody().get(mimeType).getExample());
    }
  }

  public String renderStatusCodeList(String uri, String method, String status) {
    Action action = getResourceContext(uri).getAction(ActionType.valueOf(method.toUpperCase()));
    if (status != null) {
      return renderClassPathTemplate("header_list.hbs", getResponseForAction(action, status).getHeaders());
    }
    return renderClassPathTemplate("header_list.hbs", action.getHeaders());
  }

  public String renderHeaderList(String uri, String method, String status) {
    Action action = getResourceContext(uri).getAction(ActionType.valueOf(method.toUpperCase()));
    if (status != null) {
      return renderClassPathTemplate("header_list.hbs", getResponseForAction(action, status).getHeaders());
    }
    return renderClassPathTemplate("header_list.hbs", action.getHeaders());
  }

  protected Response getResponseForAction(Action action, String status) {
    for (Map.Entry<String, Response> response: action.getResponses().entrySet()) {
      if (response.getKey().equals(status)) {
        return response.getValue();
      }
    }
    throw new IllegalArgumentException(format("no response found for status %s in action %s", status, action));
  }

  public String renderRamlContext(String templateContent) {
    return renderHandlebars(templateContent, raml);
  }

  private ResourceContext getResourceContext(String uri) {
    // FIXME: support more the one level of subresources
    for (ResourceContext resourceContext: raml.getResources()) {
      if (resourceContext.getUri().equals(uri)) {
        return resourceContext;
      }

      for (ResourceContext subResource: resourceContext.getResources()) {
        if (subResource.getUri().equals(uri)) {
          return subResource;
        }
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
