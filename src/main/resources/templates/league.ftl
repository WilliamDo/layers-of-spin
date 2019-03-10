<html>
<h2>League - ${league.name}</h2>

<#list league.seasons as season>
    ${season.startDate}
</#list>

<h3>Current Season - ${league.currentSeason.startDate}</h3>

<#list league.currentDivisions as division>
    ${division.division}
</#list>

</html>
