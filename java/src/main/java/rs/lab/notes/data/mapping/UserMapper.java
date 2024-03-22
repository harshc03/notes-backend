package rs.lab.notes.data.mapping;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import rs.lab.notes.data.model.Role;
import rs.lab.notes.data.model.RoleEnum;
import rs.lab.notes.data.model.User;
import rs.lab.notes.data.model.UserDetailsView;
import rs.lab.notes.dto.PageDto;
import rs.lab.notes.dto.UserDetailsViewDto;
import rs.lab.notes.dto.UserDto;

@Component
public class UserMapper {

    private final ModelMapper modelMapper = new ModelMapper();

    public UserMapper() {
        Converter<Set<Role>, Set<RoleEnum>> converter = c -> c == null ? null : c.getSource().stream().map(p -> p.getName()).collect(Collectors.toSet());
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.typeMap(User.class, UserDto.class).addMappings(mapper -> {
            mapper.skip(UserDto::setPassword);
            mapper.using(converter).map(User::getRoles, UserDto::setRoles);
        });
        modelMapper.typeMap(UserDetailsView.class, UserDetailsViewDto.class).addMappings(mapper -> {
            mapper.using(converter).map(UserDetailsView::getRoles, UserDetailsViewDto::setRoles);
        });
    }

    public UserDto toUserDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    public PageDto<UserDto> toPageDto(Page<User> users) {
        var pageDtoType = new TypeToken<PageDto<UserDto>>() {
        }.getType();
        return modelMapper.map(users, pageDtoType);
    }

    public Collection<UserDto> toCollectionDto(Collection<User> users) {
        var collectionDtoType = new TypeToken<Collection<UserDto>>() {
        }.getType();
        return modelMapper.map(users, collectionDtoType);
    }

    public PageDto<UserDetailsViewDto> toPageDetailsViewDto(Page<UserDetailsView> users) {
        var pageDtoType = new TypeToken<PageDto<UserDetailsViewDto>>() {
        }.getType();
        return modelMapper.map(users, pageDtoType);
    }
}
