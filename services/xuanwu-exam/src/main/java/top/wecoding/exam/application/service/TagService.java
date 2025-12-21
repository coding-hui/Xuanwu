package top.wecoding.exam.application.service;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import top.wecoding.exam.application.usecase.TagUseCase;
import top.wecoding.exam.domain.tag.Tag;
import top.wecoding.exam.domain.tag.TagRepository;

@Service
@RequiredArgsConstructor
public class TagService implements TagUseCase {

  private final TagRepository tagRepository;

  @Override
  public Tag createTag(Tag tag) {
    if (tag.getName() != null) {
      tag.setName(tag.getName().toLowerCase());
    }
    return tagRepository.save(tag);
  }

  @Override
  public Tag updateTag(Tag tag) {
    if (tag.getName() != null) {
      tag.setName(tag.getName().toLowerCase());
    }
    return tagRepository.update(tag);
  }

  @Override
  public Optional<Tag> getTag(Long id) {
    return tagRepository.findById(id);
  }

  @Override
  public Optional<Tag> getTagByName(String name) {
    return tagRepository.findByName(name);
  }

  @Override
  public void deleteTag(Long id) {
    tagRepository.deleteById(id);
  }

  @Override
  public List<Tag> listTags(String keyword) {
    return tagRepository.list(keyword);
  }
}
