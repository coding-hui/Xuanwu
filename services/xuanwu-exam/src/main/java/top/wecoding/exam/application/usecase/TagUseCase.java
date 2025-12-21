package top.wecoding.exam.application.usecase;

import java.util.List;
import java.util.Optional;

import top.wecoding.exam.domain.tag.Tag;

public interface TagUseCase {

  Tag createTag(Tag tag);

  Tag updateTag(Tag tag);

  Optional<Tag> getTag(Long id);

  Optional<Tag> getTagByName(String name);

  void deleteTag(Long id);

  List<Tag> listTags(String keyword);
}
