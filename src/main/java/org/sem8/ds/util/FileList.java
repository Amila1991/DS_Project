package org.sem8.ds.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author amila karunathilaka.
 */
public class FileList {
    private static final String[] FileArr = new String[]{"Adventures_of_Tintin", "Jack_and_Jill", "Glee", "The_Vampire_Diarie",
            "King_Arthur", "Windows_XP", "Harry_Potter", "Kung_Fu_Panda", "Lady_Gaga", "Twilight", "Windows_8",
            "Mission_Impossible", "Turn_Up_The_Music", "Super_Mario", "American_Pickers", "Microsoft_Office_2010",
            "Happy_Feet", "Modern_Family", "American_Idol", "Hacking_for_Dummies"};

    private static final Random RAND = new Random();


    public static List<String> generateFileList(int noOfFiles) {
        List<String> fileList = new ArrayList<String>();
        for (int i=0 ; i < noOfFiles ; i++) {
            String file = FileArr[RAND.nextInt(FileArr.length)];
            while (fileList.contains(file)) {
                file = FileArr[RAND.nextInt(FileArr.length)];
            }
            fileList.add(file);
        }
        return fileList;
    }

}
