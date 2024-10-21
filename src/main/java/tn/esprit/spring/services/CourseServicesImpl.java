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
        // Check if the course is null
        if (course == null) {
            throw new IllegalArgumentException("Course cannot be null");
        }

        // Check if the course type is null
        if (course.getTypeCourse() == null) {
            throw new IllegalArgumentException("Course type must not be null");
        }

        // Check if the price is valid (greater than 0)
        if (course.getPrice() == null || course.getPrice() <= 0) {
            throw new IllegalArgumentException("Course price must be greater than zero");
        }

        // Check if the level is valid
        if (course.getLevel() <= 0) {
            throw new IllegalArgumentException("Course level must be greater than zero");
        }

        // Check if support is null
        if (course.getSupport() == null) {
            throw new IllegalArgumentException("Support type must not be null");
        }

        // Check if timeSlot is valid
        if (course.getTimeSlot() <= 0) {
            throw new IllegalArgumentException("Time slot must be greater than zero");
        }

        // If all validations pass, save the course
        return courseRepository.save(course);
    }

    @Override
    public Course updateCourse(Course course) {
        // Check if the course is null
        if (course == null) {
            throw new IllegalArgumentException("Course cannot be null");
        }

        // Check if the course exists in the repository
        if (!courseRepository.existsById(course.getNumCourse())) {
            throw new IllegalArgumentException("Course not found");
        }

        // Check if the course type is null
        if (course.getTypeCourse() == null) {
            throw new IllegalArgumentException("Course type must not be null");
        }

        // Check if the price is valid (greater than 0)
        if (course.getPrice() == null || course.getPrice() <= 0) {
            throw new IllegalArgumentException("Course price must be greater than zero");
        }

        // Check if the level is valid
        if (course.getLevel() <= 0) {
            throw new IllegalArgumentException("Course level must be greater than zero");
        }

        // Check for support type
        if (course.getSupport() == null) {
            throw new IllegalArgumentException("Support type must not be null");
        }

        // Check if timeSlot is valid
        if (course.getTimeSlot() <= 0) {
            throw new IllegalArgumentException("Time slot must be greater than zero");
        }

        // If all validations pass, save and return the course
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
