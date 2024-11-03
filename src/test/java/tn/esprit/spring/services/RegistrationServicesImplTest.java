package tn.esprit.spring.services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Registration;
import tn.esprit.spring.entities.Skier;
import tn.esprit.spring.entities.TypeCourse;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IRegistrationRepository;
import tn.esprit.spring.repositories.ISkierRepository;

import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class RegistrationServicesImplTest {

    @Mock
    private IRegistrationRepository registrationRepository;

    @Mock
    private ISkierRepository skierRepository;

    @Mock
    private ICourseRepository courseRepository;

    @InjectMocks
    private RegistrationServicesImpl registrationServices;

    private Skier skier;
    private Course course;


    @BeforeEach
    void setUp() {
        skier = new Skier();
        skier.setNumSkier(1L);
        skier.setDateOfBirth(LocalDate.of(2005, 1, 1)); // Example date for age calculation

        course = new Course();
        course.setNumCourse(1L);
        course.setTypeCourse(TypeCourse.INDIVIDUAL); // Set a default TypeCourse to avoid NullPointerException

    }

    @Test
    void testAddRegistrationAndAssignToSkier_SkierDoesNotExist() {
        when(skierRepository.findById(1L)).thenReturn(Optional.empty());

        Registration result = registrationServices.addRegistrationAndAssignToSkierAndCourse(new Registration(), 1L, 1L);
        assertNull(result, "Result should be null if skier does not exist");

        verify(skierRepository).findById(1L);
        verify(courseRepository, never()).findById(anyLong());
        verify(registrationRepository, never()).save(any(Registration.class));
    }

    @Test
    void testAddRegistrationAndAssignToSkier_CourseDoesNotExist() {
        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        Registration result = registrationServices.addRegistrationAndAssignToSkierAndCourse(new Registration(), 1L, 1L);
        assertNull(result, "Result should be null if course does not exist");

        verify(skierRepository).findById(1L);
        verify(courseRepository).findById(1L);
        verify(registrationRepository, never()).save(any(Registration.class));
    }
}
