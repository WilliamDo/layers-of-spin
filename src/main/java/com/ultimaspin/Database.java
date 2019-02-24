package com.ultimaspin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    public static final String SQL = "select f.fixture_date, f.home_team_id, f.away_team_id, th.team_name home_team, ta.team_name away_team\n" +
            "from fixture f\n" +
            "  inner join fixture_team fth on fth.id = f.home_team_id\n" +
            "  inner join fixture_team fta on fta.id = f.away_team_id\n" +
            "  inner join team th on fth.team_id = th.id\n" +
            "  inner join team ta on fta.team_id = ta.id\n" +
            "where f.id = ?;";


    public static void main(String[] args) {

//        String url = "jdbc:postgresql://localhost/test";
//        Properties props = new Properties();
//        props.setProperty("user","fred");
//        props.setProperty("password","secret");
//        props.setProperty("ssl","true");
//        Connection conn = DriverManager.getConnection(url, props);

        try {

            var fixtureId = 1;

            String url = "jdbc:postgresql://localhost/ply?user=ply&password=docker";
            Connection conn = DriverManager.getConnection(url);
            FixtureDetails fixtureDetails = getFixtureDetails(fixtureId, conn);
            List<Match> matches = getFixtureMatches(fixtureId, conn);
            List<Player> homePlayers = getFixturePlayers(fixtureDetails.homeTeamId, conn);
            List<Player> awayPlayers = getFixturePlayers(fixtureDetails.awayTeamId, conn);

            Team homeTeam = new Team(fixtureDetails.homeTeam, homePlayers);
            Team awayTeam = new Team(fixtureDetails.awayTeam, awayPlayers);

            FixtureResponse fixtureResponse = new FixtureResponse(homeTeam, awayTeam, matches);

//            Gson gson = new Gson();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(fixtureResponse);
            System.out.println(json);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static FixtureDetails getFixtureDetails(int fixtureId, Connection conn) throws SQLException {
        try (PreparedStatement st = conn.prepareStatement(SQL)) {
            st.setInt(1, fixtureId);
            try (ResultSet rs = st.executeQuery()) {

                rs.next();

                FixtureDetails fixtureDetails = new FixtureDetails();
                fixtureDetails.homeTeamId = rs.getInt("home_team_id");
                fixtureDetails.homeTeam = rs.getString("home_team");
                fixtureDetails.awayTeamId = rs.getInt("away_team_id");
                fixtureDetails.awayTeam = rs.getString("away_team");

                System.out.println(rs.getString("home_team_id"));
                System.out.println(rs.getString("home_team"));
                System.out.println(rs.getString("away_team_id"));
                System.out.println(rs.getString("away_team"));

                return fixtureDetails;

            }
        }
    }

    static class FixtureDetails {

        int homeTeamId;
        String homeTeam;

        int awayTeamId;
        String awayTeam;

    }

    private static List<Match> getFixtureMatches(int fixtureId, Connection conn) throws SQLException {

        var matches = new ArrayList<Match>();

        try (PreparedStatement st = conn.prepareStatement("select * from fixture_match where fixture_id = ?")) {
            st.setInt(1, fixtureId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    System.out.println(rs.getString("home_score"));
                    System.out.println(rs.getString("away_score"));
                    matches.add(new Match(
                            rs.getInt("home_player_id"),
                            rs.getInt("away_player_id"),
                            rs.getString("home_score"),
                            rs.getString("away_score")

                    ));
                }
            }
        }

        return matches;
    }

    static class Player {

        String firstName;
        String lastName;

        Player(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }
    }

    private static List<Player> getFixturePlayers(int teamId, Connection conn) throws SQLException {

        String sql = "select *\n" +
                "from fixture_player fp\n" +
                "inner join player p on fp.player_id = p.id\n" +
                "where fp.fixture_team_id = ?";

        var players = new ArrayList<Player>();

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, teamId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    System.out.println(rs.getString("first_name") + " " + rs.getString("last_name"));
                    players.add(new Player(rs.getString("first_name"), rs.getString("last_name")));
                }
            }
        }

        return players;
    }

    static class Team {

        Team(String name, List<Player> players) {
            this.name = name;
            this.players = players;
        }

        String name;
        List<Player> players;
    }

    static class Match {

        Match(int homePlayerId, int awayPlayerId, String homeScore, String awayScore) {
            this.homePlayerId = homePlayerId;
            this.awayPlayerId = awayPlayerId;
            this.homeScore = homeScore;
            this.awayScore = awayScore;
        }

        int homePlayerId;
        int awayPlayerId;

        // TODO these should really be converted to something not string
        String homeScore;
        String awayScore;
    }

    static class FixtureResponse {

        FixtureResponse(Team homeTeam, Team awayTeam, List<Match> matches) {
            this.homeTeam = homeTeam;
            this.awayTeam = awayTeam;
            this.matches = matches;
        }

        Team homeTeam;
        Team awayTeam;
        List<Match> matches;
    }

}
