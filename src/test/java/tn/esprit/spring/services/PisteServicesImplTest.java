package tn.esprit.spring.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.entities.Color;
import tn.esprit.spring.entities.Piste;
import tn.esprit.spring.repositories.IPisteRepository;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PisteServicesImplTest {

    @InjectMocks
    private PisteServicesImpl pisteServices;

    @Mock
    private IPisteRepository pisteRepository;

    private Piste piste;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        piste = new Piste(1L, "Alpine", Color.BLUE, 300, 15, null);
    }

    @Test
    void testRetrieveAllPistes() {
        List<Piste> pistes = new ArrayList<>();
        pistes.add(piste);
        when(pisteRepository.findAll()).thenReturn(pistes);

        List<Piste> retrievedPistes = pisteServices.retrieveAllPistes();
        assertEquals(1, retrievedPistes.size());
        assertEquals(piste.getNamePiste(), retrievedPistes.get(0).getNamePiste());
        verify(pisteRepository, times(1)).findAll();
    }

    @Test
    void testAddPiste() {
        when(pisteRepository.save(any(Piste.class))).thenReturn(piste);

        Piste createdPiste = pisteServices.addPiste(piste);
        assertEquals(piste.getNumPiste(), createdPiste.getNumPiste());
        assertEquals(piste.getNamePiste(), createdPiste.getNamePiste());
        verify(pisteRepository, times(1)).save(any(Piste.class));
    }

    @Test
    void testRemovePiste() {
        Long pisteId = 1L;
        doNothing().when(pisteRepository).deleteById(pisteId);

        pisteServices.removePiste(pisteId);
        verify(pisteRepository, times(1)).deleteById(pisteId);
    }

    @Test
    void testRetrievePiste() {
        Long pisteId = 1L;
        when(pisteRepository.findById(pisteId)).thenReturn(Optional.of(piste));

        Piste retrievedPiste = pisteServices.retrievePiste(pisteId);
        assertNotNull(retrievedPiste);
        assertEquals(piste.getNamePiste(), retrievedPiste.getNamePiste());
        verify(pisteRepository, times(1)).findById(pisteId);
    }

    @Test
    void testRetrievePisteNotFound() {
        Long pisteId = 2L;
        when(pisteRepository.findById(pisteId)).thenReturn(Optional.empty());

        Piste retrievedPiste = pisteServices.retrievePiste(pisteId);
        assertNull(retrievedPiste);
        verify(pisteRepository, times(1)).findById(pisteId);
    }
}