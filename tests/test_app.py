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
        'date': '2018-12-19',

        'homeTeam': {
            'name': 'Team A',
            'players': [
                {'id': 1, 'firstName': 'Timo', 'lastName': 'Boll'},
                {'id': 2, 'firstName': 'Jike', 'lastName': 'Zhang'},
                {'id': 3, 'firstName': 'Koki', 'lastName': 'Niwa'},
            ]
        },

        'awayTeam': {
            'name': 'Team B',
            'players': [
                {'id': 4, 'firstName': 'Vladimir', 'lastName': 'Samsonov'},
                {'id': 5, 'firstName': 'Jun', 'lastName': 'Mizutani'}
            ]
        },

        'matches': [
            {
                'homePlayerId': [1],
                'awayPlayerId': [4],
                'homeScore': [11, 11, 11],
                'awayScore': [7, 8, 9]
            },
            {
                'homePlayerId': [2],
                'awayPlayerId': [5],
                'homeScore': [11, 11, 11],
                'awayScore': [7, 8, 9]
            }
        ]
    }


