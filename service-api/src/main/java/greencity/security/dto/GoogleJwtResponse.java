package greencity.security.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class GoogleJwtResponse {
    private String email;
    private String accessToken;
    private String refreshToken;
    private String expiryDate;
}
