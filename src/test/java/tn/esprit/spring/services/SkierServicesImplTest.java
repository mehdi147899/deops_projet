package tn.esprit.spring.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.entities.Skier;
import tn.esprit.spring.entities.Subscription;
import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.repositories.ISkierRepository;
import tn.esprit.spring.repositories.ISubscriptionRepository;
import org.junit.runner.RunWith;
import java.time.LocalDate;
import java.util.Optional;



import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SkierServicesImplTest {

    @Mock
    private ISkierRepository skierRepository;

    @Mock
    private ISubscriptionRepository subscriptionRepository;

    @InjectMocks
    private SkierServicesImpl skierServices;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddSkier() {
        // Arrange
        Skier skier = new Skier();
        Subscription subscription = new Subscription();
        subscription.setTypeSub(TypeSubscription.MONTHLY);  // Définition du type de souscription
        subscription.setStartDate(LocalDate.now());
        skier.setSubscription(subscription);

        // Configuration du mock pour retourner le Skier lors de l'appel à save
        when(skierRepository.save(any(Skier.class))).thenReturn(skier);

        // Act
        Skier result = skierServices.addSkier(skier);

        // Assert
        assertNotNull(result, "Le Skier retourné ne doit pas être null.");
        assertEquals(skier, result, "Le Skier retourné doit correspondre au Skier ajouté.");
        assertEquals(skier.getSubscription().getStartDate().plusMonths(1), skier.getSubscription().getEndDate(),
                "La date de fin d'abonnement devrait être un mois après la date de début.");
        verify(skierRepository, times(1)).save(skier);
    }


    @Test
    public void testRemoveSkier() {
        // Arrange
        Long skierId = 1L;
        doNothing().when(skierRepository).deleteById(skierId);

        // Act
        skierServices.removeSkier(skierId);

        // Assert
        verify(skierRepository, times(1)).deleteById(skierId);
    }

    @Test
    public void testRetrieveSkier_SkierExists() {
        // Arrange
        Long skierId = 1L;
        Skier skier = new Skier();
        when(skierRepository.findById(skierId)).thenReturn(Optional.of(skier));

        // Act
        Skier result = skierServices.retrieveSkier(skierId);

        // Assert
        assertNotNull(result);
        assertEquals(skier, result);
        verify(skierRepository, times(1)).findById(skierId);
    }

    @Test
    public void testRetrieveSkier_SkierDoesNotExist() {
        // Arrange
        Long skierId = 1L;
        when(skierRepository.findById(skierId)).thenReturn(Optional.empty());

        // Act
        Skier result = skierServices.retrieveSkier(skierId);

        // Assert
        assertNull(result);
        verify(skierRepository, times(1)).findById(skierId);
    }
}

