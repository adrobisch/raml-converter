package raml.tools.model;

import org.apache.commons.codec.digest.DigestUtils;
import org.raml.model.Action;
import org.raml.model.ActionType;
import org.raml.model.Resource;
import org.raml.model.SecurityReference;
import org.raml.model.parameter.UriParameter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ResourceContext {
  Resource resource;

  public ResourceContext(Resource resource) {
    this.resource = resource;
  }

  public String getUniqueId() {
    return DigestUtils.md5Hex(getParentUrl() + getRelativeUri());
  }

  public List<ResourceContext> getResources() {
    List<ResourceContext> resources = new ArrayList<ResourceContext>();
    for (Resource subResource : resource.getResources().values()) {
      resources.add(new ResourceContext(subResource));
    }
    return resources;
  }

  public String getType() {
    return resource.getType();
  }

  public String getDescription() {
    return resource.getDescription();
  }

  public String getDisplayName() {
    return resource.getDisplayName();
  }

  public Map<String, UriParameter> getUriParameters() {
    return resource.getUriParameters();
  }

  public java.util.Collection<ActionContext> getActions() {
    List<ActionContext> actions = new ArrayList<ActionContext>();
    for (Action action : resource.getActions().values()) {
      actions.add(new ActionContext(action));
    }
    return actions;
  }

  public String getRelativeUri() {
    return resource.getRelativeUri();
  }

  public String getParentUrl() {
    return resource.getParentUri();
  }

  public Action getAction(String name) {
    return resource.getAction(name);
  }

  public Resource getResource(String path) {
    return resource.getResource(path);
  }

  public String getUri() {
    return resource.getUri();
  }

  public List<SecurityReference> getSecuredBy() {
    return resource.getSecuredBy();
  }

  public Action getAction(ActionType name) {
    return resource.getAction(name);
  }

  public List<String> getIs() {
    return resource.getIs();
  }

  public Map<String, List<UriParameter>> getBaseUriParameters() {
    return resource.getBaseUriParameters();
  }
}
