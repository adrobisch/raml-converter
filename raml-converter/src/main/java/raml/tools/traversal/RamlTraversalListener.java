package raml.tools.traversal;

import org.raml.model.Raml;
import org.raml.model.Resource;
import raml.tools.model.ApiInformation;

public interface RamlTraversalListener {
  public void startTraversal(Raml raml);
  public void endTraversal(Raml raml);
  public void startApiInformation(ApiInformation apiInformation);
  public void startResource(String name, Resource resource);
  public void endResource(String name, Resource resource);
}
