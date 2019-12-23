package com.travix.medusa.busyflights.domain.toughjet;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.travix.medusa.busyflights.domain.serialize.ISOInstantDeSerializer;
import com.travix.medusa.busyflights.domain.serialize.ISOInstantSerializer;
import com.travix.medusa.busyflights.domain.serialize.ISOLocalDateTimeDeSerializer;

public class ToughJetResponse {

    private String carrier;
    private double basePrice;
    private double tax;
    private double discount;
    private String departureAirportName;
    private String arrivalAirportName;
    //Changed the data type from string to Local date to allow usage of converters during deserialization
    @JsonDeserialize(using = ISOInstantDeSerializer.class)
    @JsonSerialize(using = ISOInstantSerializer.class)
    private LocalDateTime outboundDateTime;
    @JsonDeserialize(using = ISOInstantDeSerializer.class)
    @JsonSerialize(using = ISOInstantSerializer.class)
    private LocalDateTime inboundDateTime;

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(final String carrier) {
        this.carrier = carrier;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(final double basePrice) {
        this.basePrice = basePrice;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(final double tax) {
        this.tax = tax;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(final double discount) {
        this.discount = discount;
    }

    public String getDepartureAirportName() {
        return departureAirportName;
    }

    public void setDepartureAirportName(final String departureAirportName) {
        this.departureAirportName = departureAirportName;
    }

    public String getArrivalAirportName() {
        return arrivalAirportName;
    }

    public void setArrivalAirportName(final String arrivalAirportName) {
        this.arrivalAirportName = arrivalAirportName;
    }

    public LocalDateTime getOutboundDateTime() {
        return outboundDateTime;
    }

    public void setOutboundDateTime(final LocalDateTime outboundDateTime) {
        this.outboundDateTime = outboundDateTime;
    }

    public LocalDateTime getInboundDateTime() {
        return inboundDateTime;
    }

    public void setInboundDateTime(final LocalDateTime inboundDateTime) {
        this.inboundDateTime = inboundDateTime;
    }
}
