import os

class Config(object):
    PG_HOSTNAME = 'localhost'
    PG_DATABASE = 'ply'
    PG_USERNAME = 'ply'
    PG_PASSWORD = 'docker'


class ProductionConfig(Config):
    PG_HOSTNAME = 'localhost'
    PG_DATABASE = 'ply'
    PG_USERNAME = 'ply'
    PG_PASSWORD = 'docker'


class EnvironmentConfig(Config):
    PG_HOSTNAME = os.environ['DATABASE_HOST']
    PG_DATABASE = 'ply'
    PG_USERNAME = 'ply'
    PG_PASSWORD = 'docker'

