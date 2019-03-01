<html>
<h2>${fixture.homeTeam.name} vs. ${fixture.awayTeam.name}</h2>

<table>
    <#list fixture.matches as match>
        <tr>
            <td>${match.homePlayerId}</td>
            <td>${match.awayPlayerId}</td>
        </tr>

    </#list>
</table>
</html>
