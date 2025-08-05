<#-- @ftlvariable name="pc" type="com.smaugslair.thitracker.data.pc.PlayerCharacter" -->

<table style="width: 100%">
  <tr>
    <td>
      <table style="width: 100%">
        <tbody>
          <tr>
            <td style="width: 67%">
              <h1>${pc.name}</h1>
            </td>
            <td>
              <p>${user}</p>
              <p>${pc.civilianId}</p>
            </td>
          </tr>
          <tr>
            <td>
              <table style="width: 100%">
                <tbody>
                <tr>
                  <td style="width: 15%;">Hero traits</td>
                  <td>
                    <table style="width: 100%">
                      <tbody>
                      <#list heroTraits as trait>
                        <tr>
                          <td style="background-color: #eee">${trait}</td>
                          <td style="border: 1px solid; text-align: center; width:20px;"></td>
                        </tr>
                      </#list>
                      </tbody>
                    </table>
                  </td>
                </tr>
                </tbody>
              </table>
              <table style="width: 100%">
                <tbody>
                <tr>
                  <td style="width: 15%;">Drama traits</td>
                  <td>
                    <table style="width: 100%">
                      <tbody>
                      <#list dramaTraits as trait>
                        <tr>
                          <td style="background-color: #eee">${trait}</td>
                          <td style="border: 1px solid; text-align: center; width:20px;"></td>
                        </tr>
                      </#list>
                      </tbody>
                    </table>
                  </td>
                </tr>
                </tbody>
              </table>
              <table style="width: 100%">
                <tbody>
                <tr>
                  <td style="text-align: right;">Perception</td>
                  <td style="border: 1px solid; text-align: center;">${Perception}</td>
                  <td style="text-align: right;">Stealth</td>
                  <td style="border: 1px solid; text-align: center;">${Stealth}</td>
                </tr>
                <tr>
                  <td style="text-align: right;">Aim</td>
                  <td style="border: 1px solid; text-align: center;">${Aim}</td>
                  <td style="text-align: right;">Dodge</td>
                  <td style="border: 1px solid; text-align: center;">${Dodge}</td>
                </tr>
                <tr>
                  <td style="text-align: right;">Strength</td>
                  <td style="border: 1px solid; text-align: center;">${Strength}</td>
                  <td style="text-align: right;">Toughness</td>
                  <td style="border: 1px solid; text-align: center;">${Toughness}</td>
                </tr>
                <tr>
                  <td style="text-align: right;">Influence</td>
                  <td style="border: 1px solid; text-align: center;">${Influence}</td>
                  <td style="text-align: right;">Self-Control</td>
                  <td style="border: 1px solid; text-align: center;">${SelfControl}</td>
                </tr>
                </tbody>
              </table>
            </td>
            <td style="vertical-align: bottom">
              <table style="width: 100%">
                <tbody>
                <tr>
                  <td style="text-align: right;">Initiative</td>
                  <td style="border: 1px solid; text-align: center;">${Initiative}</td>
                </tr>
                <tr>
                  <td style="text-align: right;">Movement</td>
                  <td style="border: 1px solid; text-align: center;">${Movement}</td>
                </tr>
                </tbody>
              </table>
            </td>
          </tr>
        </tbody>
      </table>
    </td>
  </tr>
  <tr>
    <td style="border-bottom: 1px solid #ccc;"></td>
  </tr>
  <tr>
    <td>
      <table style="width: 100%">
        <tr>
          <th style="border: 1px solid; width: 6%; vertical-align: top;">${totalPowers}</th>
          <th style="border: 1px solid; width: 47%; vertical-align: top;">${heroPowerSet0}  ${totalPower0} </th>
          <th style="border: 1px solid; width: 47%; vertical-align: top;">${heroPowerSet1}  ${totalPower1} </th>
        </tr>
          <#list powers as powerpair>
            <tr>
              <td style="color: #ccc">${powerpair.left}</td>
              <td>${powerpair.middle}</td>
              <td>${powerpair.right}</td>
            </tr>
          </#list>
      </table>
    </td>
  </tr>
</td></tr>
<tr><td><p></p></td></tr>
<tr><td>
    <p>The Hero Instant © 2024 – Andy Ashcraft, Giantsdance Games</p>
  </td></tr>
</table>