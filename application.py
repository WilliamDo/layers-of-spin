from flask import Flask
from flask import jsonify

import ply.database

app = Flask(__name__)


@app.route("/league", methods=["GET"])
def get_league():
    return jsonify({
        "name": "West Essex",
        "divisions": ["Division 1", "Division 2", "Division 3"]
    })


@app.route("/player/<int:player_id>", methods=["GET"])
def get_player(player_id):
    player = ply.database.get_player(player_id)
    return jsonify({
        "id": player["id"],
        "firstName": player["first_name"],
        "lastName": player["last_name"]
    })
