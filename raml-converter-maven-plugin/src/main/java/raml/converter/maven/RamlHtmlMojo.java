package raml.converter.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import raml.tools.html.Raml2HtmlConverter;

import java.io.*;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;

@Mojo(name = "html", defaultPhase = LifecyclePhase.PACKAGE)
public class RamlHtmlMojo extends AbstractMojo {

  @Parameter(defaultValue = "${project}", required = true, readonly = true)
  private MavenProject project;

  @Parameter(property = "project.compileClasspathElements", required = true, readonly = true)
  private List<String> classpath;

  @Parameter(required = true)
  private String raml;

  @Parameter(required = false)
  private String outputDir = "raml";

  @Parameter(required = false)
  private String extension = "html";

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    for (URL ramlFile : getRamlFiles()) {
      writeHtml(ramlFile);
    }
  }

  private void writeHtml(URL ramlStream) {
    String htmlFileName = raml + "." + extension;
    FileOutputStream fileOutputStream = getFileOutputStream(getOutputFile(htmlFileName));
    getLog().info(format("converting %s to html: %s", raml, htmlFileName));
    try {
      new Raml2HtmlConverter().convert(ramlStream.openStream(), fileOutputStream);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  File getOutputFile(String fileName) {
    String lastSegment = fileName.substring(fileName.lastIndexOf(separatorChar()) + 1);
    return new File(getOutputDirectory().getAbsolutePath() + separatorChar() + lastSegment);
  }

  protected char separatorChar() {
    return File.separatorChar;
  }

  private File getOutputDirectory() {
    File outputDirectory = new File(project.getBuild().getDirectory() + separatorChar() + outputDir);
    return mkdirs(outputDirectory);
  }

  File mkdirs(File outputDirectory) {
    outputDirectory.mkdirs();
    return outputDirectory;
  }

  private FileOutputStream getFileOutputStream(File outputFile) {
    try {
      return new FileOutputStream(outputFile);
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException(outputFile + " is a invalid path.", e);
    }
  }

  Set<URL> getRamlFiles() {
    try {
      Set<URL> urls = new HashSet<URL>();
      String[] ramlFiles = raml.split(",");

      for (String element : classpath) {
        for (String ramlFilePath : ramlFiles) {
          File ramlFile = new File(element + separatorChar() + ramlFilePath);
          if (ramlFile.exists()) {
            urls.add(ramlFile.toURI().toURL());
          }
        }
      }
      return urls;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public RamlHtmlMojo withProject(MavenProject project) {
    this.project = project;
    return this;
  }
}
