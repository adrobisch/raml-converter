package raml.tools;

import org.junit.Test;
import raml.tools.ascii.RamlAsciiDoc;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class RamlAsciiDocTest {

  @Test
  public void shouldConvertAsciiDocWithRamlResourceBlock() {
    InputStream testInput = getClass().getClassLoader().getResourceAsStream("docWithSongResource.adoc");
    assertThat(new RamlAsciiDoc().render(testInput)).contains("filter the songs by genre");
  }


}
