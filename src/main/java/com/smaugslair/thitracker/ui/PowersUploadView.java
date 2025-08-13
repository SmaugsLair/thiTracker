package com.smaugslair.thitracker.ui;

import com.smaugslair.thitracker.data.pc.HeroPower;
import com.smaugslair.thitracker.data.pc.HeroPowerRepository;
import com.smaugslair.thitracker.data.powers.*;
import com.smaugslair.thitracker.data.user.Message;
import com.smaugslair.thitracker.data.user.MessageRepository;
import com.smaugslair.thitracker.services.PowersCache;
import com.smaugslair.thitracker.ui.components.AbstractMainView;
import com.smaugslair.thitracker.ui.components.ConfirmDialog;
import com.smaugslair.thitracker.ui.components.TitleBar;
import com.smaugslair.thitracker.ui.components.UserSafeButton;
import com.smaugslair.thitracker.ui.powers.PowerSetBrowserView;
import com.smaugslair.thitracker.ui.powers.transformers.PowerSetTransformer;
import com.smaugslair.thitracker.ui.powers.transformers.PowerTransformer;
import com.smaugslair.thitracker.ui.powers.transformers.Transformer;
import com.smaugslair.thitracker.ui.powers.transformers.TransformerException;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.streams.InMemoryUploadHandler;
import com.vaadin.flow.server.streams.UploadHandler;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.security.PermitAll;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@PermitAll
@PageTitle("Powers Upload Page")
@Route(value = "powersupload", layout = MainView.class)
@UIScope
public class PowersUploadView extends AbstractMainView {

    private static final Logger log = LoggerFactory.getLogger(PowersUploadView.class);

    private final List<PowerSet> newPowerSets = new ArrayList<>();
    private final List<PowerSet> unchangedPowerSets = new ArrayList<>();
    private final List<PowerSet> updatedPowerSets = new ArrayList<>();

    private final List<Power> newPowers = new ArrayList<>();
    private final List<Power> unchangedPowers = new ArrayList<>();
    private final List<Power> updatedPowers = new ArrayList<>();
    private final List<Power> deletedPowers = new ArrayList<>();

    private final ProgressBar progressBar = new ProgressBar();


    private final PowersCache powersCache;
    private final PowerRepository powerRepository;
    private final PowerSetRepository powerSetRepository;
    private final HeroPowerRepository heroPowerRepository;
    private final MessageRepository messageRepository;

    public PowersUploadView(TitleBar titleBar, PowersCache powersCache, PowerRepository powerRepository, PowerSetRepository powerSetRepository, HeroPowerRepository heroPowerRepository, MessageRepository messageRepository) {
        super(titleBar);
        this.powersCache = powersCache;
        this.powerRepository = powerRepository;
        this.powerSetRepository = powerSetRepository;
        this.heroPowerRepository = heroPowerRepository;
        this.messageRepository = messageRepository;

        init();
    }

