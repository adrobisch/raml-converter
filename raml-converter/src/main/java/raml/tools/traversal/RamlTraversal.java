package raml.tools.traversal;

import org.raml.model.Raml;
import org.raml.model.Resource;
import raml.tools.model.ApiInformation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RamlTraversal {
  Raml raml;
  List<RamlTraversalListener> traversalListeners = new ArrayList<RamlTraversalListener>();

  public RamlTraversal(Raml raml) {
    if (raml == null) throw new IllegalArgumentException("raml must not be null");
    if (traversalListeners == null) throw new IllegalArgumentException("listeners must not be null");

    this.raml = raml;
  }

  public RamlTraversal withListener(RamlTraversalListener listener) {
    traversalListeners.add(listener);
    return this;
  }

  public void traverse() {
    notifyStartTraversal();
    notifyStartApiInformation();

    for (final Map.Entry<String, Resource> resource : raml.getResources().entrySet()) {
      eachListener(new ListenerFunction() {
        @Override
        public void apply(RamlTraversalListener listener) {
          listener.startResource(resource.getKey(), resource.getValue());
          // ACTIONS etc.
          listener.endResource(resource.getKey(), resource.getValue());
        }
      });
    }

    notifyEndTraversal();
  }

  private void notifyEndTraversal() {
    eachListener(new ListenerFunction() {
      @Override
      public void apply(RamlTraversalListener listener) {
        listener.endTraversal(raml);
      }
    });
  }

  private void notifyStartApiInformation() {
    eachListener(new ListenerFunction() {
      @Override
      public void apply(RamlTraversalListener listener) {
        listener.startApiInformation(new ApiInformation(raml.getTitle(), raml.getVersion(), raml.getBaseUri()));
      }
    });
  }

  private void notifyStartTraversal() {
    eachListener(new ListenerFunction() {
      @Override
      public void apply(RamlTraversalListener listener) {
        listener.startTraversal(raml);
      }
    });
  }

  public void eachListener(ListenerFunction listenerFunction) {
    for (RamlTraversalListener listener :  traversalListeners) {
      listenerFunction.apply(listener);
    }
  }

  public interface ListenerFunction {
    public void apply(RamlTraversalListener listener);
  }
}
