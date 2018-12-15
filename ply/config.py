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


