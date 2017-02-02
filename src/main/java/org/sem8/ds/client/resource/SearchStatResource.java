package org.sem8.ds.client.resource;

/**
 * @author amila karunathilaka.
 */
public class SearchStatResource {

    private int maxHop;
    private int minHop;
    private double avgHop;
    private double stdHop;

    private long maxLatency;
    private long minLatency;
    private double avgLatency;
    private double stdLatency;

    private int nodeDegree;

    private int joinMsgCount;
    private int leaveMsgCount;
    private int searchRequestMsgCount;
    private int searchResponseMsgCount;
    private int forwardMsgCount;
    private int totalMsgCount;

    public SearchStatResource() {
    }

    public int getMaxHop() {
        return maxHop;
    }

    public void setMaxHop(int maxHop) {
        this.maxHop = maxHop;
    }

    public int getMinHop() {
        return minHop;
    }

    public void setMinHop(int minHop) {
        this.minHop = minHop;
    }

    public double getAvgHop() {
        return avgHop;
    }

    public void setAvgHop(double avgHop) {
        this.avgHop = avgHop;
    }

    public double getStdHop() {
        return stdHop;
    }

    public void setStdHop(double stdHop) {
        this.stdHop = stdHop;
    }

    public long getMaxLatency() {
        return maxLatency;
    }

    public void setMaxLatency(long maxLatency) {
        this.maxLatency = maxLatency;
    }

    public long getMinLatency() {
        return minLatency;
    }

    public void setMinLatency(long minLatency) {
        this.minLatency = minLatency;
    }

    public double getAvgLatency() {
        return avgLatency;
    }

    public void setAvgLatency(double avgLatency) {
        this.avgLatency = avgLatency;
    }

    public double getStdLatency() {
        return stdLatency;
    }

    public void setStdLatency(double stdLatency) {
        this.stdLatency = stdLatency;
    }

    public int getNodeDegree() {
        return nodeDegree;
    }

    public void setNodeDegree(int nodeDegree) {
        this.nodeDegree = nodeDegree;
    }

    public int getJoinMsgCount() {
        return joinMsgCount;
    }

    public void setJoinMsgCount(int joinMsgCount) {
        this.joinMsgCount = joinMsgCount;
    }

    public int getLeaveMsgCount() {
        return leaveMsgCount;
    }

    public void setLeaveMsgCount(int leaveMsgCount) {
        this.leaveMsgCount = leaveMsgCount;
    }

    public int getSearchRequestMsgCount() {
        return searchRequestMsgCount;
    }

    public void setSearchRequestMsgCount(int searchRequestMsgCount) {
        this.searchRequestMsgCount = searchRequestMsgCount;
    }

    public int getSearchResponseMsgCount() {
        return searchResponseMsgCount;
    }

    public void setSearchResponseMsgCount(int searchResponseMsgCount) {
        this.searchResponseMsgCount = searchResponseMsgCount;
    }

    public int getForwardMsgCount() {
        return forwardMsgCount;
    }

    public void setForwardMsgCount(int forwardMsgCount) {
        this.forwardMsgCount = forwardMsgCount;
    }

    public int getTotalMsgCount() {
        return totalMsgCount;
    }

    public void setTotalMsgCount(int totalMsgCount) {
        this.totalMsgCount = totalMsgCount;
    }
}
