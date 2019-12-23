package com.travix.medusa.busyflights.service.supplier;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.travix.medusa.busyflights.Config;
import com.travix.medusa.busyflights.domain.SupplierEnum;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsResponse;
import com.travix.medusa.busyflights.domain.crazyair.CrazyAirResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CrazyAirService.class})
@ContextConfiguration(classes = Config.class)
public class CrazyAirServiceTest {

    @Autowired
    CrazyAirService crazyAirService;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ModelMapper modelMapper;

    private MockRestServiceServer mockServer;
    private BusyFlightsRequest busyFlightsRequest;
    private CrazyAirResponse crazyAirResponse;
    private ObjectMapper objectMapper ;

    @Before
    public void setUp() {

        mockServer = MockRestServiceServer.bindTo(restTemplate).ignoreExpectOrder(true).build();
        objectMapper = new ObjectMapper();
        crazyAirResponse = new CrazyAirResponse();
        busyFlightsRequest = new BusyFlightsRequest();
        busyFlightsRequest.setOrigin("LHR");
        busyFlightsRequest.setDestination("NYC");
        busyFlightsRequest.setDepartureDate(LocalDate.now().toString());
        busyFlightsRequest.setNumberOfPassengers(4);
        busyFlightsRequest.setReturnDate(LocalDate.now().plusDays(1).toString());
    }

    @Test
    public void testCrazyAirFlightsSearch_SingleResponse_OK() throws Exception {

        mockServer.expect(requestTo(String.format("http://localhost:8080/crazyair?from=%s&to=%s&departureDate=%s&returnDate=%s&passengerCount=%d", "LHR","NYC",LocalDate.now(), LocalDate.now().plusDays(1),Integer.valueOf(4))))
                .andRespond(withSuccess(crazyAirResponseJson(), MediaType.APPLICATION_JSON));

        final List<BusyFlightsResponse> busyFlightsResponses =
                crazyAirService.searchFlights(busyFlightsRequest);
        mockServer.verify();
        Assert.assertEquals(1, busyFlightsResponses.size());
        Assert.assertEquals(crazyAirResponse.getAirline(), busyFlightsResponses.get(0).getAirline());
        Assert.assertTrue(crazyAirResponse.getPrice() == busyFlightsResponses.get(0).getFare());
        Assert.assertEquals(crazyAirResponse.getArrivalDate(), busyFlightsResponses.get(0).getArrivalDate());
        Assert.assertEquals(crazyAirResponse.getDepartureAirportCode(), busyFlightsResponses.get(0).getDepartureAirportCode());
        Assert.assertEquals(crazyAirResponse.getDepartureDate(), busyFlightsResponses.get(0).getDepartureDate());
        Assert.assertEquals(crazyAirResponse.getDestinationAirportCode(), busyFlightsResponses.get(0).getDestinationAirportCode());
        Assert.assertEquals(SupplierEnum.CRAZY_AIR, busyFlightsResponses.get(0).getSupplier());
    }

    @Test
    public void testCrazyAirFlightsSearch_NoResponse() throws Exception {

        mockServer
                .expect(requestTo((String.format("http://localhost:8080/crazyair?from=%s&to=%s&departureDate=%s&returnDate=%s&passengerCount=%d", "LHR","NYC",LocalDate.now(), LocalDate.now().plusDays(1),Integer.valueOf(4)))))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        final List<BusyFlightsResponse> busyFlightsResponses =
                crazyAirService.searchFlights(busyFlightsRequest);
        mockServer.verify();
        Assert.assertEquals(0, busyFlightsResponses.size());
    }


    private String crazyAirResponseJson() throws JsonProcessingException {

        crazyAirResponse.setAirline("British Airways");
        crazyAirResponse.setArrivalDate(LocalDateTime.now());
        crazyAirResponse.setPrice(100);
        crazyAirResponse.setDepartureAirportCode("LHR");
        crazyAirResponse.setDestinationAirportCode("NYC");
        crazyAirResponse.setDepartureDate(LocalDateTime.now().plusDays(1));
        ArrayList<CrazyAirResponse> crazyAirResponses = new ArrayList<>();
        crazyAirResponses.add(crazyAirResponse);

        return objectMapper.writeValueAsString(crazyAirResponses);

    }
}
