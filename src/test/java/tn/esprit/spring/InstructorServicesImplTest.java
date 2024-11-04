package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Instructor;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IInstructorRepository;
import tn.esprit.spring.services.InstructorServicesImpl;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InstructorServicesImplTest {

    @Mock
    private IInstructorRepository instructorRepository;

    @Mock
    private ICourseRepository courseRepository;

    @InjectMocks
    private InstructorServicesImpl instructorServices;

    private Instructor instructor;
    private Course course;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        instructor = new Instructor(1L, "John", "Doe", LocalDate.of(2020, 1, 1), new HashSet<>());
        course = new Course(1L, 1, null, null, 100.0f, 2, null);
    }

    @Test
    void testAddInstructor() {
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        Instructor savedInstructor = instructorServices.addInstructor(instructor);

        assertNotNull(savedInstructor);
        assertEquals("John", savedInstructor.getFirstName());
        verify(instructorRepository, times(1)).save(instructor);
    }

    @Test
    void testAddInstructorWithNull() {
        assertThrows(IllegalArgumentException.class, () -> instructorServices.addInstructor(null));
        verify(instructorRepository, never()).save(any(Instructor.class));
    }

    @Test
    void testRetrieveAllInstructors() {
        when(instructorRepository.findAll()).thenReturn(Collections.singletonList(instructor));

        List<Instructor> instructors = instructorServices.retrieveAllInstructors();

        assertFalse(instructors.isEmpty());
        assertEquals(1, instructors.size());
        verify(instructorRepository, times(1)).findAll();
    }
    @Test
    void testUpdateInstructorWithNullInstructor() {
        assertThrows(IllegalArgumentException.class, () -> instructorServices.updateInstructor(null), "Expected IllegalArgumentException when instructor is null");
        verify(instructorRepository, never()).existsById(anyLong());
        verify(instructorRepository, never()).save(any(Instructor.class));
    }

    @Test
    void testUpdateInstructorWithNullId() {
        Instructor instructorWithNullId = new Instructor(null, "Jane", "Smith", LocalDate.of(2021, 1, 1), new HashSet<>());

        assertThrows(IllegalArgumentException.class, () -> instructorServices.updateInstructor(instructorWithNullId), "Expected IllegalArgumentException when instructor ID is null");
        verify(instructorRepository, never()).existsById(anyLong());
        verify(instructorRepository, never()).save(any(Instructor.class));
    }


    @Test
    void testRetrieveAllInstructorsEmptyList() {
        when(instructorRepository.findAll()).thenReturn(Collections.emptyList());

        List<Instructor> instructors = instructorServices.retrieveAllInstructors();

        assertTrue(instructors.isEmpty());
        verify(instructorRepository, times(1)).findAll();
    }
    @Test
    void testUpdateInstructor() {
        when(instructorRepository.existsById(1L)).thenReturn(true);
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        Instructor updatedInstructor = instructorServices.updateInstructor(instructor);

        assertNotNull(updatedInstructor);
        assertEquals("Doe", updatedInstructor.getLastName());
        verify(instructorRepository, times(1)).existsById(1L);
        verify(instructorRepository, times(1)).save(instructor);
    }


    @Test
    void testUpdateInstructorWithNull() {
        assertThrows(IllegalArgumentException.class, () -> instructorServices.updateInstructor(null));
        verify(instructorRepository, never()).save(any(Instructor.class));
    }

    @Test
    void testRetrieveInstructor() {
        when(instructorRepository.findById(1L)).thenReturn(Optional.of(instructor));

        Instructor retrievedInstructor = instructorServices.retrieveInstructor(1L);

        assertNotNull(retrievedInstructor);
        assertEquals(1L, retrievedInstructor.getNumInstructor());
        verify(instructorRepository, times(1)).findById(1L);
    }

    @Test
    void testRetrieveInstructorNotFound() {
        when(instructorRepository.findById(2L)).thenReturn(Optional.empty());

        Instructor retrievedInstructor = instructorServices.retrieveInstructor(2L);

        assertNull(retrievedInstructor);
        verify(instructorRepository, times(1)).findById(2L);
    }

    @Test
    void testAddInstructorAndAssignToCourse() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        Instructor result = instructorServices.addInstructorAndAssignToCourse(instructor, 1L);

        assertNotNull(result);
        assertEquals(1, result.getCourses().size());
        assertTrue(result.getCourses().contains(course));
        verify(courseRepository, times(1)).findById(1L);
        verify(instructorRepository, times(1)).save(instructor);
    }

    @Test
    void testAddInstructorAndAssignToNonExistentCourse() {
        // Arrange
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        // Act and Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            instructorServices.addInstructorAndAssignToCourse(instructor, 1L);
        });

        // Verify
        assertEquals("Course with the given ID not found", exception.getMessage());
        verify(courseRepository, times(1)).findById(1L);
        verify(instructorRepository, never()).save(any(Instructor.class));
    }


    @Test
    void testAddInstructorAndAssignToCourseWithNullInstructor() {
        assertThrows(IllegalArgumentException.class, () -> instructorServices.addInstructorAndAssignToCourse(null, 1L));
        verify(courseRepository, never()).findById(anyLong());
        verify(instructorRepository, never()).save(any(Instructor.class));
    }

    @Test
    void testAddInstructorAndAssignToCourseWithNullCourseId() {
        assertThrows(IllegalArgumentException.class, () -> instructorServices.addInstructorAndAssignToCourse(instructor, null));
        verify(courseRepository, never()).findById(anyLong());
        verify(instructorRepository, never()).save(any(Instructor.class));
    }

    @Test
    void testUpdateInstructorNotFound() {
        Instructor nonExistentInstructor = new Instructor(2L, "Jane", "Smith", LocalDate.of(2021, 1, 1), new HashSet<>());

        when(instructorRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> instructorServices.updateInstructor(nonExistentInstructor));
        verify(instructorRepository, never()).save(nonExistentInstructor);
    }
}
