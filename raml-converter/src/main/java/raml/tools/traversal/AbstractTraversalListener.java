package raml.tools.traversal;

import org.raml.model.Raml;
import org.raml.model.Resource;
import raml.tools.model.ApiInformation;

public class AbstractTraversalListener implements RamlTraversalListener {
  @Override
  public void startTraversal(Raml raml) {
  }

  @Override
  public void endTraversal(Raml raml) {
  }

  @Override
  public void startApiInformation(ApiInformation apiInformation) {
  }

  @Override
  public void startResource(String name, Resource resource) {
  }

  @Override
  public void endResource(String name, Resource resource) {
  }
}
