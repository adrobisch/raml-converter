package raml.tools.model;

import org.raml.model.*;
import org.raml.model.parameter.Header;
import org.raml.model.parameter.QueryParameter;
import org.raml.model.parameter.UriParameter;

import java.util.*;

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
    return action.getBody() == null ? Collections.<String, MimeType>emptyMap() : action.getBody();
  }

  public Map<String, ResponseContext> getResponses() {
    if (action.getResponses() == null) {
        return Collections.emptyMap();
    }
    return responseContexts();
  }

  private Map<String, ResponseContext> responseContexts() {
    Map<String, ResponseContext> responses = new HashMap<>();
    for (Map.Entry<String, Response> response : action.getResponses().entrySet()) {
        responses.put(response.getKey(), new ResponseContext(response.getValue()));
    }
    return responses;
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
