package unv.upb.safi.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import unv.upb.safi.domain.dto.request.TagRequest;
import unv.upb.safi.domain.dto.response.TagResponse;

public interface TagService {
    @Transactional
    EntityModel<TagResponse> createTag(TagRequest tagRequest);

    EntityModel<TagResponse> updateTag(Long tagId, TagRequest tagRequest);

    EntityModel<TagResponse> getTag(Long tagId);

    @Transactional
    void deleteTag(Long tagId);

    PagedModel<EntityModel<TagResponse>> getAllTags(Pageable pageable);
}
