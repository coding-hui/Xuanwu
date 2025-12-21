package top.wecoding.exam.infrastructure.persistence.tag;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import top.wecoding.exam.domain.tag.Tag;
import top.wecoding.exam.domain.tag.TagRepository;
import top.wecoding.exam.infrastructure.persistence.tag.converter.TagConverter;
import top.wecoding.exam.infrastructure.persistence.tag.mapper.TagMapper;
import top.wecoding.exam.infrastructure.persistence.tag.po.TagPO;
import top.wecoding.xuanwu.core.exception.ServerException;
import top.wecoding.xuanwu.core.exception.SystemErrorCode;

@Primary
@Repository
@RequiredArgsConstructor
public class MyBatisTagRepository implements TagRepository {

  private final TagMapper tagMapper;

  private final TagConverter tagConverter;

  @Override
  public Tag save(Tag tag) {
    if (tag.getId() != null) {
      return this.update(tag);
    }
    TagPO po = tagConverter.toPO(tag);
    po.setCreatedAt(LocalDateTime.now());
    po.setUpdatedAt(LocalDateTime.now());
    int rows = tagMapper.insert(po);
    if (rows == 0) {
      throw new ServerException(SystemErrorCode.DATABASE_ERROR);
    }
    tag.setId(po.getId());
    return tagConverter.toDomain(po);
  }

  @Override
  public Tag update(Tag tag) {
    TagPO po = tagConverter.toPO(tag);
    po.setUpdatedAt(LocalDateTime.now());
    int rows = tagMapper.update(po);
    if (rows == 0) {
      throw new ServerException(SystemErrorCode.DATABASE_ERROR);
    }
    return tagConverter.toDomain(po);
  }

  @Override
  public Optional<Tag> findById(Long id) {
    return tagMapper.selectById(id).map(tagConverter::toDomain);
  }

  @Override
  public Optional<Tag> findByName(String name) {
    return tagMapper.selectByName(name).map(tagConverter::toDomain);
  }

  @Override
  public void deleteById(Long id) {
    tagMapper.deleteById(id);
  }

  @Override
  public List<Tag> list(String keyword) {
    return tagMapper.list(keyword).stream()
        .map(tagConverter::toDomain)
        .collect(java.util.stream.Collectors.toList());
  }
}
