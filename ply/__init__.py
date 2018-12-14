from flask import Flask, jsonify, request

import ply.database


def create_app():
    app = Flask(__name__)

    @app.route("/alive", methods=["GET"])
    def alive():
        return jsonify({"alive": True})

    @app.route("/league", methods=["GET"])
    def get_league():
        return jsonify({
            "name": "West Essex",
            "divisions": ["Division 1", "Division 2", "Division 3"]
        })

    @app.route("/player", methods=["POST"])
    def create_player():
        player = request.json
        player_id = ply.database.create_player({"first_name": player["firstName"], "last_name": player["lastName"]})
        return jsonify({"id": player_id}), 201

    @app.route("/player/<int:player_id>", methods=["GET", "POST"])
    def get_player(player_id):
        player = ply.database.get_player(player_id)
        return jsonify({
            "id": player["id"],
            "firstName": player["first_name"],
            "lastName": player["last_name"]
        })

    return app
