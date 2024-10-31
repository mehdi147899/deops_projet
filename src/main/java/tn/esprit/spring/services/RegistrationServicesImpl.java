package tn.esprit.spring.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IRegistrationRepository;
import tn.esprit.spring.repositories.ISkierRepository;

import jakarta.transaction.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class RegistrationServicesImpl implements IRegistrationServices {

    private IRegistrationRepository registrationRepository;
    private ISkierRepository skierRepository;
    private ICourseRepository courseRepository;

    @Transactional
    @Override
    public Registration addRegistrationAndAssignToSkierAndCourse(Registration registration, Long numSkieur, Long numCours) {
        Optional<Skier> skierOpt = skierRepository.findById(numSkieur);
        Optional<Course> courseOpt = courseRepository.findById(numCours);

        if (skierOpt.isEmpty() || courseOpt.isEmpty()) {
            return null;
        }

        Skier skier = skierOpt.get();
        Course course = courseOpt.get();

        if (isAlreadyRegistered(registration, skier, course)) {
            return null;
        }

        int ageSkieur = calculateSkierAge(skier);
        return processCourseRegistration(registration, skier, course, ageSkieur);
    }

    private boolean isAlreadyRegistered(Registration registration, Skier skier, Course course) {
        if (registrationRepository.countDistinctByNumWeekAndSkier_NumSkierAndCourse_NumCourse(registration.getNumWeek(), skier.getNumSkier(), course.getNumCourse()) >= 1) {
            log.info("Sorry, you're already registered for this course in week: " + registration.getNumWeek());
            return true;
        }
        return false;
    }

    private int calculateSkierAge(Skier skier) {
        return Period.between(skier.getDateOfBirth(), LocalDate.now()).getYears();
    }

    private Registration processCourseRegistration(Registration registration, Skier skier, Course course, int ageSkieur) {
        switch (course.getTypeCourse()) {
            case INDIVIDUAL:
                log.info("Adding without additional checks");
                return assignRegistration(registration, skier, course);

            case COLLECTIVE_CHILDREN:
                return handleChildrenRegistration(registration, skier, course, ageSkieur);

            default:
                return handleAdultRegistration(registration, skier, course, ageSkieur);
        }
    }

    private Registration handleChildrenRegistration(Registration registration, Skier skier, Course course, int ageSkieur) {
        if (ageSkieur < 16) {
            log.info("Eligible for children course.");
            if (registrationRepository.countByCourseAndNumWeek(course, registration.getNumWeek()) < 6) {
                log.info("Course successfully added.");
                return assignRegistration(registration, skier, course);
            } else {
                log.info("Course full! Please choose another week to register.");
                return null;
            }
        } else {
            log.info("Age restriction: Register for a Collective Adult Course instead.");
            return null;
        }
    }

    private Registration handleAdultRegistration(Registration registration, Skier skier, Course course, int ageSkieur) {
        if (ageSkieur >= 16) {
            log.info("Eligible for adult course.");
            if (registrationRepository.countByCourseAndNumWeek(course, registration.getNumWeek()) < 6) {
                log.info("Course successfully added.");
                return assignRegistration(registration, skier, course);
            } else {
                log.info("Course full! Please choose another week to register.");
                return null;
            }
        }
        log.info("Age restriction: Register for a Collective Child Course instead.");
        return null;
    }

    private Registration assignRegistration(Registration registration, Skier skier, Course course) {
        registration.setSkier(skier);
        registration.setCourse(course);
        return registrationRepository.save(registration);
    }

    @Override
    public Registration addRegistrationAndAssignToSkier(Registration registration, Long numSkier) {
        Skier skier = skierRepository.findById(numSkier).orElse(null);
        registration.setSkier(skier);
        return registrationRepository.save(registration);
    }

    @Override
    public Registration assignRegistrationToCourse(Long numRegistration, Long numCourse) {
        Optional<Registration> registrationOpt = registrationRepository.findById(numRegistration);
        Optional<Course> courseOpt = courseRepository.findById(numCourse);

        if (registrationOpt.isEmpty() || courseOpt.isEmpty()) {
            log.info("Registration or Course not found, cannot assign.");
            return null;
        }

        Registration registration = registrationOpt.get();
        registration.setCourse(courseOpt.get());
        return registrationRepository.save(registration);
    }
    @Override
    public List<Integer> numWeeksCourseOfInstructorBySupport(Long numInstructor, Support support) {
        return registrationRepository.numWeeksCourseOfInstructorBySupport(numInstructor, support);
    }
}
