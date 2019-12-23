package com.travix.medusa.busyflights.domain.crazyair;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.travix.medusa.busyflights.domain.serialize.ISODateTimeSerializer;
import com.travix.medusa.busyflights.domain.serialize.ISOLocalDateTimeDeSerializer;
import com.travix.medusa.busyflights.domain.serialize.ISOLocalDateTimeSerializer;

public class CrazyAirResponse {

    private String airline;
    private double price;
    private String cabinclass;
    private String departureAirportCode;
    private String destinationAirportCode;

    //Changed the data type from string to Local date to allow usage of converters during deserialization
    @JsonDeserialize(using = ISOLocalDateTimeDeSerializer.class)
    @JsonSerialize(using = ISOLocalDateTimeSerializer.class)
    private LocalDateTime departureDate;
    @JsonDeserialize(using = ISOLocalDateTimeDeSerializer.class)
    @JsonSerialize(using = ISOLocalDateTimeSerializer.class)
    private LocalDateTime arrivalDate;

    public String getAirline() {
        return airline;
    }

    public void setAirline(final String airline) {
        this.airline = airline;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(final double price) {
        this.price = price;
    }

    public String getCabinclass() {
        return cabinclass;
    }

    public void setCabinclass(final String cabinclass) {
        this.cabinclass = cabinclass;
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
