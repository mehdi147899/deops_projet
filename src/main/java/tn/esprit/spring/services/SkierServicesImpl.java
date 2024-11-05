package tn.esprit.spring.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repositories.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@AllArgsConstructor
@Service
public class SkierServicesImpl implements ISkierServices {

    private static final Logger logger = LogManager.getLogger(SkierServicesImpl.class);

    private ISkierRepository skierRepository;
    private IPisteRepository pisteRepository;
    private ICourseRepository courseRepository;
    private IRegistrationRepository registrationRepository;
    private ISubscriptionRepository subscriptionRepository;

    @Override
    public List<Skier> retrieveAllSkiers() {
        logger.info("Retrieving all skiers from the database");
        return skierRepository.findAll();
    }

    @Override
    public Skier addSkier(Skier skier) {
        logger.info("Adding a new skier with subscription type: {}", skier.getSubscription().getTypeSub());
        switch (skier.getSubscription().getTypeSub()) {
            case ANNUAL:
                skier.getSubscription().setEndDate(skier.getSubscription().getStartDate().plusYears(1));
                break;
            case SEMESTRIEL:
                skier.getSubscription().setEndDate(skier.getSubscription().getStartDate().plusMonths(6));
                break;
            case MONTHLY:
                skier.getSubscription().setEndDate(skier.getSubscription().getStartDate().plusMonths(1));
                break;
        }
        Skier savedSkier = skierRepository.save(skier);
        logger.info("Skier with ID {} added successfully", savedSkier.getNumSkier());
        return savedSkier;
    }

    @Override
    public Skier assignSkierToSubscription(Long numSkier, Long numSubscription) {
        logger.info("Assigning subscription ID {} to skier ID {}", numSubscription, numSkier);
        Skier skier = skierRepository.findById(numSkier).orElse(null);
        Subscription subscription = subscriptionRepository.findById(numSubscription).orElse(null);

        if (skier == null || subscription == null) {
            logger.warn("Skier or subscription not found: Skier ID {}, Subscription ID {}", numSkier, numSubscription);
            return null;
        }
        
        skier.setSubscription(subscription);
        Skier updatedSkier = skierRepository.save(skier);
        logger.info("Skier ID {} assigned to subscription ID {}", skier.getNumSkier(), numSubscription);
        return updatedSkier;
    }

    @Override
    public Skier addSkierAndAssignToCourse(Skier skier, Long numCourse) {
        logger.info("Adding skier and assigning to course ID {}", numCourse);
        Skier savedSkier = skierRepository.save(skier);
        Course course = courseRepository.findById(numCourse).orElse(null);

        if (course == null) {
            logger.warn("Course ID {} not found", numCourse);
            return savedSkier;
        }

        Set<Registration> registrations = savedSkier.getRegistrations();
        for (Registration r : registrations) {
            r.setSkier(savedSkier);
            r.setCourse(course);
            registrationRepository.save(r);
        }
        logger.info("Skier ID {} assigned to course ID {}", savedSkier.getNumSkier(), numCourse);
        return savedSkier;
    }

    @Override
    public void removeSkier(Long numSkier) {
        logger.info("Removing skier with ID {}", numSkier);
        skierRepository.deleteById(numSkier);
        logger.info("Skier with ID {} removed", numSkier);
    }

    @Override
    public Skier retrieveSkier(Long numSkier) {
        logger.info("Retrieving skier with ID {}", numSkier);
        return skierRepository.findById(numSkier).orElse(null);
    }

    @Override
    public Skier assignSkierToPiste(Long numSkieur, Long numPiste) {
        logger.info("Assigning skier ID {} to piste ID {}", numSkieur, numPiste);
        Skier skier = skierRepository.findById(numSkieur).orElse(null);
        Piste piste = pisteRepository.findById(numPiste).orElse(null);

        if (skier == null || piste == null) {
            logger.warn("Skier or piste not found: Skier ID {}, Piste ID {}", numSkieur, numPiste);
            return null;
        }

        try {
            skier.getPistes().add(piste);
        } catch (NullPointerException exception) {
            logger.warn("Pistes list was null for skier ID {}, initializing new list", numSkieur);
            Set<Piste> pisteList = new HashSet<>();
            pisteList.add(piste);
            skier.setPistes(pisteList);
        }

        Skier updatedSkier = skierRepository.save(skier);
        logger.info("Skier ID {} assigned to piste ID {}", skier.getNumSkier(), numPiste);
        return updatedSkier;
    }

    @Override
    public List<Skier> retrieveSkiersBySubscriptionType(TypeSubscription typeSubscription) {
        logger.info("Retrieving skiers with subscription type {}", typeSubscription);
        return skierRepository.findBySubscription_TypeSub(typeSubscription);
    }
}
