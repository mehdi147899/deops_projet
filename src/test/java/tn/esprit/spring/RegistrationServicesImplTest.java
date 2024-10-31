package tn.esprit.spring;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.TypeCourse;
import tn.esprit.spring.entities.Registration;
import tn.esprit.spring.entities.Skier;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IRegistrationRepository;
import tn.esprit.spring.repositories.ISkierRepository;
import tn.esprit.spring.services.RegistrationServicesImpl;

class RegistrationServicesImplTest {

    @Mock
    private IRegistrationRepository registrationRepository;

    @Mock
    private ISkierRepository skierRepository;

    @Mock
    private ICourseRepository courseRepository;

    @InjectMocks
    private RegistrationServicesImpl registrationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddRegistrationAndAssignToSkierAndCourse_SuccessfulAdultRegistration() {
        // Arrange
        Registration registration = new Registration();
        registration.setNumWeek(1);

        Skier skier = new Skier();
        skier.setNumSkier(1L);
        skier.setDateOfBirth(LocalDate.of(1990, 1, 1)); // Adult

        Course course = new Course();
        course.setNumCourse(1L);
        course.setTypeCourse(TypeCourse.COLLECTIVE_ADULT);

        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(registrationRepository.countDistinctByNumWeekAndSkier_NumSkierAndCourse_NumCourse(1, 1L, 1L)).thenReturn(0L);
        when(registrationRepository.countByCourseAndNumWeek(course, 1)).thenReturn(2L); // Use Long value

        // Act
        Registration result = registrationService.addRegistrationAndAssignToSkierAndCourse(registration, 1L, 1L);

        // Assert
        assertNotNull(result);
        verify(registrationRepository).save(registration);
    }

    @Test
    void testAddRegistrationAndAssignToSkierAndCourse_FullCourse() {
        // Arrange
        Registration registration = new Registration();
        registration.setNumWeek(1);

        Skier skier = new Skier();
        skier.setNumSkier(1L);
        skier.setDateOfBirth(LocalDate.of(2010, 1, 1)); // Child

        Course course = new Course();
        course.setNumCourse(1L);
        course.setTypeCourse(TypeCourse.COLLECTIVE_CHILDREN);

        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(registrationRepository.countDistinctByNumWeekAndSkier_NumSkierAndCourse_NumCourse(1, 1L, 1L)).thenReturn(0L);
        when(registrationRepository.countByCourseAndNumWeek(course, 1)).thenReturn(6L); // Full course with Long value

        // Act
        Registration result = registrationService.addRegistrationAndAssignToSkierAndCourse(registration, 1L, 1L);

        // Assert
        assertNull(result);
        verify(registrationRepository, never()).save(any());
    }

    @Test
    void testAddRegistrationAndAssignToSkierAndCourse_DuplicateRegistration() {
        // Arrange
        Registration registration = new Registration();
        registration.setNumWeek(1);

        Skier skier = new Skier();
        skier.setNumSkier(1L);

        Course course = new Course();
        course.setNumCourse(1L);

        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(registrationRepository.countDistinctByNumWeekAndSkier_NumSkierAndCourse_NumCourse(1, 1L, 1L)).thenReturn(1L); // Duplicate with Long value

        // Act
        Registration result = registrationService.addRegistrationAndAssignToSkierAndCourse(registration, 1L, 1L);

        // Assert
        assertNull(result);
        verify(registrationRepository, never()).save(any());
    }

    @Test
    void testAddRegistrationAndAssignToSkierAndCourse_InvalidAgeForChildrenCourse() {
        // Arrange
        Registration registration = new Registration();
        registration.setNumWeek(1);

        Skier skier = new Skier();
        skier.setNumSkier(1L);
        skier.setDateOfBirth(LocalDate.of(1990, 1, 1)); // Adult

        Course course = new Course();
        course.setNumCourse(1L);
        course.setTypeCourse(TypeCourse.COLLECTIVE_CHILDREN);

        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(registrationRepository.countDistinctByNumWeekAndSkier_NumSkierAndCourse_NumCourse(1, 1L, 1L)).thenReturn(0L);

        // Act
        Registration result = registrationService.addRegistrationAndAssignToSkierAndCourse(registration, 1L, 1L);

        // Assert
        assertNull(result);
        verify(registrationRepository, never()).save(any());
    }

    @Test
    void testAddRegistrationAndAssignToSkierAndCourse_NullInputs() {
        // Act & Assert
        assertNull(registrationService.addRegistrationAndAssignToSkierAndCourse(null, 1L, 1L));
        assertNull(registrationService.addRegistrationAndAssignToSkierAndCourse(new Registration(), null, 1L));
        assertNull(registrationService.addRegistrationAndAssignToSkierAndCourse(new Registration(), 1L, null));
    }
}
