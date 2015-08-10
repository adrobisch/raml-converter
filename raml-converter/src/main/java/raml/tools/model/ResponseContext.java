package raml.tools.model;

import org.raml.model.MimeType;
import org.raml.model.Response;
import org.raml.model.parameter.Header;

import java.util.Collections;
import java.util.Map;

public class ResponseContext {

    Response response;

    public ResponseContext(Response response) {
        this.response = response;
    }

    public Map<String, Header> getHeaders() {
        return response.getHeaders() == null ? Collections.<String, Header>emptyMap() : response.getHeaders();
    }

    public String getDescription() {
        return response.getDescription();
    }

    public Map<String, MimeType> getBody() {
        return response.getBody() == null ? Collections.<String, MimeType>emptyMap() : response.getBody();
    }

}
