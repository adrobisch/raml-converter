package raml.converter.maven;

import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.apache.maven.project.MavenProject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JMockit.class)
public class RamlHtmlMojoTest {

  @Mocked
  MavenProject mavenProject;

  @Test
  public void shouldSupportBackslashInOutputFileName() {
    new Expectations() {{
      mavenProject.getBuild().getDirectory();
      result = "buildDir";
    }};

    File outputFile = new RamlHtmlMojo() {
      @Override
      protected char separatorChar() {
        return '\\';
      }

      @Override
      File mkdirs(File outputDirectory) {
        return outputDirectory;
      }

    }.withProject(mavenProject).getOutputFile("dir\\test.raml.html");

    assertThat(outputFile.getPath()).endsWith("buildDir\\raml\\test.raml.html");
  }
}