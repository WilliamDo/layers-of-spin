from flask import Flask, jsonify

import ply.api
import ply.config
import ply.database


def create_app():
    app = Flask(__name__)

    app.config.from_object(config.DockerConfig()) # FIXME make this an environment variable

    @app.route("/alive", methods=["GET"])
    def alive():
        return jsonify({"alive": True})

    @app.route("/league", methods=["GET"])
    def get_league():
        return jsonify({
            "name": "West Essex",
            "divisions": ["Division 1", "Division 2", "Division 3"]
        })

    app.register_blueprint(ply.api.bp)

    return app
