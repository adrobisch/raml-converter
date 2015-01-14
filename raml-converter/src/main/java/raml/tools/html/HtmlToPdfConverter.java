package raml.tools.html;

import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextOutputDevice;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.pdf.ITextUserAgent;
import org.xhtmlrenderer.resource.XMLResource;
import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class HtmlToPdfConverter {

  public static void createPDF(String xhtmlString, OutputStream os) {
    try {
      ITextRenderer renderer = new ITextRenderer();
      ResourceLoaderUserAgent callback = new ResourceLoaderUserAgent(renderer.getOutputDevice());
      callback.setSharedContext(renderer.getSharedContext());
      renderer.getSharedContext ().setUserAgentCallback(callback);

      Document doc = XMLResource.load(new InputSource(new ByteArrayInputStream(xhtmlString.getBytes()))).getDocument();

      renderer.setDocument(doc, xhtmlString);
      renderer.layout();
      renderer.createPDF(os);

      os.close();
      os = null;
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      if (os != null) {
        try {
          os.close();
        } catch (IOException e) {
          // ignore
        }
      }
    }
  }

  private static class ResourceLoaderUserAgent extends ITextUserAgent
  {
    public ResourceLoaderUserAgent(ITextOutputDevice outputDevice) {
      super(outputDevice);
    }

    protected InputStream resolveAndOpenStream(String uri) {
      InputStream is = super.resolveAndOpenStream(uri);
      return is;
    }
  }
}
