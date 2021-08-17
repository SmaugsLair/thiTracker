package com.smaugslair.thitracker.ui;

import com.smaugslair.thitracker.data.powers.Power;
import com.smaugslair.thitracker.data.powers.PowerSet;
import com.smaugslair.thitracker.data.powers.Sheetable;
import com.smaugslair.thitracker.services.SessionService;
import com.smaugslair.thitracker.ui.powers.PowerSetBrowserView;
import com.smaugslair.thitracker.ui.powers.transformers.PowerSetTransformer;
import com.smaugslair.thitracker.ui.powers.transformers.PowerTransformer;
import com.smaugslair.thitracker.ui.powers.transformers.Transformer;
import com.smaugslair.thitracker.ui.powers.transformers.TransformerException;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@PageTitle("Powers Upload Page")
@Route(value = "powersupload", layout = MainView.class)
public class PowersUpload extends VerticalLayout {

    private static final Logger log = LoggerFactory.getLogger(PowersUpload.class);

    private final List<PowerSet> newPowerSets = new ArrayList<>();
    private final List<PowerSet> unchangedPowerSets = new ArrayList<>();
    private final List<PowerSet> updatedPowerSets = new ArrayList<>();

    private final List<Power> newPowers = new ArrayList<>();
    private final List<Power> unchangedPowers = new ArrayList<>();
    private final List<Power> updatedPowers = new ArrayList<>();

    private final ProgressBar progressBar = new ProgressBar();

    public PowersUpload(SessionService sessionService) {

        Map<String, PowerSet> cachedPowerSetMap = new HashMap<>();
        sessionService.getPowersCache().getPowerSetList().forEach(powerSet -> cachedPowerSetMap.put(powerSet.getSsid(), powerSet));

        Map<String, Power> cachedPowerMap = new HashMap<>();
        sessionService.getPowerRepo().findAll().forEach(power -> cachedPowerMap.put(power.getSsid(), power));

        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        VerticalLayout output = new VerticalLayout();
        Button saveButton = new Button("Save changes", event -> {
            sessionService.getPowerRepo().saveAll(newPowers);
            sessionService.getPowerRepo().saveAll(updatedPowers);
            sessionService.getPowerSetRepo().saveAll(newPowerSets);
            sessionService.getPowerSetRepo().saveAll(updatedPowerSets);
            sessionService.getPowersCache().load();
            event.getSource().getUI().ifPresent(ui -> ui.navigate(PowerSetBrowserView.class));
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
        Details newPowerDetails = new Details("New Powers", newPowerLayout);
        newPowerDetails.setEnabled(false);
        Details updatedPowerDetails = new Details("Modified Powers", updatedPowerLayout);
        updatedPowerDetails.setEnabled(false);
        Details unchangedPowerDetails = new Details("Unchanged Powers", unchangedPowerLayout);
        unchangedPowerDetails.setEnabled(false);


        results.add(newClutchDetails, updatedClutchDetails, unchangedClutchDetails);
        results.add(newPowerDetails, updatedPowerDetails, unchangedPowerDetails);


        upload.addSucceededListener(event -> {
            output.removeAll();
            output.add(new Label("Filename uploaded: "+event.getFileName()));
            progressBar.setIndeterminate(true);
            output.add(progressBar);
            OPCPackage pkg;
            try {
                pkg = OPCPackage.open(buffer.getInputStream());
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
                        newClutchLayout.add(new Label(loadedPowerSet.getName()));
                    }
                    else if (loadedPowerSet.deepEquals(oldPowerSet)) {
                        unchangedClutchLayout.add(new Label(loadedPowerSet.getName()));
                        unchangedPowerSets.add(oldPowerSet);
                    }
                    else {
                        updatedClutchLayout.add(new Label(loadedPowerSet.getName()));
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
                    Power oldPower = cachedPowerMap.get(loadedPower.getSsid());
                    if (oldPower == null) {
                        newPowers.add(loadedPower);
                        newPowerLayout.add(new Label(loadedPower.getName()));
                    }
                    else if (loadedPower.deepEquals(oldPower)) {
                        unchangedPowerLayout.add(new Label(loadedPower.getName()));
                        unchangedPowers.add(oldPower);
                    }
                    else {
                        updatedPowerLayout.add(new Label(loadedPower.getName()));
                        updatedPowers.add(loadedPower);
                    }

                });


                newPowerDetails.setSummaryText("New Powers: " +newPowers.size());
                newPowerDetails.setEnabled(true);
                unchangedPowerDetails.setSummaryText("Unchanged Powers: " +unchangedPowers.size());
                unchangedPowerDetails.setEnabled(true);
                updatedPowerDetails.setSummaryText("Modified Powers: " +updatedPowers.size());
                updatedPowerDetails.setEnabled(true);

                progressBar.setValue(1);
                saveButton.setEnabled(!newPowers.isEmpty() || !newPowerSets.isEmpty()
                        || !updatedPowers.isEmpty() || !updatedPowerSets.isEmpty());


            } catch (Throwable t) {
                t.printStackTrace();
                output.add(new Span(t.getMessage()));
                for (StackTraceElement line: t.getStackTrace()) {
                    output.add(new Span(line.toString()));
                }
            }
            progressBar.setIndeterminate(false);
        });

        upload.addFileRejectedListener(event -> {
            output.removeAll();
            output.add(event.getErrorMessage());
        });
        upload.getElement().addEventListener("file-remove", event -> output.removeAll());

        Dialog dialog = new Dialog();
        dialog.add(output, saveButton, results);

        upload.addStartedListener(event -> {dialog.open();});

        add(upload);
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
                Sheetable sheetable = transformer.transformRow(sheet.getRow(i));
                if (sheetable.getSsid() != null && sheetable.getSsid().length() > 0) {
                    //log.info(sheetable.toString());
                    map.put(sheetable.getName(), sheetable);
                }
            }
        }
        return map;

    }


}
