package de.audibene.core.domain.events.subject;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class SubjectsProperties<S extends SubjectProperties> {
    private final Map<String, S> subjects = new LinkedHashMap<>();

    public SubjectsProperties() {
        subjects.put("defaults", defaults());
    }

    protected abstract S defaults();

    public Map<String, S> getSubjects() {
        return subjects;
    }

    public S getSubject(String name) {
        return subjects.getOrDefault(name, subjects.get("defaults"));
    }
}
