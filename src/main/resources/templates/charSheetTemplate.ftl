<#-- @ftlvariable name="pc" type="com.smaugslair.thitracker.data.pc.PlayerCharacter" -->

<table style="width: 100%"><tr><td>
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
    </td></tr>
  <tr><td style="border-bottom: 1px solid #ccc;"></td></tr>
  <tr><td>

      <table style="width: 100%">
        <tr>
          <td style="border: 1px solid; text-align: center; width: 10%; vertical-align: top;"">
          <table>
            <tr><td style="border: 1px solid;">${totalPowers}</td></tr>
            <tr><td style="color: #ccc">Level 1</td></tr>
            <tr><td style="color: #ccc">Level 2</td></tr>
            <tr><td style="color: #ccc">Level 3</td></tr>
            <tr><td style="color: #ccc">Level 4</td></tr>
            <tr><td style="color: #ccc">Level 5</td></tr>
            <tr><td style="color: #ccc">Level 6</td></tr>
            <tr><td style="color: #ccc">Level 7</td></tr>
            <tr><td style="color: #ccc">Level 8</td></tr>
            <tr><td style="color: #ccc">Level 9</td></tr>
            <tr><td style="color: #ccc">Level 10</td></tr>
          </table>
    </td>
    <td style="width: 45%; vertical-align: top">
      <table>
        <tr>
          <td>${heroPowerSet0}</td>
          <td style="border: 1px solid; text-align: center; width: 15px">${totalPower0}</td>
        </tr>
          <#list heroPowerList0 as power>
            <tr><td>${power}</td></tr>
          </#list>
      </table>
    </td>
    <td style="width: 45%; vertical-align: top">
      <table>
        <tr>
          <td>${heroPowerSet1}</td>
          <td style="border: 1px solid; text-align: center; width: 15px">${totalPower1}</td>
        </tr>
          <#list heroPowerList1 as power>
            <tr><td>${power}</td></tr>
          </#list>
      </table>
    </td>
  </tr>
</table>
</td></tr>
<tr><td><p></p></td></tr>
<tr><td>
    <p>The Hero Instant © 2024 – Andy Ashcraft, Giantsdance Games</p>
  </td></tr>
</table>