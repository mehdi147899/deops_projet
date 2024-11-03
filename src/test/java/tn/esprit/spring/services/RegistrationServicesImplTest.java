package tn.esprit.spring.services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.Registration;
import tn.esprit.spring.entities.Skier;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IRegistrationRepository;
import tn.esprit.spring.repositories.ISkierRepository;

import java.util.Optional;

class RegistrationServicesImplTest {

    @Mock
    private IRegistrationRepository registrationRepository;
    @Mock
    private ISkierRepository skierRepository;
    @Mock
    private ICourseRepository courseRepository;

    @InjectMocks
    private RegistrationServicesImpl registrationServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testAddRegistrationAndAssignToSkier_SkierExists() {
        Registration registration = new Registration();
        Skier skier = new Skier();
        skier.setNumSkier(1L);
        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        Registration result = registrationServices.addRegistrationAndAssignToSkier(new Registration(), 1L);
        assertNotNull(result.getSkier(), "Skier should be assigned");
        assertEquals(1L, result.getSkier().getNumSkier(), "Skier ID should match");
    }

    @Test
    void testAddRegistrationAndAssignToSkier_SkierDoesNotExist() {
        when(skierRepository.findById(anyLong())).thenReturn(Optional.empty());

        Registration result = registrationServices.addRegistrationAndAssignToSkier(new Registration(), 1L);
        assertNull(result, "Result should be null if skier does not exist");
    }

}
