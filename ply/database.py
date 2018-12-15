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
            curs.execute("SELECT * FROM player where id = %s", (player_id,))
            data = curs.fetchone()
            curs.close()
            return data
