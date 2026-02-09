package com.relayflow.backend.domain;

import com.relayflow.backend.service.InvalidStatusTransitionException;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
//Ce test n’utilise ni Spring ni DB. Il valide juste ton métier
public class ParcelDomainTest {
    @Test
    void create_should_initialize_defaults(){
        Parcel p= Parcel.create("REF-006");
        assertNotNull(p.getCreatedAt());
        assertNotNull(p.getUpdatedAt());
        assertEquals(ParcelStatus.CREATED,p.getStatus());
        assertEquals("REF-006",p.getReference());
    }
    @Test
    void chaneStatus_should_allow_CREATED_to_IN_TRANSIT(){
        Parcel p= Parcel.create("REF-007");
        Instant before=p.getUpdatedAt();
        p.changeStatus(ParcelStatus.IN_TRANSIT);
        assertEquals(ParcelStatus.IN_TRANSIT,p.getStatus());
        assertTrue(!p.getUpdatedAt().isBefore(before));    }
    @Test
    void chaneStatus_should_allow_IN_TRANSIT_toARRIVED_at_RELAY() {
        Parcel p = Parcel.create("REF-008");
        p.changeStatus(ParcelStatus.IN_TRANSIT);
        p.changeStatus(ParcelStatus.ARRIVED_AT_RELAY);
        assertEquals(ParcelStatus.ARRIVED_AT_RELAY, p.getStatus());
    }

    @Test
    void chaneStatus_should_allow_ARRIVED_at_RELAY_to_DELIVERED() {
        Parcel p = Parcel.create("REF-009");
        p.changeStatus(ParcelStatus.IN_TRANSIT);
        p.changeStatus(ParcelStatus.ARRIVED_AT_RELAY);
        p.changeStatus(ParcelStatus.DELIVERED);
        assertEquals(ParcelStatus.DELIVERED, p.getStatus());
    }
@Test
    void changeStatus_should_reject_invalid_transition(){
        Parcel p= Parcel.create("REF-010");
        assertThrows(InvalidStatusTransitionException.class,()->p.changeStatus(ParcelStatus.DELIVERED));
}
@Test
    void changeStatus_should_reject_any_transition_from_DELIVERED(){
        Parcel p= Parcel.create("REF-011");
        p.changeStatus(ParcelStatus.IN_TRANSIT);
        p.changeStatus(ParcelStatus.ARRIVED_AT_RELAY);
        p.changeStatus(ParcelStatus.DELIVERED);
        assertThrows(InvalidStatusTransitionException.class,()->p.changeStatus(ParcelStatus.IN_TRANSIT));
}
    }
