package com.travix.medusa.busyflights.integrationTests;

import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.travix.medusa.busyflights.domain.SupplierEnum;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsResponse;
import com.travix.medusa.busyflights.domain.crazyair.CrazyAirResponse;
import com.travix.medusa.busyflights.domain.toughjet.ToughJetResponse;
import com.travix.medusa.busyflights.handlers.RestTemplateResponseErrorHandler;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

/* This is ideally a functional test but for the same class, unit test should be written as well */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BusyFlightsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ModelMapper modelMapper ;

    private ObjectMapper objectMapper ;

    private MockRestServiceServer mockServer;
    private BusyFlightsRequest busyFlightsRequest;

    private ToughJetResponse toughJetResponse;
    private CrazyAirResponse crazyAirResponse;

    @Before
    public void setUp() {

        mockServer = MockRestServiceServer.bindTo(restTemplate).ignoreExpectOrder(true).build();
        objectMapper = new ObjectMapper();
        toughJetResponse = new ToughJetResponse();
        crazyAirResponse = new CrazyAirResponse();
        busyFlightsRequest = new BusyFlightsRequest();
        busyFlightsRequest.setOrigin("LHR");
        busyFlightsRequest.setDestination("NYC");
        busyFlightsRequest.setDepartureDate(LocalDate.now().toString());
        busyFlightsRequest.setNumberOfPassengers(4);
        busyFlightsRequest.setReturnDate(LocalDate.now().plusDays(1).toString());
    }

    @Test
    public void testBusyFlightsSearchShouldReturnOk() throws Exception {

        mockServer.expect(requestTo(String.format("http://localhost:8080/crazyair?from=%s&to=%s&departureDate=%s&returnDate=%s&passengerCount=%d", "LHR","NYC",LocalDate.now(), LocalDate.now().plusDays(1),Integer.valueOf(4))))
                .andRespond(withSuccess(crazyAirResponseJson(), MediaType.APPLICATION_JSON));
        mockServer.expect(requestTo(String.format("http://localhost:8080/toughjet?from=%s&to=%s&outboundDate=%s&inboundDate=%s&numberOfAdults=%d","LHR","NYC",LocalDate.now(), LocalDate.now().plusDays(1),Integer.valueOf(4))))
                .andRespond(withSuccess(toughJetResponseJson(), MediaType.APPLICATION_JSON));

        this.mockMvc.perform(get("/busyFlights/search?origin={0}&destination={1}&departureDate={2}&returnDate={3}&numberOfPassengers={4}", "LHR","NYC",LocalDate.now(), LocalDate.now().plusDays(1),Integer.valueOf(4)))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        responseJson(toBusyFlightsResponse(crazyAirResponse), toBusyFlightsResponse(toughJetResponse))));
        mockServer.verify();
    }

    @Test
    public void testBusyFlightsSearchShouldReturnOk_WhenAtLeastOneSupplierIsAvailable() throws Exception {

        restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());

        mockServer
                .expect(requestTo((String.format("http://localhost:8080/crazyair?from=%s&to=%s&departureDate=%s&returnDate=%s&passengerCount=%d", "LHR","NYC",LocalDate.now(), LocalDate.now().plusDays(1),Integer.valueOf(4)))))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));
        mockServer.expect(once(), requestTo(String.format("http://localhost:8080/toughjet?from=%s&to=%s&outboundDate=%s&inboundDate=%s&numberOfAdults=%d","LHR","NYC",LocalDate.now(), LocalDate.now().plusDays(1),Integer.valueOf(4))))
                .andRespond(withSuccess(toughJetResponseJson(), MediaType.APPLICATION_JSON));

        this.mockMvc.perform(get("/busyFlights/search?origin={0}&destination={1}&departureDate={2}&returnDate={3}&numberOfPassengers={4}", "LHR","NYC",LocalDate.now(), LocalDate.now().plusDays(1),Integer.valueOf(4)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson(toBusyFlightsResponse(toughJetResponse))));
        mockServer.verify();

    }

    @Test
    public void testBusyFlightsSearchShouldReturnNothing_WhenNoResponseIsReceivedFromSuppliers() throws Exception {

        restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());

        mockServer
                .expect(requestTo(String.format("http://localhost:8080/crazyair?from=%s&to=%s&departureDate=%s&returnDate=%s&passengerCount=%d", "LHR","NYC",LocalDate.now(), LocalDate.now().plusDays(1),Integer.valueOf(4))))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));
        mockServer.expect(once(), requestTo(String.format("http://localhost:8080/toughjet?from=%s&to=%s&outboundDate=%s&inboundDate=%s&numberOfAdults=%d","LHR","NYC",LocalDate.now(), LocalDate.now().plusDays(1),Integer.valueOf(4))))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        this.mockMvc.perform(get("/busyFlights/search?origin={0}&destination={1}&departureDate={2}&returnDate={3}&numberOfPassengers={4}", "LHR","NYC",LocalDate.now(), LocalDate.now().plusDays(1),Integer.valueOf(4)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson()));
        mockServer.verify();
    }

    @Test
    public void testBusyFlightsSearchShouldThrowExceptionForInvalidRequest() throws Exception {
        restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
        this.mockMvc.perform(get("/busyFlights/search?origin={0}&destination={1}&departureDate={2}&returnDate={3}&numberOfPassengers={4}", "London","NYC",LocalDate.now(), LocalDate.now().plusDays(1),Integer.valueOf(4)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    private String responseJson(BusyFlightsResponse... response) throws JsonProcessingException {


        return objectMapper.writeValueAsString(Arrays.asList(response));
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

    private String toughJetResponseJson() throws JsonProcessingException {

        toughJetResponse.setCarrier("Swiss Airways");
        toughJetResponse.setInboundDateTime(LocalDateTime.now());
        toughJetResponse.setBasePrice(200);
        toughJetResponse.setTax(30);
        toughJetResponse.setDiscount(1);
        ArrayList<ToughJetResponse> toughJetResponsesList = new ArrayList<>();
        toughJetResponsesList.add(toughJetResponse);

        return objectMapper.writeValueAsString(toughJetResponsesList);

    }

    private BusyFlightsResponse toBusyFlightsResponse(CrazyAirResponse crazyAirResponse){
        BusyFlightsResponse busyFlightsResponse = modelMapper.map(crazyAirResponse, BusyFlightsResponse.class);
        busyFlightsResponse.setSupplier(SupplierEnum.CRAZY_AIR);
        busyFlightsResponse.setFare(Precision.round(crazyAirResponse.getPrice(), 2));
        if (crazyAirResponse.getArrivalDate() != null)
            busyFlightsResponse.setArrivalDate(crazyAirResponse.getArrivalDate());

        if (crazyAirResponse.getDepartureDate()!= null)
            busyFlightsResponse.setDepartureDate(crazyAirResponse.getDepartureDate());
        return busyFlightsResponse;
    }

    private BusyFlightsResponse toBusyFlightsResponse(ToughJetResponse toughJetResponse){
        BusyFlightsResponse busyFlightsResponse = modelMapper.map(toughJetResponse, BusyFlightsResponse.class);
        busyFlightsResponse.setSupplier(SupplierEnum.TOUGH_JET);
        busyFlightsResponse.setFare(calculateFareFromToughJet(toughJetResponse));
        busyFlightsResponse.setAirline(toughJetResponse.getCarrier());
        if (toughJetResponse.getInboundDateTime() != null)
            busyFlightsResponse.setArrivalDate(toughJetResponse.getInboundDateTime());

        if (toughJetResponse.getOutboundDateTime()!= null)
            busyFlightsResponse.setDepartureDate(toughJetResponse.getOutboundDateTime());
        return busyFlightsResponse;
    }

    private Double calculateFareFromToughJet(ToughJetResponse toughJetResponse){
        return Precision.round(toughJetResponse.getBasePrice() + toughJetResponse.getTax() - toughJetResponse.getDiscount(), 2) ;
    }

    //TODO 1 Write unit test for BusyFlightsController
    //TODO 2 Unit test cases for validation of fields in BusyFlightsRequest
}
