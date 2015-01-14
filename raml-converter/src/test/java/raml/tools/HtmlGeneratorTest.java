package raml.tools;

import org.junit.Test;
import org.raml.model.Raml;
import org.raml.parser.visitor.RamlDocumentBuilder;
import raml.tools.html.HtmlGenerator;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class HtmlGeneratorTest {
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
    Raml raml = new RamlDocumentBuilder().build(ramlInput);
    HtmlGenerator htmlGenerator = spy(HtmlGenerator.build().build());
    new RamlTraversal(raml).withListener(htmlGenerator).traverse();
    IoUtil.writeToFile(htmlGenerator.getOutput(), "target/test.html");
    return htmlGenerator.getOutput();
  }
}