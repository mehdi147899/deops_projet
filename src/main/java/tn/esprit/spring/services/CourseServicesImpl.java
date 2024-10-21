package tn.esprit.spring.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.repositories.ICourseRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class CourseServicesImpl implements ICourseServices {

    private final ICourseRepository courseRepository;

    @Override
    public List<Course> retrieveAllCourses() {
        return courseRepository.findAll();
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
        return courseRepository.findById(numCourse).orElse(null);
    }

    @Override
    public boolean exists(Long id) {
        return courseRepository.existsById(id); // Use the existsById method provided by JPA
    }
}
