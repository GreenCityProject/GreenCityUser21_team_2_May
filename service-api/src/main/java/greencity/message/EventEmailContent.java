package greencity.message;

import greencity.dto.event.EventStatus;

import java.time.format.DateTimeFormatter;

public class EventEmailContent {

    public static String createEventEmailContent(EventEmailMessage eventEmailMessage) {
        String eventDetails;

        if (eventEmailMessage.getStatus() == EventStatus.ONLINE) {
            eventDetails = String.format(
                    "<p><strong>Mode:</strong> Online</p>" +
                            "<p><strong>Link:</strong> <a href='%s'>Join Event</a></p>",
                    eventEmailMessage.getLink()
            );
        } else if (eventEmailMessage.getStatus() == EventStatus.OFFLINE) {
            eventDetails = String.format(
                    "<p><strong>Mode:</strong> Offline</p>" +
                            "<p><strong>Location:</strong> %s</p>",
                    eventEmailMessage.getAddress().getAddressEn()
            );
        } else {
            eventDetails = String.format(
                    "<p><strong>Mode:</strong> Online and Offline</p>" +
                            "<p><strong>Link:</strong> <a href='%s'>Join Event</a></p>" +
                            "<p><strong>Location:</strong> %s</p>",
                    eventEmailMessage.getLink(),
                    eventEmailMessage.getAddress().getAddressEn()
            );
        }

        return String.format(
                "<html>" +
                        "<head>" +
                        "<style>" +
                        "body { font-family: Arial, sans-serif; line-height: 1.6; background-color: #f4f4f4; }" +
                        ".container { width: 80%%; margin: 20px auto; padding: 20px; border: 1px solid #ddd; border-radius: 10px; background-color: #fff; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }" +
                        "h2 { color: #333; }" +
                        ".details { margin-top: 20px; }" +
                        ".details p { margin: 5px 0; }" +
                        ".footer { margin-top: 30px; padding-top: 10px; border-top: 1px solid #ddd; font-size: 0.9em; color: #777; }" +
                        "</style>" +
                        "</head>" +
                        "<body>" +
                        "<div class='container'>" +
                        "<h2>%s</h2>" +
                        "<p>Dear %s,</p>" +
                        "<p>We are thrilled to inform you about your upcoming event! Here are the details:</p>" +
                        "<div class='details'>" +
                        "<p><strong>Event Title:</strong> %s</p>" +
                        "<p><strong>Description:</strong> %s</p>" +
                        "%s" + // eventDetails
                        "<p><strong>Start Date and Time:</strong> %s</p>" +
                        "<p><strong>End Date and Time:</strong> %s</p>" +
                        "<p><strong>Link to Event:</strong> <a href='%s'>View Event</a></p>" +
                        "</div>" +
                        "<p>We are excited about the event and appreciate your efforts in making it happen. This event promises to be informative and engaging, providing great opportunities for learning and networking.</p>" +
                        "<div class='footer'>" +
                        "<p>Thank you for your dedication.</p>" +
                        "<p>Best regards,<br>GreenCity</p>" +
                        "</div>" +
                        "</div>" +
                        "</body>" +
                        "</html>",
                eventEmailMessage.getSubject(),
                eventEmailMessage.getAuthor(),
                eventEmailMessage.getEventTitle(),
                eventEmailMessage.getDescription(),
                eventDetails, // eventDetails
                eventEmailMessage.getStartDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                eventEmailMessage.getEndDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                eventEmailMessage.getLinkToEvent()
        );
    }

}
