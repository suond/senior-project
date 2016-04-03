package application;

import java.util.Vector;

import javafx.util.Pair;

public class FourLegStop extends TwoLaneRural{
	
	public static Pair<Double[],String[]> conductAnalysis(String[] str, boolean[] bools){
		
		//builds estimate of annual crashes on road segment
				double spf = buildSPF(str,bools);
				
				Vector<Double> res1 = new Vector<Double>(); //stores crash reduction values
				Vector<String> res2 = new Vector<String>();
				
				//check str 6,7,8 and the bool 1
				double temp = 0;
				//skew reduction
				temp = spf * skewCMF(Double.parseDouble(str[6]));
				if (Double.parseDouble(str[6]) != 0) {
					res1.add(temp-spf);
					res2.add("Reduce skew of intersection to 0");
				}
				//left approach cmf
				temp = spf * numLeftCMF(Integer.parseInt(str[7]));
				if (temp<spf) {
					res1.add(spf-temp);
					res2.add("With " + Integer.parseInt(str[7]) + " number of left approaches");
				}
				temp = spf * numRightCMF(Integer.parseInt(str[8]));
				if (temp<spf) {
					res1.add(spf-temp);
					res2.add("With " + Integer.parseInt(str[8]) + " number of right approaches");
				}
				temp = spf * lightCMF(bools[1]);
					if (temp<spf) {
						res1.add(spf-temp);
						res2.add("Implementing Lighting");
					}
				Double[] a1 = new Double[res1.size()];
				String[] a2 = new String[res2.size()];
				 return new Pair<Double[],String[]>(res1.toArray(a1),res2.toArray(a2));
	}
	//gets the base spf, used in the final predicted spf by multiplying it by the various cmfs
	private static double buildSPF(String[] str, boolean[] bools) {
	//Nspf = exp [-8.56 + .60 x In(AADT major) + .61 x In(AADTmin)
		// use str4 for major, str5 for minor, use Math.log() for ln, use Math.exp(shit up there)
		double major = Math.log(Double.parseDouble(str[4]));
		double minor = Math.log(Double.parseDouble(str[5]));
		major = major*.6;
		minor = minor*.61;
		double result = major + minor -8.56;
		
		return Math.exp(result);
	}
	//makes 4 function for each cmf
	static double skewCMF (double skew) {
		
		return Math.exp(.0054 * skew);
	}
	static double numLeftCMF (int number) {
		if (number == 1) {
			return .72;
		}else if (number ==2 ) {
			return .52;
		}else return 1;
	}
	static double numRightCMF (int number) {
		if (number == 1) {
			return .86;
		}else if (number ==2 ) {
			return .74;
		}else return 1;
	}
	static double lightCMF (boolean lol) {
		if(lol) {
			return 1- 0.38*.244;
		}else return 1;
	}
	static double finalCrash (String[] str, boolean[] bools) {
		double result = 0;
		double spf = buildSPF(str, bools);
		result = spf * skewCMF(Double.parseDouble(str[6])) * numLeftCMF(Integer.parseInt(str[7])) * numRightCMF(Integer.parseInt(str[8])) * lightCMF(bools[1]);
		return result;
	}
}