    public void init() {
        ConfirmDialog dialog = new ConfirmDialog();

        Map<String, PowerSet> cachedPowerSetMap = new HashMap<>();
        powersCache.getPowerSetList().forEach(powerSet -> cachedPowerSetMap.put(powerSet.getSsid(), powerSet));

        Map<String, Power> cachedPowerMap = new HashMap<>();
        powerRepository.findAll().forEach(power -> cachedPowerMap.put(power.getSsid(), power));


        VerticalLayout output = new VerticalLayout();
        Button saveButton = new UserSafeButton("Save changes", event -> {
            powerRepository.saveAll(newPowers);
            powerRepository.saveAll(updatedPowers);
            powerSetRepository.saveAll(newPowerSets);
            powerSetRepository.saveAll(updatedPowerSets);
            handleDeletedPowers();
            handleOrphanedPowers();
            powersCache.load();
            dialog.close();
            UI.getCurrent().navigate(PowerSetBrowserView.class);
        });
        saveButton.setEnabled(false);
        HorizontalLayout results = new HorizontalLayout();

        VerticalLayout newClutchLayout = new VerticalLayout();
        VerticalLayout updatedClutchLayout = new VerticalLayout();
        VerticalLayout unchangedClutchLayout = new VerticalLayout();
        Details newClutchDetails = new Details("New PowerSets", newClutchLayout);
        newClutchDetails.setEnabled(false);
        Details updatedClutchDetails = new Details("Modified PowerSets", updatedClutchLayout);
        updatedClutchDetails.setEnabled(false);
        Details unchangedClutchDetails = new Details("Unchanged PowerSets", unchangedClutchLayout);
        unchangedClutchDetails.setEnabled(false);


        VerticalLayout newPowerLayout = new VerticalLayout();
        VerticalLayout updatedPowerLayout = new VerticalLayout();
        VerticalLayout unchangedPowerLayout = new VerticalLayout();
        VerticalLayout deletedPowerLayout = new VerticalLayout();
        Details newPowerDetails = new Details("New Powers", newPowerLayout);
        newPowerDetails.setEnabled(false);
        Details updatedPowerDetails = new Details("Modified Powers", updatedPowerLayout);
        updatedPowerDetails.setEnabled(false);
        Details unchangedPowerDetails = new Details("Unchanged Powers", unchangedPowerLayout);
        unchangedPowerDetails.setEnabled(false);
        Details deletedPowerDetails = new Details("Deleted Powers", deletedPowerLayout);
        deletedPowerDetails.setEnabled(false);


        results.add(newClutchDetails, updatedClutchDetails, unchangedClutchDetails);
        results.add(newPowerDetails, updatedPowerDetails, unchangedPowerDetails, deletedPowerDetails);

        InMemoryUploadHandler handler = UploadHandler.inMemory(
                (metadata, data) -> {
                    output.removeAll();
                    output.add(new Span("Filename uploaded: "+metadata.fileName()));
                    progressBar.setIndeterminate(true);
                    output.add(progressBar);
                    OPCPackage pkg;
                    try {
                        pkg = OPCPackage.open(new ByteArrayInputStream(data));
                        XSSFWorkbook workbook = new XSSFWorkbook(pkg);
                        PowerTransformer powerTransformer = new PowerTransformer();
                        PowerSetTransformer powerSetTransformer = new PowerSetTransformer();
                        validateSheetMetadata(workbook, powerSetTransformer);
                        validateSheetMetadata(workbook, powerTransformer);

                        Map<String, PowerSet> powerSetsInSheet = (Map<String, PowerSet>) loadSheet(workbook, powerSetTransformer);
                        log.info("PowerSets found:"+ powerSetsInSheet.size());

                        Map<String, Power> powersInSheet = (Map<String, Power>) loadSheet(workbook, powerTransformer);
                        log.info("Powers found:"+ powersInSheet.size());


                        powerSetsInSheet.forEach((name, loadedPowerSet) -> {
                            //max[0] = Math.max(max[0], loadedPowerSet.getAbilityText().length());
                            PowerSet oldPowerSet = cachedPowerSetMap.get(loadedPowerSet.getSsid());
                            if (oldPowerSet == null) {
                                newPowerSets.add(loadedPowerSet);
                                newClutchLayout.add(new Span(loadedPowerSet.getName()));
                            }
                            else if (loadedPowerSet.deepEquals(oldPowerSet)) {
                                unchangedClutchLayout.add(new Span(loadedPowerSet.getName()));
                                unchangedPowerSets.add(oldPowerSet);
                            }
                            else {
                                updatedClutchLayout.add(new Span(loadedPowerSet.getName()));
                                updatedPowerSets.add(loadedPowerSet);
                            }

                        });

                        //log.info("Max ability text:"+max[0]);

                        newClutchDetails.setSummaryText("New PowerSets: " + newPowerSets.size());
                        newClutchDetails.setEnabled(true);
                        unchangedClutchDetails.setSummaryText("Unchanged PowerSets: " + unchangedPowerSets.size());
                        unchangedClutchDetails.setEnabled(true);
                        updatedClutchDetails.setSummaryText("Modified PowerSets: " + updatedPowerSets.size());
                        updatedClutchDetails.setEnabled(true);

                        powersInSheet.forEach((name, loadedPower) -> {
                            //max[0] = Math.max(max[0], loadedPower.getSubPowers().length());
                            //Power oldPower = cachedPowerMap.get(loadedPower.getSsid());
                            Power oldPower = cachedPowerMap.remove(loadedPower.getSsid());
                            if (oldPower == null) {
                                newPowers.add(loadedPower);
                                newPowerLayout.add(new Span(loadedPower.getName()));
                            }
                            else if (loadedPower.deepEquals(oldPower)) {
                                unchangedPowerLayout.add(new Span(loadedPower.getName()));
                                unchangedPowers.add(oldPower);
                            }
                            else {
                                updatedPowerLayout.add(new Span(loadedPower.getName()));
                                updatedPowers.add(loadedPower);
                            }

                        });

                        deletedPowers.addAll(cachedPowerMap.values());
                        for (Power power : deletedPowers) {
                            deletedPowerLayout.add(new Span(power.getName()));
                        }



                        newPowerDetails.setSummaryText("New Powers: " +newPowers.size());
                        newPowerDetails.setEnabled(true);
                        unchangedPowerDetails.setSummaryText("Unchanged Powers: " +unchangedPowers.size());
                        unchangedPowerDetails.setEnabled(true);
                        updatedPowerDetails.setSummaryText("Modified Powers: " +updatedPowers.size());
                        updatedPowerDetails.setEnabled(true);

                        deletedPowerDetails.setSummaryText("Deleted Powers: " +deletedPowers.size());
                        deletedPowerDetails.setEnabled(true);

                        progressBar.setValue(1);
                        saveButton.setEnabled(!newPowers.isEmpty() || !newPowerSets.isEmpty()
                                || !updatedPowers.isEmpty() || !updatedPowerSets.isEmpty()
                                || !deletedPowers.isEmpty());


                    }
                    catch (Throwable t) {
                        output.add(new Span(t.getMessage()));
                    }
                    progressBar.setIndeterminate(false);
                }).whenStart(transferContext -> dialog.open()

        );

        Upload upload = new Upload(handler);
        upload.setAcceptedFileTypes("application/xlsx", ".xlsx");

        upload.addFileRejectedListener(event -> {
            output.removeAll();
            output.add(event.getErrorMessage());
        });
        upload.getElement().addEventListener("file-remove", event -> output.removeAll());

        dialog.add(output, results);
        dialog.setConfirmButton(saveButton);

        add(new Paragraph("Accepted file format: MS Excel (.xlsx)"));
        add(upload);
    }

