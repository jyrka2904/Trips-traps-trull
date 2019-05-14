import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.beans.binding.Bindings;

public class TripsTrapsTrull extends Application{
	private String kelleKord = "X"; //selle abi teame, kelle käik on
	private Ruudustik[][] maatriks = new Ruudustik[3][3]; //
	private Label label = new Label();
	
	@Override
	public void start(Stage peaLava) throws Exception {
		
		GridPane ruudustik = new GridPane(); //loome Gridi abil ruudustiku
		
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++) {
				//lisame ruudustiku ruudud (3x3) ning loome igale ruudule tema Ruudustiku
				ruudustik.add(maatriks[i][j] = new Ruudustik(), j, i);
			}
		}
		   
		BorderPane bpane = new BorderPane();
		bpane.setCenter(ruudustik);
		bpane.setBottom(label);
		   
		Scene stseen = new Scene(bpane, 450, 500);
		peaLava.setTitle("Trips-traps-trull");
		peaLava.setScene(stseen);
		peaLava.show();
	}
		
	public boolean RuudustikOnTäis() {
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++) {
				//kui mingi ruut ruudustikus on tühi (mangija on tühi), pole mäng veel läbi
				if(maatriks[i][j].getMangija().isBlank())
					return false;
				}
			}
		//kui ei leidu tühja ruutu, on mäng läbi
			return true; 
		}
		
	public boolean onVõitnud(String m) {
		for(int i = 0; i < 3; i++) {
			//kui mingi rida koosneb samadest märkidest(X või O), tagastatakse true
			if(maatriks[i][0].getMangija().equals(m) && maatriks[i][1].getMangija().equals(m) && maatriks[i][2].getMangija().equals(m)) {
				return true;
			}
		}				
		for(int j = 0; j < 3; j++) {
			//kui mingi veerg koosneb samadest märkidest(X või O), tagastatakse true
			if (maatriks[0][j].getMangija().equals(m)&& maatriks[1][j].getMangija().equals(m) && maatriks[2][j].getMangija().equals(m)) {
				return true;
				}	
		}
		//kui kumbki diagonaal koosneb samadest märkidest(X või O), tagastatakse true
		if((maatriks[0][0].getMangija().equals(m) && maatriks[1][1].getMangija().equals(m) && maatriks[2][2].getMangija().equals(m)) || 
				(maatriks[0][2].getMangija().equals(m) && maatriks[1][1].getMangija().equals(m) && maatriks[2][0].getMangija().equals(m))) {
			return true;
			}
		//kui eelnevad tingimused ei kehti, tagastatakse false
		return false;
		}
		
		
	public class Ruudustik extends Pane {
		 
		private String mangija = " "; //hetke mängija

		public Ruudustik() {		      
			setPrefSize(500, 500);
			setStyle("-fx-border-color: black"); //määrame ruudustiku värvi
			setOnMouseClicked(e -> handleMouseClick()); //kui vajutame antud ruutu
		}
		    
		public String getMangija() {
			return mangija;
		}

		public void setMangija(String m) {
			mangija = m; //määrame antud ruudu mängija(X või O)
			if (m.equals("X")) { //kujutame X-i ruudustiku antud ruudus
				Line line1 = new Line(20, 20, 20, 20);
				//seome joone kasti suurusega, et akna suurust muutes muutuks ka joon
				line1.endXProperty().bind(widthProperty().subtract(20)); 
				line1.endYProperty().bind(heightProperty().subtract(20));
				Line line2 = new Line(20, 20, 20, 20);
				//seome joone kasti suurusega, et akna suurust muutes muutuks ka joon
				line2.startYProperty().bind(heightProperty().subtract(20));
				line2.endXProperty().bind(widthProperty().subtract(20));
		      
				getChildren().addAll(line1, line2); 
		    }
			else if (m.equals("O")) { //kujutame O ruudustiku antud ruudus
				Circle ring = new Circle(50, 50, 55);
		        //seome ringi kasti suurusega, et akna suurust muutes muutuks ka ringi keskpunkt ja raadius
		        ring.centerXProperty().bind(widthProperty().divide(2));
		        ring.centerYProperty().bind(heightProperty().divide(2));
		        ring.radiusProperty().bind(Bindings.min(widthProperty().divide(2).subtract(10), heightProperty().divide(2).subtract(10)));
		        ring.setStroke(Color.BLACK);
		        ring.setFill(Color.TRANSPARENT);
		        
		        getChildren().add(ring); 
		    }
		}
		   
		private void handleMouseClick() {
			// Kui ruut on tühi ning mäng pole läbi
			if (mangija.isBlank() && !kelleKord.isBlank()) {
				setMangija(kelleKord); 

				if (onVõitnud(kelleKord)) { //kui onVõitnud tagastab true, on mäng läbi
					label.setText(kelleKord + " on võitnud! Mäng on läbi!");
					kelleKord = " ";
		        }
				else if (RuudustikOnTäis()) { //kui terve ruudustik on täis, on mäng lõppenud viigiga
					label.setText("Viik! Mäng on läbi!");
					kelleKord = " "; 
		        }
		        else { //muul juhul vahetame kelle kord on 
		        	if(kelleKord.equals("X"))
		        		kelleKord = "O";
		        	else if(kelleKord.equals("O"))
		        		kelleKord = "X";
		        	label.setText(kelleKord + "'i kord");
		        }
			}
		}
	}
	   
	public static void main(String[] args) {
		launch(args);
	}
}
