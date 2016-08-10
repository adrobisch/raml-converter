package raml.tools;

import jodd.jerry.Jerry;
import org.junit.Test;
import raml.tools.html.Raml2HtmlConverter;
import raml.tools.util.IoUtil;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import static jodd.jerry.Jerry.jerry;

import static org.assertj.core.api.Assertions.assertThat;

public class Raml2HtmlConverterTest {
  @Test
  public void shouldProcessExampleWithOutErrors() {
    String generatedHtml = generateHtml("example.raml");

    assertThat(generatedHtml).isNotNull();
    assertThat(generatedHtml).startsWith("<!DOCTYPE HTML>");
  }

  @Test
  public void generatedHtmlContainsBaseUri() {
    String generatedHtml = generateHtml("music.raml");

    assertThat(generatedHtml).isNotNull();
    assertThat(generatedHtml).contains("http://example.api.com/{version}");
  }

  @Test
  public void requiredAttributesAreParsedCorrectly() {
    String generatedHtml = generateHtml("json_schema_required_properties.raml");
    Jerry doc = jerry(generatedHtml);

    String topLevelRequiredText = doc.$("div[data-property=\"toplevel\"]").$(".property-required .required-text").text();

    assertThat(topLevelRequiredText).isEqualTo("required");
  }

  String generateHtml(String ramlFile) {
    InputStream ramlInput = getClass().getClassLoader().getResourceAsStream(ramlFile);
    String ramlHtml = new Raml2HtmlConverter().convert(ramlInput, new ByteArrayOutputStream()).toString();
    IoUtil.writeToFile(ramlHtml, "target/test.html");
    return ramlHtml;
  }
}