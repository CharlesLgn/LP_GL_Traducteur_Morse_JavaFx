package com.controller;

import com.method.JouerSon;
import com.util.Utilitaires;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.*;

import java.io.*;

import static com.method.TranslatorListe.romainToMorse;

public class FrToMorseController {

    @FXML
    Button btCheminMorse;

    @FXML
    Button btJouerSonFrToMorse;

    @FXML
    TextField texBoxCheminFrToMorse;

    @FXML
    TextField textboxExporterFrToMorse;

    @FXML
    TextArea richTextboxFrToMorse;

    @FXML
    Button btTradFrToMorse;

    @FXML
    Button btExporterFrToMorse;

    @FXML
    Button btNouvelleTrad;

    public void btCheminMorseClick (MouseEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisissez un fichier texte");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.getExtensionFilters();
        try {
            File file = fileChooser.showOpenDialog(Utilitaires.getScene(event));
            if (file != null) {
                this.texBoxCheminFrToMorse.setText(file.getAbsolutePath());
            }
            this.texBoxCheminFrToMorse.setDisable(true);
        }catch (Exception ex){
            System.out.println(ex);
        }
    }

    public void btTradFrToMorseClick(MouseEvent event){
        if(this.texBoxCheminFrToMorse.getText() != null && !this.texBoxCheminFrToMorse.getText().isEmpty()) {

            try {
                InputStream flux = new FileInputStream(this.texBoxCheminFrToMorse.getText());
                InputStreamReader lecture = new InputStreamReader(flux);
                BufferedReader buff=new BufferedReader(lecture);
                String ligne;
                String resmorse = "";
                while ((ligne=buff.readLine())!=null){
                    for (char lettre:ligne.toCharArray()) {
                        String ajoutlettre = null;
                        if(romainToMorse(lettre) != "  "){
                            resmorse = resmorse + romainToMorse(lettre) + " ";
                        }else{
                            resmorse = resmorse + romainToMorse(lettre);
                        }
                    }
                    this.richTextboxFrToMorse.appendText(resmorse.trim());
                }
                buff.close();
                this.btTradFrToMorse.setDisable(true);
            }catch(Exception ex){
                System.out.println(ex);
            }
        }
        else{
            com.method.Alert.alertGenerique("Sélectionnez dans un premier temps un fichier texte à traduire");
        }
    }

    public void btJouerSonFrToMorseClick(MouseEvent event){
        String str;
        str = this.richTextboxFrToMorse.getText();

        final Service<Void> jouerSonService = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        try {
                            JouerSon.jouerson(str);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                };
            }
        };

        jouerSonService.start();
    }

    public void btExporterFrToMorseClick(MouseEvent event){
        try {
            if (this.richTextboxFrToMorse.getText() != null && !this.richTextboxFrToMorse.getText().isEmpty()) {
                DirectoryChooser directorychooser = new DirectoryChooser();
                directorychooser.setTitle("Choisissez un répertoire ou exporter votre traduction");
                File selectedDirectory = directorychooser.showDialog(Utilitaires.getScene(event));
                if (selectedDirectory != null) {
                    File fichierexport = new File(selectedDirectory + "\\" + this.textboxExporterFrToMorse.getText() + ".txt");
                    fichierexport.createNewFile();
                    FileWriter fichierexportwrite =new FileWriter(fichierexport);
                    fichierexportwrite.write(this.richTextboxFrToMorse.getText());
                    fichierexportwrite.close();
                    //region messagebox
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Succès");
                    alert.initStyle(StageStyle.DECORATED);
                    alert.setHeaderText("Export");
                    alert.setContentText("La traduction a été exportée");
                    ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("/resource/Images/icon.png"));
                    alert.showAndWait();
                    //endregion
                } else {
                    com.method.Alert.alertGenerique("Sélectionnez un fichier texte valide");
                }
            } else {
                com.method.Alert.alertGenerique("Vérifiez qu'un traduction a été effectuée");
            }
        }catch (Exception ex){
            System.out.println(ex);
        }
    }

    public void btNouvelleTradClick(MouseEvent event){
        this.texBoxCheminFrToMorse.clear();
        this.richTextboxFrToMorse.clear();
        this.textboxExporterFrToMorse.clear();
        this.btTradFrToMorse.setDisable(false);
        this.btCheminMorse.setDisable(false);
    }
}
