/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.nyc;

import java.net.URL;
import java.util.ResourceBundle;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.nyc.model.Localita;
import it.polito.tdp.nyc.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="cmbProvider"
    private ComboBox<String> cmbProvider; // Value injected by FXMLLoader

    @FXML // fx:id="txtDistanza"
    private TextField txtDistanza; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML // fx:id="txtStringa"
    private TextField txtStringa; // Value injected by FXMLLoader
    
    @FXML // fx:id="txtTarget"
    private ComboBox<Localita> txtTarget; // Value injected by FXMLLoader

    @FXML
    void doAnalisiGrafo(ActionEvent event) {
    	
    	txtResult.appendText("VERTICI CON PIU' VICINI:\n");
    	for (Localita l : model.getVicini())
    		txtResult.appendText(l.toString() + ", # vicini = " + model.getMaxVicini() + '\n');
    	
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	
    	if (this.txtStringa.getText() == "") {
    		txtResult.setText("Inserisci una stringa (s)");
    		return;
    	}
    	
    	if (this.txtTarget.getValue() == null) {
    		txtResult.setText("Scegli un target (t)");
    		return;
    	}
    	
    	if (this.txtTarget.getValue().getLocation().contains(txtStringa.getText())) {
    		txtResult.setText("Scegli una località che non contenga la stringa inserita");
    		return;
    	}
    		
    	txtResult.setText("PERCORSO CALCOLATO:\n");
    	
    	for (Localita l : model.getCammino(this.txtTarget.getValue(), this.txtStringa.getText()))
    		txtResult.appendText(l.toString() + '\n');
    	
    	
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	
    	if (this.cmbProvider.getValue() == null) {
    		txtResult.setText("Scegli un provider");
    		return;
    	}
    	if (this.txtDistanza.getText() == "") {
    		txtResult.setText("Inserisci una distanza");
    		return;
    	}
    	try {
    		Double.parseDouble(txtDistanza.getText());
    	}
    	catch (Exception e) {
    		txtResult.setText("Inserisci un valore numerico per la distanza (x)");
    		return;
    	}
    	
    	SimpleWeightedGraph<Localita, DefaultWeightedEdge> graph = model.creaGrafo(this.cmbProvider.getValue(),Double.parseDouble(txtDistanza.getText()));
    	txtResult.setText("Grafo creato con " + graph.vertexSet().size() + " vertici e " + graph.edgeSet().size() + " archi.\n\n");
    	
    	this.txtTarget.getItems().clear();
    	this.txtTarget.getItems().addAll(graph.vertexSet());
    	
    	this.btnAnalisi.setDisable(false);
    	this.btnPercorso.setDisable(false);
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbProvider != null : "fx:id=\"cmbProvider\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtDistanza != null : "fx:id=\"txtDistanza\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtStringa != null : "fx:id=\"txtStringa\" was not injected: check your FXML file 'Scene.fxml'.";

    }

    public void setModel(Model model) {
    	this.model = model;
    	
    	cmbProvider.getItems().addAll(model.getAllProvider());
    	
    	this.btnAnalisi.setDisable(true);
    	this.btnPercorso.setDisable(true);
    }
}
