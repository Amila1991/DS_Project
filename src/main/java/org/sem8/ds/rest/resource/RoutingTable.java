package org.sem8.ds.rest.resource;

import org.sem8.ds.util.constant.NodeConstant;

import java.util.*;

/**
 * Created by yellowflash on 1/25/17.
 */
public class RoutingTable {
    //    private ArrayList<String> neighbouringTable;
    private List<NodeResource> nodeList = new ArrayList<NodeResource>();

    private static RoutingTable table;

    private RoutingTable() {
    }

    public void addNeighBour(NodeResource node) {
        this.nodeList.add(node);
    }

    public boolean removeNeighbour(NodeResource node) {
        for (int i = 0; i < nodeList.size(); i++) {
            if (nodeList.get(i).equals(node)) {
                nodeList.remove(i);
                return true;
            }
        }
        return false;
    }

    public List<NodeResource> getNodeList() {
        return nodeList;
    }

    public static RoutingTable getInstance() {
        if (RoutingTable.table == null) {
            RoutingTable.table = new RoutingTable();
        }
        return RoutingTable.table;
    }
}
