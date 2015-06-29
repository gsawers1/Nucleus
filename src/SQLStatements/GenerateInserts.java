
/**
*	ID INT,
*	Longitude DOUBLE,
*	Latitude DOUBLE,
*	TimeAndDate INT,
*	Person INT,
*/

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
public class GenerateInserts{
	
	public static void main(String args[]){

		final int numIntervals = 288;
		final int intervalsPerHour = 12;
		final long millisecondsPerDay = 86400000;
		final long millisecondsPerHour = 3600000;
		final long millisecondsPer5Minutes = 300000;
		String insertFormat = "INSERT INTO Locations \n VALUES (";
		int id = 1;
		int person = 1;

		File file = new File("LocationInserts.sql");
		BufferedWriter bw = null;
		FileWriter fw = null;
		try{
			file.createNewFile();
			fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);
		}
		catch(IOException e){
			System.out.println("Error opening file");
		}

		//Write for Greg
		double workLatitude = 37.422372;
		double workLongitude = -122.084434;
		double homeLatitude = 37.382843;
		double homeLongitude = -122.224788;
		double lunchLatitudeA = 37.421367;
		double lunchLongitudeA = -122.083425;
		double lunchLatitudeB = 37.416339;
		double lunchLongitudeB = -122.079520;
		double planetGraniteLat = 37.384430;
		double planetGraniteLong = -122.010663;
		long timeInfected = 1003552566;
		long currentTime = timeInfected - millisecondsPerDay;
		double homeWorkDifferenceLat= (workLatitude - homeLatitude) /5;
		double homeWorkDifferenceLong = (workLongitude - homeLongitude) /5;

