import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.revwalk.RevCommit;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;


public class Proto {
 
	public static void main(String[] args)
			throws IOException, InvalidRemoteException, TransportException, GitAPIException, InterruptedException, CsvException {
 
		// Local directory on this machine where we will clone remote repo.
		File localRepoDir = new File("C:\\Users\\Public\\ift3913_tp4");
		System.out.println("Veuillez entrer l'adresse du repo desire: ");
		Scanner scanner = new Scanner(System.in); 
		
		String lien = scanner.nextLine();
		
		
		// Monitor to get git command progress printed on java System.out console
		TextProgressMonitor consoleProgressMonitor = new TextProgressMonitor(new PrintWriter(System.out));
 
		/*
		 * Git clone remote repo into local directory.
		 * 
		 */
		System.out.println("\n>>> Cloning repository\n");
		if(localRepoDir.exists())
		{
		}

		Repository repo = Git.cloneRepository().setProgressMonitor(consoleProgressMonitor).setDirectory(localRepoDir).setURI(lien.toString()).call().getRepository();
		scanner.close();
		try (Git git = new Git(repo)) {
			/*
			 * Get list of all branches (including remote) & print
			 * 
			 * Equivalent of --> $ git branch -a
			 * 
			 */
			System.out.println("\n>>> Listing all branches\n");
			git.branchList().setListMode(ListMode.ALL).call().stream().forEach(r -> System.out.println(r.getName()));
 
			
		
			
			/* On print tous les commits*/
			String treeName = "refs/heads/master"; // tag or branch
			LinkedHashMap<String, Integer> valeurCSV = new LinkedHashMap<String, Integer>();
			ArrayList<Double> mediane = new ArrayList<Double>();
			int compteur = 1;
			int compteurCommits = 0;
			System.out.println(compteurCommits);
			for (RevCommit commit : git.log().add(repo.resolve(treeName)).call()) {	
				if(compteurCommits < 400)
				{
				File temp = new File("classes.csv");
				if(temp.exists())
				{
					temp.delete();
				}
			    git.checkout().setName(commit.getName()).call();
			    valeurCSV.put(commit.getName(), nombreClasses(localRepoDir.listFiles()));	
			    CSV csv = new CSV(TP.iterateOnFiles(localRepoDir.listFiles()));
			    CSVReader reader = new CSVReader(new FileReader(temp.getAbsolutePath()));
			    List<String[]> classes = reader.readAll();
			    reader.close();
			    ArrayList<String> valeurs = new ArrayList<String>();
			    for (int i = compteur; i <= nombreClasses(localRepoDir.listFiles()) + compteur-1;i++) {
			    	String[] classe = classes.get(i);
			    	if(!classe[1].contains("classe"))
			    	{
			        valeurs.add(classe[1]);
			    	}
			    }
			    double medianeValeur = 0.0;
			    if(!valeurs.isEmpty())
			    {
			    	medianeValeur = mediane(valeurs);
			    }
			    valeurs.clear();
			    compteur += nombreClasses(localRepoDir.listFiles());
			    mediane.add(medianeValeur);
			    System.out.println("Current commit " + compteurCommits);
			    compteurCommits++;
				}
			    }
			 CSVMaker csv = new CSVMaker(valeurCSV, mediane);
		}
		

	}
	public static void deleteDirectoryRecursion(File localRepoDir) throws IOException {
		  if (localRepoDir.isDirectory()) {
			    File[] entries = localRepoDir.listFiles();
			    if (entries != null) {
			      for (File entry : entries) {
			        deleteDirectoryRecursion(entry);
			      }
			    }
			  }
			  if (!localRepoDir.delete()) {
			    throw new IOException("Failed to delete " + localRepoDir);
			  }
			}
	public static int nombreClasses(File[] directoryListing) {
		
		int nombre = 0;
		if (directoryListing != null) {
		      for (File child : directoryListing) 
		      {
		    	  String extension = "";
		    	  int i = child.getName().lastIndexOf('.');
		    	  if (i > 0) {
		    	      extension = child.getName().substring(i+1);
		    	  }

		    	  if(!child.isDirectory() && extension.equals("java"))
		    	  {
		    		  nombre++;
		    	  }
		    	  else if (child.isDirectory())
		    	  {
		  		   nombre += nombreClasses(child.listFiles());
		    	  }
		    	  
		      }
		      
		   }
		return nombre;
	}
	
	public static double mediane(ArrayList<String> valeurs) {
        Collections.sort(valeurs);

        if (valeurs.size() % 2 == 1)
            return Double.parseDouble(valeurs.get((valeurs.size() + 1) / 2 - 1));
        else {
            double lower = Double.parseDouble(valeurs.get(valeurs.size() / 2 - 1));
            double upper = Double.parseDouble(valeurs.get(valeurs.size() / 2));

            return (lower + upper) / 2.0;
        }
    }
}

