import psycopg2
import psycopg2.extras

# TODO what if this connection is dropped? how to reconnect!?
conn = psycopg2.connect(dbname="ply", user="postgres", password="docker", host="localhost")


def create_player(player):
    with conn:
        with conn.cursor() as curs:
            curs.execute("INSERT INTO player (first_name, last_name) VALUES (%s, %s)", (player["first_name"], player["last_name"]))


def get_player(player_id):
    with conn.cursor(cursor_factory=psycopg2.extras.DictCursor) as curs:
        curs.execute("SELECT * FROM player where id = %s", (player_id,))
        data = curs.fetchone()
        curs.close()
        return data

# create_player({"first_name": "Joe", "last_name": "Bloggs"})
