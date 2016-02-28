package application;

import javafx.util.Pair;

public abstract class SiteType {
	
	/*--------------------------------
	 * Methods in this class are meant to be overridden by concrete classes
	 *--------------------------------*/
	public static Pair<Double[],String[]> conductAnalysis(String[] strings,boolean[] bools){
		return null;
	}
	
	@SuppressWarnings({ "null", "unused" })
	private double buildSPF(){
		return (Double) null;
	}

}
