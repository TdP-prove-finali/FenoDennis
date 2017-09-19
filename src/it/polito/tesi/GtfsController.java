package it.polito.tesi;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tesi.bean.Agenzia;
import it.polito.tesi.bean.Fermata;
import it.polito.tesi.bean.Linea;
import it.polito.tesi.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class GtfsController {

	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private DatePicker dataPicker;

    @FXML
    private ComboBox<Integer> comboOra;

    @FXML
    private ComboBox<Integer> comboMinuti;

    @FXML
    private TextArea txtResult;
    
    @FXML
    private ListView<Fermata> lvPartenza;

    @FXML
    private ListView<Fermata> lvArrivo;

    @FXML
    private ListView<Linea> lvLinea;

    @FXML
    private ListView<Agenzia> lvAgenzia;
    
    @FXML
    private Canvas canvas;

    @FXML
    void doSimula(ActionEvent event) {

    	model.calcolaPercorso(lvPartenza.getSelectionModel().getSelectedItem(), 
    			lvArrivo.getSelectionModel().getSelectedItem());
    	txtResult.appendText(model.getPercorsoEdgeList());
    	    	
    }

    @FXML
    void initialize() {
        assert dataPicker != null : "fx:id=\"dataPicker\" was not injected: check your FXML file 'gtfs.fxml'.";
        assert comboOra != null : "fx:id=\"comboOra\" was not injected: check your FXML file 'gtfs.fxml'.";
        assert comboMinuti != null : "fx:id=\"comboMinuti\" was not injected: check your FXML file 'gtfs.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'gtfs.fxml'.";
        assert canvas != null : "fx:id=\"canvas\" was not injected: check your FXML file 'gtfs.fxml'.";
        assert lvPartenza != null : "fx:id=\"lvPartenza\" was not injected: check your FXML file 'Gtfs.fxml'.";
        assert lvArrivo != null : "fx:id=\"lvArrivo\" was not injected: check your FXML file 'Gtfs.fxml'.";
        assert lvLinea != null : "fx:id=\"lvLinea\" was not injected: check your FXML file 'Gtfs.fxml'.";
        assert lvAgenzia != null : "fx:id=\"lvAgenzia\" was not injected: check your FXML file 'Gtfs.fxml'.";
        }

	public void setModel(Model m) {
		this.model = m ;

		List<Fermata> f = new ArrayList<>(this.model.getFermate().values());
		Collections.sort(f);
		
		List<Linea> l = new ArrayList<>(this.model.getLinee().values());
		Collections.sort(l);
		
		List<Agenzia> a = new ArrayList<>(this.model.getAgenzie().values());
		Collections.sort(a);
		
        for(int i=0;i<24;i++)
        	comboOra.getItems().add(Integer.valueOf(i)) ;  
		for(int i=0;i<4;i++)
			comboMinuti.getItems().add(Integer.valueOf(i*15));
		
        if(comboOra.getItems().size() > 0)
        	comboOra.setValue(comboOra.getItems().get(0)); 
        if(comboMinuti.getItems().size() > 0)
        	comboMinuti.setValue(comboMinuti.getItems().get(0)); 

		lvPartenza.getItems().addAll(f);
		lvArrivo.getItems().addAll(f);
		lvLinea.getItems().addAll(l);
		lvAgenzia.getItems().addAll(a);
		model.creaGrafo(LocalDateTime.now());
		
		
	}
}
