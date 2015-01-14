package raml.tools;

import org.raml.model.Raml;

public class RamlTemplateInput {
  Raml raml;

  public RamlTemplateInput(Raml raml) {
    this.raml = raml;
  }

  public Raml getRaml() {
    return raml;
  }
}
