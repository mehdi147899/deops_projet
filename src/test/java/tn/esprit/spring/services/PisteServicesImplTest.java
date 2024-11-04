package tn.esprit.spring.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.Piste;
import tn.esprit.spring.repositories.IPisteRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PisteServicesImplTest {

    @Mock
    private IPisteRepository pisteRepository;

    @InjectMocks
    private PisteServicesImpl pisteServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRetrieveAllPistes() {
        Piste piste1 = new Piste();
        Piste piste2 = new Piste();
        List<Piste> pistes = Arrays.asList(piste1, piste2);

        when(pisteRepository.findAll()).thenReturn(pistes);

        List<Piste> result = pisteServices.retrieveAllPistes();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(pisteRepository, times(1)).findAll();
    }

    @Test
    void testAddPiste() {
        Piste piste = new Piste();
        when(pisteRepository.save(piste)).thenReturn(piste);

        Piste result = pisteServices.addPiste(piste);

        assertNotNull(result);
        verify(pisteRepository, times(1)).save(piste);
    }

    @Test
    void testRemovePiste() {
        Long numPiste = 1L;

        doNothing().when(pisteRepository).deleteById(numPiste);

        pisteServices.removePiste(numPiste);

        verify(pisteRepository, times(1)).deleteById(numPiste);
    }

    @Test
    void testRetrievePiste() {
        Long numPiste = 1L;
        Piste piste = new Piste();
        when(pisteRepository.findById(numPiste)).thenReturn(Optional.of(piste));

        Piste result = pisteServices.retrievePiste(numPiste);

        assertNotNull(result);
        verify(pisteRepository, times(1)).findById(numPiste);
    }

    @Test
    void testRetrievePiste_NotFound() {
        Long numPiste = 1L;
        when(pisteRepository.findById(numPiste)).thenReturn(Optional.empty());

        Piste result = pisteServices.retrievePiste(numPiste);

        assertNull(result);
        verify(pisteRepository, times(1)).findById(numPiste);
    }
}
