package application;

import java.util.Vector;

import javafx.util.Pair;

public class UndividedRoadSegment extends TwoLaneRural {

	public static Pair<Double[],String[]> conductAnalysis(String[] strings,boolean[] bools){
		//builds estimate of annual crashes on road segment
		double spf = buildSPF(strings,bools);
		
		Vector<Double> res1 = new Vector<Double>(); //stores crash reduction values
		Vector<String> res2 = new Vector<String>(); //stores names of recommendations
		
		//check for improvements using bool values
		if(!bools[1]){ //if no rumble strip present
			double temp = spf*rumbleStripCMF(true);
			if(temp<spf){
				res1.add(spf-temp);
				res2.add("Add Rumble Strip");
			}
		}
		if(!bools[2]){
			double temp = spf*passingLaneCMF(true,false);
			if(temp<spf){
				res1.add(spf-temp);
				res2.add("Add a Passing Lane");
			}
			if(!bools[3]){
				temp = spf*passingLaneCMF(true,true);
				if(temp<spf){
					res1.add(spf-temp);
					res2.add("Add a Short 4-Lane Section");
				}
			}
		}
		if(!bools[4]){
			double temp = spf*twoWayLeftTurnCMF(Double.parseDouble(strings[10]));
			if(temp<spf){
				res1.add(spf-temp);
				res2.add("Add a 2-Way Left Turn Lane");
			}
		}
		if(!bools[5]){
			double temp = spf*lightingCMF(true);
			if(temp<spf){
				res1.add(spf-temp);
				res2.add("Add Aditional Lighting");
			}
		}
		if(!bools[5]){
			double temp = spf*autoSpeedEnforcement(true);
			if(temp<spf){
				res1.add(spf-temp);
				res2.add("Add Automated Spped Enforcement");
			}
		}
		//check for improvements using string values
		
		//check for road width improvements
		if(Double.parseDouble(strings[6]) < 12){//if road width less than maximum
			String[] strings2 = new String[strings.length];
			System.arraycopy(strings,0,strings2,0,strings.length);
			strings2[6] = "12";
			double spf2 = buildSPF(strings2,bools);
			if(spf2<spf){
				res1.add(spf-spf2);
				res2.add("Increase Road Width: 12 FT");
			}
			if(Double.parseDouble(strings[6]) < 11){
				strings2[6] = "11";
				spf2 = buildSPF(strings2,bools);
				if(spf2<spf){
					res1.add(spf-spf2);
					res2.add("Increase Road Width: 11 FT");
				}
			}
			if(Double.parseDouble(strings[6]) < 10){
				strings2[6] = "10";
				spf2 = buildSPF(strings2,bools);
				if(spf2<spf){
					res1.add(spf-spf2);
					res2.add("Increase Road Width: 10 FT");
				}
			}
		}
		//check for shoulder width 
		if(Double.parseDouble(strings[7]) < 8){//if shoulder width less than maximum
			String[] strings2 = new String[strings.length];
			System.arraycopy(strings,0,strings2,0,strings.length);
			strings2[7] = "8";
			double spf2 = buildSPF(strings2,bools);
			if(spf2<spf){
				res1.add(spf-spf2);
				res2.add("Increase Shoulder Width: 8 FT");
			}
			if(Double.parseDouble(strings[7]) < 6){
				strings2[7] = "6";
				spf2 = buildSPF(strings2,bools);
				if(spf2<spf){
					res1.add(spf-spf2);
					res2.add("Increase Road Width: 6 FT");
				}
			}
			if(Double.parseDouble(strings[7]) < 4){
				strings2[7] = "4";
				spf2 = buildSPF(strings2,bools);
				if(spf2<spf){
					res1.add(spf-spf2);
					res2.add("Increase Road Width: 4 FT");
				}
			}
		}
		//check for shoulder type improvements
		if(!strings[8].toLowerCase().equals("paved")){//if not best shoulder type
			String[] strings2 = new String[strings.length];
			System.arraycopy(strings,0,strings2,0,strings.length);
			
			if(strings[8].toLowerCase().equals("gravel")){
				strings2[8] = "paved";
				double spf2 = buildSPF(strings2,bools);
				if(spf2<spf){
					res1.add(spf-spf2);
					res2.add("Change Shoulder Material: Paved");
				}
			}
			if(strings[8].toLowerCase().equals("composite")){
				strings2[8] = "paved";
				double spf2 = buildSPF(strings2,bools);
				if(spf2<spf){
					res1.add(spf-spf2);
					res2.add("Change Shoulder Material: Paved");
				}
				strings2[8] = "gravel";
				spf2 = buildSPF(strings2,bools);
				if(spf2<spf){
					res1.add(spf-spf2);
					res2.add("Change Shoulder Material: Gravel");
				}
			}
			if(strings[8].toLowerCase().equals("turf")){
				strings2[8] = "paved";
				double spf2 = buildSPF(strings2,bools);
				if(spf2<spf){
					res1.add(spf-spf2);
					res2.add("Change Shoulder Material: Paved");
				}
				strings2[8] = "gravel";
				spf2 = buildSPF(strings2,bools);
				if(spf2<spf){
					res1.add(spf-spf2);
					res2.add("Change Shoulder Material: Gravel");
				}
				strings2[8] = "composite";
				spf2 = buildSPF(strings2,bools);
				if(spf2<spf){
					res1.add(spf-spf2);
					res2.add("Change Shoulder Material: Composite");
				}
			}
		}
		//check for grade improvements
		if(Double.parseDouble(strings[9]) > 0.03){//0.03 or less, best case scenario 
			String[] strings2 = new String[strings.length];
			System.arraycopy(strings,0,strings2,0,strings.length);
			
			if(Double.parseDouble(strings[9]) > 0.03 && Double.parseDouble(strings[9]) <= 0.06){
				strings2[9] = "0.03";
				double spf2 = buildSPF(strings2,bools);
				if(spf2<spf){
					res1.add(spf-spf2);
					res2.add("Change Grade: grade<=3");
				}
			}
			if(Double.parseDouble(strings[9]) > 0.06 ){
				strings2[9] = "0.03";
				double spf2 = buildSPF(strings2,bools);
				if(spf2<spf){
					res1.add(spf-spf2);
					res2.add("Change Grade: grade<=3");
				}
				strings2[9] = "0.04";
				spf2 = buildSPF(strings2,bools);
				if(spf2<spf){
					res1.add(spf-spf2);
					res2.add("Change Grade: 3<grade<=6");
				}
			}
			
		}
		


		Double[] a1 = new Double[res1.size()];
		String[] a2 = new String[res2.size()];
		return new Pair<Double[],String[]>(res1.toArray(a1),res2.toArray(a2));
	}
	
