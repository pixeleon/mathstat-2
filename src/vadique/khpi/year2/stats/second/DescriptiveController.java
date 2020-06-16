package vadique.khpi.year2.stats.second;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;
import vadique.khpi.year2.stats.second.VarsPair;
//import javafx.scene.chart.BarChart;

public class DescriptiveController implements Initializable {
	
	private ObservableList<VarsPair> obsList;
	private  Map<Double, Integer> freqX;
	//private Double xVar, yVar, covar, r, b0, b1;
	
	@FXML LineChart<Number,Number> lineChartXPolygon;
	@FXML TableView<VarsPair> tableViewVars;
	@FXML LineChart <Number,Number>lineChartRegrLine;
	@FXML TextField textFieldCorrCoeff;
	@FXML TextField textFieldDetermCoeff;
	@FXML TextField textFieldRegrConst;
	@FXML TextField textFieldRegrCoeff;
	@FXML TableColumn<VarsPair,Double> tableColumnX;
	@FXML TableColumn<VarsPair,Double> tableColumnY;
	@FXML TextField textFieldXMin;
	@FXML TextField textFieldXMax;
	@FXML TextField textFieldXRange;
	@FXML TextField textFieldXMean;
	@FXML TextField textFieldXMedian;
	@FXML TextField textFieldXMode;
	@FXML TextField textFieldXVariance;
	@FXML TextField textFieldXDeviation;
	@FXML TextField textFieldXSkewness;
	@FXML TextField textFieldXKurtosis;
	@FXML Label labelPredY;

	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		tableViewVars.setPlaceholder(new Label(""));
		obsList = FXCollections.observableArrayList(
				new VarsPair(69, 53.9),
				new VarsPair(68, 48.9),
				new VarsPair(65, 46.8),
				new VarsPair(74, 52.2),
				new VarsPair(66, 48.7),
				new VarsPair(72, 49.2),
				new VarsPair(72, 53.2),
				new VarsPair(72, 53.7),
				new VarsPair(75, 57.6),
				new VarsPair(76, 58.3),
				new VarsPair(62, 45.7),
				new VarsPair(69, 49.6),
				new VarsPair(72, 49.4),
				new VarsPair(66, 49.5),
				new VarsPair(69, 51.7),
				new VarsPair(65, 43.6),
				new VarsPair(68, 51.5),
				new VarsPair(71, 46.6),
				new VarsPair(67, 43.8)
				);
		initTable();
		
	}
	
	private void initList() {
		List<VarsPair> list = new ArrayList<VarsPair>();
		obsList = FXCollections.observableList(list);
	}
	
	private void initTable() {
	    tableViewVars.setItems(obsList);
	    tableColumnX.setCellValueFactory(new PropertyValueFactory<>("x"));
	    tableColumnX.setCellFactory(
	            TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
	    tableColumnX.setOnEditCommit(t -> (t.getTableView().getItems().get(t.getTablePosition().getRow()))
	    		.setX(t.getNewValue()));
	    tableColumnY.setCellValueFactory(new PropertyValueFactory<>("y"));
	    tableColumnY.setCellFactory(
	            TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
	    tableColumnY.setOnEditCommit(t -> (t.getTableView().getItems().get(t.getTablePosition().getRow()))
	    		.setY(t.getNewValue()));
	}

	@FXML public void doAdd(ActionEvent event) {
		if (obsList == null) {
			initList();
		}
		obsList.add(new VarsPair(0,0));
		initTable();
	}

	@FXML public void doRemove(ActionEvent event) {
		if (obsList == null) {
            return;
        }
        if (obsList.size() > 0) {
            obsList.remove(obsList.size() - 1);
        }
        if (obsList.size() <= 0) {
            obsList = null;
        }
	}
	
	@FXML public void doClear(ActionEvent event) {
		textFieldCorrCoeff.setText("");
		textFieldDetermCoeff.setText("");
		textFieldRegrConst.setText("");
		textFieldRegrCoeff.setText("");
		tableViewVars.setItems(null);
		tableViewVars.setPlaceholder(new Label(""));
		lineChartRegrLine.getData().clear();
		lineChartXPolygon.getData().clear();
		textFieldXMin.setText("");
		textFieldXMax.setText("");
		textFieldXRange.setText("");
		textFieldXMean.setText("");
		textFieldXMedian.setText("");
		textFieldXMode.setText("");
		textFieldXVariance.setText("");
		textFieldXDeviation.setText("");
		textFieldXSkewness.setText("");
		textFieldXKurtosis.setText("");
		initList();
		initTable();
	}

	@FXML public void doAnalyze(ActionEvent event) {
		if (obsList.size() <= 1) {
			return;
		}
		//Collections.sort(obsList);
		freqX = new HashMap<Double, Integer>();
		for (VarsPair vp : obsList) {
			Integer n = freqX.get(vp.getX());
            freqX.put(vp.getX(), (n == null) ? 1 : n + 1);
		}
		updateTextFields();
		updatePolygon();
		updateGaph();
	}
	
	public double getXMean() {
		double xTotal = 0;
		for(int i = 0; i < obsList.size(); i++) {
			xTotal += obsList.get(i).getX();
		}
		return xTotal / obsList.size();
	}
	
	public double getYMean() {
		double yTotal = 0;
		for(int i = 0; i < obsList.size(); i++) {
			yTotal += obsList.get(i).getY();
		}
		return yTotal / obsList.size();
	}
	
	public double getXVar() {
		double xSum = 0;
		for(int i = 0; i < obsList.size(); i++) {
			double xi = obsList.get(i).getX() - getXMean();
			xSum += xi * xi;
		}
		return xSum / (obsList.size()-1);
	}
	
	public double getYVar() {
		double ySum = 0;
		for(int i = 0; i < obsList.size(); i++) {
			double yi = obsList.get(i).getY() - getYMean();
			ySum += yi * yi;
		}
		return ySum / (obsList.size()-1);
	}
	
	public double getCovar() {
		double xySum = 0;
		for(int i = 0; i < obsList.size(); i++) {
			double xi = obsList.get(i).getX() - getXMean();
			double yi = obsList.get(i).getY() - getYMean();
			xySum += xi * yi;
		}
		return xySum / (obsList.size()-1);
	}
	
	public double getCorrCoeff() {
		return getCovar() / (Math.sqrt(getXVar()) * Math.sqrt(getYVar()));
	}
	
	public double getB1() {
		return getCorrCoeff() * Math.sqrt(getYVar()) / Math.sqrt(getXVar());
	}
	
	public double getB0() {
		return getYMean() - getB1() * getXMean();
	}
	
	public double getXMax() {
		double xMax = obsList.get(0).getX();
		for (int i = 1; i < obsList.size(); i++) {
			if (obsList.get(i).getX() > xMax) {
				xMax = obsList.get(i).getX();			
			}
		}
		return xMax;
	}

	public double getXMin() {
		double xMin = obsList.get(0).getX();
		for (int i = 1; i < obsList.size(); i++) {
			if (obsList.get(i).getX() < xMin) {
				xMin = obsList.get(i).getX();			
			}
		}
		return xMin;
	}
	
	public double getYMax() {
		double yMax = obsList.get(0).getY();
		for (int i = 1; i < obsList.size(); i++) {
			if (obsList.get(i).getY() > yMax) {
				yMax = obsList.get(i).getY();			
			}
		}
		return yMax;
	}
	
	public double getYMin() {
		double yMin = obsList.get(0).getX();
		for (int i = 1; i < obsList.size(); i++) {
			if (obsList.get(i).getX() < yMin) {
				yMin = obsList.get(i).getX();			
			}
		}
		return yMin;
	}
	
	public double getXRange() {
		return Math.abs(getXMax()-getXMin());
	}
	
	public double getYRange() {
		return Math.abs(getYMax()-getYMin());
	}
	
	public List<Double> getXMode() {
		double maxN = Collections.max(freqX.values());
		List<Double> xModes = new ArrayList<Double>();
		for(Map.Entry<Double, Integer> me : freqX.entrySet()) {
			if(me.getValue() == maxN)
			{
				xModes.add(me.getKey());
			}
		}
		return xModes;
		
	}
	
	public double getXMedian() {
		ArrayList<VarsPair> vpList = new ArrayList<>(obsList);
		Collections.sort(vpList);
		int midX = vpList.size() / 2;
		  if ((vpList.size() & 1) == 0) {
			  return (vpList.get(midX).getX()+vpList.get((midX+1)).getX())/2;
		  }
		  else {
			  return vpList.get(midX+1).getX();
		  }
	}
	
	public double getXDeviation() {
		return Math.sqrt(getXVar());
	}
	
	public double getXSkewness() {
		double xSum = 0;
		for(int i = 0; i < obsList.size(); i++) {
			double xi = obsList.get(i).getX() - getXMean();
			xSum += xi * xi * xi;
		}
		double m3 = xSum / (obsList.size()-1);
		return m3 / (Math.pow(getXDeviation(), 3));
	}
	
	public double getXKurtosis() {
		double xSum = 0;
		for(int i = 0; i < obsList.size(); i++) {
			double xi = obsList.get(i).getX() - getXMean();
			xSum += xi * xi * xi * xi;
		}
		double m4 = xSum / (obsList.size()-1);
		return m4 / (Math.pow(getXDeviation(), 4));
	}
	
	private void updateTextFields() {
		textFieldXMin.setText(getXMin()+"");
		textFieldXMax.setText(getXMax()+"");
		textFieldXRange.setText(getXRange()+"");
		textFieldXMean.setText(getXMean()+"");
		textFieldXMedian.setText(getXMedian()+"");
		textFieldXMode.setText(getXMode()+"");
		textFieldXVariance.setText(getXVar()+"");
		textFieldXDeviation.setText(getXDeviation()+"");
		textFieldXSkewness.setText(getXDeviation()+"");
		textFieldXKurtosis.setText(getXKurtosis()+"");
		textFieldCorrCoeff.setText(getCorrCoeff() + "");
		textFieldDetermCoeff.setText((getCorrCoeff()*getCorrCoeff()) + "");
		textFieldRegrConst.setText(getB0() + "");
		textFieldRegrCoeff.setText(getB1() + "");
		labelPredY.setText("predY = " + getB0() + " + (" + getB1() + "*X)");
	}
	
	private void updatePolygon() {
		lineChartXPolygon.getData().clear();
		XYChart.Series<Number, Number> freqSeries = new XYChart.Series<>();
		Map<Double, Integer> freqX = new HashMap<Double, Integer>();
		for (VarsPair vp : obsList) {
			Integer n = freqX.get(vp.getX());
            freqX.put(vp.getX(), (n == null) ? 1 : n + 1);
		}
		for(Map.Entry<Double, Integer> me : freqX.entrySet()) {
			freqSeries.getData().add(new XYChart.Data<>(me.getKey(), me.getValue()));
		}
		lineChartXPolygon.getData().add(freqSeries);
		NumberAxis axisX = (NumberAxis) lineChartXPolygon.getXAxis();
		double h = getXRange() / freqX.size();
        axisX.setAutoRanging(false);
        axisX.setLowerBound(getXMin()-h);
        axisX.setUpperBound(getXMax()+h);
        axisX.setTickUnit(h);
	}
	
	private void updateGaph() {
		lineChartRegrLine.getData().clear();

		XYChart.Series<Number, Number> regrSeries = new XYChart.Series<>();
		
		regrSeries.getData().add(new XYChart.Data<>(getXMin(), predY(getXMin())));
		regrSeries.getData().add(new XYChart.Data<>(getXMax(), predY(getXMax())));
		lineChartRegrLine.getData().add(regrSeries);
		XYChart.Series<Number, Number> points = new XYChart.Series<>();
		for (int i = 0; i < obsList.size(); i++) {
			points.getData().add(new XYChart.Data<Number, Number>
					(obsList.get(i).getX(), obsList.get(i).getY()));
		}
		lineChartRegrLine.getData().add(points);
		NumberAxis axisX = (NumberAxis) lineChartRegrLine.getXAxis();
		double hX = getXRange() / obsList.size();
        axisX.setAutoRanging(false);
        axisX.setLowerBound(getXMin()-hX);
        axisX.setUpperBound(getXMax()+hX);
        axisX.setTickUnit(hX);
        double hY = getYRange() / obsList.size();
        NumberAxis axisY = (NumberAxis) lineChartRegrLine.getYAxis();
        axisY.setAutoRanging(false);
        axisY.setLowerBound(getYMin()-hY);
        axisY.setUpperBound(getYMax()+hY);
        axisY.setTickUnit(hY);
        
	}
	
	private double predY(double x) {
		return getB0() + getB1()*x;
	}

}
