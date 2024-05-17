package ftvp.earthquakeapp.persistence.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;

public class RequestMaker {

    OkHttpClient client = new OkHttpClient();
    ObjectMapper mapper = new ObjectMapper();

    String protocol;
    String host;
    String path;

    public RequestMaker() {}

    public RequestMaker(String protocol, String host, String path) {
        this.protocol = protocol;
        this.host = host;
        this.path = path;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getHost() {
        return host;
    }

    public String getPath() {
        return path;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