	private static double buildSPF(String strings[], boolean[] bools){
		double spf =  Double.parseDouble(strings[5])*Double.parseDouble(strings[2])
				*365.0*Math.pow(10,-6)*Math.pow(Math.E,-0.312);
		//apply lane width cmf
		spf*=laneWidthCMF(Double.parseDouble(strings[5]),Double.parseDouble(strings[6]));
		//apply shoulder cmf
		spf*=shoulderWidthTypeCMF(Double.parseDouble(strings[5]),
				Double.parseDouble(strings[7]),strings[8]);
		//apply horizontal curve cmf if applicable
		if(bools[0]){
			spf*=horizontalCurveCMF(Double.parseDouble(strings[2]),
					Double.parseDouble(strings[13]),bools[0]);
			spf*=horizontalCurveSuperelevationCMF(Double.parseDouble(strings[14]));
		}
		//apply grade cmf
		spf*=gradeCMF(Double.parseDouble(strings[9]));
		//apply driveway density cmf
		spf*=drivewayDensityCMF(Double.parseDouble(strings[10]),Double.parseDouble(strings[5]));
		//apply rumble strip cmf
		spf*=rumbleStripCMF(bools[1]);
		//apply passing lane cmf
		spf*=passingLaneCMF(bools[2],bools[3]);
		//apply 2 way left turn lane cmf
		spf*=twoWayLeftTurnCMF(Double.parseDouble(strings[10]));
		//apply road design cmf
		spf*=roadDesignCMF(Double.parseDouble(strings[11]));
		//apply lighting cmf
		spf*=lightingCMF(bools[5]);
		//apply speed camera cmf
		spf*=autoSpeedEnforcement(bools[6]);
		return spf;
	}
	
	private static double laneWidthCMF(double aadt, double width){
		double cmfRA = 0;
		
		//calculate cmfRA value
		if(aadt<400){
			if(width <= 9){
				cmfRA=1.05;
			}
			else if(width == 10){
				cmfRA=1.02;
			}
			else if(width == 11){
				cmfRA=1.01;
			}
			else{
				cmfRA=1.00;
			}
		}
		else if(aadt>2000){
			if(width <= 9){
				cmfRA=1.50;
			}
			else if(width == 10){
				cmfRA=1.30;
			}
			else if(width == 11){
				cmfRA=1.05;
			}
			else{
				cmfRA=1.00;
			}
		}
		else{//aadt between 400 and 2000
			if(width <= 9){
				cmfRA=1.05+2.81*Math.pow(10,-4)*(aadt-400);
			}
			else if(width == 10){
				cmfRA=1.02+1.75*Math.pow(10,-4)*(aadt-400);
			}
			else if(width == 11){
				cmfRA=1.01+2.5*Math.pow(10,-5)*(aadt-400);
			}
			else{
				cmfRA=1.00;
			}
		}
		double pRA = 0.574; //TODO handle actual porprotion rather estimate
		
		return (cmfRA-1.0)*pRA+1.0;
	}
	
