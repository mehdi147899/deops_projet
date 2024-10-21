package tn.esprit.spring;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Support;
import tn.esprit.spring.entities.TypeCourse;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.services.CourseServicesImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class CourseServicesImplTest {

    @InjectMocks
    private CourseServicesImpl courseServices;

    @Mock
    private ICourseRepository courseRepository;

    private Course course;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        course = new Course(1L, 3, TypeCourse.COLLECTIVE_CHILDREN, Support.SKI, 100.0f, 2, null);
    }

    @Test
    void testRetrieveAllCourses() {
        List<Course> courses = new ArrayList<>();
        courses.add(course);
        when(courseRepository.findAll()).thenReturn(courses);

        List<Course> retrievedCourses = courseServices.retrieveAllCourses();

        assertEquals(1, retrievedCourses.size());
        assertEquals(course.getNumCourse(), retrievedCourses.get(0).getNumCourse());
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void testAddCourse() {
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        Course createdCourse = courseServices.addCourse(course);

        assertEquals(course.getNumCourse(), createdCourse.getNumCourse());
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void testRetrieveAllCoursesEmpty() {
        when(courseRepository.findAll()).thenReturn(new ArrayList<>());

        List<Course> retrievedCourses = courseServices.retrieveAllCourses();

        assertNotNull(retrievedCourses);
        assertEquals(0, retrievedCourses.size());
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void testAddDuplicateCourse() {
        when(courseRepository.save(any(Course.class))).thenThrow(new IllegalArgumentException("Course already exists"));

        assertThrows(IllegalArgumentException.class, () -> courseServices.addCourse(course));

        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void testUpdateCourseWithNull() {
        assertThrows(IllegalArgumentException.class, () -> courseServices.updateCourse(null));
        verify(courseRepository, never()).save(any(Course.class));
    }

    @Test
    void testUpdateCourseNotFound() {
        when(courseRepository.existsById(course.getNumCourse())).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> courseServices.updateCourse(course));

        verify(courseRepository, times(1)).existsById(course.getNumCourse());
    }

    @Test
    void testRetrieveAllCoursesThrowsException() {
        when(courseRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> courseServices.retrieveAllCourses());

        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void testRetrieveCourseWithNullId() {
        assertThrows(IllegalArgumentException.class, () -> courseServices.retrieveCourse(null));
        verify(courseRepository, never()).findById(anyLong());
    }

    @Test
    void testRetrieveAllCoursesMultiple() {
        Course anotherCourse = new Course(2L, 4, TypeCourse.COLLECTIVE_CHILDREN, Support.SNOWBOARD, 120.0f, 3, null);
        List<Course> courses = new ArrayList<>();
        courses.add(course);
        courses.add(anotherCourse);

        when(courseRepository.findAll()).thenReturn(courses);

        List<Course> retrievedCourses = courseServices.retrieveAllCourses();

        assertNotNull(retrievedCourses);
        assertEquals(2, retrievedCourses.size());
        assertTrue(retrievedCourses.stream().anyMatch(c -> c.getNumCourse().equals(course.getNumCourse())));
        assertTrue(retrievedCourses.stream().anyMatch(c -> c.getNumCourse().equals(anotherCourse.getNumCourse())));
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void testUpdateCourse() {
        when(courseRepository.existsById(course.getNumCourse())).thenReturn(true);
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        Course updatedCourse = courseServices.updateCourse(course);

        assertEquals(course.getNumCourse(), updatedCourse.getNumCourse());
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void testRetrieveCourse() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        Course retrievedCourse = courseServices.retrieveCourse(1L);

        assertNotNull(retrievedCourse);
        assertEquals(course.getNumCourse(), retrievedCourse.getNumCourse());
        verify(courseRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateCourseWithInvalidSupport() {
        Course courseToUpdate = new Course(1L, 3, TypeCourse.COLLECTIVE_CHILDREN, null, 100.0f, 2, null);
        when(courseRepository.existsById(courseToUpdate.getNumCourse())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> courseServices.updateCourse(courseToUpdate));
        verify(courseRepository, never()).save(any(Course.class));
    }

    @Test
    void testAddCourseWithInvalidTypeCourse() {
        Course invalidCourse = new Course(2L, 3, null, Support.SKI, 100.0f, 2, null);

        assertThrows(IllegalArgumentException.class, () -> courseServices.addCourse(invalidCourse));
        verify(courseRepository, never()).save(any(Course.class));
    }

    @Test
    void testRetrieveCourseWithNonExistentId() {
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        Course retrievedCourse = courseServices.retrieveCourse(99L);

        assertNull(retrievedCourse);
        verify(courseRepository, times(1)).findById(99L);
    }

    @Test
    void testAddCourseWhenRepositoryFails() {
        when(courseRepository.save(any(Course.class))).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> courseServices.addCourse(course));
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void testRetrieveCourseNotFound() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        Course retrievedCourse = courseServices.retrieveCourse(1L);

        assertNull(retrievedCourse);
        verify(courseRepository, times(1)).findById(1L);
    }

    @Test
    void testExistsByIdTrue() {
        when(courseRepository.existsById(1L)).thenReturn(true);

        assertTrue(courseServices.exists(1L));
        verify(courseRepository, times(1)).existsById(1L);
    }

    @Test
    void testExistsByIdFalse() {
        when(courseRepository.existsById(1L)).thenReturn(false);

        assertFalse(courseServices.exists(1L));
        verify(courseRepository, times(1)).existsById(1L);
    }

    @Test
    void testAddCourseWithNull() {
        assertThrows(IllegalArgumentException.class, () -> courseServices.addCourse(null));
        verify(courseRepository, never()).save(any(Course.class));
    }

    @Test
    void testUpdateNonExistingCourse() {
        Course nonExistingCourse = new Course(99L, 3, TypeCourse.COLLECTIVE_CHILDREN, Support.SKI, 100.0f, 2, null);
        when(courseRepository.existsById(nonExistingCourse.getNumCourse())).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            courseServices.updateCourse(nonExistingCourse);
        });

        assertEquals("Course not found", exception.getMessage());
        verify(courseRepository, times(1)).existsById(nonExistingCourse.getNumCourse());
    }
}