    private void handleOrphanedPowers() {
        for (Power power : updatedPowers) {
            if (power.getPowerSets() == null) {
                log.info("Newly orphaned power " + power.getName());
                deletePowerFromUsers(power);
            }
        }
        for (Power power : unchangedPowers) {
            if (power.getPowerSets() == null) {
                log.info("Older orphaned power " + power.getName());
                deletePowerFromUsers(power);
            }
        }
    }

    private void handleDeletedPowers() {
        if (deletedPowers.isEmpty()) {
            return;
        }
        for (Power power : deletedPowers) {
            deletePowerFromUsers(power);
            powerRepository.delete(power);
        }
    }

    private void deletePowerFromUsers(Power power) {
        List<HeroPower> heroPowers = heroPowerRepository.findAllByPower(power);
        for (HeroPower heroPower : heroPowers) {
            //log.info("Deletion target"+heroPower.toString());
            Message message = new Message();
            message.setUserId(heroPower.getPlayerCharacter().getUserId());
            message.setText("The " + power.getName() + " power has been removed from the game and removed from your character: "
                    + heroPower.getPlayerCharacter().getName()+". Consult with your GM and choose a new power."
            );
            messageRepository.save(message);
            heroPowerRepository.delete(heroPower);
        }/*
        List<HeroSubPower> heroSubPowers = sessionService.getHspRepo().findAllByPower(power);
        for (HeroSubPower heroSubPower : heroSubPowers) {
            //log.info("Deletion target"+heroPower.toString());
            Message message = new Message();
            message.setUserId(heroSubPower.getPlayerCharacter().getUserId());
            message.setText("The " + power.getName() + " power has been removed from the game and removed from your character: "
                    + heroSubPower.getPlayerCharacter().getName()+". Consult with your GM and choose a new power."
            );
            sessionService.getMessageRepository().save(message);
            sessionService.getHspRepo().delete(heroSubPower);
        }*/
    }

    private void validateSheetMetadata(XSSFWorkbook workbook, Transformer<? extends Sheetable> transformer) throws IllegalArgumentException {
        XSSFSheet sheet = workbook.getSheet(transformer.getSheetName());
        if (sheet == null) {
            throw new IllegalArgumentException("Didn't find a sheet named: "+transformer.getSheetName());
        }
        String[] labels = transformer.getLabels();
        int labelRowIndex = transformer.getLabelRowIndex();
        XSSFRow labelRow = sheet.getRow(labelRowIndex);
        for (int i = 0; i < labels.length; ++i) {
            if (!labels[i].equals(labelRow.getCell(i).toString())) {
                throw new IllegalArgumentException("Sheet:"+transformer.getSheetName()+
                        "-Label Row:"+labelRowIndex+"|Cell:"+i+" has unexpected value["
                        + labelRow.getCell(i).toString() + "] expected["+labels[i]+"]");
            }
        }

    }


    private Map<String, ? extends Sheetable> loadSheet(XSSFWorkbook workbook, Transformer<? extends Sheetable> transformer) throws TransformerException {
        Map<String, Sheetable> map = new HashMap<>();
        XSSFSheet sheet = workbook.getSheet(transformer.getSheetName());
        log.info("sheet size: "+sheet.getPhysicalNumberOfRows());
        for (int i = 0; i < sheet.getPhysicalNumberOfRows(); ++i) {
            if (i > transformer.getLabelRowIndex()) {
                if (sheet.getRow(i).getCell(0).toString().equals("ZZSTOP")) {
                    //looking at the last row;
                    break;
                }
                Sheetable sheetable = transformer.transformRow(sheet.getRow(i));
                if (sheetable.getSsid() != null && sheetable.getSsid().length() > 0) {
                    map.put(sheetable.getName(), sheetable);
                }
            }
        }
        return map;

    }


    @Override
    public String getTitle() {
        return "Upload PowerSet Spreadsheet";
    }
}
