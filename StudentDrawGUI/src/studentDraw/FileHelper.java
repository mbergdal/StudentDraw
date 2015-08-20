package studentDraw;
import java.io.*;
import java.util.*;


public class FileHelper {
	public static Hashtable<String, Integer> getAvailableStudents(String classListFileName, String notPresentFileName, String winnersFileName){
		Hashtable<String, Integer> allStudents = getStudentsFromFile(classListFileName);
		Hashtable<String, Integer> notPresent = getStudentsFromFile(notPresentFileName);
		Hashtable<String, Integer> winners = getStudentsFromFile(winnersFileName);
		
		Hashtable<String, Integer> availableStudents = intersectHashTables(allStudents, notPresent, winners);
		return availableStudents;
	}
	
	public static void writeWinnerToFile(String fileName, String winner){
		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("resources/" + fileName, true)))) {
		    out.println(winner);
		}catch (Exception e){
			System.out.println("Someting went wrong " + e.getMessage());
		}
	}
	
	private static Hashtable<String, Integer> intersectHashTables(
			Hashtable<String, Integer> allStudents,
			Hashtable<String, Integer> notPresent,
			Hashtable<String, Integer> winners) 
	{
		
		if (!notPresent.isEmpty()){
			allStudents.keySet().removeAll(notPresent.keySet());
		}
		
		if (!winners.isEmpty()){
			allStudents.keySet().removeAll(winners.keySet());
		}
		
		return allStudents;
	}


	private static Hashtable<String, Integer> getStudentsFromFile(String fileName){	
		Hashtable<String, Integer> students = null;
		try(BufferedReader reader = new BufferedReader(new FileReader("resources/" + fileName))){
			String line = null;
			students = new Hashtable<String, Integer>();
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("#")) 
					continue;
				String[] parts = line.split(":");
				if(parts.length > 1){
					students.put(parts[0], Integer.parseInt(parts[1]));
				} else {
					students.put(parts[0], -1);
				}			
			}
		} catch (Exception e){
			System.out.println("Someting went wrong " + e.getMessage());
		}
		
		return students;
	}
}
