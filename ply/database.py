import psycopg2
import psycopg2.extras

from flask import current_app, g


# TODO add a method to close the connection?
def get_connection():
    if 'conn' not in g:
        g.conn = psycopg2.connect(
            dbname=current_app.config['PG_DATABASE'],
            user=current_app.config['PG_USERNAME'],
            password=current_app.config['PG_PASSWORD'],
            host=current_app.config['PG_HOSTNAME']
        )

    return g.conn


def create_player(player):
    with get_connection() as conn:
        with conn.cursor(cursor_factory=psycopg2.extras.DictCursor) as curs:
            curs.execute("INSERT INTO player (first_name, last_name) VALUES (%s, %s) RETURNING id", (player["first_name"], player["last_name"]))
            data = curs.fetchone()
            return data["id"]


def get_player(player_id):
    with get_connection() as conn:
        with conn.cursor(cursor_factory=psycopg2.extras.DictCursor) as curs:
            curs.execute("SELECT * FROM player WHERE id = %s", (player_id, ))
            data = curs.fetchone()
            curs.close()
            return data

FIXTURE_QUERY = '''
select f.fixture_date, f.home_team_id, f.away_team_id, th.team_name home_team, ta.team_name away_team
from fixture f
  inner join fixture_team fth on fth.id = f.home_team_id
  inner join fixture_team fta on fta.id = f.away_team_id
  inner join team th on fth.team_id = th.id
  inner join team ta on fta.team_id = ta.id
where f.id = 1;
'''

FIXTURE_PLAYER_QUERY = '''
select *
from fixture_player fp
inner join player p on fp.player_id = p.id
where fp.fixture_team_id = %s
'''


def get_fixture_players(fixture_team_id):
    with get_connection() as conn:
        with conn.cursor(cursor_factory=psycopg2.extras.DictCursor) as curs:
            curs.execute(FIXTURE_PLAYER_QUERY, (fixture_team_id, ))
            return [format_player(player) for player in curs]


def get_matches(fixture_id):
    with get_connection() as conn:
        with conn.cursor(cursor_factory=psycopg2.extras.DictCursor) as curs:
            curs.execute('select * from fixture_match where fixture_id = %s', (fixture_id, ))
            return [format_match(match) for match in curs]


def get_fixture_details(fixture_id):
    with get_connection() as conn:
        with conn.cursor(cursor_factory=psycopg2.extras.DictCursor) as curs:
            curs.execute(FIXTURE_QUERY, (fixture_id, ))
            return curs.fetchone()


def get_fixture(fixture_id):
    fixture = get_fixture_details(fixture_id)
    matches = get_matches(fixture_id)

    # TODO there is a downside here that the two queries here are dependent on the first one and cannot be run in parallel
    home_players = get_fixture_players(fixture['home_team_id'])
    away_players = get_fixture_players(fixture['away_team_id'])

    return {
        'date': fixture['fixture_date'].strftime('%Y-%m-%d'),

        'homeTeam': {
            'name': fixture['home_team'],
            'players': home_players
        },

        'awayTeam': {
            'name': fixture['away_team'],
            'players': away_players
        },
        'matches': matches
    }


def format_match(match):
    return {
        'homePlayerId': [match['home_player_id']],
        'awayPlayerId': [match['away_player_id']],
        'homeScore': match['home_score'],
        'awayScore': match['away_score']
    }


def format_player(player):
    return {
        'id': player['player_id'],
        'firstName': player['first_name'],
        'lastName': player['last_name'],
    }
