package com.relayflow.backend.service;

import com.relayflow.backend.api.dto.CreateParcelRequest;
import com.relayflow.backend.api.dto.ParcelResponse;
import com.relayflow.backend.domain.Parcel;
import com.relayflow.backend.domain.ParcelStatus;
import com.relayflow.backend.repository.ParcelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
//Ici on teste le service avec repo mocké : pas de DB
public class ParcelServiceTest {
    private ParcelRepository repo ;
    private ParcelService service;
    //remarque au lieu d'utilise @before each setuo on utilise ça
    //@ExtendWith(MockitoExtension.class)
    //class ParcelServiceTest {
    //
    //  @Mock
    //  ParcelRepository repo;
    //
    //  @InjectMocks
    //  ParcelService service;
    //Là, Mockito crée service et injecte repo automatiquement.(plus recommandé)
    @BeforeEach//init manuel
    void setUp(){
        repo=mock(ParcelRepository.class);
        service = new ParcelService(repo);
    }
    @Test
    void create_should_throw_when_reference_exists(){
        when (repo.existsByReference("REF-012")).thenReturn(true);
        assertThrows(DuplicateReferenceException.class,()->service.create(new CreateParcelRequest("REF-012")));
    }
    @Test
    void create_should_save_and_return_response() {
        when(repo.existsByReference("REF-013")).thenReturn(false);
        Parcel saved = Parcel.create("REF-013");
        when(repo.save(any(Parcel.class))).thenReturn(saved);
        ParcelResponse res =service.create(new CreateParcelRequest("REF-013"));
assertEquals("REF-013" , res.reference());
assertEquals(ParcelStatus.CREATED ,res.status());
ArgumentCaptor<Parcel> captor =ArgumentCaptor.forClass(Parcel.class);
verify(repo).save(captor.capture());
        assertEquals("REF-013",captor.getValue().getReference());
    }
    @Test
    void changStatus_should_throw_not_found(){
        when(repo.existsByReference("REF-014")).thenReturn(false);
    assertThrows(ParcelNotFoundException.class,()->service.changeStatus("REF-404", ParcelStatus.IN_TRANSIT));
    }
    @Test
    void changStatus_should_throw_invalid_transition() {
        Parcel p = Parcel.create("REF-015");
        when(repo.findByReference("REF-015")).thenReturn(Optional.of(p));
        assertThrows(InvalidStatusTransitionException.class,
                () -> service.changeStatus("REF-015", ParcelStatus.DELIVERED));
    }
    @Test
    void changeStatus_should_update_status_when_valid() {
        Parcel p = Parcel.create("REF-016"); // CREATED
        when(repo.findByReference("REF-016")).thenReturn(Optional.of(p));

        ParcelResponse res = service.changeStatus("REF-016", ParcelStatus.IN_TRANSIT);

        assertEquals(ParcelStatus.IN_TRANSIT, res.status());
    }
    }


