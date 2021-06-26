extern crate postgres;
#[macro_use]
extern crate serde_derive;

use postgres::{Connection, TlsMode};
use serde::{Serialize};
use serde_json::Result;

#[derive(Debug, Deserialize, Serialize)]
struct Player {
    id: i32,
    first_name: String,
    last_name: String,
}

#[derive(Debug)]
struct Fixture {
    home_team_id: i32,
    away_team_id: i32,
    home_team: String,
    away_team: String
}

#[derive(Debug, Deserialize, Serialize)]
struct Team {
    name: String,
    players: Vec<Player>
}

#[derive(Debug, Deserialize, Serialize)]
struct FixtureResponse {
    homeTeam: Team,
    awayTeam: Team
}

fn main() {
    let conn = Connection::connect("postgres://ply:docker@localhost:5432/ply", TlsMode::None).unwrap();
    let fixture = get_fixture(&conn, 1);
    let home_players = get_players(&conn, fixture.home_team_id);
    let home_players_map = get_players_map(&conn, fixture.home_team_id);
    let away_players = get_players(&conn, fixture.away_team_id);

    println!("{:?}", fixture);
    println!("{:?}", home_players);
    println!("{:?}", away_players);

    let response = FixtureResponse {
        homeTeam: Team {
            name: fixture.home_team,
            players: home_players
        },
        awayTeam: Team {
            name: fixture.away_team,
            players: away_players
        }
    };

    println!("{:?}", response);

    // Serialize it to a JSON string.
    let j = serde_json::to_string(&response).unwrap();

    // Print, write to a file, or send to an HTTP server.
    println!("{}", j);

}

fn get_players(conn: &Connection, team_id: i32) -> Vec<Player> {

    let mut players: Vec<Player> = Vec::new();

    let sql = "select *
                        from fixture_player fp
                        inner join player p on fp.player_id = p.id
                        where fp.fixture_team_id = $1";

    for row in &conn.query(sql, &[&team_id]).unwrap() {
        let player = Player {
            id: row.get("id"),
            first_name: row.get("first_name"),
            last_name: row.get("last_name"),
        };
        println!("Found player {}: {}", player.id, player.last_name);
        players.push(player);
    }

    players
}

fn get_players_map(conn: &Connection, team_id: i32) -> Vec<Player> {

    let sql = "select *
                        from fixture_player fp
                        inner join player p on fp.player_id = p.id
                        where fp.fixture_team_id = $1";

    let players = conn.query(sql, &[&team_id]).unwrap().iter().map(|row| {
        Player {
            id: row.get("id"),
            first_name: row.get("first_name"),
            last_name: row.get("last_name"),
        }
    }).collect::<Vec<_>>();

    players
}

fn get_fixture(conn: &Connection, fixture_id: i32) -> Fixture {

    let sql = "select f.fixture_date, f.home_team_id, f.away_team_id, th.team_name home_team, ta.team_name away_team
                        from fixture f
                        inner join fixture_team fth on fth.id = f.home_team_id
                        inner join fixture_team fta on fta.id = f.away_team_id
                        inner join team th on fth.team_id = th.id
                        inner join team ta on fta.team_id = ta.id
                        where f.id = $1";

    let rows = conn.query(sql, &[&fixture_id]).unwrap();

    if rows.len() == 1 {
        let row = rows.get(0);
        return Fixture {
            home_team_id: row.get(1),
            away_team_id: row.get(2),
            home_team: row.get(3),
            away_team: row.get(4)
        };
    } else {
        return Fixture {
            home_team_id: 0,
            away_team_id: 0,

            home_team: String::new(),
            away_team: String::new()
        };
    }

}
