package com.epam.esm.persistence.model.enums;

public enum Action {
    INSERTING("INSERTING"), UPDATING("UPDATING"), DELETING("DELETING");

    private final String name;

    Action(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
