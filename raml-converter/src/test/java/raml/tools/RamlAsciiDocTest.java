package raml.tools;

import org.junit.Test;
import raml.tools.ascii.RamlAsciiDoc;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class RamlAsciiDocTest {

  @Test
  public void shouldConvertAsciiDocWithRamlResource() {
    InputStream testInput = getClass().getClassLoader().getResourceAsStream("docWithSongResource.adoc");
    assertThat(new RamlAsciiDoc().render(testInput)).contains("filter the songs by genre");
  }

  @Test
  public void shouldConvertAsciiDocWithRamlHeaders() {
    InputStream testInput = getClass().getClassLoader().getResourceAsStream("docWithRamlHeaders.adoc");
    String headersList = new RamlAsciiDoc().render(testInput);

    assertThat(headersList)
      .contains("a header required by the vendor")
      .contains("X-Vendor-Header");
  }

  @Test
  public void shouldConvertAsciiDocWithRamlHeadersForResponse() {
    InputStream testInput = getClass().getClassLoader().getResourceAsStream("docWithRamlResponseHeaders.adoc");
    String headersList = new RamlAsciiDoc().render(testInput);

    assertThat(headersList)
      .contains("a response header")
      .contains("X-Vendor-Response-Header");
  }

}
