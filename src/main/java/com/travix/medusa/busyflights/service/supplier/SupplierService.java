package com.travix.medusa.busyflights.service.supplier;

import java.util.List;
import java.util.stream.Collectors;

import com.travix.medusa.busyflights.domain.SupplierEnum;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsResponse;

public abstract class SupplierService<T, R> {


    public List<BusyFlightsResponse> searchFlights(final BusyFlightsRequest request) {

        List<R> supplierResponse = search(toSupplierRequest(request));
        return supplierResponse.stream().map(y -> toBusyFlightsResponse(y)).collect(Collectors.toList());

    }

    public abstract SupplierEnum supplier();

    protected abstract List<R> search(T request);

    protected abstract T toSupplierRequest(BusyFlightsRequest busyFlightsRequest);

    protected abstract BusyFlightsResponse toBusyFlightsResponse(R supplierResponse);

}
