package it.polito.tesi;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import it.polito.tesi.bean.Agenzia;
import it.polito.tesi.bean.Fermata;
import it.polito.tesi.bean.Linea;
import it.polito.tesi.model.Model;

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
    private ComboBox<Integer> comboDurata;
    
    @FXML
    private CheckBox checkConnesso;
    
    @FXML
    private ListView<Fermata> lvPartenza;

    @FXML
    private ListView<Fermata> lvArrivo;

    @FXML
    private ListView<Linea> lvLinea;

    @FXML
    private ListView<Agenzia> lvAgenzia;
    
    @FXML
    private TextField txtCapienzaMax;

    @FXML
    private TextField txtPersone;

    @FXML
    private TextField txtTempo;

    @FXML
    private Slider sProbLinea;

    @FXML
    private Label lPermanenzaLinea;

    @FXML
    private Slider sProbFlusso;

    @FXML
    private Label lPermanenzaFlusso;

    @FXML
    private Slider sCambio;

    @FXML
    private Label lCambio;

    @FXML
    private Slider sCoefficiente;

    @FXML
    private Label lCoefficiente;

    @FXML
    private TextArea txtResult;

    @FXML
    private PieChart pieChart;

    @FXML
    private BarChart<String, Integer> barChart;
    
    @FXML
    private BarChart<String, Integer> barChart2;
    
    @FXML
    private Button btnP;
    
    @FXML
    private Button btnS;
    
    @FXML
    void cCambio(MouseEvent event) {
    	lCambio.setText(String.format("%.2f",sCambio.getValue()));
    }

    @FXML
    void cCoefficiente(MouseEvent event) {
    	lCoefficiente.setText(String.format("%.2f",sCoefficiente.getValue()));
    }

    @FXML
    void cProbFlusso(MouseEvent event) {
    	lPermanenzaFlusso.setText(String.format("%.2f",sProbFlusso.getValue()));
    }

    @FXML
    void cProbLinea(MouseEvent event) {
    	lPermanenzaLinea.setText(String.format("%.2f",sProbLinea.getValue()));
    }

    @FXML
    void doPercorso(ActionEvent event) {
    	
    	StringBuilder s = new StringBuilder();
    	boolean error = false ;
    	
		try {
//			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
			LocalDate dataMin = LocalDate.of(2010, 1, 1) ;
			LocalDate dataMax = LocalDate.now() ;
			LocalDate data = dataPicker.getValue() ;
			
			if(data==null){ 
				error = true ;
				s.append("Devi selezionare una data per la simulazione.\n"); 
			}
			
			if(data.isBefore(dataMin) || data.isAfter(dataMax)){ 
				error = true ;
				s.append("Inserisci una data compresa tra il 01/01/2010 e oggi.\n"); 
			}
			
			int ora = comboOra.getValue(); 
			int minuti = comboMinuti.getValue() ;
			int durata = comboDurata.getValue() ;
			
			if(ora + durata > 23) durata= 23-ora ;
			boolean connesso = checkConnesso.isSelected() ;
			
			Fermata partenza = lvPartenza.getSelectionModel().getSelectedItem() ;
			Fermata arrivo = lvArrivo.getSelectionModel().getSelectedItem() ;

			if(partenza==null || arrivo == null){ 
				error = true ;
				s.append("Devi selezionare una fermata di partenza e una di destionazione.\n");
			}
			
			if(partenza.equals(arrivo)){ 
				error = true ;
				s.append("La fermata di partenza e di destinazione non devono coincidere.\n");
			}
			
			if(!error){
				this.model.creaGrafo(LocalDateTime.of(data, LocalTime.of(ora, minuti)), durata, connesso);
				this.model.calcolaPercorso(partenza, arrivo);
				txtResult.appendText(this.model.getPercorsoEdgeList());			
				btnS.setDisable(false);
			}
			
		} catch (RuntimeException e) {
			error = true ;
//			s.append(e.getMessage());
		}
		
		if(error){
	    	Alert alert = new Alert(AlertType.WARNING);
	    	alert.setTitle("Errore");
	    	alert.setHeaderText("Errore");
	    	alert.setContentText(s.toString());
	    	alert.showAndWait();
		}
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@FXML
    void doSimula(ActionEvent event) {

    	StringBuilder s = new StringBuilder();
    	boolean error = false ;
    	
		try {
				int capienza = Integer.parseInt( txtCapienzaMax.getText());
				
				if(capienza<30 || capienza >1000){ 
					error = true ;
					s.append("La capienza deve essere compresa tra 30 e 1000.\n"); 
				}	
				
				int nPersFermata = Integer.parseInt( txtPersone.getText());

				if(nPersFermata<1 || nPersFermata >200){ 
					error = true ;
					s.append("Il numero di persone deve essere compresa tra 1 e 200.\n"); 
				}	
				
				int intervallo = Integer.parseInt( txtTempo.getText());

				if(intervallo<1 || intervallo >30){ 
					error = true ;
					s.append("L'intervallo temporale deve essere compreso tra 1 e 30 secondi.\n"); 
				}	
				
				double probLinea = sProbLinea.getValue();
				double probFlusso = sProbFlusso.getValue();
				double probCambio = sCambio.getValue();
				double coefficiente = sCoefficiente.getValue();
				
				if(!error){

					this.model.Simula(capienza, nPersFermata, intervallo, probLinea,
							probFlusso, probCambio, coefficiente);
					
					if(this.model.getPercorsoTempoTotale()<0){
				    	Alert alert = new Alert(AlertType.INFORMATION);
				    	alert.setTitle("Info");
				    	alert.setHeaderText("La simulazione non terrà conto del flusso di passeggeri.");
				    	alert.setContentText("Non è stato trovato un percorso per le fermate indicate, quindi la simulazione non terrà conto del flusso.");
				    	alert.showAndWait();
					}

					
					int sodd = this.model.getClientiSoddisfati();
					int parz = this.model.getClientiParzSoddisfati();
					int ind = this.model.getClientiInoddisfati();
					int tot = sodd + parz + ind;
					
					StringBuilder result = new StringBuilder();
				if(tot>0){
					result.append("\nRisultato della simulazione.\n\nClienti Soddisfatti: "+sodd+
							"\nClienti Parzialmente Soddisfatti: "+parz+
							"\nClienti Non Soddisfatti: "+ind+
							"\nTotali: "+tot+"\n");
					
					this.pieChart.setVisible(true);
					this.pieChart.setData(FXCollections.observableArrayList(
			                new PieChart.Data("Soddisfatti", sodd),
			                new PieChart.Data("Parzialmente Soddisfatti", parz),
			                new PieChart.Data("Non soddisfatti", ind)));
					this.pieChart.setTitle("Soddisfazione Clienti");
					this.pieChart.setLegendVisible(true);
					
					List<FermataNumero> g1 = this.model.getSoddFermata(); 
					List<LineaNumero> g2 = this.model.getSoddLinea(); 
					
					barChart.setVisible(true);
					barChart.setTitle("Insoddisfatti per Fermata");
					
					XYChart.Series series1 = new XYChart.Series();
			        
					result.append("\nClienti insoddisfatti per Fermata."); 
					for(int i = 0 ; i < g1.size() ; i++){
						result.append("\n"+(i+1)+") "+g1.get(i).toString()) ;
//				        series1.getData().add(new XYChart.Data(g1.get(i).getFermata().getName(), g1.get(i).getNumero()));
				        series1.getData().add(new XYChart.Data(""+(i+1), g1.get(i).getNumero()));
				        if(i==9)i=g1.size(); 
					}
					barChart.getData().clear(); 
			        barChart.getData().add(series1);					
					
					barChart2.setVisible(true);
					barChart2.setTitle("Insoddisfatti per Linea");
					
			        XYChart.Series series2 = new XYChart.Series();
			        
					result.append("\n\nClienti insoddisfatti per Linea."); 
					for(int i = 0 ; i < g2.size() ; i++){
						result.append("\n"+(i+1)+") "+g2.get(i).toString()) ;
				        series2.getData().add(new XYChart.Data(""+(i+1), g2.get(i).getNumero()));
				        if(i==9)i=g2.size(); 
					}
					barChart2.getData().clear(); 
			        barChart2.getData().add(series2);					

				}
				else{
					error=true ;
					result.append("\n\nLa scarsità di mezzi in questa fascia oraria non ha consentito l'esecuzione della simulazione.\n\n") ;
					s.append("\n\nLa scarsità di mezzi in questa fascia oraria non ha consentito l'esecuzione della simulazione.\n\n") ;
				}
					txtResult.appendText(result.toString()+"\n\n");
				}
				
			} catch (NumberFormatException e) {
				error = true ;
				s.append(e.getMessage());
			}
			
			if(error){
		    	Alert alert = new Alert(AlertType.WARNING);
		    	alert.setTitle("Errore");
		    	alert.setHeaderText("Errore");
		    	alert.setContentText(s.toString());
		    	alert.showAndWait();
			}
    	
    }

    @FXML
    void initialize() {
        assert dataPicker != null : "fx:id=\"dataPicker\" was not injected: check your FXML file 'Gtfs.fxml'.";
        assert comboOra != null : "fx:id=\"comboOra\" was not injected: check your FXML file 'Gtfs.fxml'.";
        assert comboMinuti != null : "fx:id=\"comboMinuti\" was not injected: check your FXML file 'Gtfs.fxml'.";
        assert checkConnesso != null : "fx:id=\"checkConnesso\" was not injected: check your FXML file 'Gtfs.fxml'.";
        assert lvPartenza != null : "fx:id=\"lvPartenza\" was not injected: check your FXML file 'Gtfs.fxml'.";
        assert lvArrivo != null : "fx:id=\"lvArrivo\" was not injected: check your FXML file 'Gtfs.fxml'.";
        assert lvLinea != null : "fx:id=\"lvLinea\" was not injected: check your FXML file 'Gtfs.fxml'.";
        assert lvAgenzia != null : "fx:id=\"lvAgenzia\" was not injected: check your FXML file 'Gtfs.fxml'.";
        assert txtCapienzaMax != null : "fx:id=\"txtCapienzaMax\" was not injected: check your FXML file 'Gtfs.fxml'.";
        assert txtPersone != null : "fx:id=\"txtPersone\" was not injected: check your FXML file 'Gtfs.fxml'.";
        assert txtTempo != null : "fx:id=\"txtTempo\" was not injected: check your FXML file 'Gtfs.fxml'.";
        assert sProbLinea != null : "fx:id=\"sProbLinea\" was not injected: check your FXML file 'Gtfs.fxml'.";
        assert lPermanenzaLinea != null : "fx:id=\"lPermanenzaLinea\" was not injected: check your FXML file 'Gtfs.fxml'.";
        assert sProbFlusso != null : "fx:id=\"sProbFlusso\" was not injected: check your FXML file 'Gtfs.fxml'.";
        assert lPermanenzaFlusso != null : "fx:id=\"lPermanenzaFlusso\" was not injected: check your FXML file 'Gtfs.fxml'.";
        assert sCambio != null : "fx:id=\"sCambio\" was not injected: check your FXML file 'Gtfs.fxml'.";
        assert lCambio != null : "fx:id=\"lCambio\" was not injected: check your FXML file 'Gtfs.fxml'.";
        assert sCoefficiente != null : "fx:id=\"sCoefficiente\" was not injected: check your FXML file 'Gtfs.fxml'.";
        assert lCoefficiente != null : "fx:id=\"lCoefficiente\" was not injected: check your FXML file 'Gtfs.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Gtfs.fxml'.";
        assert pieChart != null : "fx:id=\"pieChart\" was not injected: check your FXML file 'Gtfs.fxml'.";
        assert barChart != null : "fx:id=\"barChart\" was not injected: check your FXML file 'Gtfs.fxml'.";
        assert btnP != null : "fx:id=\"btnP\" was not injected: check your FXML file 'Gtfs.fxml'.";
        assert btnS != null : "fx:id=\"btnS\" was not injected: check your FXML file 'Gtfs.fxml'.";
        assert comboDurata != null : "fx:id=\"comboDurata\" was not injected: check your FXML file 'Gtfs.fxml'.";
        assert barChart2 != null : "fx:id=\"barChart2\" was not injected: check your FXML file 'Gtfs.fxml'.";
        }

	public void setModel(Model m) {
		this.model = m ;

		try{
			btnS.setDisable(true);
			m.creaModel();
			
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
		  for(int i=1;i<=10;i++)
	        	comboDurata.getItems().add(Integer.valueOf(i)) ; 
		  
	        if(comboOra.getItems().size() > 0)
	        	comboOra.setValue(comboOra.getItems().get(10)); 
	        if(comboMinuti.getItems().size() > 0)
	        	comboMinuti.setValue(comboMinuti.getItems().get(0)); 
	        if(comboDurata.getItems().size() > 0)
	        	comboDurata.setValue(comboDurata.getItems().get(2)); 
	        
			lvPartenza.getItems().addAll(f);
			lvArrivo.getItems().addAll(f);
			lvLinea.getItems().addAll(l);
			lvAgenzia.getItems().addAll(a);
		} catch (RuntimeException e) {
			btnP.setDisable(true);
			
	    	Alert alert = new Alert(AlertType.WARNING);
	    	alert.setTitle("Errore");
	    	alert.setHeaderText("Nessuna connessione con il DB");
	    	alert.setContentText(e.getMessage());
	    	alert.showAndWait();
	    	
		}		
		
	}
}
