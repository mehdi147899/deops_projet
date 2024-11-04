package tn.esprit.spring.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repositories.*;

class SkierServicesImplTest {

    @Mock
    private ISkierRepository skierRepository;

    @Mock
    private IPisteRepository pisteRepository;

    @Mock
    private ICourseRepository courseRepository;

    @Mock
    private IRegistrationRepository registrationRepository;

    @Mock
    private ISubscriptionRepository subscriptionRepository;

    @InjectMocks
    private SkierServicesImpl skierService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAssignSkierToSubscription() {
        // Arrange
        Long skierId = 1L;
        Long subscriptionId = 1L;
        Skier skier = new Skier();
        Subscription subscription = new Subscription();

        when(skierRepository.findById(skierId)).thenReturn(Optional.of(skier));
        when(subscriptionRepository.findById(subscriptionId)).thenReturn(Optional.of(subscription));
        when(skierRepository.save(any(Skier.class))).thenReturn(skier);  // Mock save to return skier

        // Act
        Skier result = skierService.assignSkierToSubscription(skierId, subscriptionId);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(subscription, result.getSubscription(), "Subscription should be assigned to skier");
        verify(skierRepository).save(skier);
    }

    @Test
    void testAddSkierAndAssignToCourse() {
        // Arrange
        Skier skier = new Skier();
        Long courseId = 1L;
        Course course = new Course();
        Registration registration = new Registration();
        skier.setRegistrations(Set.of(registration));

        when(courseRepository.getById(courseId)).thenReturn(course);
        when(skierRepository.save(any(Skier.class))).thenReturn(skier);
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        // Act
        Skier result = skierService.addSkierAndAssignToCourse(skier, courseId);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(course, registration.getCourse(), "Course should be assigned to registration");
        assertEquals(skier, registration.getSkier(), "Skier should be assigned to registration");
        verify(skierRepository).save(skier);
        verify(registrationRepository).save(registration);
    }

    @Test
    void testAssignSkierToPiste() {
        // Arrange
        Long skierId = 1L;
        Long pisteId = 1L;
        Skier skier = new Skier();
        Piste piste = new Piste();
        skier.setPistes(new HashSet<>());

        when(skierRepository.findById(skierId)).thenReturn(Optional.of(skier));
        when(pisteRepository.findById(pisteId)).thenReturn(Optional.of(piste));
        when(skierRepository.save(any(Skier.class))).thenReturn(skier);  // Mock save to return skier

        // Act
        Skier result = skierService.assignSkierToPiste(skierId, pisteId);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertNotNull(result.getPistes(), "Skier's pistes set should not be null");
        assertTrue(result.getPistes().contains(piste), "Piste should be added to skier's pistes");
        verify(skierRepository).save(skier);
    }

    @Test
    void testRetrieveSkiersBySubscriptionType() {
        // Arrange
        TypeSubscription type = TypeSubscription.ANNUAL;
        List<Skier> skiers = List.of(new Skier(), new Skier());

        when(skierRepository.findBySubscription_TypeSub(type)).thenReturn(skiers);

        // Act
        List<Skier> result = skierService.retrieveSkiersBySubscriptionType(type);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(skiers.size(), result.size(), "Returned list size should match mocked list size");
        assertEquals(skiers, result, "Returned list should match the mocked list");
    }

    @Test
    void testAssignSkierToSubscriptionNotFound() {
        // Arrange
        Long skierId = 1L;
        Long subscriptionId = 1L;

        when(skierRepository.findById(skierId)).thenReturn(Optional.empty());
        when(subscriptionRepository.findById(subscriptionId)).thenReturn(Optional.of(new Subscription()));

        // Act & Assert
        assertThrows(NullPointerException.class, 
            () -> skierService.assignSkierToSubscription(skierId, subscriptionId), 
            "Should throw NullPointerException if skier is not found"
        );
    }
}
