package com.ultimaspin.retrodev.client;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.*;
import com.ultimaspin.retrodev.shared.FieldVerifier;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TableTennisLeagueManager implements EntryPoint {
  /**
   * The message displayed to the user when the server cannot be reached or
   * returns an error.
   */
  private static final String SERVER_ERROR = "An error occurred while "
      + "attempting to contact the server. Please check your network "
      + "connection and try again.";

  /**
   * Create a remote service proxy to talk to the server-side Greeting service.
   */
  private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
//    SplitLayoutPanel p = new SplitLayoutPanel();
    DockLayoutPanel p = new DockLayoutPanel(Style.Unit.EM);

    StackLayoutPanel stackLayoutPanel = new StackLayoutPanel(Style.Unit.EM);

    // Create a tree with a few items in it.
    TreeItem root = new TreeItem();
    root.setText("root");
    root.addTextItem("item0");
    root.addTextItem("item1");
    root.addTextItem("item2");

    // Add a CheckBox to the tree
    TreeItem item = new TreeItem(new CheckBox("item3"));
    root.addItem(item);

    Tree t = new Tree();
//    t.addItem(root);

    stackLayoutPanel.add(t, new HTML("Leagues"), 4);
    stackLayoutPanel.add(new HTML("that content"), new HTML("Clubs"), 4);
    stackLayoutPanel.add(new HTML("the other content"), new HTML("Players"), 4);

    p.addWest(stackLayoutPanel, 24);

    Frame frame = new Frame("index.action");
    frame.setHeight("100%");
    frame.setWidth("100%");

    FlowPanel flowPanel = new FlowPanel(); // This must be a FlowPanel for the frame height and width to be set to 100%
    flowPanel.add(frame);

    p.add(flowPanel);

    greetingService.getLeagues(new AsyncCallback<List<String>>() {
      @Override
      public void onFailure(Throwable throwable) {
        t.addTextItem("Something went wrong: " + throwable.getMessage());
      }

      @Override
      public void onSuccess(List<String> strings) {
        t.removeItems();
        strings.forEach(it -> {
          Label label = new Label(it);
          label.addClickHandler(clickEvent -> {
            frame.setUrl("viewLeague.action?leagueName=" + it);
          });
          t.add(label);

        });
      }
    });

    RootLayoutPanel.get().add(p);
  }
}
