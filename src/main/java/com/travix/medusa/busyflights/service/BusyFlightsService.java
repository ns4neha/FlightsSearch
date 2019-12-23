package com.travix.medusa.busyflights.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsResponse;
import com.travix.medusa.busyflights.service.supplier.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BusyFlightsService {

    @Autowired
    List<SupplierService> supplierServiceList;

    public List<BusyFlightsResponse> searchFlights(final BusyFlightsRequest busyFlightsRequest){

        Supplier<List<BusyFlightsResponse>> supplier = ArrayList::new;
        BiConsumer<List<BusyFlightsResponse>, SupplierService> accumulator =
                (List<BusyFlightsResponse> a, SupplierService i) -> {
                    a.addAll(i.searchFlights(busyFlightsRequest));
                };

        BiConsumer<List<BusyFlightsResponse>, List<BusyFlightsResponse>> combiner =
                (a1, a2) -> {
                    a1.addAll(a2);
                };


        final List<BusyFlightsResponse> busyFlightsResponses =
                supplierServiceList.parallelStream().collect(supplier, accumulator, combiner);
        Collections.sort(busyFlightsResponses, Comparator.comparingDouble(BusyFlightsResponse::getFare));
        return busyFlightsResponses;
    }
}
