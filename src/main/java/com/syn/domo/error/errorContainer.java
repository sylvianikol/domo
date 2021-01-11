package com.syn.domo.error;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class errorContainer {

    private Map<String, Set<String>> errors;

    public errorContainer() {
        this.errors = new HashMap<>();
    }

    public Map<String, Set<String>> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, Set<String>> errors) {
        this.errors = errors;
    }

}
