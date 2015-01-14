package raml.tools.model;

import org.raml.model.*;
import org.raml.model.parameter.UriParameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RamlContext {
  Raml raml;

  public RamlContext(Raml raml) {
    this.raml = raml;
  }

  public List<ResourceContext> getResources() {
    List<ResourceContext> resources = new ArrayList<ResourceContext>();
    for (Resource resource : raml.getResources().values()) {
      resources.add(new ResourceContext(resource));
    }
    return resources;
  }

  public List<DocumentationItem> getDocumentation() {
    return raml.getDocumentation();
  }

  public String getTitle() {
    return raml.getTitle();
  }

  public String getVersion() {
    return raml.getVersion();
  }

  public String getBaseUri() {
    return raml.getBaseUri();
  }

  public String getBasePath() {
    return raml.getBasePath();
  }

  public String getUri() {
    return raml.getUri();
  }

  public String getMediaType() {
    return raml.getMediaType();
  }

  public Map<String, UriParameter> getBaseUriParameters() {
    return raml.getBaseUriParameters();
  }

  public List<Map<String, org.raml.model.Template>> getResourceTypes() {
    return raml.getResourceTypes();
  }

  public List<Map<String, org.raml.model.Template>> getTraits() {
    return raml.getTraits();
  }

  public List<Map<String, String>> getSchemas() {
    return raml.getSchemas();
  }

  public List<Protocol> getProtocols() {
    return raml.getProtocols();
  }

  public List<Map<String, SecurityScheme>> getSecuritySchemes() {
    return raml.getSecuritySchemes();
  }

  public List<SecurityReference> getSecuredBy() {
    return raml.getSecuredBy();
  }

  public Map<String, SchemaContext> getConsolidatedSchemas() {
    Map<String, SchemaContext> schemas = new HashMap<>();

    for (Map.Entry<String, String> schema : raml.getConsolidatedSchemas().entrySet()) {
      schemas.put(schema.getKey(), new SchemaContext(schema.getKey(), schema.getValue()));
    }

    return schemas;
  }
}
