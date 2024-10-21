package tn.esprit.spring.services;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.repositories.ICourseRepository;

import java.util.Collections;
import java.util.List;
@AllArgsConstructor
@Service
public class CourseServicesImpl implements ICourseServices {

    private final ICourseRepository courseRepository;

    private static final Logger logger = LoggerFactory.getLogger(CourseServicesImpl.class);

    @Override
    public List<Course> retrieveAllCourses() {
        try {
            List<Course> courses = courseRepository.findAll();
            if (courses.isEmpty()) {
                logger.warn("No courses available");
            }
            return courses;
        } catch (DataAccessException e) {
            logger.error("Database error while retrieving courses", e);
            // Return an empty list instead of throwing an exception
            return Collections.emptyList();
        }
    }

    @Override
    public Course addCourse(Course course) {
        if (course == null) {
            throw new IllegalArgumentException("Course cannot be null");
        }
        if (course.getTypeCourse() == null) {
            throw new IllegalArgumentException("Course type must not be null");
        }
        // Optionally, add more validation for other course properties here
        return courseRepository.save(course);
    }

    @Override
    public Course updateCourse(Course course) {
        if (course == null) {
            throw new IllegalArgumentException("Course cannot be null");
        }
        if (!courseRepository.existsById(course.getNumCourse())) {
            throw new IllegalArgumentException("Course not found");
        }

        // Check for support type
        if (course.getSupport() == null) {
            throw new IllegalArgumentException("Support type must not be null");
        }

        // Save and return the course
        return courseRepository.save(course);
    }

    @Override
    public Course retrieveCourse(Long numCourse) {
        if (numCourse == null) {
            throw new IllegalArgumentException("Course ID cannot be null");
        }
        if (numCourse < 0) {
            throw new IllegalArgumentException("Course ID must not be negative");
        }
        return courseRepository.findById(numCourse).orElse(null);
    }


    @Override
    public boolean exists(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Course ID cannot be null");
        }
        return courseRepository.existsById(id);
    }

}
