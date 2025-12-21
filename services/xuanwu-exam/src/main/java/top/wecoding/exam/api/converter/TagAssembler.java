package top.wecoding.exam.api.converter;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import top.wecoding.exam.api.dto.tag.CreateTagRequest;
import top.wecoding.exam.api.dto.tag.TagResponse;
import top.wecoding.exam.api.dto.tag.UpdateTagRequest;
import top.wecoding.exam.domain.tag.Tag;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TagAssembler {

  Tag toDomain(CreateTagRequest request);

  Tag toDomain(UpdateTagRequest request);

  TagResponse toResponse(Tag tag);
}
