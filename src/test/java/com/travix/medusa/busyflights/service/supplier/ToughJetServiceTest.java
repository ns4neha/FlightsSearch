package com.travix.medusa.busyflights.service.supplier;

import static org.springframework.test.web.client.ExpectedCount.once;
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
import com.travix.medusa.busyflights.domain.toughjet.ToughJetResponse;
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
@SpringBootTest(classes = {ToughJetService.class})
@ContextConfiguration(classes = Config.class)
public class ToughJetServiceTest {

    @Autowired
    ToughJetService toughJetService;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ModelMapper modelMapper;

    private MockRestServiceServer mockServer;
    private BusyFlightsRequest busyFlightsRequest;
    private ToughJetResponse toughJetResponse;
    private ObjectMapper objectMapper ;

    @Before
    public void setUp() {

        mockServer = MockRestServiceServer.bindTo(restTemplate).ignoreExpectOrder(true).build();
        objectMapper = new ObjectMapper();
        toughJetResponse = new ToughJetResponse();
        busyFlightsRequest = new BusyFlightsRequest();
        busyFlightsRequest.setOrigin("LHR");
        busyFlightsRequest.setDestination("NYC");
        busyFlightsRequest.setDepartureDate(LocalDate.now().toString());
        busyFlightsRequest.setNumberOfPassengers(4);
        busyFlightsRequest.setReturnDate(LocalDate.now().plusDays(1).toString());
    }

    @Test
    public void testCrazyAirFlightsSearch_SingleResponse_OK() throws Exception {

        mockServer.expect(once(), requestTo(String.format("http://localhost:8080/toughjet?from=%s&to=%s&outboundDate=%s&inboundDate=%s&numberOfAdults=%d","LHR","NYC",LocalDate.now(), LocalDate.now().plusDays(1),Integer.valueOf(4))))
                .andRespond(withSuccess(toughJetResponseJson(), MediaType.APPLICATION_JSON));

        final List<BusyFlightsResponse> busyFlightsResponses =
                toughJetService.searchFlights(busyFlightsRequest);
        mockServer.verify();
        Assert.assertEquals(1, busyFlightsResponses.size());
        Assert.assertEquals(toughJetResponse.getCarrier(), busyFlightsResponses.get(0).getAirline());
        Assert.assertTrue(toughJetResponse.getBasePrice() + toughJetResponse.getTax() - toughJetResponse.getDiscount() == busyFlightsResponses.get(0).getFare());
        Assert.assertEquals(toughJetResponse.getInboundDateTime(), busyFlightsResponses.get(0).getArrivalDate());
        Assert.assertEquals(toughJetResponse.getDepartureAirportName(), busyFlightsResponses.get(0).getDepartureAirportCode());
        Assert.assertEquals(toughJetResponse.getOutboundDateTime(), busyFlightsResponses.get(0).getDepartureDate());
        Assert.assertEquals(toughJetResponse.getArrivalAirportName(), busyFlightsResponses.get(0).getDestinationAirportCode());
        Assert.assertEquals(SupplierEnum.TOUGH_JET, busyFlightsResponses.get(0).getSupplier());

    }

    @Test
    public void testCrazyAirFlightsSearch_NoResponse() throws Exception {

        mockServer.expect(once(), requestTo(String.format("http://localhost:8080/toughjet?from=%s&to=%s&outboundDate=%s&inboundDate=%s&numberOfAdults=%d","LHR","NYC",LocalDate.now(), LocalDate.now().plusDays(1),Integer.valueOf(4))))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        final List<BusyFlightsResponse> busyFlightsResponses =
                toughJetService.searchFlights(busyFlightsRequest);
        mockServer.verify();
        Assert.assertEquals(0, busyFlightsResponses.size());
    }


    private String toughJetResponseJson() throws JsonProcessingException {

        toughJetResponse.setCarrier("Swiss Airways");
        toughJetResponse.setInboundDateTime(LocalDateTime.now());
        toughJetResponse.setBasePrice(200);
        toughJetResponse.setTax(30);
        toughJetResponse.setDiscount(1);
        toughJetResponse.setArrivalAirportName("NYC");
        toughJetResponse.setDepartureAirportName("LHR");
        toughJetResponse.setOutboundDateTime(LocalDateTime.now());
        ArrayList<ToughJetResponse> toughJetResponsesList = new ArrayList<>();
        toughJetResponsesList.add(toughJetResponse);

        return objectMapper.writeValueAsString(toughJetResponsesList);

    }
}
