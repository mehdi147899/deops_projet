package tn.esprit.spring.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IRegistrationRepository;
import tn.esprit.spring.repositories.ISkierRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
@Slf4j
@AllArgsConstructor
@Service
public class RegistrationServicesImpl implements  IRegistrationServices{

    private IRegistrationRepository registrationRepository;
    private ISkierRepository skierRepository;
    private ICourseRepository courseRepository;


    @Override
    public Registration addRegistrationAndAssignToSkier(Registration registration, Long numSkier) {
        Skier skier = skierRepository.findById(numSkier).orElse(null);
        registration.setSkier(skier);
        return registrationRepository.save(registration);
    }

    @Override
    public Registration assignRegistrationToCourse(Long numRegistration, Long numCourse) {
        Registration registration = registrationRepository.findById(numRegistration).orElse(null);
        Course course = courseRepository.findById(numCourse).orElse(null);

        if (registration == null || course == null) {
            log.warn("Registration with ID {} or Course with ID {} not found.", numRegistration, numCourse);
            return null;
        }

        registration.setCourse(course);
        return registrationRepository.save(registration);
    }


    @Transactional
    @Override
    public Registration addRegistrationAndAssignToSkierAndCourse(Registration registration, Long numSkieur, Long numCours) {
        Skier skier = skierRepository.findById(numSkieur).orElse(null);
        if (skier == null) {
            log.warn("Skier with ID {} not found.", numSkieur);
            return null;
        }

        Course course = courseRepository.findById(numCours).orElse(null);
        if (course == null) {
            log.warn("Course with ID {} not found.", numCours);
            return null;
        }

        if (registrationRepository.countDistinctByNumWeekAndSkier_NumSkierAndCourse_NumCourse(
                registration.getNumWeek(), skier.getNumSkier(), course.getNumCourse()) >= 1) {
            log.info("Skier {} is already registered for Course {} in week {}.",
                    skier.getNumSkier(), course.getNumCourse(), registration.getNumWeek());
            return null;
        }

        int ageSkieur = Period.between(skier.getDateOfBirth(), LocalDate.now()).getYears();
        log.info("Skier {} age calculated as {}", skier.getNumSkier(), ageSkieur);

        // Existing logic for course assignment based on age and type
        switch (course.getTypeCourse()) {
            case INDIVIDUAL:
                log.info("Registering skier {} to individual course {}.", skier.getNumSkier(), course.getNumCourse());
                return assignRegistration(registration, skier, course);
            case COLLECTIVE_CHILDREN:
                if (ageSkieur < 16 && registrationRepository.countByCourseAndNumWeek(course, registration.getNumWeek()) < 6) {
                    log.info("Skier {} added to children's collective course {}.", skier.getNumSkier(), course.getNumCourse());
                    return assignRegistration(registration, skier, course);
                } else {
                    log.info("Course {} is full or age not allowed for skier {}.", course.getNumCourse(), skier.getNumSkier());
                }
                break;
            default:
                if (ageSkieur >= 16 && registrationRepository.countByCourseAndNumWeek(course, registration.getNumWeek()) < 6) {
                    log.info("Skier {} added to adult's collective course {}.", skier.getNumSkier(), course.getNumCourse());
                    return assignRegistration(registration, skier, course);
                } else {
                    log.info("Course {} is full or age not allowed for skier {}.", course.getNumCourse(), skier.getNumSkier());
                }
        }
        return null;
    }


    private Registration assignRegistration (Registration registration, Skier skier, Course course){
        registration.setSkier(skier);
        registration.setCourse(course);
        return registrationRepository.save(registration);
    }

    @Override
    public List<Integer> numWeeksCourseOfInstructorBySupport(Long numInstructor, Support support) {
        return registrationRepository.numWeeksCourseOfInstructorBySupport(numInstructor, support);
    }

}
