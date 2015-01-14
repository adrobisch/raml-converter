package raml.tools.model;

import org.raml.model.*;
import org.raml.model.parameter.Header;
import org.raml.model.parameter.QueryParameter;
import org.raml.model.parameter.UriParameter;

import java.util.List;
import java.util.Map;

public class ActionContext {
  Action action;

  public ActionContext(Action action) {
    this.action = action;
  }

  public ActionType getType() {
    return action.getType();
  }

  public String getDescription() {
    return action.getDescription();
  }

  public Map<String, Header> getHeaders() {
    return action.getHeaders();
  }

  public Map<String, QueryParameter> getQueryParameters() {
    return action.getQueryParameters();
  }

  public Map<String, MimeType> getBody() {
    return action.getBody();
  }

  public Map<String, Response> getResponses() {
    return action.getResponses();
  }

  public Resource getResource() {
    return action.getResource();
  }

  public List<String> getIs() {
    return action.getIs();
  }

  public List<Protocol> getProtocols() {
    return action.getProtocols();
  }

  public List<SecurityReference> getSecuredBy() {
    return action.getSecuredBy();
  }

  public Map<String, UriParameter> getUriParameters() {
    // FIXME should return all uri params, not only the most recent ones
    return action.getResource().getUriParameters();
  }
}
