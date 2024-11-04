package tn.esprit.spring.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Instructor;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IInstructorRepository;

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        instructor = new Instructor(1L, "John", "Doe", LocalDate.of(2020, 1, 1), new HashSet<>());
    }

    @Test
    void testAddInstructor() {
        when(instructorRepository.save(instructor)).thenReturn(instructor);

        Instructor savedInstructor = instructorServices.addInstructor(instructor);

        assertNotNull(savedInstructor);
        assertEquals(instructor.getFirstName(), savedInstructor.getFirstName());
        verify(instructorRepository, times(1)).save(instructor);
    }

    @Test
    void testRetrieveAllInstructors() {
        List<Instructor> instructors = Arrays.asList(instructor);
        when(instructorRepository.findAll()).thenReturn(instructors);

        List<Instructor> retrievedInstructors = instructorServices.retrieveAllInstructors();

        assertFalse(retrievedInstructors.isEmpty());
        assertEquals(1, retrievedInstructors.size());
        verify(instructorRepository, times(1)).findAll();
    }

    @Test
    void testUpdateInstructor() {
        when(instructorRepository.save(instructor)).thenReturn(instructor);

        Instructor updatedInstructor = instructorServices.updateInstructor(instructor);

        assertNotNull(updatedInstructor);
        assertEquals(instructor.getLastName(), updatedInstructor.getLastName());
        verify(instructorRepository, times(1)).save(instructor);
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
    void testRetrieveInstructor_NotFound() {
        when(instructorRepository.findById(2L)).thenReturn(Optional.empty());

        Instructor retrievedInstructor = instructorServices.retrieveInstructor(2L);

        assertNull(retrievedInstructor);
        verify(instructorRepository, times(1)).findById(2L);
    }

    @Test
    void testAddInstructorAndAssignToCourse() {
        Course course = new Course();
        course.setNumCourse(1L);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(instructorRepository.save(instructor)).thenReturn(instructor);

        Instructor savedInstructor = instructorServices.addInstructorAndAssignToCourse(instructor, 1L);

        assertNotNull(savedInstructor);
        assertEquals(1, savedInstructor.getCourses().size());
        verify(courseRepository, times(1)).findById(1L);
        verify(instructorRepository, times(1)).save(instructor);
    }

    @Test
    void testAddInstructorAndAssignToCourse_CourseNotFound() {
        when(courseRepository.findById(2L)).thenReturn(Optional.empty());

        Instructor savedInstructor = instructorServices.addInstructorAndAssignToCourse(instructor, 2L);

        assertNotNull(savedInstructor);
        assertTrue(savedInstructor.getCourses().isEmpty());
        verify(courseRepository, times(1)).findById(2L);
        verify(instructorRepository, times(1)).save(instructor);
    }



    @Test
    void testAddInstructorAndAssignMultipleCourses() {
        Course course1 = new Course();
        course1.setNumCourse(1L);
        Course course2 = new Course();
        course2.setNumCourse(2L);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course1));
        when(courseRepository.findById(2L)).thenReturn(Optional.of(course2));
        when(instructorRepository.save(instructor)).thenReturn(instructor);

        instructorServices.addInstructorAndAssignToCourse(instructor, 1L);
        instructorServices.addInstructorAndAssignToCourse(instructor, 2L);

        assertEquals(2, instructor.getCourses().size());
        verify(courseRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).findById(2L);
        verify(instructorRepository, times(2)).save(instructor);
    }
}
