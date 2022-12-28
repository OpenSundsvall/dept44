package se.sundsvall.dept44.configuration.feign.logging.util;

import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.util.SocketUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

@SuppressWarnings("deprecation")
public abstract class FeignHttpServerRunner {
	protected static HttpServer server;
	protected static int port;
	
	@BeforeAll
	static void setupServer() throws IOException {
		port = SocketUtils.findAvailableTcpPort();
		server = HttpServer.create(new InetSocketAddress(port), 0);
		server.createContext("/get/string", exchange -> {
			String response = "response";
			exchange.sendResponseHeaders(200, response.length());
			try (OutputStream os = exchange.getResponseBody()) {
				os.write(response.getBytes());
			}
		});
		server.createContext("/post/bad-request", exchange -> {
			String response = "response";
			exchange.sendResponseHeaders(400, response.length());
			try (OutputStream os = exchange.getResponseBody()) {
				os.write(response.getBytes());
			}
		});
		server.setExecutor(null);
		server.start();
	}

	@AfterAll
	static void destroyServer() {
		server.stop(1);
	}
}
