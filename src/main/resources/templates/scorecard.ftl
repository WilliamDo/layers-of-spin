<!DOCTYPE html>
<html>

<head>
    <title>Fixture - ${fixture.homeTeam.name} vs. ${fixture.awayTeam.name}</title>
    <link href="https://fonts.googleapis.com/css?family=Comfortaa|Concert+One|Raleway|Sniglet|Thasadith" rel="stylesheet">
    <link rel="stylesheet" href="/static/league.css">
</head>

<body>

    <div id="root">
        <div id="header">
            <div class="center-core">
                <h1 class>Spinners Table Tennis League</h1>
            </div>
        </div>



        <div class="navigation">
            <div class="center-core">
                <ul>
                    <li><a href="#">Home</a></li>
                    <li class="dropdown">
                        <a href="#">Divisions</a>
                        <div class="dropdown-content">
                            <a href="#">Division 1</a>
                            <a href="#">Division 2</a>
                            <a href="#">Division 3</a>
                        </div>
                    </li>
                    <li class="dropdown">
                        <a href="#">Teams</a>
                        <div class="dropdown-content">
                            <a href="#">Top Spinners</a>
                            <a href="#">Side Rotations</a>
                            <a href="#">Back Stoppers</a>
                        </div>
                    </li>
                    <li><a href="#">Archive</a></li>
                </ul>
            </div>

        </div>


        <div id="site-body">

            <h2 class="fixture-title">${fixture.homeTeam.name} <span class="score-title game-home">6 - 4</span> ${fixture.awayTeam.name}</h2>

            Date: ${fixture.date}

            <table>
                <col style="width:30%">
                <col style="width:40%">
                <col style="width:30%">
                <thead>
                    <tr>
                        <th>Team A</th>
                        <th>Score</th>
                        <th>Team B</th>
                    </tr>
                </thead>

                <tbody>

                    <#list fixture.matches as match>
                        <#assign homePlayer=fixture.homeTeam.getPlayer(match.homePlayerId)>
                        <#assign awayPlayer=fixture.awayTeam.getPlayer(match.awayPlayerId)>
                        <tr>
                            <td>${homePlayer.firstName} ${homePlayer.lastName}</td>
                            <td>
                                <ul class="games">
                                <#list 0..<match.numberOfGames as gameNumber>
                                    <li class="game">${match.homeScore[gameNumber]} - ${match.awayScore[gameNumber]}</li>
                                </#list>
                                </ul>

                            </td>
                            <td>${awayPlayer.firstName} ${awayPlayer.lastName}</td>
                        </tr>

                    </#list>


                    <tr>
                        <td>Patrick Chila</td>
                        <td>
                            <ul class="games">
                                <li class="game game-home">11-5</li>
                                <li class="game game-home">11-8</li>
                                <li class="game game-home">11-9</li>
                            </ul>
                        </td>
                        <td>Ma Long</td>
                    </tr>

                </tbody>

            </table>

        </div>

    </div>



</body>

</html>