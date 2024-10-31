package tn.esprit.spring.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IRegistrationRepository;
import tn.esprit.spring.repositories.ISkierRepository;

import jakarta.transaction.Transactional;
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

    @Override
    public Registration addRegistrationAndAssignToSkier(@Nullable Registration registration, @Nullable Long numSkier) {
        if (registration == null || numSkier == null) {
            log.warn("Registration or Skier ID is null");
            return null;
        }

        Optional<Skier> skierOpt = skierRepository.findById(numSkier);
        if (!skierOpt.isPresent()) {
            log.warn("Skier not found for ID: " + numSkier);
            return null;
        }

        registration.setSkier(skierOpt.get());
        return registrationRepository.save(registration);
    }

    @Override
    public Registration assignRegistrationToCourse(@Nullable Long numRegistration, @Nullable Long numCourse) {
        if (numRegistration == null || numCourse == null) {
            log.warn("Registration ID or Course ID is null");
            return null;
        }

        Optional<Registration> registrationOpt = registrationRepository.findById(numRegistration);
        Optional<Course> courseOpt = courseRepository.findById(numCourse);

        if (!registrationOpt.isPresent() || !courseOpt.isPresent()) {
            log.warn("Registration or Course not found");
            return null;
        }

        Registration registration = registrationOpt.get();
        registration.setCourse(courseOpt.get());
        return registrationRepository.save(registration);
    }

    @Transactional
    @Override
    public Registration addRegistrationAndAssignToSkierAndCourse(@Nullable Registration registration, @Nullable Long numSkieur, @Nullable Long numCours) {
        if (registration == null || numSkieur == null || numCours == null) {
            log.warn("Invalid input: null value for registration, skier ID, or course ID");
            return null;
        }

        Optional<Skier> skierOpt = skierRepository.findById(numSkieur);
        Optional<Course> courseOpt = courseRepository.findById(numCours);

        if (!skierOpt.isPresent() || !courseOpt.isPresent()) {
            log.warn("Skier or Course not found");
            return null;
        }

        Skier skier = skierOpt.get();
        Course course = courseOpt.get();

        if (isDuplicateRegistration(registration, skier, course)) {
            return null;
        }

        int ageSkieur = calculateSkierAge(skier);
        log.info("Age " + ageSkieur);

        if (isEligibleForCourseType(course, ageSkieur, registration)) {
            return assignRegistration(registration, skier, course);
        }

        return registration;
    }

    private boolean isDuplicateRegistration(Registration registration, Skier skier, Course course) {
        if (registrationRepository.countDistinctByNumWeekAndSkier_NumSkierAndCourse_NumCourse(
                registration.getNumWeek(), skier.getNumSkier(), course.getNumCourse()) >= 1) {
            log.info("Already registered for this course in week: " + registration.getNumWeek());
            return true;
        }
        return false;
    }

    private int calculateSkierAge(@Nullable Skier skier) {
        if (skier == null || skier.getDateOfBirth() == null) {
            log.warn("Skier or Skier's date of birth is null");
            return 0;
        }
        return Period.between(skier.getDateOfBirth(), LocalDate.now()).getYears();
    }

    private boolean isEligibleForCourseType(Course course, int ageSkieur, Registration registration) {
        switch (course.getTypeCourse()) {
            case INDIVIDUAL:
                log.info("Add without tests");
                return true;

            case COLLECTIVE_CHILDREN:
                return handleChildCourse(ageSkieur, course, registration);

            default:
                return handleAdultCourse(ageSkieur, course, registration);
        }
    }

    private boolean handleChildCourse(int ageSkieur, Course course, Registration registration) {
        if (ageSkieur < 16) {
            log.info("Ok CHILD!");
            return checkCourseCapacity(course, registration);
        } else {
            log.info("Ineligible for children's course; try an adult course");
            return false;
        }
    }

    private boolean handleAdultCourse(int ageSkieur, Course course, Registration registration) {
        if (ageSkieur >= 16) {
            log.info("Ok ADULT!");
            return checkCourseCapacity(course, registration);
        } else {
            log.info("Ineligible for adult course; try a children's course");
            return false;
        }
    }

    private boolean checkCourseCapacity(Course course, Registration registration) {
        if (registrationRepository.countByCourseAndNumWeek(course, registration.getNumWeek()) < 6) {
            log.info("Course successfully added!");
            return true;
        } else {
            log.info("Course is full; choose another week");
            return false;
        }
    }

    private Registration assignRegistration(Registration registration, Skier skier, Course course) {
        registration.setSkier(skier);
        registration.setCourse(course);
        return registrationRepository.save(registration);
    }

    @Override
    public List<Integer> numWeeksCourseOfInstructorBySupport(Long numInstructor, Support support) {
        return registrationRepository.numWeeksCourseOfInstructorBySupport(numInstructor, support);
    }
}
