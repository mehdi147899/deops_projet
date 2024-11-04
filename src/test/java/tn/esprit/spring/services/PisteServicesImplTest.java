package tn.esprit.spring.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.entities.Piste;
import tn.esprit.spring.entities.Color;
import tn.esprit.spring.repositories.IPisteRepository;

import java.util.*;

@ExtendWith(MockitoExtension.class)
class PisteServicesImplTest {

    @Mock
    IPisteRepository pisteRepository;

    @InjectMocks
    PisteServicesImpl pisteService;

    @Test
    void retrieveAllPistes_emptyList() {
        Mockito.when(pisteRepository.findAll()).thenReturn(Collections.emptyList());
        List<Piste> results = pisteService.retrieveAllPistes();

        Mockito.verify(pisteRepository, Mockito.times(1)).findAll();
        Assertions.assertTrue(results.isEmpty());
    }

    @Test
    void addPiste() {
        Piste piste = new Piste(1L, "Piste 1", Color.GREEN, 1200, 35, new HashSet<>());

        Mockito.when(pisteRepository.save(piste)).thenReturn(piste);
        Piste result = pisteService.addPiste(piste);

        Mockito.verify(pisteRepository, Mockito.times(1)).save(piste);
        Assertions.assertNotNull(result);
        Assertions.assertEquals("Piste 1", result.getNamePiste());
        Assertions.assertEquals(Color.GREEN, result.getColor());
    }
    @Test
    void retrieveAllPistes() {
        List<Piste> pisteList = Arrays.asList(
                new Piste(1L, "Piste 1", Color.BLUE, 1000, 30, new HashSet<>()),
                new Piste(2L, "Piste 2", Color.RED, 1500, 40, new HashSet<>())
        );

        Mockito.when(pisteRepository.findAll()).thenReturn(pisteList);
        List<Piste> results = pisteService.retrieveAllPistes();

        Mockito.verify(pisteRepository, Mockito.times(1)).findAll();
        Assertions.assertEquals(2, results.size());
    }
    @Test
    void removePiste() {
        Long numPiste = 1L;

        pisteService.removePiste(numPiste);

        Mockito.verify(pisteRepository, Mockito.times(1)).deleteById(numPiste);
    }
    @Test
    void removePiste_nonExistentId() {
        Long nonExistentId = 99L;
        pisteService.removePiste(nonExistentId);
        Mockito.verify(pisteRepository, Mockito.times(1)).deleteById(nonExistentId);
    }
    @Test
    void retrievePiste_notFound() {
        Long nonExistentId = 99L;
        Mockito.when(pisteRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        Piste result = pisteService.retrievePiste(nonExistentId);

        Mockito.verify(pisteRepository, Mockito.times(1)).findById(nonExistentId);
        Assertions.assertNull(result);
    }
    @Test
    void retrievePiste() {
        Long numPiste = 1L;
        Piste piste = new Piste(numPiste, "Piste 1", Color.BLACK, 2000, 45, new HashSet<>());

        Mockito.when(pisteRepository.findById(numPiste)).thenReturn(Optional.of(piste));
        Piste result = pisteService.retrievePiste(numPiste);

        Mockito.verify(pisteRepository, Mockito.times(1)).findById(numPiste);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(numPiste, result.getNumPiste());
    }
}