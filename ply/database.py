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
select home_player.first_name || ' ' || home_player.last_name as home_player, away_player.first_name || ' ' || away_player.last_name as away_player
from fixture f
inner join "match" m on f.id = m.fixture_id
inner join player home_player on home_player.id = m.home_player_id
inner join player away_player on away_player.id = m.away_player_id
where f.id = %s
'''


def get_matches(fixture_id):
    with get_connection() as conn:
        with conn.cursor(cursor_factory=psycopg2.extras.DictCursor) as curs:
            curs.execute('select * from "match" where fixture_id = %s', (fixture_id, ))
            return [format_match(match) for match in curs]

def get_fixture_details(fixture_id):
    with get_connection() as conn:
        with conn.cursor(cursor_factory=psycopg2.extras.DictCursor) as curs:
            curs.execute('select * from fixture where id = %s', (fixture_id, ))
            return curs.fetchone()

def get_fixture(fixture_id):
    fixture = get_fixture_details(fixture_id)
    matches = get_matches(fixture_id)

    return {
        'date': fixture['fixture_date'].strftime('%Y-%m-%d'),

        'homeTeam': {
            'name': fixture['home_team'],
            'players': [
                {'id': 1, 'firstName': 'Timo', 'lastName': 'Boll'},
                {'id': 2, 'firstName': 'Player', 'lastName': '2'}
            ]
        },

        'awayTeam': {
            'name': fixture['away_team'],
            'players': [
                {'id': 4, 'firstName': 'Vladimir', 'lastName': 'Samsonov'},
                {'id': 5, 'firstName': 'Player', 'lastName': '5'}
            ]
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
