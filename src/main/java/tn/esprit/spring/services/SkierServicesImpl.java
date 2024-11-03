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
        return skierRepository.findAll();
    }

    @Override
    public Skier addSkier(Skier skier) {
        logger.info("Démarrage de la méthode addSkier avec le Skier : {}", skier);

        try {
            // Déterminer la date de fin de l'abonnement en fonction du type
            switch (skier.getSubscription().getTypeSub()) {
                case ANNUAL:
                    skier.getSubscription().setEndDate(skier.getSubscription().getStartDate().plusYears(1));
                    logger.debug("Abonnement ANNUAL, date de fin fixée à : {}", skier.getSubscription().getEndDate());
                    break;
                case SEMESTRIEL:
                    skier.getSubscription().setEndDate(skier.getSubscription().getStartDate().plusMonths(6));
                    logger.debug("Abonnement SEMESTRIEL, date de fin fixée à : {}", skier.getSubscription().getEndDate());
                    break;
                case MONTHLY:
                    skier.getSubscription().setEndDate(skier.getSubscription().getStartDate().plusMonths(1));
                    logger.debug("Abonnement MONTHLY, date de fin fixée à : {}", skier.getSubscription().getEndDate());
                    break;
            }

            Skier savedSkier = skierRepository.save(skier);
            logger.info("Skier ajouté avec succès : {}", savedSkier);
            return savedSkier;

        } catch (Exception e) {
            logger.error("Erreur lors de l'ajout du Skier : ", e);
            return null;
        }
    }

    @Override
    public Skier assignSkierToSubscription(Long numSkier, Long numSubscription) {
        Skier skier = skierRepository.findById(numSkier).orElse(null);
        Subscription subscription = subscriptionRepository.findById(numSubscription).orElse(null);
        skier.setSubscription(subscription);
        return skierRepository.save(skier);
    }

    @Override
    public Skier addSkierAndAssignToCourse(Skier skier, Long numCourse) {
        Skier savedSkier = skierRepository.save(skier);
        Course course = courseRepository.getById(numCourse);
        Set<Registration> registrations = savedSkier.getRegistrations();
        for (Registration r : registrations) {
            r.setSkier(savedSkier);
            r.setCourse(course);
            registrationRepository.save(r);
        }
        return savedSkier;
    }

    @Override
    public void removeSkier(Long numSkier) {
        logger.info("Démarrage de la méthode removeSkier pour l'ID : {}", numSkier);

        try {
            skierRepository.deleteById(numSkier);
            logger.info("Skier supprimé avec succès, ID : {}", numSkier);
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression du Skier avec ID : {}", numSkier, e);
        }
    }

    @Override
    public Skier retrieveSkier(Long numSkier) {
        logger.info("Démarrage de la méthode retrieveSkier pour l'ID : {}", numSkier);

        Skier skier = skierRepository.findById(numSkier).orElse(null);
        if (skier != null) {
            logger.info("Skier récupéré avec succès : {}", skier);
        } else {
            logger.warn("Aucun Skier trouvé avec l'ID : {}", numSkier);
        }
        return skier;
    }
    @Override
    public Skier assignSkierToPiste(Long numSkieur, Long numPiste) {
        Skier skier = skierRepository.findById(numSkieur).orElse(null);
        Piste piste = pisteRepository.findById(numPiste).orElse(null);
        try {
            skier.getPistes().add(piste);
        } catch (NullPointerException exception) {
            Set<Piste> pisteList = new HashSet<>();
            pisteList.add(piste);
            skier.setPistes(pisteList);
        }

        return skierRepository.save(skier);
    }

    @Override
    public List<Skier> retrieveSkiersBySubscriptionType(TypeSubscription typeSubscription) {
        return skierRepository.findBySubscription_TypeSub(typeSubscription);
    }
}
