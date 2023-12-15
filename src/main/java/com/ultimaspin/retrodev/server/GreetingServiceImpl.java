package com.ultimaspin.retrodev.server;

import com.google.gwt.thirdparty.guava.common.collect.Lists;
import com.ultimaspin.retrodev.League;
import com.ultimaspin.retrodev.LeagueDao;
import com.ultimaspin.retrodev.client.GreetingService;
import com.ultimaspin.retrodev.shared.FieldVerifier;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
    GreetingService {

  private static final Logger logger = LogManager.getLogger(GreetingServiceImpl.class);

  public String greetServer(String input) throws IllegalArgumentException {
    // Verify that the input is valid. 
    if (!FieldVerifier.isValidName(input)) {
      // If the input is not valid, throw an IllegalArgumentException back to
      // the client.
      throw new IllegalArgumentException(
          "Name must be at least 4 characters long");
    }

    String serverInfo = getServletContext().getServerInfo();
    String userAgent = getThreadLocalRequest().getHeader("User-Agent");

    // Escape data from the client to avoid cross-site script vulnerabilities.
    input = escapeHtml(input);
    userAgent = escapeHtml(userAgent);

    return "Hello, " + input + "!<br><br>I am running " + serverInfo
        + ".<br><br>It looks like you are using:<br>" + userAgent;
  }

  @Override
  public List<String> getLeagues() {
    try {
      logger.debug("Fetching leagues");
      List<String> leaguesFromDb = new LeagueDao().getLeagues().stream().map(League::getLeagueName).collect(Collectors.toList());
      List<String> leagues = new ArrayList<>();
      leagues.add("Becontree");
      leagues.add("Romford");
      leagues.addAll(leaguesFromDb);
      return leagues;
    } catch (Exception e) {
      logger.error("Could not fetch leagues from database", e);
      throw e;
    }
  }

  /**
   * Escape an html string. Escaping data received from the client helps to
   * prevent cross-site script vulnerabilities.
   * 
   * @param html the html string to escape
   * @return the escaped string
   */
  private String escapeHtml(String html) {
    if (html == null) {
      return null;
    }
    return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(
        ">", "&gt;");
  }
}
