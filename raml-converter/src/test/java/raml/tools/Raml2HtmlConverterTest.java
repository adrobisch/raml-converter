package raml.tools;

import org.junit.Test;
import raml.tools.html.Raml2HtmlConverter;
import raml.tools.util.IoUtil;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

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

  private String generateHtml(String ramlFile) {
    InputStream ramlInput = getClass().getClassLoader().getResourceAsStream(ramlFile);
    String ramlHtml = new Raml2HtmlConverter().convert(ramlInput, new ByteArrayOutputStream()).toString();
    IoUtil.writeToFile(ramlHtml, "target/test.html");
    return ramlHtml;
  }
}