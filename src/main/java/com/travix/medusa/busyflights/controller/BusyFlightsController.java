package com.travix.medusa.busyflights.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsResponse;
import com.travix.medusa.busyflights.service.BusyFlightsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BusyFlightsController {

    @Autowired
    BusyFlightsService busyFlightsService;

    @GetMapping(value = "/busyFlights/search", produces = "application/json")
    public List<BusyFlightsResponse> getFlightsList(BusyFlightsRequest busyFlightsRequest){
        validate(busyFlightsRequest);
        return busyFlightsService.searchFlights(busyFlightsRequest);
    }

    private void validate(final BusyFlightsRequest busyFlightsRequest) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<BusyFlightsRequest>> violations = validator.validate(busyFlightsRequest);

        if(!violations.isEmpty()){
            String message = violations.stream().map(v -> v.getMessage()).collect(Collectors.joining("; "));
            throw new ConstraintViolationException(message, violations);
        }
    }
}