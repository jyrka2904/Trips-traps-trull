import javafx.event.EventHandler;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import java.io.*;
import java.util.Arrays;
import java.util.List;

//võiks lisada ka skoorihoidja! mis teha tekstist lugemisega??
public class TripsTrapsTrull extends Application{
	private String kelleKord = "X"; //selle abi teame, kelle käik on
	private String kelleKordAjutine = " ";
	private Ruudustik[][] maatriks = new Ruudustik[3][3]; //
	private Label kord = new Label("X");
	private GridPane ruudustik = new GridPane(); //loome Gridi abil ruudustiku	
	private Stage ajutine = new Stage();
	private List<Integer> tulemused = Arrays.asList(0, 0, 0);
	
	@Override
	public void start(Stage peaLava) throws Exception {
		ajutine = peaLava;
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++) {
				//lisame ruudustiku ruudud (3x3) ning loome igale ruudule tema Ruudustiku
				ruudustik.add(maatriks[i][j] = new Ruudustik(), j, i);
			}
		}
		
		BorderPane bpane = new BorderPane();
		BorderPane bpane2 = new BorderPane();
		bpane.setAlignment(kord, Pos.TOP_CENTER);
		bpane.setCenter(ruudustik);
		bpane2.setCenter(kord);
		bpane.setTop(bpane2);
		bpane.setStyle("-fx-background-color: #FFFFFF;");
		bpane2.setStyle("-fx-background-color: #202020;");
		kord.setTextFill(Color.web("#FFFFFF"));
		   
		Scene stseen = new Scene(bpane, 450, 500);
		peaLava.setTitle("Trips-traps-trull");
		peaLava.setScene(stseen);
		peaLava.show();
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
		
		public void setmangija(String m) {
			this.mangija = m;
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
			if (mangija.isBlank() && !kelleKord.isBlank()) {// Kui ruut on tühi ning mäng pole läbi
				setMangija(kelleKord); 
				
				if (onVõitnud(kelleKord) || RuudustikOnTäis()) { //kui terve ruudustik on täis, on mäng lõppenud viigiga
					kelleKordAjutine = kelleKord;
					if(onVõitnud(kelleKord)) {
						kord.setText(kelleKord + " on võitnud! Mäng on läbi!");
						if (kelleKord.equals("X"))
							tulemused.set(0, tulemused.get(0) + 1);
						else
							tulemused.set(1, tulemused.get(1) + 1);
						kelleKord = " ";
					}
					else {
						kord.setText("Viik! Mäng on läbi!");
						tulemused.set(2, tulemused.get(2) + 1);
						kelleKord = " ";
					}
					Stage lava = new Stage();
					Button nupp1 = new Button("Uus mäng");
					Button nupp2 = new Button("Lõpeta");
					nupp1.setOnMouseClicked(new EventHandler<MouseEvent>() { 
						public void handle(MouseEvent me) {
							cleanUp();
							lava.close();
							ajutine.close();
							try {
								start(ajutine);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
					nupp1.setOnKeyPressed(new EventHandler<KeyEvent>() {
						public void handle(KeyEvent ke) {
							if(ke.getCode() == KeyCode.ENTER) {
								cleanUp();
								lava.close();
								ajutine.close();
								try {
									start(ajutine);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					});
					nupp2.setOnMouseClicked(new EventHandler<MouseEvent>() {
						public void handle(MouseEvent me) {
							String tekst ="X võitude arv: " + tulemused.get(0) + " | O võitude arv: " + tulemused.get(1) + " | Viikide arv: " + tulemused.get(2);
							try { //üritame tulemused faili kirjutada
								BufferedWriter bw = new BufferedWriter(new FileWriter("tulemused.txt"));
								bw.write(tekst);
								bw.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
							 //üritame tulemusi failist lugeda, et neid ekraanile kuvada
							try(BufferedWriter bw = new BufferedWriter(new FileWriter("tulemused.txt")); 
									BufferedReader br = new BufferedReader(new FileReader("tulemused.txt"))){
								bw.write(tekst);
								bw.close();
								String line = br.readLine();
								br.close();
								Stage lavaUus = new Stage();
								VBox kast = new VBox(10);
								kast.setStyle("-fx-background-color: #202020;");
								Label lopp = new Label();
								lopp.setTextFill(Color.web("#FFFFFF"));
								lopp.setText(line);
								kast.setAlignment(Pos.CENTER);
								kast.getChildren().addAll(lopp);
								Scene stseen = new Scene(kast, 350, 50);
								lavaUus.setScene(stseen);
								lavaUus.show();
							}
							 catch (IOException e) {
								e.printStackTrace();
							}
							//System.out.println(tekst);
							lava.close();
							ajutine.close();
						}
					});
					VBox vBox = new VBox(10);
					vBox.setStyle("-fx-background-color: #202020;");
					vBox.setAlignment(Pos.CENTER);
					vBox.getChildren().addAll(kord, nupp1, nupp2);
					Scene stseen = new Scene(vBox, 200, 130);
					lava.setScene(stseen);
					lava.show();
		        }
		        else { //muul juhul vahetame kelle kord on 
		        	if(kelleKord.equals("X"))
		        		kelleKord = "O";
		        	else if(kelleKord.equals("O"))
		        		kelleKord = "X";
		        	kord.setText(kelleKord);
		        }
			}
		}
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
	
	public void cleanUp() {

		ruudustik.getChildren().clear(); //tühjendame GridPane'i
		
    	if(kelleKordAjutine.equals("X")) //uuesti alustades alustame sellest, kes kaotas või kes viimase märgi pani
    		kelleKord = "O";
    	else if(kelleKordAjutine.equals("O"))
    		kelleKord = "X";
		kord.setText(kelleKord);
	}
	   
	public static void main(String[] args) {
		launch(args);
	}
}
