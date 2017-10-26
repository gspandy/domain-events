package de.audibene.core.domain.events.azure.properties;

import de.audibene.core.domain.events.subject.SubjectProperties;
import de.audibene.core.domain.events.subject.SubjectsProperties;

public class AzureSubjectsProperties extends SubjectsProperties<SubjectProperties> {
    private String connectionString;

    public String getConnectionString() {
        return connectionString;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

    @Override
    protected SubjectProperties defaults() {
        return new SubjectProperties();
    }
}
