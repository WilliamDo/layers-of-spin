package com.ultimaspin.retrodev;

import com.opensymphony.xwork2.ActionSupport;

public class ViewLeagueAction extends ActionSupport {

    private String leagueName;
    public String execute() {
        return SUCCESS;
    }

    public String getLeagueName() {
        return this.leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

}