	private static double shoulderWidthTypeCMF(double aadt,double width,String type){
		double cmfWRA = 0;
		
		//calculate cmfRA value
		if(aadt<400){
			if(width == 0){
				cmfWRA=1.10;
			}
			else if(width == 2){
				cmfWRA=1.07;
			}
			else if(width == 4){
				cmfWRA=1.02;
			}
			else if(width == 6){
				cmfWRA=1.00;
			}
			else{
				cmfWRA=0.98;
			}
		}
		else if(aadt>2000){
			if(width == 0){
				cmfWRA=1.50;
			}
			else if(width == 2){
				cmfWRA=1.30;
			}
			else if(width == 4){
				cmfWRA=1.15;
			}
			else if(width == 6){
				cmfWRA=1.00;
			}
			else{
				cmfWRA=0.87;
			}
		}
		else{//aadt between 400 and 2000
			if(width == 0){
				cmfWRA=1.10+2.5*Math.pow(10,-4)*(aadt-400);
			}
			else if(width == 2){
				cmfWRA=1.07+1.43*Math.pow(10,-4)*(aadt-400);
			}
			else if(width == 4){
				cmfWRA=1.02+8.125*Math.pow(10,-5)*(aadt-400);
			}
			else if(width == 6){
				cmfWRA=1.00;
			}
			else{
				cmfWRA=0.98+6.875*Math.pow(10,-5)*(aadt-400);
			}
		}
		
		//calculate cmfTRA
		double cmfTRA =0;
		if(type.toLowerCase().equals("paved")){
			cmfTRA=1.0;
		}
		else if(type.toLowerCase().equals("gravel")){
			if(width>=1.0)
				cmfTRA = 1.0;
			else if(2 <= width && width <=4){
				cmfTRA = 1.01;
			}
			else
				cmfTRA = 1.02;
		}
		else if(type.toLowerCase().equals("composite")){
			if(width==0)
				cmfTRA=1.0;
			else if (width==1)
				cmfTRA=1.01;
			else if (width==2)
				cmfTRA=1.02;
			else if (width==3)
				cmfTRA=1.02;
			else if (width==4)
				cmfTRA=1.03;
			else if (width==6)
				cmfTRA=1.04;
			else if (width==8)
				cmfTRA=1.06;
		}
		else if(type.toLowerCase().equals("turf")){
			if(width==0)
				cmfTRA=1.0;
			else if (width==1)
				cmfTRA=1.01;
			else if (width==2)
				cmfTRA=1.03;
			else if (width==3)
				cmfTRA=1.04;
			else if (width==4)
				cmfTRA=1.05;
			else if (width==6)
				cmfTRA=1.08;
			else if (width==8)
				cmfTRA=1.11;
		}
		
		double pRA = 0.574; //TODO handle actual porprotion rather estimate
		
		return (cmfWRA*cmfTRA-1.0)*pRA+1.0;
	}
	
	private static double horizontalCurveCMF(double length, double radius,boolean spiral){
		
		double s = 0.0;
		if(spiral)
			s=1.0;
		return ((1.55*length)+(80.2/radius)-(0.012*s))/(1.55*length);
	}
	
	private static double horizontalCurveSuperelevationCMF(double se){
		if(se<0.01)
			return 1.00;
		else if(se <=0.01 && se<0.2)
			return 1.00+6*(se-0.01);
		else
			return 1.06+3*(se-0.02);
	}
	
	private static double gradeCMF(double grade){
		if(grade <=0.03)
			return 1.00;
		else if(grade>0.03 && grade<= 0.06)
			return 1.10;
		else
			return 1.16;
	}
	
	private static double drivewayDensityCMF(double dd, double aadt){
		return (0.322+dd*Math.abs(0.05-0.005*aadt))/(0.322+5+Math.abs(0.05-0.005*aadt));
	}
	
	private static double rumbleStripCMF(boolean present){
		if(present)
			return 0.94;
		else
			return 1.00;
	}
	
	private static double passingLaneCMF(boolean passingLane, boolean short4Lane){
		if(passingLane)
			return 0.75;
		else if(short4Lane)
			return 0.65;
		else return 1.00;
	}
	
	private static double twoWayLeftTurnCMF(double dd){
		if(dd <= 5.0)//only apply cmf if driveway density greater than 5
			return 1.0;
		
		double pLTD = 0.5; //TODO use real data if available
		
		double pDWY = ((0.0047*dd)+(0.0024*dd*dd))/(1.199+(0.0047*dd)+(0.0024*dd*dd));
		
		return 1.0-(0.7*pDWY*pLTD);
		
	}
	
	private static double roadDesignCMF(double hazardRating){
		return (Math.pow(Math.E,(-0.6869+0.0668*hazardRating)))/Math.pow(Math.E,-0.4865);
	}
	
	private static double lightingCMF(boolean lights){
		//TODO real values
		double pINR = 0.382;
		double pPNR = 0.618;
		double pNR = 0.370;
		if(lights)
			return 1.00;
		else
			return 1.0-Math.abs((1.0-0.72*pINR-0.83*pPNR)*pNR);
			
	}
	
	private static double autoSpeedEnforcement(boolean present){
		if(present)
			return 0.93;
		else
			return 1.00;
	}
}
