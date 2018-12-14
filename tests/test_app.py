import pytest

import ply


@pytest.fixture
def client():
    yield ply.create_app().test_client()


def test_create_player(client):
    rv = client.post('/player', json={'firstName': 'Adam', 'lastName': 'Eve'})
    assert rv.status_code == 201
    json = rv.json
    print(json)

    rv = client.get(f'/player/{json["id"]}')
    assert rv.status_code == 200
    json = rv.json
    print(json)

    assert json['firstName'] == 'Adam'
    assert json['lastName'] == 'Eve'

