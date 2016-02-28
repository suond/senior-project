package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;
import javafx.util.Pair;

public class UndividedRoadController {

	 @FXML
	private Label jurisdictionText;

    @FXML
    private TableColumn<String[], String> numCol;

    @FXML
    private Label roadText;

    @FXML
    private TableColumn<String[], String> nameCol;

    @FXML
    private Label facilityText;

    @FXML
    private TableView<String[]> resultTable;

    @FXML
    private Label siteText;
    
    public void setRoad(String text){
    	roadText.setText(text);
    }
    
    public void setJurisdiction(String text){
    	jurisdictionText.setText(text);
    }
    
    public void setFacility(String text){
    	facilityText.setText(text);
    }
    
    public void setSite(String text){
    	siteText.setText(text);
    }
    
    public void fillTable(Pair<Double[],String[]>data){
    	
    	//create cell factory for numcol
    	Callback<CellDataFeatures<String[], String>, ObservableValue<String>> numCellFactory =
				new Callback<CellDataFeatures<String[], String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<String[], String> cellData) {
				        String[] s = cellData.getValue();
				        final int t = resultTable.getColumns().indexOf(numCol);
				        SimpleStringProperty sp = new SimpleStringProperty(s[t]);
				        return sp;
				}};
		//create cell factory for namecol
	    Callback<CellDataFeatures<String[], String>, ObservableValue<String>> nameCellFactory =
				new Callback<CellDataFeatures<String[], String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<String[], String> cellData) {
					   String[] s = cellData.getValue();
					   final int t = resultTable.getColumns().indexOf(nameCol);
					   SimpleStringProperty sp = new SimpleStringProperty(s[t]);
					   return sp;
				}};
		
		//fill columns 
		numCol.setCellValueFactory(numCellFactory);
		nameCol.setCellValueFactory(nameCellFactory);
		Double[] nums = data.getKey();
		String[] strings = data.getValue();
		for(int i=0; i<nums.length; i++){
			String[] temp = new String[2];
			temp[0] = strings[i];
			temp[1] = nums[i].toString();
			resultTable.getItems().add(temp);
		}
			
    	
    }

}
