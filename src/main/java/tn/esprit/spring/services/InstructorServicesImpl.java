package tn.esprit.spring.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Instructor;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IInstructorRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@Service
public class InstructorServicesImpl implements IInstructorServices {

    private final IInstructorRepository instructorRepository;
    private final ICourseRepository courseRepository;

    @Override
    public Instructor addInstructor(Instructor instructor) {
        if (instructor == null) {
            throw new IllegalArgumentException("Instructor cannot be null");
        }
        return instructorRepository.save(instructor);
    }

    @Override
    public List<Instructor> retrieveAllInstructors() {
        return instructorRepository.findAll();
    }

    @Override
    public Instructor updateInstructor(Instructor instructor) {
        if (instructor == null || instructor.getNumInstructor() == null) {
            throw new IllegalArgumentException("Instructor or instructor ID cannot be null");
        }
        if (!instructorRepository.existsById(instructor.getNumInstructor())) {
            throw new IllegalArgumentException("Instructor with the given ID not found");
        }
        return instructorRepository.save(instructor);
    }

    @Override
    public Instructor retrieveInstructor(Long numInstructor) {
        if (numInstructor == null) {
            throw new IllegalArgumentException("Instructor ID cannot be null");
        }
        return instructorRepository.findById(numInstructor).orElse(null);
    }

    @Override
    public Instructor addInstructorAndAssignToCourse(Instructor instructor, Long numCourse) {
        if (instructor == null) {
            throw new IllegalArgumentException("Instructor cannot be null");
        }
        if (numCourse == null) {
            throw new IllegalArgumentException("Course ID cannot be null");
        }

        Optional<Course> optionalCourse = courseRepository.findById(numCourse);
        if (optionalCourse.isEmpty()) {
            throw new IllegalArgumentException("Course with the given ID not found");
        }

        Course course = optionalCourse.get();
        Set<Course> courseSet = new HashSet<>();
        courseSet.add(course);
        instructor.setCourses(courseSet);

        return instructorRepository.save(instructor);
    }
}
