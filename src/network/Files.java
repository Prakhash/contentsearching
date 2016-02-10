package network;

import java.util.*;

/**
 * Created by Ruba on 1/10/2016.
 */
public class Files {
    private static ArrayList<String> files = new ArrayList<String>(Arrays.asList("Adventures of Tintin", "Jack and Jill",
            "Glee", "The Vampire Diarie", "King Arthur", "Windows XP",
            "Harry Potter", "Kung Fu Panda", "Lady Gaga", "Twilight",
            "Windows 8", "Mission Impossible", "Turn Up The Music",
            "Super Mario", "American Pickers", "Microsoft Office 2010",
            "Happy Feet", "Modern Family", "American Idol",
            "Hacking for Dummies"));

    public static ArrayList<String> getRandomFiles(){
        ArrayList<String> selectedFiles = new ArrayList<String>();

        Collections.shuffle(files);
        Random rand = new Random();

        //Decide number of files between 3 to 5
        int numberOfFiles = 3 + rand.nextInt(3);

        //choose numberOfFiles randomly
        for(int i=0; i<numberOfFiles; i++) {
            selectedFiles.add(files.get(i));
        }

        return selectedFiles;
    }
}
