package org.jaxrx;

import org.jaxrx.core.JaxRxException;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

import com.sun.jersey.spi.container.servlet.ServletContainer;

/**
 * This is the main class to start the Jetty server to offer RESTful web
 * services support.
 * 
 * @author Sebastian Graf, Christian Gruen, Lukas Lewandowski,
 *         University of Konstanz
 * 
 */
public final class JettyServer {
  /**
   * Server reference.
   */
  private transient Server server;

  /**
   * Constructor, creating and starting a new server. 
   * 
   * @param port web server port
   * @throws Exception exception
   */
  public JettyServer(final int port) throws Exception {
    server = new Server(port);
    register(server);
    server.start();
  }

  /**
   * Constructor, attaching JAX-RX to the specified server.
   * 
   * @param s server instance
   */
  public static void register(final Server s) {
    final ServletHolder sh = new ServletHolder(ServletContainer.class);
    sh.setInitParameter(
        "com.sun.jersey.config.property.resourceConfigClass",
        "com.sun.jersey.api.core.PackagesResourceConfig");
    sh.setInitParameter("com.sun.jersey.config.property.packages",
        "org.jaxrx.resource");

    new Context(s, "/", Context.SESSIONS).addServlet(sh, "/");
  }

  /**
   * Stops the server.
   */
  public void stop() {
    try {
      server.stop();
    } catch(final Exception exce) {
      throw new JaxRxException(exce);
    }
  }
}
