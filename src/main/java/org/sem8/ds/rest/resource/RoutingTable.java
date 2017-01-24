package org.sem8.ds.rest.resource;

import org.sem8.ds.util.constant.NodeConstant;

import java.util.*;

/**
 * Created by yellowflash on 1/25/17.
 */
public class RoutingTable {
    //    private ArrayList<String> neighbouringTable;
    private final Map<String, String> neighbouringTable = new HashMap<>();
    private final Map<String, List<String>> fileMap = new HashMap<>();
    private static RoutingTable table;

    private RoutingTable() {
    }

    public void addNeighBour(String ip, int port) {
        this.neighbouringTable.put(ip + ":" + port, NodeConstant.APIMessages.DISCONNECTED);
    }

    public void removeNeighbour(String ip, int port) {
        this.neighbouringTable.remove(ip + ":" + port);
    }

    public void updateNeighbourState(String ip, int port, String status) {
        this.neighbouringTable.put(ip + ":" + port, status);
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
        }
        return tempSet;
    }

    public Map<String, String> getNeighbouringTable() {
        return neighbouringTable;
    }

    public Map<String, List<String>> getFileMap() {
        return fileMap;
    }

    public static RoutingTable getInstance() {
        if (RoutingTable.table == null) {
            RoutingTable.table = new RoutingTable();
        }
        return RoutingTable.table;
    }
}
