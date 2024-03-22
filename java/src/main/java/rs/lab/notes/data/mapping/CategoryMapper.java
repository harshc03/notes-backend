package rs.lab.notes.data.mapping;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import rs.lab.notes.data.model.Category;
import rs.lab.notes.dto.CategoryDto;
import rs.lab.notes.dto.PageDto;

@Component
public class CategoryMapper {

    private final ModelMapper modelMapper = new ModelMapper();

    public CategoryMapper() {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public CategoryDto toCategoryDto(Category category) {
        return modelMapper.map(category, CategoryDto.class);
    }

    public PageDto<CategoryDto> toPageDto(Page<Category> categories) {
        var pageDtoType = new TypeToken<PageDto<CategoryDto>>() {
        }.getType();
        return modelMapper.map(categories, pageDtoType);
    }
}
