package top.wecoding.exam.infrastructure.persistence.tag.converter;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import top.wecoding.exam.domain.tag.Tag;
import top.wecoding.exam.infrastructure.persistence.tag.po.TagPO;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TagConverter {

  TagPO toPO(Tag tag);

  Tag toDomain(TagPO tagPO);
}
