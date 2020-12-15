import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
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




public class Test {
 
	public static void main(String[] args)
			throws IOException, InvalidRemoteException, TransportException, GitAPIException, InterruptedException, CsvException {
 
		// Local directory on this machine where we will clone remote repo.
		File localRepoDir = new File("C:\\Users\\Public\\ift3913_tp4");
		
		
		// Monitor to get git command progress printed on java System.out console
		TextProgressMonitor consoleProgressMonitor = new TextProgressMonitor(new PrintWriter(System.out));
 
		/*
		 * Git clone remote repo into local directory.
		 * 
		 * Equivalent of --> $ git clone https://github.com/Ravikharatmal/test.git
		 */
		System.out.println("\n>>> Cloning repository\n");
		if(localRepoDir.exists())
		{
		deleteDirectoryRecursion(localRepoDir);
		}
		
		Repository repo = Git.cloneRepository().setProgressMonitor(consoleProgressMonitor).setDirectory(localRepoDir).setURI("https://github.com/laurencefortin/ift3913-tp4").call().getRepository();
 
		try (Git git = new Git(repo)) {
			/*
			 * Get list of all branches (including remote) & print
			 * 
			 * Equivalent of --> $ git branch -a
			 * 
			 */
			System.out.println("\n>>> Listing all branches\n");
			git.branchList().setListMode(ListMode.ALL).call().stream().forEach(r -> System.out.println(r.getName()));
 
			
			/*
			 * Verify commit log
			 * 
			 * Equivalent of --> $ git log
			 * 
			 */
			System.out.println("\n>>> Printing commit log\n");
			Iterable<RevCommit> commitLog = git.log().call();
			commitLog.forEach(r -> System.out.println(r.getFullMessage()));
			
			
			/* On print tous les commits*/
			String treeName = "refs/heads/master"; // tag or branch
			LinkedHashMap<String, Integer> valeurCSV = new LinkedHashMap<String, Integer>();
			for (RevCommit commit : git.log().add(repo.resolve(treeName)).call()) {	
				File temp = new File("classes.csv");
				if(temp.exists())
				{
					temp.delete();
				    Thread.sleep(5000);

				}
			    git.checkout().setName(commit.getName()).call();
			    valeurCSV.put(commit.getName(), nombreClasses(localRepoDir.listFiles()));
			    TP.iterateOnFiles(localRepoDir.listFiles());
			    CSVReader reader = new CSVReader(new FileReader(temp.getAbsolutePath()));
			    List<String[]> classes = reader.readAll();
			    reader.close();
			    ArrayList<String> valeurs = new ArrayList<String>();
			    for (String[] classe : classes) {
			    	if(!classe[1].contains("classe"))
			    	{
			    	System.out.println(classe[1]);
			        valeurs.add(classe[1]);
			    	}
			    }
			    double mediane = mediane(valeurs);
			    valeurs.clear();
			    System.out.println("mediane : " + mediane);
			    }
			new CSVMaker(valeurCSV);

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

