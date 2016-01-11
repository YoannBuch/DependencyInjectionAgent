package diagent;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response.Status;

public class DependencyGraphWebServer extends NanoHTTPD {

	private static ObjectMapper mapper = new ObjectMapper();

	public DependencyGraphWebServer(int port) {
		super(port);
	}

	@Override
	public Response serve(IHTTPSession session) {

		if (isDependencyGraphRequested(session)) {

			return getResponseForDependencyGraphRequest();
			
		} else {
			
			return getResponseForIndexRequest();
		}	
	}

	private Response getResponseForIndexRequest() {
		return newFixedLengthResponse(getIndexHtmlContent());
	}

	private Response getResponseForDependencyGraphRequest() {
		try {
			byte[] jsonBytes = mapper.writeValueAsBytes(DependencyGraphService.getDependencyGraph());

			return newFixedLengthResponse(Status.ACCEPTED, "application/json", new ByteArrayInputStream(jsonBytes),
					jsonBytes.length);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Could not create response for dependency graph", e);
		}
	}

	private boolean isDependencyGraphRequested(IHTTPSession session) {
		return session.getUri().endsWith(".json");
	}

	private String getIndexHtmlContent() {
		try {
			InputStream is = getClass().getResourceAsStream("/index.html");
			String indexHtml = IOUtils.toString(is, "UTF-8");

			return indexHtml;
		} catch (IOException e) {
			throw new RuntimeException("Could not generate content for index.html", e);
		}
	}
}
