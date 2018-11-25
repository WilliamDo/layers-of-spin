import psycopg2
import psycopg2.extras

# conn = psycopg2.connect(dbname="ply", user="postgres", password="docker", host="localhost")
# cur = conn.cursor()
# cur.execute("SELECT * FROM league")
# data = cur.fetchone()
#
# print(data)
#
# cur.close()
# conn.close()


def create_player(player):
    conn = psycopg2.connect(dbname="ply", user="postgres", password="docker", host="localhost")
    cur = conn.cursor()
    cur.execute("INSERT INTO player (first_name, last_name) VALUES (%s, %s)", (player["first_name"], player["last_name"]))

    conn.commit()

    cur.close()
    conn.close()

def get_player(id):
    conn = psycopg2.connect(dbname="ply", user="postgres", password="docker", host="localhost")
    cur = conn.cursor(cursor_factory=psycopg2.extras.DictCursor)
    cur.execute("SELECT * FROM player where id = %s", (id,))
    data = cur.fetchone()
    cur.close()
    conn.close()
    return data

# create_player({"first_name": "Joe", "last_name": "Bloggs"})
#
# player = get_player(4)
#
# print(player["id"])
# print(player["first_name"])
# print(player["last_name"])
