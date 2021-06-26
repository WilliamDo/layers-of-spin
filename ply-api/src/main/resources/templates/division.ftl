<!DOCTYPE html>
<html>

<head>
    <title>League Home Page</title>
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

            <h2>Division 1</h2>

            <table>
                <tr>
                    <th>#</th>
                    <th>Team</th>
                    <th>Played</th>
                    <th>Win</th>
                    <th>Loss</th>
                    <th>Draw</th>
                    <th>Points</th>
                </tr>

                <#list teams as team>
                    <tr>
                        <td>${team.id}</td>
                        <td>${team.name}</td>
                        <td>0</td>
                        <td>0</td>
                        <td>0</td>
                        <td>0</td>
                        <td>0</td>
                    </tr>
                </#list>
            </table>

            <h2>Player Averages</h2>

            <table>
                <tr>
                    <th>Name</th>
                    <th>Team</th>
                    <th>Played</th>
                    <th>Win</th>
                    <th>Loss</th>
                    <th>Average</th>
                </tr>

                <tr>
                    <td>Player A</td>
                    <td>Team A</td>
                    <td>0</td>
                    <td>0</td>
                    <td>0</td>
                    <td>0%</td>
                </tr>

                <tr>
                    <td>Player A</td>
                    <td>Team A</td>
                    <td>0</td>
                    <td>0</td>
                    <td>0</td>
                    <td>0%</td>
                </tr>

                <tr>
                    <td>Player A</td>
                    <td>Team A</td>
                    <td>0</td>
                    <td>0</td>
                    <td>0</td>
                    <td>0%</td>
                </tr>

                <tr>
                    <td>Player A</td>
                    <td>Team A</td>
                    <td>0</td>
                    <td>0</td>
                    <td>0</td>
                    <td>0%</td>
                </tr>

                <tr>
                    <td>Player A</td>
                    <td>Team A</td>
                    <td>0</td>
                    <td>0</td>
                    <td>0</td>
                    <td>0%</td>
                </tr>

                <tr>
                    <td>Player A</td>
                    <td>Team A</td>
                    <td>0</td>
                    <td>0</td>
                    <td>0</td>
                    <td>0%</td>
                </tr>
            </table>

        </div>

    </div>



</body>

</html>