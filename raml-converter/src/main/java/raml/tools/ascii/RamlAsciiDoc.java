package raml.tools.ascii;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.AttributesBuilder;
import org.asciidoctor.OptionsBuilder;

import java.io.File;
import java.io.InputStream;

import static org.asciidoctor.Asciidoctor.Factory.create;

public class RamlAsciiDoc {

  public String convert(File file, File outputFile) {
    return asciiDoctor().convertFile(file, options(attributes()).toFile(outputFile));
  }

  public String convert(InputStream asciiDocStream) {
    return asciiDoctor().convert(convertStreamToString(asciiDocStream), options(attributes()));
  }

  public String render(InputStream asciiDocStream) {
    return asciiDoctor().render(convertStreamToString(asciiDocStream), options(attributes()));
  }

  public String render(File inputFile, File outputFile) {
    return asciiDoctor().renderFile(inputFile, options(attributes()).toFile(outputFile));
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
