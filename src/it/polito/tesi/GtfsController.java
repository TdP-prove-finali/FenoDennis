package it.polito.tesi;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tesi.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;

public class GtfsController {

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

	private Model model;

    @FXML
    void doSimula(ActionEvent event) {

    }

    @FXML
    void initialize() {
        assert dataPicker != null : "fx:id=\"dataPicker\" was not injected: check your FXML file 'gtfs.fxml'.";
        assert comboOra != null : "fx:id=\"comboOra\" was not injected: check your FXML file 'gtfs.fxml'.";
        assert comboMinuti != null : "fx:id=\"comboMinuti\" was not injected: check your FXML file 'gtfs.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'gtfs.fxml'.";
		
        for(int i=0;i<24;i++)
        	comboOra.getItems().add(Integer.valueOf(i)) ;  
		for(int i=0;i<4;i++)
			comboMinuti.getItems().add(Integer.valueOf(i*15));
		
        if(comboOra.getItems().size() > 0)
        	comboOra.setValue(comboOra.getItems().get(0)); 
        if(comboMinuti.getItems().size() > 0)
        	comboMinuti.setValue(comboMinuti.getItems().get(0)); 
    }

	public void setModel(Model model) {
		this.model = model ;
	}
}
