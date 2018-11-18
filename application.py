from flask import Flask
from flask import jsonify

app = Flask(__name__)

@app.route("/league", methods=["GET"])
def league():
    return jsonify({
        "name": "West Essex",
        "divisions": ["Division 1", "Division 2", "Division 3"]
    })

@app.route("/player", methods=["GET"])
def player():
    return jsonify({"firstName": "Joe", "lastName": "Bloggs"})