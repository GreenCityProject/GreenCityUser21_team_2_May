package greencity.dto.violation;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class UserViolationMailDto {

    private static final String EMAIL_REGEX = "[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})";

    @NotNull
    private String name;
    @NotNull
    @Pattern(regexp = EMAIL_REGEX, message = "Email has to be in a valid email format!")
    private String email;
    @NotNull
    private String language;
    private String violationDescription;
}
