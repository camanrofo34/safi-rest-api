package unv.upb.safi.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import unv.upb.safi.domain.dto.request.TagRequest;
import unv.upb.safi.domain.dto.response.TagResponse;

public interface TagService {
    @Transactional
    TagResponse createTag(TagRequest tagRequest);

    TagResponse updateTag(Long tagId, TagRequest tagRequest);

    TagResponse getTag(Long tagId);

    @Transactional
    void deleteTag(Long tagId);

    Page<TagResponse> getAllTags(int page, int size, String sortBy, Sort.Direction direction);
}
