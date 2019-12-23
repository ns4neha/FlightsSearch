package com.travix.medusa.busyflights.service.supplier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travix.medusa.busyflights.domain.SupplierEnum;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsResponse;
import com.travix.medusa.busyflights.domain.crazyair.CrazyAirRequest;
import com.travix.medusa.busyflights.domain.crazyair.CrazyAirResponse;
import org.apache.commons.math3.util.Precision;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CrazyAirService extends SupplierService<CrazyAirRequest, CrazyAirResponse> {

    @Autowired
    ModelMapper modelMapper;

    @Value("${crazy.air.url}")
    private String url ;

    @Autowired
    private RestTemplate restTemplate ;

    @Autowired
    ObjectMapper objectMapper;


    @Override
    protected List<CrazyAirResponse> search(CrazyAirRequest request) {

        ResponseEntity<CrazyAirResponse[]> crazyAirResponse = restTemplate.getForEntity(url, CrazyAirResponse[].class,
                objectMapper.convertValue(request, Map.class));

        if (crazyAirResponse.hasBody())
            return Arrays.asList(crazyAirResponse.getBody());
        return Collections.emptyList() ;
    }

    @Override
    public SupplierEnum supplier() {
        return SupplierEnum.CRAZY_AIR;
    }

    @Override
    protected CrazyAirRequest toSupplierRequest(BusyFlightsRequest busyFlightsRequest){
        CrazyAirRequest crazyAirRequest = modelMapper.map(busyFlightsRequest, CrazyAirRequest.class);
        crazyAirRequest.setPassengerCount(busyFlightsRequest.getNumberOfPassengers());
        return crazyAirRequest;
    }

    @Override
    protected BusyFlightsResponse toBusyFlightsResponse(CrazyAirResponse crazyAirResponse){
        BusyFlightsResponse busyFlightsResponse = modelMapper.map(crazyAirResponse, BusyFlightsResponse.class);
        busyFlightsResponse.setSupplier(supplier());
        busyFlightsResponse.setFare(Precision.round(crazyAirResponse.getPrice(), 2));
        return busyFlightsResponse;
    }
}
