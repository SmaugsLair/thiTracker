<#-- @ftlvariable name="powerMap" type="java.util.Map<Integer, SortedSet<Power>>" -->
<#-- @ftlvariable name="powerSet" type="com.smaugslair.thitracker.data.powers.PowerSet" -->
<#-- FreeMarker template for documenting a single Hero Instant PowerSet
 To learn more about FreeMarker visit https://freemarker.apache.org/ -->

Super Hero Power – ${powerSet.name?upper_case}
Updated ${powerSet.updated}

${powerSet.openText}

${powerSet.name} Powers Basic Ability Improvements
${powerSet.abilityText}

<#list powerSet.powerSetMods as modItem>
${modItem.ability} ${modItem.value}<#sep>
</#list>

${powerSet.name} Powers
${powerSet.powersText}

<#list powerMap as tier, powers>
Tier ${tier}
<#list powers as power>
* ${power.name}
${power.fullDescr}
</#list>
</#list>

The Hero Instant © 2024 – Andy Ashcraft, Giantsdance Games