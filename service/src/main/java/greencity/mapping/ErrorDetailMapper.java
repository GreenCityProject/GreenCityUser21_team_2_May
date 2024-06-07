package greencity.mapping;

import greencity.dto.ErrorDetailDto;
import greencity.exception.exceptions.AccessIsDeniedException;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ErrorDetailMapper {

    @Mapping(target = "cause", expression = "java(\"id\")")
    @Mapping(target = "message", expression = "java(\"Access is denied for user with google token id=[%s]\"" +
            ".formatted(ex.getGoogleToken()))")
    ErrorDetailDto from(AccessIsDeniedException ex);

}
