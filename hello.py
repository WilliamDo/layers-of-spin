from flask import Flask
from flask import jsonify

app = Flask(__name__)

@app.route("/")
def hello():
    return jsonify({ "message": "Hello World!" })

