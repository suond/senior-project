package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Vector;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import javafx.util.Pair;

public class GUI_Controller implements Initializable {
	
	private Image image;
	private Vector<TableColumn<String[],String>> colVec = new Vector<TableColumn<String[],String>>();
	
    @FXML
    private Button checkCoutermeasureButton;

    @FXML
    private Button viewButton;
    
    @FXML
    private TableView<String[]> dataTable;

    @FXML
    private Button analysisButton;

    @FXML
    private Button importButton;

    @FXML
    private HBox dialogPane;
    
   // @FXML
	//private UndividedRoadController urc;

    @FXML
    void importData(ActionEvent event) {
    	
    	//Create confirmation box
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setGraphic(new ImageView(this.getClass().getResource("file.png").toString()));
    	alert.setTitle("Choose Input Method");
    	alert.setHeaderText("Would you like to input data locally or"
    			+ " from a database?");
    	alert.setContentText("Choose an option");
    	ButtonType local = new ButtonType("Local");
    	ButtonType db = new ButtonType("Database");
    	ButtonType cancel = new ButtonType("cancel",ButtonData.CANCEL_CLOSE);
    	alert.getButtonTypes().setAll(local,db,cancel);
    	Optional<ButtonType> choice = alert.showAndWait();
    	
    	//handles local file input
    	if(choice.get() == local){
    		FileChooser fc = new FileChooser();
        	File importFile = fc.showOpenDialog(null);
        	if(importFile == null)
        		return;
        	
        	BufferedReader br = null;
        	String line = "";
        	String cvsSplitBy = ",";
        	
        	try {
    			br = new BufferedReader(new FileReader(importFile.toString()));
    			//use fist line header info to build columns
    			line = br.readLine();
    			String[] tmp = line.split(cvsSplitBy);
    			for(int i=0; i<tmp.length; i++){
    				TableColumn<String[],String> tmpCol = new TableColumn<String[], String>(tmp[i]);
    				colVec.add(tmpCol);//add column to storage vector
    				dataTable.getColumns().add(tmpCol);// add column to table
    				Callback<CellDataFeatures<String[], String>, ObservableValue<String>> genericCellFactory =
    						new Callback<CellDataFeatures<String[], String>, ObservableValue<String>>() {
    						@Override
    						public ObservableValue<String> call(CellDataFeatures<String[], String> cellData) {
    						        String[] s = cellData.getValue();
    						        final int t = dataTable.getColumns().indexOf(tmpCol);
    						        SimpleStringProperty sp = new SimpleStringProperty(s[t]);
    						        return sp;
    						}};
    				
    				tmpCol.setCellValueFactory(genericCellFactory);
    			}
    			while((line = br.readLine()) != null){
    				String[] fields = line.split(cvsSplitBy);
    				dataTable.getItems().add(fields);
    			}
    			
    		} catch (FileNotFoundException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}
    	//handle database import
    	else if(choice.get() == db){
    		//TODO database stuff
    	}
    	else{
    		//do nothing since action canceled
    	}
    	
    	
    	
    }

    @FXML
    void viewData(ActionEvent event) {
    	dialogPane.getChildren().clear();
    	dialogPane.getChildren().add(dataTable);
    }

    @FXML
    void startAnalysis(ActionEvent event) {
    	
    	
    	List<String> ops = new ArrayList<>();
    	ops.add("Two Lane Rural Highway");
    	ops.add("Multi-Lane Rural Highway");
    	ops.add("Freeways and Arterial Roads");
    	
    	ChoiceDialog<String> dialog = new ChoiceDialog<>("Two Lane Rural Highway",ops);
    	dialog.setGraphic(new ImageView(this.getClass().getResource("road.png").toString()));
    	dialog.setTitle("Facility Type");
    	dialog.setHeaderText("Facility Type");
    	dialog.setContentText("Please Select a Facility Type");
    	Optional<String> result = dialog.showAndWait();
    	if(!result.isPresent())
    		return;
    	if(result.get() == "Two Lane Rural Highway"){
    		List<String> ops2 = new ArrayList<>();
        	ops2.add("Undivided road segment");
        	ops2.add("Unsignalized three-leg stop");
        	ops2.add("Unsignalized four-leg stop");
        	ops2.add("Signalized four-leg stop");
        	
        	ChoiceDialog<String> dialog2 = new ChoiceDialog<>("Undivided road segment",ops2);
        	dialog2.setGraphic(new ImageView(this.getClass().getResource("road.png").toString()));
        	dialog2.setTitle("Site Type");
        	dialog2.setHeaderText("Site Type");
        	dialog2.setContentText("Please Select a Site Type");
        	Optional<String> result2 = dialog2.showAndWait();
        	if(!result.isPresent())
        		return;
        	
        	//get additional information for undivided road segments
        	if(result2.isPresent() && result2.get() == "Undivided road segment"){
        		
        		//used for storing input values
        		String[] val1 = new String[16];
        		boolean[] val2 = new boolean[8];
        		val2[7] = true; //flag to stop future dialogs if cancel pressed
        		
        		//get input strings
        		Dialog<Pair<String[], boolean[]>> d = new Dialog<>();
        		d.setTitle("Additional Information");
        		d.setHeaderText("Please provide some additional information");
        		ButtonType okType = new ButtonType("Next", ButtonData.OK_DONE);
        		d.getDialogPane().getButtonTypes().addAll(okType, ButtonType.CANCEL);
        		GridPane grid = new GridPane();
        		grid.setHgap(10);
        		grid.setVgap(10);
        		grid.setPadding(new Insets(20, 150, 10, 10));
        		TextField jurisdiction = new TextField();
        		jurisdiction.setPromptText("Jurisdiction");
        		TextField r1 = new TextField();
        		r1.setPromptText("Main Road");
        		TextField length = new TextField();
        		length.setPromptText("Miles");
        		TextField sDate = new TextField();
        		sDate.setPromptText("Start Year");
        		TextField eDate = new TextField();
        		eDate.setPromptText("End Year");
        		TextField aadt = new TextField();
        		aadt.setPromptText("Annual Average Daily Traffic");
        		TextField rWidth = new TextField();
        		rWidth.setPromptText("Feet");
        		TextField sWidth = new TextField();
        		sWidth.setPromptText("Feet");
        		TextField sType = new TextField();
        		sType.setPromptText("Paved,Gravel,...");
        		TextField grade = new TextField();
        		TextField dDensity = new TextField();
        		dDensity.setPromptText("Driveway Density");
        		TextField roadsideHazard = new TextField();
        		grade.setPromptText("Precent. ex: 0.01");
        		roadsideHazard.setPromptText("Roadside Hazard Rating");
        		grid.add(jurisdiction,1,0);
        		grid.add(r1, 1, 1);
        		grid.add(length,1,2);
        		grid.add(sDate,1,3);
        		grid.add(eDate,1,4);
        		grid.add(aadt, 1, 5);
        		grid.add(rWidth,1,6);
        		grid.add(sWidth,1,7);
        		grid.add(sType,1,8);
        		grid.add(grade,1,9);
        		grid.add(dDensity,1,10);
        		grid.add(roadsideHazard,1,11);
        		grid.add(new Label("Jurisdiction"),0,0);
        		grid.add(new Label("Main Road"), 0, 1);
        		grid.add(new Label("Segment Length"),0,2);
        		grid.add(new Label("Start Year"), 0, 3);
        		grid.add(new Label("End Year"), 0, 4);
        		grid.add(new Label("AADT"), 0, 5);
        		grid.add(new Label("Road Width"),0,6);
        		grid.add(new Label("Shoulder Width"),0,7);
        		grid.add(new Label("Shoulder Type"),0,8);
        		grid.add(new Label("Grade"),0,9);
        		grid.add(new Label("Driveway Density"),0,10);
        		grid.add(new Label("Roadside Hazard Rating"),0,11);
        		d.getDialogPane().setContent(grid);
        		d.setResultConverter(dialogButton -> {
        			if(dialogButton == okType){
        				val1[0] = jurisdiction.getCharacters().toString();
        				val1[1] = r1.getCharacters().toString();
        				val1[2] = length.getCharacters().toString();
        				val1[3] = sDate.getCharacters().toString();
        				val1[4] = eDate.getCharacters().toString();
        				val1[5] = aadt.getCharacters().toString();
        				val1[6] = rWidth.getCharacters().toString();
        				val1[7] = sWidth.getCharacters().toString();
        				val1[8] = sType.getCharacters().toString();
        				val1[9] = grade.getCharacters().toString();
        				val1[10] = dDensity.getCharacters().toString();
        				val1[11] = roadsideHazard.getCharacters().toString();
        				val2[7] = false;//set flag
        			}
        			return null;
        		});
        		
        		//get boolean input
        		Dialog<Pair<String[], boolean[]>> d2 = new Dialog<>();
        		d2.setTitle("Additional Information");
        		d2.setHeaderText("Please provide some additional information");
        		d2.getDialogPane().getButtonTypes().addAll(okType, ButtonType.CANCEL);
        		GridPane grid2 = new GridPane();
        		grid2.setHgap(10);
        		grid2.setVgap(10);
        		grid2.setPadding(new Insets(20, 150, 10, 10));
        		HBox hCurveBox = new HBox();
        		RadioButton hCurveYes = new RadioButton("Yes");
        		RadioButton hCurveNo = new RadioButton("No");
        		ToggleGroup hCurveGroup = new ToggleGroup();
        		hCurveYes.setToggleGroup(hCurveGroup);
        		hCurveNo.setToggleGroup(hCurveGroup);
        		hCurveNo.setSelected(true);
        		hCurveBox.getChildren().addAll(hCurveYes,hCurveNo);
        		HBox rumbleBox = new HBox();
        		RadioButton rumbleYes = new RadioButton("Yes");
        		RadioButton rumbleNo = new RadioButton("No");
        		ToggleGroup rumbleGroup = new ToggleGroup();
        		rumbleYes.setToggleGroup(rumbleGroup);
        		rumbleNo.setToggleGroup(rumbleGroup);
        		rumbleNo.setSelected(true);
        		rumbleBox.getChildren().addAll(rumbleYes,rumbleNo);
        		HBox passBox = new HBox();
        		RadioButton passYes = new RadioButton("Yes");
        		RadioButton passNo = new RadioButton("No");
        		ToggleGroup passGroup = new ToggleGroup();
        		passYes.setToggleGroup(passGroup);
        		passNo.setToggleGroup(passGroup);
        		passNo.setSelected(true);
        		passBox.getChildren().addAll(passYes,passNo);
        		HBox fourBox = new HBox();
        		RadioButton fourYes = new RadioButton("Yes");
        		RadioButton fourNo = new RadioButton("No");
        		ToggleGroup fourGroup = new ToggleGroup();
        		fourYes.setToggleGroup(fourGroup);
        		fourNo.setToggleGroup(fourGroup);
        		fourNo.setSelected(true);
        		fourBox.getChildren().addAll(fourYes,fourNo);
        		HBox twoBox = new HBox();
        		RadioButton twoYes = new RadioButton("Yes");
        		RadioButton twoNo = new RadioButton("No");
        		ToggleGroup twoGroup = new ToggleGroup();
        		twoYes.setToggleGroup(twoGroup);
        		twoNo.setToggleGroup(twoGroup);
        		twoNo.setSelected(true);
        		twoBox.getChildren().addAll(twoYes,twoNo);
        		HBox lightBox = new HBox();
        		RadioButton lightYes = new RadioButton("Yes");
        		RadioButton lightNo = new RadioButton("No");
        		ToggleGroup lightGroup = new ToggleGroup();
        		lightYes.setToggleGroup(lightGroup);
        		lightNo.setToggleGroup(lightGroup);
        		lightNo.setSelected(true);
        		lightBox.getChildren().addAll(lightYes,lightNo);
        		HBox speedBox = new HBox();
        		RadioButton speedYes = new RadioButton("Yes");
        		RadioButton speedNo = new RadioButton("No");
        		ToggleGroup speedGroup = new ToggleGroup();
        		speedYes.setToggleGroup(speedGroup);
        		speedNo.setToggleGroup(speedGroup);
        	    speedNo.setSelected(true);
        		speedBox.getChildren().addAll(speedYes,speedNo);
        		grid2.add(hCurveBox,1,0);
        		grid2.add(rumbleBox,1,1);
        		grid2.add(passBox,1,2);
        		grid2.add(fourBox,1,3);
        		grid2.add(twoBox,1,4);
        		grid2.add(lightBox,1,5);
        		grid2.add(speedBox,1,6);
        		grid2.add(new Label("Horizontal Curve"),0,0);
        		grid2.add(new Label("Center Rumble Strip"),0,1);
        		grid2.add(new Label("Passing Lane"),0,2);
        		grid2.add(new Label("Short 4-Lane Section"),0,3);
        		grid2.add(new Label("2-Way Left-Turn Lane"),0,4);
        		grid2.add(new Label("Roadway Segment Lighting"),0,5);
        		grid2.add(new Label("Automated Speed Enforcement"),0,6);
        		d2.getDialogPane().setContent(grid2);
        		d2.setResultConverter(dialogButton -> {
        			if(dialogButton == okType){
        				RadioButton tmp = (RadioButton) hCurveGroup.getSelectedToggle();
        				if(tmp.getText().equals("No"))
        					val2[0] = false;
        				else
        					val2[0] = true;
        				tmp = (RadioButton) rumbleGroup.getSelectedToggle();
        				if(tmp.getText().equals("No"))
        					val2[1] = false;
        				else
        					val2[1] = true;
        				tmp = (RadioButton) passGroup.getSelectedToggle();
        				if(tmp.getText().equals("No"))
        					val2[2] = false;
        				else
        					val2[2] = true;
        				tmp = (RadioButton) fourGroup.getSelectedToggle();
        				if(tmp.getText().equals("No"))
        					val2[3] = false;
        				else
        					val2[3] = true;
        				tmp = (RadioButton) twoGroup.getSelectedToggle();
        				if(tmp.getText().equals("No"))
        					val2[4] = false;
        				else
        					val2[4] = true;
        				tmp = (RadioButton) lightGroup.getSelectedToggle();
        				if(tmp.getText().equals("No"))
        					val2[5] = false;
        				else
        					val2[5] = true;
        				tmp = (RadioButton) speedGroup.getSelectedToggle();
        				if(tmp.getText().equals("No"))
        					val2[6] = false;
        				else
        					val2[6] = true;
        				val2[7] = false;//set flag
        				
        				
        			}
					return null;
        		});
        		
        		//get additional info strings about horizontal curves
        		//get input strings
        		Dialog<Pair<String[], boolean[]>> d3 = new Dialog<>();
        		d3.setTitle("Additional Horizontal Curve Information");
        		d3.setHeaderText("Please provide some additional information");
        		d3.getDialogPane().getButtonTypes().addAll(okType, ButtonType.CANCEL);
        		GridPane grid3 = new GridPane();
        		grid3.setHgap(10);
        		grid3.setVgap(10);
        		grid3.setPadding(new Insets(20, 150, 10, 10));
        		TextField curveLength = new TextField();
        		curveLength.setPromptText("Feet");
        		TextField radius = new TextField();
        		radius.setPromptText("Feet");
        		TextField superE = new TextField();
        		superE.setPromptText("Precent ex. 0.01");
        		TextField maxSuperE = new TextField();
        		maxSuperE.setPromptText("Precent ex. 0.01");
        		grid3.add(curveLength,1,0);
        		grid3.add(radius,1,1);
        		grid3.add(superE,1,2);
        		grid3.add(maxSuperE,1,3);
        		grid3.add(new Label("Length of Horizontal Curve"),0,0);
        		grid3.add(new Label("Radius of Horizontal Curve"),0,1);
        		grid3.add(new Label("Super Elevation of Horizontal Curve"),0,2);
        		grid3.add(new Label("Max Super Elevation of Jurisdiction"),0,3);
        		d3.getDialogPane().setContent(grid3);
        		d3.setResultConverter(dialogButton -> {
        			if(dialogButton == okType){
        				val1[12] = curveLength.getCharacters().toString();
        				val1[13] = radius.getCharacters().toString();
        				val1[14] = superE.getCharacters().toString();
        				val1[15] = maxSuperE.getCharacters().toString();
        				val2[7] = false;
        			}
					return null;
        		});
        		
        		//display windows
        		d.showAndWait();
        		if(val2[7])
        			return;
        		val2[7] = true; //reset flag
        		d2.showAndWait();
        		if(val2[7])
        			return;
        		val2[7] = true; //reset flag
        		//get additional info if horizontal curve present
        		if(val2[0]){
        			d3.showAndWait();
        			if(val2[7])
            			return;
        		}
        		//Sanitize data
        		if(val1[0] == null){
        			val1[0] = "road";
        		}
        		if(val1[1] == null){
        			val1[1] = "jurisdiction";
        		}
        		if(val1[2] == null || !isNumeric(val1[2])){
        			val1[2] = "1";
        		}
        		if(val1[5] == null || !isNumeric(val1[5])){
        			val1[5] = "100";
        		}
        		if(val1[6] == null || !isNumeric(val1[6])){
        			val1[6] = "12";
        		}
        		if(val1[7] == null || !isNumeric(val1[7])){
        			val1[7] = "6";
        		}
        		if(val1[8] == null || !isNumeric(val1[8])){
        			val1[8] = "paved";
        		}
        		if(val1[9] == null || !isNumeric(val1[9])){
        			val1[9] = "0.0";
        		}
        		if(val1[10] == null || !isNumeric(val1[10])){
        			val1[10] = "5";
        		}
        		if(val1[11] == null || !isNumeric(val1[11])){
        			val1[11] = "3";
        		}
        		if(val1[12] == null || !isNumeric(val1[12])){
        			val1[12] = "1";
        		}
        		if(val1[13] == null || !isNumeric(val1[13])){
        			val1[13] = "1";
        		}
        		if(val1[14] == null || !isNumeric(val1[14])){
        			val1[14] = "0.0";
        		}
        		if(val1[15] == null || !isNumeric(val1[15])){
        			val1[15] = "0.0";
        		}
        
        		//generate recommendations
        		Pair<Double[],String[]> recs = UndividedRoadSegment.conductAnalysis(val1,val2);
        		//load countermeasure display screen
            	UndividedRoadController urc = new UndividedRoadController();
    			FXMLLoader loader = new FXMLLoader(getClass().getResource("UndividedRoadSegment.fxml"));
    			try {
    				loader.setController(urc);
    				Parent temp = (Parent) loader.load();
    				dialogPane.getChildren().clear();
    				dialogPane.getChildren().add(temp);
    				urc.setJurisdiction(val1[0]);
    				urc.setRoad(val1[1]);
    				urc.setFacility("2-Lane Rural Highway");
    				urc.setSite("Undivided Road Segement");
    				urc.fillTable(recs);
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
        			
        	}
        	
    	}
    	else if(result.get() == "Multi-Lane Rural Highway"){
    		List<String> ops2 = new ArrayList<>();
        	ops2.add("Four lane undivided road segment");
        	ops2.add("Four lane divided road segment");
        	ops2.add("Unsignalized three-leg stop");
        	ops2.add("Unsignalized four-leg stop");
        	ops2.add("Signalized four-leg stop");
        	
        	ChoiceDialog<String> dialog2 = new ChoiceDialog<>("Four lane undivided road segment",ops2);
        	dialog2.setGraphic(new ImageView(this.getClass().getResource("road.png").toString()));
        	dialog2.setTitle("Site Type");
        	dialog2.setHeaderText("Site Type");
        	dialog2.setContentText("Please Select a Site Type");
        	Optional<String> result2 = dialog2.showAndWait();
        	if(!result2.isPresent())
        		return;
    	}
    	else if(result.get() == "Freeways and Arterial Roads"){
    		List<String> ops2 = new ArrayList<>();
        	ops2.add("Two lane undivided arterial");
        	ops2.add("Three lane arterials a center two way left turn lane");
        	ops2.add("Four lane undivided arterials");
        	ops2.add("Four lane divided arterials");
        	ops2.add("Five lane arterials including a center");
        	ops2.add("Unsignaled three-leg intersection");
        	ops2.add("Signalized three-leg intersection");
        	ops2.add("Unsignalized four-leg intersection");
        	ops2.add("Signalized four-leg intersection");
        	
        	ChoiceDialog<String> dialog2 = new ChoiceDialog<>("Two lane undivided arterial",ops2);
        	dialog2.setGraphic(new ImageView(this.getClass().getResource("road.png").toString()));
        	dialog2.setTitle("Site Type");
        	dialog2.setHeaderText("Site Type");
        	dialog2.setContentText("Please Select a Site Type");
        	Optional<String> result2 = dialog2.showAndWait();
        	if(!result2.isPresent())
        		return;
    	}

    }

    @FXML
    void startCheck(ActionEvent event) {

    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		image = new Image("screen.jpg");
		ImageView startScreen = new ImageView(image);
		dialogPane.getChildren().clear();
		dialogPane.getChildren().add(startScreen);
		
	}
	
	private static boolean isNumeric(String str)
	{
	  return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
	}




}
