import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.TypeCourse;
import tn.esprit.spring.entities.Support; // Assurez-vous d'importer Support
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.services.CourseServicesImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CourseServicesImplTest { // Changed from public to package-private

    @InjectMocks
    private CourseServicesImpl courseServices;

    @Mock
    private ICourseRepository courseRepository;

    private Course course;

    @BeforeEach
    void setUp() { // Changed from public to package-private
        MockitoAnnotations.openMocks(this);
        course = new Course(1L, 3, TypeCourse.COLLECTIVE_CHILDREN, Support.SKI, 100.0f, 2, null);
    }

    @Test
    void testRetrieveAllCourses() { // Changed from public to package-private
        List<Course> courses = new ArrayList<>();
        courses.add(course);

        when(courseRepository.findAll()).thenReturn(courses);

        List<Course> retrievedCourses = courseServices.retrieveAllCourses();

        assertEquals(1, retrievedCourses.size());
        assertEquals(course.getNumCourse(), retrievedCourses.get(0).getNumCourse());
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void testAddCourse() { // Changed from public to package-private
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        Course createdCourse = courseServices.addCourse(course);

        assertEquals(course.getNumCourse(), createdCourse.getNumCourse());
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void testUpdateCourse() { // Changed from public to package-private
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        Course updatedCourse = courseServices.updateCourse(course);

        assertEquals(course.getNumCourse(), updatedCourse.getNumCourse());
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void testRetrieveCourse() { // Changed from public to package-private
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        Course retrievedCourse = courseServices.retrieveCourse(1L);

        assertNotNull(retrievedCourse);
        assertEquals(course.getNumCourse(), retrievedCourse.getNumCourse());
        verify(courseRepository, times(1)).findById(1L);
    }

    @Test
    void testRetrieveCourseNotFound() { // Changed from public to package-private
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        Course retrievedCourse = courseServices.retrieveCourse(1L);

        assertNull(retrievedCourse);
        verify(courseRepository, times(1)).findById(1L);
    }
}
