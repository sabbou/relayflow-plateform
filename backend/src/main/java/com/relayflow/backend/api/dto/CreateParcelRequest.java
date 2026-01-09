package com.relayflow.backend.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateParcelRequest(@NotBlank
                                    @Size(max = 50)
                                    String reference ) {

}
