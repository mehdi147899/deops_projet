package tn.esprit.spring.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.entities.Instructor;
import tn.esprit.spring.entities.Registration;
import tn.esprit.spring.entities.Support;
import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.services.IRegistrationServices;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@Tag(name = "\uD83D\uDDD3️Registration Management")
@RestController
@RequestMapping("/registration")
@RequiredArgsConstructor
public class RegistrationRestController {
    private final IRegistrationServices registrationServices;
    private static final Logger logger = LogManager.getLogger(RegistrationRestController.class);

    @Operation(description = "Add Registration and Assign to Skier")
    @PutMapping("/addAndAssignToSkier/{numSkieur}")
    public Registration addAndAssignToSkier(@RequestBody Registration registration,
                                            @PathVariable("numSkieur") Long numSkieur)
    {
        logger.info("Attempting to add and assign registration to skier with ID: {}", numSkieur);
        Registration result = registrationServices.addRegistrationAndAssignToSkier(registration, numSkieur);
        logger.info("Registration added and assigned to skier with ID: {}", numSkieur);
        return result;
    }

    @Operation(description = "Assign Registration to Course")
    @PutMapping("/assignToCourse/{numRegis}/{numSkieur}")
    public Registration assignToCourse(@PathVariable("numRegis") Long numRegistration,
                                       @PathVariable("numSkieur") Long numCourse) {
        logger.info("Assigning registration ID {} to course ID {}", numRegistration, numCourse);
        Registration result = registrationServices.assignRegistrationToCourse(numRegistration, numCourse);
        logger.info("Registration ID {} assigned to course ID {}", numRegistration, numCourse);
        return result;
    }

    @Operation(description = "Add Registration and Assign to Skier and Course")
    @PutMapping("/addAndAssignToSkierAndCourse/{numSkieur}/{numCourse}")
    public Registration addAndAssignToSkierAndCourse(@RequestBody Registration registration,
                                                     @PathVariable("numSkieur") Long numSkieur,
                                                     @PathVariable("numCourse") Long numCourse)
    {
        logger.info("Starting process to add and assign registration to both skier ID {} and course ID {}", numSkieur, numCourse);
        Registration result = registrationServices.addRegistrationAndAssignToSkierAndCourse(registration, numSkieur, numCourse);
        logger.info("Registration added and assigned to skier ID {} and course ID {}", numSkieur, numCourse);
        return result;
    }

    @Operation(description = "Numbers of the weeks when an instructor has given lessons in a given support")
    @GetMapping("/numWeeks/{numInstructor}/{support}")
    public List<Integer> numWeeksCourseOfInstructorBySupport(@PathVariable("numInstructor")Long numInstructor,
                                                             @PathVariable("support") Support support) {
        logger.info("Fetching number of weeks instructor ID {} has given lessons on support {}", numInstructor, support);
        List<Integer> weeks = registrationServices.numWeeksCourseOfInstructorBySupport(numInstructor, support);
        logger.info("Number of weeks fetched for instructor ID {} on support {}", numInstructor, support);
        return weeks;
    }
}
