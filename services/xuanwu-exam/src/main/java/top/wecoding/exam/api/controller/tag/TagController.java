package top.wecoding.exam.api.controller.tag;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.wecoding.exam.api.converter.TagAssembler;
import top.wecoding.exam.api.dto.tag.CreateTagRequest;
import top.wecoding.exam.api.dto.tag.TagResponse;
import top.wecoding.exam.api.dto.tag.UpdateTagRequest;
import top.wecoding.exam.application.usecase.TagUseCase;
import top.wecoding.exam.domain.exception.ExamErrorCode;
import top.wecoding.exam.domain.tag.Tag;
import top.wecoding.xuanwu.core.base.R;
import top.wecoding.xuanwu.core.exception.BaseUncheckedException;

@RequiredArgsConstructor
@RestController("tagController.v1")
@RequestMapping(value = "/tags")
public class TagController {

  private final TagUseCase tagUseCase;

  private final TagAssembler tagAssembler;

  @PostMapping(version = "1")
  public R<TagResponse> createTag(@RequestBody @Validated CreateTagRequest req) {
    Tag t = tagAssembler.toDomain(req);
    return R.ok(tagAssembler.toResponse(tagUseCase.createTag(t)));
  }

  @PutMapping(value = "/{id}", version = "1")
  public R<TagResponse> updateTag(
      @PathVariable Long id, @RequestBody @Validated UpdateTagRequest req) {
    req.setId(id);
    Tag t = tagAssembler.toDomain(req);
    return R.ok(tagAssembler.toResponse(tagUseCase.updateTag(t)));
  }

  @GetMapping(value = "/{id}", version = "1")
  public R<TagResponse> getTag(@PathVariable Long id) {
    return tagUseCase
        .getTag(id)
        .map(q -> R.ok(tagAssembler.toResponse(q)))
        .orElseThrow(() -> new BaseUncheckedException(ExamErrorCode.TAG_NOT_FOUND));
  }

  @GetMapping(version = "1")
  public R<List<TagResponse>> listTags(
      @RequestParam(value = "keyword", required = false) String keyword) {
    List<TagResponse> list =
        tagUseCase.listTags(keyword).stream()
            .map(tagAssembler::toResponse)
            .collect(Collectors.toList());
    return R.ok(list);
  }

  @DeleteMapping(value = "/{id}", version = "1")
  public R<Void> deleteTag(@PathVariable Long id) {
    tagUseCase.deleteTag(id);
    return R.ok();
  }
}
