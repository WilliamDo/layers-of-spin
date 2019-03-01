<html>
<h2>${fixture.homeTeam.name} vs. ${fixture.awayTeam.name}</h2>

<table>
    <#list fixture.matches as match>
        <#assign homePlayer=fixture.homeTeam.getPlayer(match.homePlayerId)>
        <#assign awayPlayer=fixture.awayTeam.getPlayer(match.awayPlayerId)>
        <tr>
            <td>${homePlayer.firstName} ${homePlayer.lastName}</td>
            <td>${awayPlayer.firstName} ${awayPlayer.lastName}</td>
        </tr>

    </#list>
</table>
</html>