		try{
			for(int j = 0; j < 8; j++){
				for(int i = 0; i< intervalsPerHour; i++){
					bw.write(insertFormat + id +"," + homeLongitude + "," + homeLatitude + "," + currentTime + ", 1" + ");\n");
					currentTime += millisecondsPer5Minutes;
					id++;
				}
				for(int i = 0; i< 5; i++){
					bw.write(insertFormat + id +"," + (homeLongitude - homeWorkDifferenceLong*i) + "," + (homeLatitude + homeWorkDifferenceLat * i) + "," + currentTime + ", 1" + ");\n");
					currentTime += millisecondsPer5Minutes;
					id++;
				}
				for(int i = 0; i< intervalsPerHour * 4; i++){
					bw.write(insertFormat + id+"," + workLongitude + "," + workLatitude + "," + currentTime + ", 1" + ");\n");
					currentTime += millisecondsPer5Minutes;
					id++;
				}
				if(j %2 == 0){
					for(int i = 0; i< intervalsPerHour; i++){
						bw.write(insertFormat + id+"," + lunchLongitudeA + "," + lunchLatitudeA + "," + currentTime + ", 1" + ");\n");
						currentTime += millisecondsPer5Minutes;
						id++;
					}
				}
				else{
					for(int i = 0; i< intervalsPerHour; i++){
						bw.write(insertFormat + id+"," + lunchLongitudeB + "," + lunchLatitudeB + "," + currentTime + ", 1" + ");\n");
						currentTime += millisecondsPer5Minutes;
						id++;
					}
				}

				for(int i = 0; i< intervalsPerHour * 5; i++){
					bw.write(insertFormat + id+"," + workLongitude + "," + workLatitude + "," + currentTime + ", 1" + ");\n");
					currentTime += millisecondsPer5Minutes;
					id++;
				}

				for(int i = 0; i< 5; i++){
					bw.write(insertFormat + id+"," + (homeLongitude + homeWorkDifferenceLong*i) + "," + (homeLatitude - homeWorkDifferenceLat * i) + "," + currentTime + ", 1" + ");\n");
					currentTime += millisecondsPer5Minutes;
					id++;
				}
				if(j % 3 == 0 || j % 3 == 2){
					for(int i = 0; i< intervalsPerHour * 2; i++){
						bw.write(insertFormat + id+"," + planetGraniteLong + "," + planetGraniteLat + "," + currentTime + ", 1" + ");\n");
						currentTime += millisecondsPer5Minutes;
						id++;
					}
					for(int i = 0; i< intervalsPerHour * 11; i++){
						bw.write(insertFormat + id+"," + homeLongitude + "," + homeLatitude + "," + currentTime + ", 1" + ");\n");
						currentTime += millisecondsPer5Minutes;
						id++;
					}
				}
				else{
					for(int i = 0; i< intervalsPerHour * 13; i++){
						bw.write(insertFormat + id+"," + homeLongitude + "," + homeLatitude + "," + currentTime + ", 1" + ");\n");
						currentTime += millisecondsPer5Minutes;
						id++;
					}
				}
			}

			double tobsHomeLatitude = 37.434249;
			double tobsHomeLongitude = -122.146339;
			double barLatitude = 37.406451;
			double barLongitude = -122.064692;
			double lunchLatitudeC = 37.420651;
			double lunchLongitudeC =  -122.093489;

			currentTime = timeInfected - millisecondsPerDay;
			homeWorkDifferenceLat = (tobsHomeLatitude - workLatitude ) /5;
			homeWorkDifferenceLong = (workLongitude - tobsHomeLongitude ) /5;

			for(int j = 0; j < 8; j++){
				for(int i = 0; i< intervalsPerHour; i++){
					bw.write(insertFormat + id+"," + tobsHomeLongitude + "," + tobsHomeLatitude + "," + currentTime + ", 2" + ");\n");
					currentTime += millisecondsPer5Minutes;
					id++;
				}
				for(int i = 0; i< 5; i++){
					bw.write(insertFormat + id+"," + (tobsHomeLongitude - homeWorkDifferenceLong*i) + "," + (tobsHomeLatitude + homeWorkDifferenceLat * i) + "," + currentTime + ", 2" + ");\n");
					currentTime += millisecondsPer5Minutes;
					id++;
				}
				for(int i = 0; i< intervalsPerHour *4; i++){
					bw.write(insertFormat + id+"," + workLongitude + "," + workLatitude + "," + currentTime + ", 2" + ");\n");
					currentTime += millisecondsPer5Minutes;
					id++;
				}
				if(j %2 == 0){
					for(int i = 0; i< intervalsPerHour; i++){
						bw.write(insertFormat + id+"," + lunchLongitudeA + "," + lunchLatitudeA + "," + currentTime + ", 2" + ");\n");
						currentTime += millisecondsPer5Minutes;
						id++;
					}
				}
				else{
					for(int i = 0; i< intervalsPerHour; i++){
						bw.write(insertFormat + id+"," + lunchLongitudeC + "," + lunchLatitudeC + "," + currentTime + ", 2" + ");\n");
						currentTime += millisecondsPer5Minutes;
						id++;
					}
				}

				for(int i = 0; i< intervalsPerHour * 5; i++){
					bw.write(insertFormat + id+"," + workLongitude + "," + workLatitude + "," + currentTime + ", 2" + ");\n");
					currentTime += millisecondsPer5Minutes;
					id++;
				}

				for(int i = 0; i< 5; i++){
					bw.write(insertFormat + id+"," + (tobsHomeLongitude + homeWorkDifferenceLong*i) + "," + (tobsHomeLatitude - homeWorkDifferenceLat * i) + "," + currentTime + ", 2" + ");\n");
					currentTime += millisecondsPer5Minutes;
					id++;
				}
				if(j % 3 == 0){
					for(int i = 0; i< intervalsPerHour * 3; i++){
						bw.write(insertFormat + id+"," + barLongitude + "," + barLatitude + "," + currentTime + ", 2" + ");\n");
						currentTime += millisecondsPer5Minutes;
						id++;
					}
					for(int i = 0; i< intervalsPerHour * 10; i++){
						bw.write(insertFormat + id+"," + tobsHomeLongitude + "," + tobsHomeLatitude + "," + currentTime + ", 2" + ");\n");
						currentTime += millisecondsPer5Minutes;
						id++;
					}
				}
				else{
					for(int i = 0; i< intervalsPerHour * 13; i++){
						bw.write(insertFormat + id+"," + tobsHomeLongitude + "," + tobsHomeLatitude + "," + currentTime + ", 2" + ");\n");
						currentTime += millisecondsPer5Minutes;
						id++;
					}
				}
			}

			double julioWorkLat = 37.413459;
			double julioWorkLong = -122.056174;
			double lunchLatitudeD = 37.418231;
			double lunchLongitudeD =  -122.056947;

			currentTime = timeInfected - millisecondsPerDay;
			homeWorkDifferenceLat = (tobsHomeLatitude - julioWorkLat ) /5;
			homeWorkDifferenceLong = (julioWorkLong - tobsHomeLongitude ) /5;


			for(int j = 0; j < 8; j++){
				for(int i = 0; i< intervalsPerHour; i++){
					bw.write(insertFormat + id+"," + tobsHomeLongitude + "," + tobsHomeLatitude + "," + currentTime + ", 3" + ");\n");
					currentTime += millisecondsPer5Minutes;
					id++;
				}
				for(int i = 0; i< 5; i++){
					bw.write(insertFormat + id+"," + (tobsHomeLongitude - homeWorkDifferenceLong*i) + "," + (tobsHomeLatitude+ homeWorkDifferenceLat * i) + "," + currentTime + ", 3" + ");\n");
					currentTime += millisecondsPer5Minutes;
					id++;
				}
				for(int i = 0; i< intervalsPerHour *4; i++){
					bw.write(insertFormat + id+"," + julioWorkLong + "," + julioWorkLat + "," + currentTime + ", 3" + ");\n");
					currentTime += millisecondsPer5Minutes;
					id++;
				}
				for(int i = 0; i< intervalsPerHour; i++){
					bw.write(insertFormat + id+"," + lunchLongitudeD + "," + lunchLatitudeD + "," + currentTime + ", 3" + ");\n");
					currentTime += millisecondsPer5Minutes;
					id++;
				}
			
				for(int i = 0; i< intervalsPerHour * 5; i++){
					bw.write(insertFormat + id+"," + workLongitude + "," + workLatitude + "," + currentTime + ", 3" + ");\n");
					currentTime += millisecondsPer5Minutes;
					id++;
				}

				for(int i = 0; i< 5; i++){
					bw.write(insertFormat + id+"," + (tobsHomeLongitude + homeWorkDifferenceLong*i) + "," + (tobsHomeLatitude - homeWorkDifferenceLat * i) + "," + currentTime + ", 3" + ");\n");
					currentTime += millisecondsPer5Minutes;
					id++;
				}
				if(j % 3 == 0){
					for(int i = 0; i< intervalsPerHour * 3; i++){
						bw.write(insertFormat + id+"," + barLongitude + "," + barLatitude + "," + currentTime + ", 3" + ");\n");
						currentTime += millisecondsPer5Minutes;
						id++;
					}
					for(int i = 0; i< intervalsPerHour * 10; i++){
						bw.write(insertFormat + id+"," + tobsHomeLongitude + "," + tobsHomeLatitude + "," + currentTime + ", 3" + ");\n");
						currentTime += millisecondsPer5Minutes;
						id++;
					}
				}
				else{
					for(int i = 0; i< intervalsPerHour * 13; i++){
						bw.write(insertFormat + id+"," + tobsHomeLongitude + "," + tobsHomeLatitude + "," + currentTime + ", 3" + ");\n");
						currentTime += millisecondsPer5Minutes;
						id++;
					}
				}
			}

			double starkHomeLat = 37.447061;
			double starkHomeLong = -122.248735;
			double starkWorkLat = 37.422304;
			double starkWorkLong = -122.083726;
			currentTime = timeInfected - millisecondsPerDay;
			homeWorkDifferenceLat = (starkHomeLat- starkWorkLat) /5;
			homeWorkDifferenceLong = (starkHomeLong - starkWorkLong ) /5;

			for(int j = 0; j < 8; j++){
				for(int i = 0; i< intervalsPerHour; i++){
					bw.write(insertFormat + id+"," + starkHomeLong + "," + starkHomeLat + "," + currentTime + ", 4" + ");\n");
					currentTime += millisecondsPer5Minutes;
					id++;
				}
				for(int i = 0; i< 5; i++){
					bw.write(insertFormat + id+"," + (starkHomeLong  - homeWorkDifferenceLong*i) + "," + (starkHomeLat + homeWorkDifferenceLat * i) + "," + currentTime + ", 4" + ");\n");
					currentTime += millisecondsPer5Minutes;
					id++;
				}
				for(int i = 0; i< intervalsPerHour * 4; i++){
					bw.write(insertFormat + id+"," + starkWorkLong + "," + starkWorkLat + "," + currentTime + ", 4" + ");\n");
					currentTime += millisecondsPer5Minutes;
					id++;
				}
				if(j %2 == 0){
					for(int i = 0; i< intervalsPerHour; i++){
						bw.write(insertFormat + id+"," + lunchLongitudeA + "," + lunchLatitudeA + "," + currentTime + ", 4" + ");\n");
						currentTime += millisecondsPer5Minutes;
						id++;
					}
				}
				else{
					for(int i = 0; i< intervalsPerHour; i++){
						bw.write(insertFormat + id+"," + lunchLongitudeB + "," + lunchLatitudeB + "," + currentTime + ", 4" + ");\n");
						currentTime += millisecondsPer5Minutes;
						id++;
					}
				}

				for(int i = 0; i< intervalsPerHour * 5; i++){
					bw.write(insertFormat + id+ "," + starkWorkLong + "," + starkWorkLat + "," + currentTime + ", 4" + ");\n");
					currentTime += millisecondsPer5Minutes;
					id++;
				}

				for(int i = 0; i< 5; i++){
					bw.write(insertFormat + id+"," + (starkHomeLong  + homeWorkDifferenceLong*i) + "," + (starkHomeLat - homeWorkDifferenceLat * i) + "," + currentTime + ", 4" + ");\n");
					currentTime += millisecondsPer5Minutes;
					id++;
				}
				if(j % 3 == 0 || j % 3 == 2){
					for(int i = 0; i< intervalsPerHour * 2; i++){
						bw.write(insertFormat + id+"," + planetGraniteLong + "," + planetGraniteLat + "," + currentTime + ", 4" + ");\n");
						currentTime += millisecondsPer5Minutes;
						id++;
					}
					for(int i = 0; i< intervalsPerHour * 11; i++){
						bw.write(insertFormat + id+"," + starkHomeLong  + "," + starkHomeLat + "," + currentTime + ", 4" + ");\n");
						currentTime += millisecondsPer5Minutes;
						id++;
					}
				}
				else{
					for(int i = 0; i< intervalsPerHour * 13; i++){
						bw.write(insertFormat + id+"," + starkHomeLong  + "," + starkHomeLat + "," + currentTime + ", 4" + ");\n");
						currentTime += millisecondsPer5Minutes;
						id++;
					}
				}
			}

		}
		catch(IOException e){
			System.out.println("LOL TERRIBLE CODING PRACTICES WUT");
		}

	}
}