package com.travix.medusa.busyflights.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.travix.medusa.busyflights.Config;
import com.travix.medusa.busyflights.domain.SupplierEnum;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsResponse;
import com.travix.medusa.busyflights.service.supplier.SupplierService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {BusyFlightsService.class})
@ContextConfiguration(classes = Config.class)
public class BusyFlightsServiceTest {


    @Autowired
    BusyFlightsService busyFlightsService;

    @MockBean
    SupplierService crazyAirSupplierService;

    private BusyFlightsRequest busyFlightsRequest;
    private BusyFlightsResponse crazyAirBusyFlightsResponse;

    @Before
    public void setUp() {
        busyFlightsRequest = new BusyFlightsRequest();
        busyFlightsRequest.setOrigin("LHR");
        busyFlightsRequest.setDestination("NYC");
        busyFlightsRequest.setDepartureDate(LocalDate.now().toString());
        busyFlightsRequest.setNumberOfPassengers(4);
        busyFlightsRequest.setReturnDate(LocalDate.now().plusDays(1).toString());
        crazyAirBusyFlightsResponse = crazyAirFlightResponse();
    }

    @Test
    public void testBusyFlightsService(){

        Mockito.when(crazyAirSupplierService.searchFlights(busyFlightsRequest)).thenReturn(Lists.newArrayList(crazyAirBusyFlightsResponse));
        List<BusyFlightsResponse> busyFlightsResponses = busyFlightsService.searchFlights(busyFlightsRequest);
        Assert.assertTrue(busyFlightsResponses.size() == 1);
        Assert.assertEquals(crazyAirBusyFlightsResponse.getAirline(), busyFlightsResponses.get(0).getAirline());
        Assert.assertTrue(crazyAirBusyFlightsResponse.getFare() == busyFlightsResponses.get(0).getFare());
        Assert.assertEquals(crazyAirBusyFlightsResponse.getArrivalDate(), busyFlightsResponses.get(0).getArrivalDate());
        Assert.assertEquals(crazyAirBusyFlightsResponse.getDepartureAirportCode(), busyFlightsResponses.get(0).getDepartureAirportCode());
        Assert.assertEquals(crazyAirBusyFlightsResponse.getDepartureDate(), busyFlightsResponses.get(0).getDepartureDate());
        Assert.assertEquals(crazyAirBusyFlightsResponse.getDestinationAirportCode(), busyFlightsResponses.get(0).getDestinationAirportCode());
        Assert.assertEquals(SupplierEnum.CRAZY_AIR, busyFlightsResponses.get(0).getSupplier());
    }

    private BusyFlightsResponse crazyAirFlightResponse() {
    	BusyFlightsResponse busyFlightsResponse = new BusyFlightsResponse();
        busyFlightsResponse.setAirline("British Airways");
        busyFlightsResponse.setDepartureAirportCode(busyFlightsRequest.getOrigin());
        busyFlightsResponse.setDestinationAirportCode(busyFlightsRequest.getDestination());
        busyFlightsResponse.setSupplier(SupplierEnum.CRAZY_AIR);
        busyFlightsResponse.setFare(22);
        busyFlightsResponse.setArrivalDate(LocalDateTime.now().plusDays(1));
        busyFlightsResponse.setDepartureDate(LocalDateTime.now());
        return busyFlightsResponse;
    }
}
