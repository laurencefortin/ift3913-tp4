import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;

public class CSV {
		//Delimiter used in CSV file
		private static final String COMMA_DELIMITER = ", ";
		private static final String NEW_LINE_SEPARATOR = "\n";
	    
	    //CSV file header
	    private static final String FILE_HEADER = "id_version, n_classes";
	    
		public CSV(LinkedHashMap<String, Integer> valeurCSV) {
			 ecrireCSV(valeurCSV);
		 }
		
		  /**
		   * permet d'ecrire le csv de classe 
		   * @return un fichier csv creer remplis d'informations sur les classes
		   * */ 
		    public void ecrireCSV(LinkedHashMap<String, Integer> valeurCSV  ){
 
				String fileName = "file.csv";
		        FileWriter fileWriter = null;
		                 
		        try {
		            fileWriter = new FileWriter(fileName);
		 
		            //Write the CSV file header
		            fileWriter.append(FILE_HEADER.toString());
		             
		            //Add a new line separator after the header
		            fileWriter.append(NEW_LINE_SEPARATOR);
		             
		            //Write a new student object list to the CSV file
		           
		            for (Map.Entry value : valeurCSV.entrySet()) {
		            	fileWriter.append(String.valueOf(value.getKey()));  //id_version hexadecimal
		                fileWriter.append(COMMA_DELIMITER);
		                fileWriter.append(String.valueOf(value.getValue())); //n_classes nombre de fichier java
		                fileWriter.append(COMMA_DELIMITER);
		                fileWriter.append(NEW_LINE_SEPARATOR);
		            }		              
		           
		        } catch (Exception e) {
		            System.out.println("Error in CsvFileWriter !!!");
		            e.printStackTrace();
		        } finally {
		             
		            try {
		                fileWriter.flush();
		                fileWriter.close();
		            } catch (IOException e) {
		                System.out.println("Error while flushing/closing fileWriter !!!");
		                e.printStackTrace();
		            } 
		        }
		  }
	}