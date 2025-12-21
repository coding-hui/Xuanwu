package top.wecoding.exam.domain.tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository {

  Tag save(Tag tag);

  Tag update(Tag tag);

  Optional<Tag> findById(Long id);

  Optional<Tag> findByName(String name);

  void deleteById(Long id);

  List<Tag> list(String keyword);
}
