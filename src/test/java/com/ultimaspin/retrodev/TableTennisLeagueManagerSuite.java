package com.ultimaspin.retrodev;

import com.ultimaspin.retrodev.client.TableTennisLeagueManagerTest;
import com.google.gwt.junit.tools.GWTTestSuite;
import junit.framework.Test;
import junit.framework.TestSuite;

public class TableTennisLeagueManagerSuite extends GWTTestSuite {
  public static Test suite() {
    TestSuite suite = new TestSuite("Tests for TableTennisLeagueManager");
    suite.addTestSuite(TableTennisLeagueManagerTest.class);
    return suite;
  }
}
