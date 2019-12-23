package com.travix.medusa.busyflights.domain;

public enum SupplierEnum {

    CRAZY_AIR("CrazyAir"), TOUGH_JET("ToughJet");

    private String name ;

    SupplierEnum(final String name) {
        this.name = name ;
    }

    public String getName() {
        return name;
    }
}
