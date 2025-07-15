<span>
<#list powerSets as powerSet>
  <h2>${powerSet.name?upper_case}</h2>
  <p>${powerSet.openText}</p>
  <h3>Basic Ability Improvements</h3>
  <p>${powerSet.abilityText}</p>
  <p>
<#list powerSet.powerSetMods as k, modItem>
    ${modItem.ability.displayName} ${modItem.value}<#sep>
</#list>
</p>
  <h3>Power details</h3>
  <p>${powerSet.powersText}</p>
  <ul>
<#list powerSet.powers as power>
  <li>
<h4>${power.name}</h4>
<p>${power.shortDescr}</p>
<p>${power.fullDescr}</p>
<p>
<#list power.powerMods as k, modItem>
    ${modItem.ability.displayName} ${modItem.value}<#sep>
</#list>
</p>
</li>
</#list>
</ul>
</#list>
<p>The Hero Instant © 2024 – Andy Ashcraft, Giantsdance Games</p>
</span>