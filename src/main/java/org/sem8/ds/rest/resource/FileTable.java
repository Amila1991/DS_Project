package org.sem8.ds.rest.resource;

import org.sem8.ds.util.constant.NodeConstant;

import java.util.*;

/**
 * Created by yellowflash on 1/25/17.
 */
public class FileTable {
    //    private ArrayList<String> neighbouringTable;

    private final Map<String, List<String>> fileMap = new HashMap<>();
    private static FileTable table;

    private FileTable() {
    }

    public void addFile(String fileName) {
        String[] temp = fileName.split("_");
        List<String> fileList;
        ArrayList<String> content;
        for (int i = 0; i < temp.length; i++) {
//            System.out.println(temp[i]);
            fileList = this.fileMap.get(temp[i]);
            if (fileList == null) {
                content = new ArrayList<>();
                content.add(fileName);
                this.fileMap.put(temp[i], content);
            } else {
                fileList.add(fileName);
            }

        }
    }

    public Set<String> searchFile(String fileName) {
        Set<String> tempSet = new HashSet<>();
        String[] tempKeywords = fileName.split(" ");
        List<String> fileList;
        Iterator<String> fileIterator;
        for (int i = 0; i < tempKeywords.length; i++) {
            fileList = this.fileMap.get(tempKeywords[i]);
            for (String string : fileList) {
                tempSet.add(string);
            }
/*            fileIterator = fileList.iterator();
            while (fileIterator.hasNext()) {
                tempSet.add(fileIterator.next());
            }*/
        }
        return tempSet;
    }

    public Map<String, List<String>> getFileMap() {
        return fileMap;
    }

    public static FileTable getInstance() {
        if (FileTable.table == null) {
            FileTable.table = new FileTable();
        }
        return FileTable.table;
    }
}
