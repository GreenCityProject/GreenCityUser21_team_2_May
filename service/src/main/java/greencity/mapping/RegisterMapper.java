package greencity.mapping;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import greencity.dto.user.UserVO;
import greencity.entity.User;
import greencity.security.dto.SuccessSignUpDto;
import greencity.security.dto.ownsecurity.OwnSignUpDto;
import greencity.security.service.OwnSecurityService;
import greencity.security.service.OwnSecurityServiceImpl;
import jakarta.annotation.PostConstruct;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {OwnSecurityService.class})
@Component
public interface RegisterMapper {

    SuccessSignUpDto mapGoogleTokenToSignUpDto(GoogleIdToken.Payload payload);

    OwnSignUpDto mapGoogleTokenToOwnSignUpDto(GoogleIdToken.Payload payload);

    User mapToEntity(SuccessSignUpDto dto, @MappingTarget User user);

    UserVO mapToVO(User user);

}
