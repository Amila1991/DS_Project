package org.sem8.ds.services.stat;

import org.apache.commons.math3.stat.descriptive.StatisticalSummary;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.sem8.ds.client.remote.ResponseInterface;
import org.sem8.ds.client.resource.SearchStatResource;
import org.sem8.ds.util.constant.NodeConstant;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.SocketException;
import java.util.*;

/**
 * @author amila karunathilaka.
 */
public class NodeStatService {

    private String userName;
    private int nodeDegree;

    private Map<NodeConstant.NodeMsgType, Integer> msgCount;

    private List<Integer> hopList;
    private List<Long> latencyList;

    private SummaryStatistics hopStat;
    private SummaryStatistics latencyStat;

    private ResponseInterface anInterface;

    public void init() throws SocketException {
        msgCount = new HashMap<>();
        msgCount.put(NodeConstant.NodeMsgType.JOIN, 0);
        msgCount.put(NodeConstant.NodeMsgType.LEAVE, 0);
        msgCount.put(NodeConstant.NodeMsgType.SEARCH, 0);
        msgCount.put(NodeConstant.NodeMsgType.SEARCHRESPONSE, 0);
        msgCount.put(NodeConstant.NodeMsgType.FORWARD, 0);

        hopList = new Vector<>();
        latencyList = new Vector<>();

        hopStat = new SummaryStatistics();
        latencyStat = new SummaryStatistics();
    }

    public void increaseMsgCount(NodeConstant.NodeMsgType msgType) {
        msgCount.put(msgType, msgCount.get(msgType)+1);
        anInterface.setTotalMsgCount(totalMsgCount());
    }

    public int totalMsgCount() {
        int tot = msgCount.get(NodeConstant.NodeMsgType.JOIN);
        tot += msgCount.get(NodeConstant.NodeMsgType.LEAVE);
        tot += msgCount.get(NodeConstant.NodeMsgType.SEARCH);
        tot += msgCount.get(NodeConstant.NodeMsgType.SEARCHRESPONSE);
        tot += msgCount.get(NodeConstant.NodeMsgType.FORWARD);
        return tot;
    }

    public SearchStatResource generateStat(String userName) {
        SearchStatResource statResource = new SearchStatResource();

        StatisticalSummary summary = hopStat.getSummary();
        statResource.setMaxHop((int) summary.getMax());
        statResource.setMinHop((int) summary.getMin());
        statResource.setAvgHop(summary.getMean());
        statResource.setStdHop(summary.getStandardDeviation());

      //  System.out.println("No of Hops : " + summary.getN());

        summary = latencyStat.getSummary();
        statResource.setMaxLatency((long) summary.getMax());
        statResource.setMinLatency((long) summary.getMin());
        statResource.setAvgLatency(summary.getMean());
        statResource.setStdLatency(summary.getStandardDeviation());

      //  System.out.println("No of Latencies : " + summary.getN());

        statResource.setNodeDegree(nodeDegree);

        statResource.setJoinMsgCount(msgCount.get(NodeConstant.NodeMsgType.JOIN));
        statResource.setLeaveMsgCount(msgCount.get(NodeConstant.NodeMsgType.LEAVE));
        statResource.setSearchRequestMsgCount(msgCount.get(NodeConstant.NodeMsgType.SEARCH));
        statResource.setSearchResponseMsgCount(msgCount.get(NodeConstant.NodeMsgType.SEARCHRESPONSE));
        statResource.setForwardMsgCount(msgCount.get(NodeConstant.NodeMsgType.FORWARD));
        statResource.setTotalMsgCount(totalMsgCount());

        writeToFIle(statResource, userName);

        return statResource;
    }

    private void writeToFIle(SearchStatResource searchStatResource, String userName) {
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(userName + "_NodeStat.txt");

            printWriter.println("Hop Max : " + searchStatResource.getMaxHop());
            printWriter.println("Hop Min : " + searchStatResource.getMinHop());
            printWriter.println("Hop Avg : " + searchStatResource.getAvgHop());
            printWriter.println("Hop Std : " + searchStatResource.getStdHop() + "\n");

            printWriter.println("Latency Max : " + searchStatResource.getMaxLatency());
            printWriter.println("Latency Min : " + searchStatResource.getMinLatency());
            printWriter.println("Latency Avg : " + searchStatResource.getAvgLatency());
            printWriter.println("Latency Std : " + searchStatResource.getStdLatency() + "\n");

            printWriter.println("Degree of Node : " + searchStatResource.getNodeDegree() + "\n");

            printWriter.println("Join msg count : " + searchStatResource.getJoinMsgCount());
            printWriter.println("Leave msg count : " + searchStatResource.getLeaveMsgCount());
            printWriter.println("Search Request msg count : " + searchStatResource.getSearchRequestMsgCount());
            printWriter.println("Search Response msg count : " + searchStatResource.getSearchResponseMsgCount());
            printWriter.println("Forward msg count : " + searchStatResource.getForwardMsgCount());
            printWriter.println("Total msg count : " + searchStatResource.getTotalMsgCount() + "\n");

            printWriter.flush();
            printWriter.close();

            printWriter = new PrintWriter(userName + "_HopList.txt");
            for (int hop : hopList) {
                printWriter.println(hop);
            }
            printWriter.flush();
            printWriter.close();

            printWriter = new PrintWriter(userName + "_latencyList.txt");
            for (long latency : latencyList) {
                printWriter.println(latency);
            }
            printWriter.flush();
            printWriter.close();

        } catch (FileNotFoundException e) {
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getNodeDegree() {
        return nodeDegree;
    }

    public void setNodeDegree(int nodeDegree) {
        this.nodeDegree = nodeDegree;
    }

    public List<Integer> getHopList() {
        return hopList;
    }

    public void addHop(int hop) {
        this.hopStat.addValue(hop);
        this.hopList.add(hop);
    }

    public void setHopList(List<Integer> hopList) {
        this.hopList = hopList;
    }

    public List<Long> getLatencyList() {
        return latencyList;
    }

    public void addLatency(long latency) {
        this.latencyStat.addValue(latency);
        this.latencyList.add(latency);
    }

    public void setLatencyList(List<Long> latencyList) {
        this.latencyList = latencyList;
    }

    public ResponseInterface getAnInterface() {
        return anInterface;
    }

    public void setAnInterface(ResponseInterface anInterface) {
        this.anInterface = anInterface;
    }
}
