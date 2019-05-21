{-# LANGUAGE OverloadedStrings #-}
{-# LANGUAGE DeriveGeneric #-}

module Lib
    ( someFunc, hello, fixturePlayers, fixtureDetails, fixture
    ) where

import GHC.Generics

import Database.PostgreSQL.Simple
import Database.PostgreSQL.Simple.FromRow

import Data.Foldable

import GHC.Generics
import Data.Aeson
import Data.Aeson.Types

import qualified Data.Text as Text
import qualified Data.ByteString.Lazy as B

data Player = Player { id :: Int, first_name :: String, last_name :: String } deriving (Generic, Show)
data FixtureDetails = FixtureDetails { homeTeamId :: Int, awayTeamId :: Int, homeTeam :: String, awayTeam :: String} deriving (Generic, Show)

data FixtureResponse = FixtureResponse FixtureDetails [Player] [Player] deriving (Generic, Show)

instance ToJSON Player where
    toEncoding = genericToEncoding defaultOptions

instance ToJSON FixtureDetails where
    toEncoding = genericToEncoding defaultOptions

instance ToJSON FixtureResponse where
    toEncoding = genericToEncoding defaultOptions

instance FromRow Player where
    fromRow = Player <$> field <*> field <*> field

instance FromRow FixtureDetails where
    fromRow = FixtureDetails <$> field <*> field <*> field <*> field

someFunc :: IO ()
someFunc = putStrLn "someFunc"

hello :: IO Int
hello = do
  conn <- connectPostgreSQL "host=localhost port=5432 dbname=ply user=ply password=docker connect_timeout=10"
  [Only i] <- query_ conn "select 2 + 2"
  return i

fixturePlayers :: Int -> IO [Player]
fixturePlayers teamId = do
    conn <- connectPostgreSQL "host=localhost port=5432 dbname=ply user=ply password=docker connect_timeout=10"
    query conn "select p.id, p.first_name, p.last_name from fixture_player fp inner join player p on fp.player_id = p.id where fp.fixture_team_id = ?" [teamId]

fixtureDetails :: Int -> IO FixtureDetails
fixtureDetails fixtureId = do 
    conn <- connectPostgreSQL "host=localhost port=5432 dbname=ply user=ply password=docker connect_timeout=10"
    [fixture] <- query conn fixtureDetailsQuery [fixtureId]
    return fixture

fixture :: Int -> IO B.ByteString
fixture fixtureId = do
    details <- fixtureDetails fixtureId
    let FixtureDetails homeId awayId home away = details
    homePlayers <- fixturePlayers homeId
    awayPlayers <- fixturePlayers awayId
    let response = (FixtureResponse details homePlayers awayPlayers)
    return (encode response)


fixtureDetailsQuery :: Query
fixtureDetailsQuery = "select f.home_team_id, f.away_team_id, th.team_name home_team, ta.team_name away_team \
\from fixture f \
\inner join fixture_team fth on fth.id = f.home_team_id \
\inner join fixture_team fta on fta.id = f.away_team_id \
\inner join team th on fth.team_id = th.id \
\inner join team ta on fta.team_id = ta.id \
\where f.id = ?"