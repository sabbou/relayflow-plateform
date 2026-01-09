package com.relayflow.backend.api;


import com.relayflow.backend.api.dto.CreateParcelRequest;
import com.relayflow.backend.api.dto.ParcelResponse;
import com.relayflow.backend.domain.Parcel;
import com.relayflow.backend.repository.ParcelRepository;
import com.relayflow.backend.service.ParcelService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/parcels")
public class ParcelController {
    private final ParcelService parcelService;
    public ParcelController(ParcelService parcelService) {
        this.parcelService = parcelService;
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParcelResponse createParcel(@RequestBody @Valid  CreateParcelRequest req) {
        return parcelService.create(req);
    }
    @GetMapping("/{id}")
    public ParcelResponse getParcelById(@PathVariable UUID id) {
        return parcelService.getById(id);
    }

}
