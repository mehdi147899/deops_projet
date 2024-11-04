package tn.esprit.spring.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.Piste;
import tn.esprit.spring.repositories.IPisteRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PisteServicesImplTest {

    @Mock
    private IPisteRepository pisteRepository;

    @InjectMocks
    private PisteServicesImpl pisteServices;

    private Piste piste;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        piste = new Piste();
        piste.setNumPiste(1L);
        piste.setNamePiste("Alpine Run");
        piste.setLength(1500);
        piste.setSlope(20);
    }

    @Test
    void testRetrieveAllPistes() {
        when(pisteRepository.findAll()).thenReturn(Collections.singletonList(piste));

        List<Piste> pistes = pisteServices.retrieveAllPistes();

        assertFalse(pistes.isEmpty());
        assertEquals(1, pistes.size());
        verify(pisteRepository, times(1)).findAll();
    }

    @Test
    void testRetrieveAllPistesEmptyList() {
        when(pisteRepository.findAll()).thenReturn(Collections.emptyList());

        List<Piste> pistes = pisteServices.retrieveAllPistes();

        assertTrue(pistes.isEmpty());
        verify(pisteRepository, times(1)).findAll();
    }

    @Test
    void testAddPiste() {
        when(pisteRepository.save(any(Piste.class))).thenReturn(piste);

        Piste savedPiste = pisteServices.addPiste(piste);

        assertNotNull(savedPiste);
        assertEquals("Alpine Run", savedPiste.getNamePiste());
        verify(pisteRepository, times(1)).save(piste);
    }



    @Test
    void testRetrievePiste() {
        when(pisteRepository.findById(1L)).thenReturn(Optional.of(piste));

        Piste retrievedPiste = pisteServices.retrievePiste(1L);

        assertNotNull(retrievedPiste);
        assertEquals(1L, retrievedPiste.getNumPiste());
        verify(pisteRepository, times(1)).findById(1L);
    }

    @Test
    void testRetrievePisteNotFound() {
        when(pisteRepository.findById(2L)).thenReturn(Optional.empty());

        Piste retrievedPiste = pisteServices.retrievePiste(2L);

        assertNull(retrievedPiste);
        verify(pisteRepository, times(1)).findById(2L);
    }

    @Test
    void testRemovePiste() {
        Long numPiste = 1L;
        doNothing().when(pisteRepository).deleteById(numPiste);

        pisteServices.removePiste(numPiste);

        verify(pisteRepository, times(1)).deleteById(numPiste);
    }


}