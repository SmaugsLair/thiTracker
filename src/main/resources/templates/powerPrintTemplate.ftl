<span>
<#list powerSets as powerSet>
  <h2>${powerSet.name?upper_case}</h2>
  <p>${powerSet.openText}</p>
  <h3>Basic Ability Improvements</h3>
  <p>${powerSet.abilityText}</p>
  <p>${powerSet.powerSetMods}</p>
  <h3>Power details</h3>
  <p>${powerSet.powersText}</p>
  <ul>
<#list powerSet.powers as power>
  <li>
<h4>${power.name}</h4>
<p>${power.short}</p>
<p>${power.full}</p>
<p>${power.extraText}</p>
      <p>
    <#if power.subPowers?has_content>
      subPowers is not empty.
    <#else>
      subPowers is empty or missing.
    </#if>
      </p>
</li>
</#list>
</ul>
</#list>
<p>The Hero Instant © 2024 – Andy Ashcraft, Giantsdance Games</p>
</span>