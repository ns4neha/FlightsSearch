package com.travix.medusa.busyflights.domain.busyflights;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.travix.medusa.busyflights.domain.SupplierEnum;
import com.travix.medusa.busyflights.domain.serialize.ISODateTimeSerializer;


public class BusyFlightsResponse {

    private String airline;
    private SupplierEnum supplier ;
    //This should be rounded to two decimal places
    private double fare ;
    private String departureAirportCode;
    private String destinationAirportCode;

    @JsonSerialize(using = ISODateTimeSerializer.class)
    private LocalDateTime departureDate ;

    @JsonSerialize(using = ISODateTimeSerializer.class)
    private LocalDateTime arrivalDate ;

    public String getAirline() {
        return airline;
    }

    public void setAirline(final String airline) {
        this.airline = airline;
    }

    public SupplierEnum getSupplier() {
        return supplier;
    }

    public void setSupplier(final SupplierEnum supplier) {
        this.supplier = supplier;
    }

    public double getFare() {
        return fare;
    }

    public void setFare(final double fare) {
        this.fare = fare;
    }

    public String getDepartureAirportCode() {
        return departureAirportCode;
    }

    public void setDepartureAirportCode(final String departureAirportCode) {
        this.departureAirportCode = departureAirportCode;
    }

    public String getDestinationAirportCode() {
        return destinationAirportCode;
    }

    public void setDestinationAirportCode(final String destinationAirportCode) {
        this.destinationAirportCode = destinationAirportCode;
    }

    public LocalDateTime getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(final LocalDateTime departureDate) {
        this.departureDate = departureDate;
    }

    public LocalDateTime getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(final LocalDateTime arrivalDate) {
        this.arrivalDate = arrivalDate;
    }


}
