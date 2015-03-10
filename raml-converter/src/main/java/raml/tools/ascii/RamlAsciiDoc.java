package raml.tools.ascii;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.AttributesBuilder;
import org.asciidoctor.OptionsBuilder;

import java.io.File;
import java.io.InputStream;

import static org.asciidoctor.Asciidoctor.Factory.create;

public class RamlAsciiDoc {

  public String render(InputStream asciiDocStream) {
    return asciiDoctor().render(convertStreamToString(asciiDocStream), options(attributes()));
  }

  private Asciidoctor asciiDoctor() {
    return create();
  }

  private AttributesBuilder attributes() {
    return AttributesBuilder.attributes()
      .backend("html");
  }

  private OptionsBuilder options(AttributesBuilder attributesBuilder) {
    return OptionsBuilder.options()
      .attributes(attributesBuilder);
  }

  protected String convertStreamToString(java.io.InputStream is) {
    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
    return s.hasNext() ? s.next() : "";
  }

}
