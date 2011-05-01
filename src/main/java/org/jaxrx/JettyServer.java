package org.jaxrx;

import org.apache.shiro.web.servlet.IniShiroFilter;
import org.jaxrx.core.JaxRxException;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.FilterHolder;
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
   * Constructor.
   * 
   * @param port web server port
   * @throws Exception exception
   */
  public JettyServer(final int port) throws Exception {
    server = new Server(port);

    final ServletHolder servHolder = new ServletHolder(ServletContainer.class);
    servHolder.setInitParameter(
        "com.sun.jersey.config.property.resourceConfigClass",
        "com.sun.jersey.api.core.PackagesResourceConfig");
    servHolder.setInitParameter("com.sun.jersey.config.property.packages",
        "org.jaxrx.resource");

    final Context context = new Context(server, "/", Context.SESSIONS);
    context.addServlet(servHolder, "/");
    server.start();
  }

  /**
   * Constructor.
   * 
   * @param port web server port
   * @param shiroIniFile A {@link String} representation of the shiro
   *          configuration file.
   * @throws Exception exception
   */
  public JettyServer(final int port, final String shiroIniFile)
      throws Exception {
    server = new Server(port);
    final ServletHolder servHolder = new ServletHolder(ServletContainer.class);
    servHolder.setInitParameter(
        "com.sun.jersey.config.property.resourceConfigClass",
        "com.sun.jersey.api.core.PackagesResourceConfig");
    servHolder.setInitParameter("com.sun.jersey.config.property.packages",
        "org.jaxrx.resource");
    final Context context = new Context(server, "/", Context.SESSIONS);
    context.addServlet(servHolder, "/");
    final FilterHolder aFilter = new FilterHolder(IniShiroFilter.class);
    aFilter.setInitParameter("config", shiroIniFile);
    context.addFilter(aFilter, "/*", Handler.REQUEST);
    server.start();
    System.out.println("Server started with authentication/authorization support");
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
