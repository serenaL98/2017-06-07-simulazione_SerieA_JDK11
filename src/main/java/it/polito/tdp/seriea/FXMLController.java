package it.polito.tdp.seriea;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.seriea.model.Model;
import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.Team;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ChoiceBox<Season> boxSeason;

    @FXML
    private ChoiceBox<Team> boxTeam;

    @FXML
    private TextArea txtResult;

    @FXML
    void handleCarica(ActionEvent event) {

    	txtResult.clear();
    	
    	Season stag = this.boxSeason.getValue();
    	if(stag == null) {
    		txtResult.setText("Selezionare una stagione dal menù.");
    		return;
    	}
    	
    	String stagione = stag.getDescription();
    	
    	txtResult.appendText("Crea grafo...");
    	
    	this.model.creaGrafo(stagione);
    	
    	txtResult.appendText("\n\n#VERTICI: "+this.model.numeroVertici());
    	txtResult.appendText("\n#ARCHI: "+this.model.numeroArchi());
    	txtResult.appendText("\n\nClassifica finale:\n"+this.model.classificaFinale(stagione));
    	
    	this.boxTeam.getItems().addAll(this.model.elencoSquadre());
    }

    @FXML
    void handleDomino(ActionEvent event) {

    	txtResult.clear();
    	
    	Team squadra = this.boxTeam.getValue();
    	
    	txtResult.appendText("Cammino massimo:\n"+this.model.risultato(squadra));
    	
    }

    @FXML
    void initialize() {
        assert boxSeason != null : "fx:id=\"boxSeason\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert boxTeam != null : "fx:id=\"boxTeam\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'SerieA.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
		this.boxSeason.getItems().addAll(this.model.elencoStagioni());
	}
}
