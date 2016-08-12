package raml.converter.maven;

import org.apache.maven.project.MavenProject;
import org.junit.Test;
import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;

public class RamlHtmlMojoTest {

  MavenProject mavenProject = mock(MavenProject.class, RETURNS_DEEP_STUBS);

  @Test
  public void returnsProperOutputFileForUrl() {
    given(mavenProject.getBuild().getDirectory()).willReturn("buildDir");

    File outputFile = new RamlHtmlMojo()
            .withProject(mavenProject)
            .getOutputFile(getClass().getClassLoader().getResource("test.raml"));

    assertThat(outputFile.getName()).isEqualTo("test.raml.html");
    assertThat(outputFile.getParentFile().getName()).isEqualTo("raml");
    assertThat(outputFile.getParentFile().getParentFile().getName()).isEqualTo("buildDir");
  }

}