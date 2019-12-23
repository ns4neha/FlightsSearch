package com.travix.medusa.busyflights.service.supplier;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travix.medusa.busyflights.domain.SupplierEnum;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsResponse;
import com.travix.medusa.busyflights.domain.toughjet.ToughJetRequest;
import com.travix.medusa.busyflights.domain.toughjet.ToughJetResponse;
import org.apache.commons.math3.util.Precision;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ToughJetService extends SupplierService<ToughJetRequest, ToughJetResponse> {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Value("${tough.jet.url}")
    private String url ;

    @Autowired
    private RestTemplate restTemplate ;

    @Override
    protected List<ToughJetResponse> search(ToughJetRequest request) {

        ResponseEntity<ToughJetResponse[]> toughJetResponses =
                restTemplate.getForEntity(url, ToughJetResponse[].class,
                        objectMapper.convertValue(request, Map.class));
        if (toughJetResponses.hasBody())
            return Arrays.asList(toughJetResponses.getBody());
        return Collections.emptyList() ;

    }

    @Override
    public SupplierEnum supplier() {
        return SupplierEnum.TOUGH_JET;
    }

    protected ToughJetRequest toSupplierRequest(BusyFlightsRequest busyFlightsRequest){
        ToughJetRequest toughJetRequest = modelMapper.map(this, ToughJetRequest.class);

        //TODO Can create typemaps and associate with model mapper
        toughJetRequest.setFrom(busyFlightsRequest.getOrigin());
        toughJetRequest.setOutboundDate(busyFlightsRequest.getDepartureDate());
        toughJetRequest.setTo(busyFlightsRequest.getDestination());
        toughJetRequest.setInboundDate(busyFlightsRequest.getReturnDate());
        toughJetRequest.setNumberOfAdults(busyFlightsRequest.getNumberOfPassengers());
        return toughJetRequest ;
    }

    protected BusyFlightsResponse toBusyFlightsResponse(ToughJetResponse toughJetResponse){

        if (modelMapper.getTypeMap(ToughJetResponse.class, BusyFlightsResponse.class) == null) {

            modelMapper.addMappings(
                    new PropertyMap<ToughJetResponse, BusyFlightsResponse>() {
                        protected void configure() {
                            map(source.getArrivalAirportName()).setDestinationAirportCode(null);
                            map(source.getDepartureAirportName()).setDepartureAirportCode(null);
                            map(source.getCarrier()).setAirline(null);
                            map(source.getInboundDateTime()).setArrivalDate(null);
                            map(source.getOutboundDateTime()).setDepartureDate(null);

                        }
                    }
            );
        }

        BusyFlightsResponse busyFlightsResponse = modelMapper.map(toughJetResponse, BusyFlightsResponse.class);
        busyFlightsResponse.setSupplier(supplier());

        //TODO Can refactor this to tidy it up in the converters and associate it to modelmapper
        busyFlightsResponse.setFare(calculateFareFromToughJet(toughJetResponse));

        return busyFlightsResponse;
    }

    private Double calculateFareFromToughJet(ToughJetResponse toughJetResponse){
        return Precision.round(toughJetResponse.getBasePrice() + toughJetResponse.getTax() - toughJetResponse.getDiscount(), 2) ;
    }


}
