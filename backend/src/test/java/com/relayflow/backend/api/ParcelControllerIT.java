package com.relayflow.backend.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.relayflow.backend.api.dto.UpdateParcelStatusRequest;
import com.relayflow.backend.domain.Parcel;
import com.relayflow.backend.domain.ParcelStatus;
import com.relayflow.backend.repository.ParcelRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


//Ici on lance Spring mais pas un vrai serveur : MockMvc.
//On va mocker la DB via @MockBean ParcelRepositor
@SpringBootTest
@AutoConfigureMockMvc
public class ParcelControllerIT {
    @Autowired
    MockMvc mvc ;
    //ici on n'a pas mockeé le service:
    //ici le but de notre test d'integretion est le test d'integration API dans notre cas et non pas on teste uniquement le controller
    //pour info deux types de test différents
    //1-test unitaire controller (isolé)  on mock le service  controller->mock service
    //2-Test d’intégration API (ce qu’on fait ici) : on teste Controller + Service + logique métie
    //controller-service->mock repository
    //si je mcoke le service dans notre cas , o ne teste plus les régles métier, les exceptions , les transititos , le service loic
    //je teste ainsi seulement controller appelle service  ça sera trop faible comme test
    //donc test unitaire controller ->@WebMvcTest(Controller.class) @Mockbean Service : testse seulement routing+mapping JSON
    //et pour test integration api : @SpringBootTest @Mockbean repo  but tester controller+service+domain ensemble
     @MockitoBean  ParcelRepository repo ;

    @Autowired
    ObjectMapper om;
    //ObjectMapper = cnvertisseur Java <-> JSON
    //elle sert à java ->Json String json = objectMapper.writeValueAsString(obj)
    //JSON ->Java
    //Parcel p = ojectMapper.readValue(jsonParcel.class)
    //pk spring l'a déjà : dans mon pom.xml spring boot starter web ce starter invlut Jackson automatiquement , Spring créé un ObjectMapper déjà configuré
    //donc je peux l'injecter directement
    //dans les test api , on doit envoyer du json
    //sans ObjecttMapper je devrais ecrire "{\"status\":\"IN_TRANSIT\"}":horrible
    //avec ObjectMapper  om.writeValueAsString(dto) propre , sur , automatique

@Test
    void getByReference_should_return_200() throws Exception {
    Parcel p = Parcel.create("REF-017");
    when(repo.findByReference("REF-017")).thenReturn(Optional.of(p));
     mvc.perform(get("/api/parcels/by-reference/REF-017"))
             .andExpect(status().isOk()).andExpect(jsonPath("$.reference").value("REF-017"))
             .andExpect(jsonPath("$.status").value("CREATED"));

}
@Test
    void getByReference_should_return_404() throws Exception {
    when (repo.findByReference("REF-X")).thenReturn(Optional.empty());
    mvc.perform(get("/api/parcels/by-reference/REF-X"))
            .andExpect(status().isNotFound());

}

@Test
    void patchStatus_should_return_200_when_is_valid() throws Exception {
    Parcel p = Parcel.create("REF-018");
    when(repo.findByReference("REF-018")).thenReturn(Optional.of(p));
    String body = om.writeValueAsString(new UpdateParcelStatusRequest(ParcelStatus.IN_TRANSIT));
mvc.perform(patch("/api/parcels/REF-018/status")
        .contentType(MediaType.APPLICATION_JSON)
        .content(body))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("IN_TRANSIT"));

}

@Test
    void patchStatus_should_return_400_when_invalid_transition() throws Exception {
    Parcel p = Parcel.create("REF-019");//CREATED
    when(repo.findByReference("REF-019")).thenReturn(Optional.of(p));
    //invalid : CREATED ->DELIVERED
    String body = om.writeValueAsString(new UpdateParcelStatusRequest(ParcelStatus.DELIVERED));
    mvc.perform(patch("/api/parcels/REF-019/status")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body))
            .andExpect(status().isBadRequest());

}
    @Test
    void patchStatus_should_return_404_when_not_found() throws Exception {

        when(repo.findByReference("REF-020")).thenReturn(Optional.empty());

        String body = om.writeValueAsString(new UpdateParcelStatusRequest(ParcelStatus.IN_TRANSIT));
        mvc.perform(patch("/api/parcels/REF-020/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound());

}
}
