package de.audibene.core.domain.events.subject;

public class SubjectProperties {
    private int maximumConnections = 1;
    private int maximumProcessors = 1;

    public int getMaximumConnections() {
        return maximumConnections;
    }

    public void setMaximumConnections(int maximumConnections) {
        this.maximumConnections = maximumConnections;
    }

    public int getMaximumProcessors() {
        return maximumProcessors;
    }

    public void setMaximumProcessors(int maximumProcessors) {
        this.maximumProcessors = maximumProcessors;
    }
}
