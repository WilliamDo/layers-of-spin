import ply.database

from flask import Blueprint, jsonify, request

bp = Blueprint('api', __name__, url_prefix='/api')


@bp.route("/player", methods=["POST"])
def create_player():
    player = request.json
    player_id = ply.database.create_player({"first_name": player["firstName"], "last_name": player["lastName"]})
    return jsonify({"id": player_id}), 201


@bp.route("/player/<int:player_id>", methods=["GET", "POST"])
def get_player(player_id):
    player = ply.database.get_player(player_id)
    return jsonify({
        "id": player["id"],
        "firstName": player["first_name"],
        "lastName": player["last_name"]
    })


@bp.route("/fixture/<int:fixture_id>", methods=["GET"])
def get_fixture(fixture_id):
    fixture = ply.database.get_fixture(fixture_id)
    return jsonify(fixture)
