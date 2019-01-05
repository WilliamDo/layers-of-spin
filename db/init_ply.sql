CREATE TABLE player (
  id SERIAL PRIMARY KEY,
  first_name VARCHAR,
  last_name VARCHAR
);

CREATE TABLE league (
  id SERIAL PRIMARY KEY,
  league_name VARCHAR
);

CREATE TABLE team (
  id SERIAL PRIMARY KEY,
  team_name VARCHAR
);

CREATE TABLE fixture (
  id SERIAL PRIMARY KEY,
  fixture_date DATE,
  home_team VARCHAR,
  away_team VARCHAR
);

CREATE TABLE match (
  id SERIAL PRIMARY KEY,
  fixture_id INTEGER REFERENCES fixture(id),
  home_player_id INTEGER REFERENCES player(id),
  away_player_id INTEGER REFERENCES player(id),
  home_score JSONB,
  away_score JSONB
);

DO $$
DECLARE
  v_fixture_id INT;
  v_home_player_1_id INT;
  v_home_player_2_id INT;
  v_home_player_3_id INT;
  v_away_player_1_id INT;
  v_away_player_2_id INT;
  v_away_player_3_id INT;
BEGIN
  INSERT INTO player (first_name, last_name)
  VALUES ('Timo', 'Boll')
  RETURNING id INTO v_home_player_1_id;

  INSERT INTO player (first_name, last_name)
  VALUES ('Jike', 'Zhang')
  RETURNING id INTO v_home_player_2_id;

  INSERT INTO player (first_name, last_name)
  VALUES ('Koki', 'Niwa')
  RETURNING id INTO v_home_player_3_id;

  INSERT INTO player (first_name, last_name)
  VALUES ('Vladimir', 'Samsonov')
  RETURNING id INTO v_away_player_1_id;

  INSERT INTO player (first_name, last_name)
  VALUES ('Jun', 'Mizutani')
  RETURNING id INTO v_away_player_2_id;

  INSERT INTO fixture (fixture_date, home_team, away_team)
  VALUES ('2018-12-19', 'Team A', 'Team B')
  RETURNING id INTO v_fixture_id;

  INSERT INTO match (fixture_id, home_player_id, away_player_id, home_score, away_score)
  VALUES (v_fixture_id, v_home_player_1_id, v_away_player_1_id, '[11, 11, 11]', '[7, 8, 9]');

  INSERT INTO match (fixture_id, home_player_id, away_player_id, home_score, away_score)
  VALUES (v_fixture_id, v_home_player_2_id, v_away_player_2_id, '[11, 11, 11]', '[7, 8, 9]');
END $$;


