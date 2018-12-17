import pytest

import ply


@pytest.fixture
def client():
    yield ply.create_app().test_client()


def test_create_player(client):
    create_player_response = client.post('/api/player', json={'firstName': 'Adam', 'lastName': 'Eve'})
    assert create_player_response.status_code == 201
    create_player_result = create_player_response.json

    get_player_response = client.get(f'/api/player/{create_player_result["id"]}')
    assert get_player_response.status_code == 200
    player = get_player_response.json

    assert player['firstName'] == 'Adam'
    assert player['lastName'] == 'Eve'


def test_get_fixture(client):
    fixture_response = client.get('/api/fixture/1')
    assert fixture_response.status_code == 200
    fixture = fixture_response.json

    assert fixture == {
        'matches': [
            {
                'home_player': 'Timo Boll',
                'away_player': 'Vladimir Samsonov'
            }
        ]
    }


