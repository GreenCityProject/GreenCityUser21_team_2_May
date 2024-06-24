package greencity.controller;

import greencity.constant.HttpStatuses;
import greencity.dto.econews.EcoNewsForSendEmailDto;
import greencity.dto.notification.NotificationDto;
import greencity.dto.violation.UserViolationMailDto;
import greencity.message.*;
import greencity.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    /**
     * Method for sending news for users who subscribed for updates.
     *
     * @param message - object with all necessary data for sending email
     * @author Taras Kavkalo
     */
    @PostMapping("/addEcoNews")
    public ResponseEntity<Object> addEcoNews(@RequestBody @Valid EcoNewsForSendEmailDto message) {
        emailService.sendCreatedNewsForAuthor(message);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Method for sending notification to userss who subscribed for updates about
     * added new places.
     *
     * @param message - object with all necessary data for sending email
     * @author Taras Kavkalo
     */
    @PostMapping("/sendReport")
    public ResponseEntity<Object> sendReport(@RequestBody SendReportEmailMessage message) {
        emailService.sendAddedNewPlacesReportEmail(message.getSubscribers(), message.getCategoriesDtoWithPlacesDtoMap(),
            message.getEmailNotification());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Method for sending simple notification to {@code User} about status change.
     *
     * @param message - object with all necessary data for sending email
     * @author Taras Kavkalo
     */
    @Operation(
            summary = "Change the status of a place and notify the author via email",
            description = "This endpoint allows the user to change the status of a specified place. " +
                    "Upon changing the status, an email notification is sent to the author. " +
                    "The request body must contain the author's first name, the place name, " +
                    "the new place status, and the author's email address."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST)
    })
    @PostMapping("/changePlaceStatus")
    public ResponseEntity<Object> changePlaceStatus(@Valid @RequestBody SendChangePlaceStatusEmailMessage message) {
        emailService.sendChangePlaceStatusEmail(message.getAuthorFirstName(), message.getPlaceName(),
            message.getPlaceStatus(), message.getAuthorEmail());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Sends email notification about not marked habits during 3 last days.
     *
     * @param sendHabitNotification - object with all necessary data for sending
     *                              email
     * @author Taras Kavkalo
     */
    @Operation(
            summary = "Send habit notification email",
            description = "This endpoint allows the user to send a habit notification email to a specified recipient. " +
                    "The request body must contain the recipient's name and email address."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND)
    })
    @PostMapping("/sendHabitNotification")
    public ResponseEntity<Object> sendHabitNotification(@Valid @RequestBody SendHabitNotification sendHabitNotification) {
        emailService.sendHabitNotification(sendHabitNotification.getName(), sendHabitNotification.getEmail());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Sends email notification about violation to user on email.
     *
     * @param dto {@link UserViolationMailDto} - object with all necessary data for
     *            sending email.
     * @author Zakhar Veremchuk
     */
    @Operation(summary = "Send user violation email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = HttpStatuses.FORBIDDEN),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND)
    })
    @PostMapping("/sendUserViolation")
    public ResponseEntity<Object> sendUserViolation(@Valid @RequestBody UserViolationMailDto dto) {
        emailService.sendUserViolationEmail(dto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Sends notification to user on email.
     *
     * @param notification {@link NotificationDto} - object with all necessary data
     *                     for sending notification via email.
     * @param email        {@link String} - user's email.
     * @author Ann Sakhno
     */
    @Operation(summary = "Send notification to user via email")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
        @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST),
        @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
        @ApiResponse(responseCode = "403", description = HttpStatuses.FORBIDDEN)
    })
    @PostMapping("/notification")
    public ResponseEntity<Object> sendUserNotification(@RequestBody NotificationDto notification,
        @RequestParam("email") String email) {
        emailService.sendNotificationByEmail(notification, email);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Sends notification to user on email.
     *
     * @param message {@link EventEmailMessage} - object with all necessary data
     *                     for sending notification via email.
     * @author Roman Kasarab
     */
    @Operation(summary = "Send notification to user via email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = HttpStatuses.FORBIDDEN)
    })
    @PostMapping("/sendEventNotification")
    public ResponseEntity<Object> sendEventNotification(@RequestBody EventEmailMessage message) {
        emailService.sendNotificationMessageByEmail(message);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Sends notification event was commented to user on email.
     *
     * @param message {@link EventCommentMessage} - object with all necessary data
     *                     for sending notification via email.
     */
    @Operation(summary = "Send notification to user via email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST),
            @ApiResponse(responseCode = "401", description = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = HttpStatuses.FORBIDDEN)
    })
    @PostMapping("/sendEventCommentNotification")
    public ResponseEntity<Object> sendEventCommentNotification(@RequestBody EventCommentMessage message) {
        emailService.sendEventCommentNotificationMessageByEmail(message);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
