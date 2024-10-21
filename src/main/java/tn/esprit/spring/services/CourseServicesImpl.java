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
            return Collections.emptyList();
        }
    }

    @Override
    public Course addCourse(Course course) {
        validateCourse(course);
        return courseRepository.save(course);
    }

    @Override
    public Course updateCourse(Course course) {
        if (course == null) {
            throw new IllegalArgumentException("Course cannot be null");
        }

        // Check if the course exists in the repository
        if (!courseRepository.existsById(course.getNumCourse())) {
            throw new IllegalArgumentException("Course not found");
        }

        // Validate the course object
        validateCourse(course);
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

    private void validateCourse(Course course) {
        if (course == null) {
            throw new IllegalArgumentException("Course cannot be null");
        }
        if (course.getTypeCourse() == null) {
            throw new IllegalArgumentException("Course type must not be null");
        }
        if (course.getPrice() == null || course.getPrice() <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }
        if (course.getLevel() <= 0) {
            throw new IllegalArgumentException("Level must be greater than zero");
        }
        if (course.getSupport() == null) {
            throw new IllegalArgumentException("Support type must not be null");
        }
        if (course.getTimeSlot() <= 0) {
            throw new IllegalArgumentException("Time slot must be greater than zero");
        }
    }
}
