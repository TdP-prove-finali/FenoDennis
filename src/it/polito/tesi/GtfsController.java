package it.polito.tesi;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tesi.bean.Fermata;
import it.polito.tesi.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
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
    private ComboBox<String> comboPartenza;

    @FXML
    private ComboBox<Fermata> comboArrivo;

    @FXML
    private TextArea txtResult;

    @FXML
    private Canvas canvas;

    @FXML
    void doSimula(ActionEvent event) {

    }

    @FXML
    void initialize() {
        assert dataPicker != null : "fx:id=\"dataPicker\" was not injected: check your FXML file 'gtfs.fxml'.";
        assert comboOra != null : "fx:id=\"comboOra\" was not injected: check your FXML file 'gtfs.fxml'.";
        assert comboMinuti != null : "fx:id=\"comboMinuti\" was not injected: check your FXML file 'gtfs.fxml'.";
        assert comboPartenza != null : "fx:id=\"comboPartenza\" was not injected: check your FXML file 'gtfs.fxml'.";
        assert comboArrivo != null : "fx:id=\"comboArrivo\" was not injected: check your FXML file 'gtfs.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'gtfs.fxml'.";
        assert canvas != null : "fx:id=\"canvas\" was not injected: check your FXML file 'gtfs.fxml'.";
		
    }

	public void setModel(Model m) {
		this.model = m ;
//		System.out.println(this.model.getFermate().values());
		List<Fermata> f = new ArrayList<>(this.model.getFermate().values());
        for(int i=0;i<24;i++)
        	comboOra.getItems().add(Integer.valueOf(i)) ;  
		for(int i=0;i<4;i++)
			comboMinuti.getItems().add(Integer.valueOf(i*15));
		
        if(comboOra.getItems().size() > 0)
        	comboOra.setValue(comboOra.getItems().get(0)); 
        if(comboMinuti.getItems().size() > 0)
        	comboMinuti.setValue(comboMinuti.getItems().get(0)); 

		
System.out.println(comboPartenza);		
//		comboPartenza.getItems().add("ciao");
		
	}
}
