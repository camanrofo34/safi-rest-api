package unv.upb.safi.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import unv.upb.safi.controller.TagController;
import unv.upb.safi.domain.dto.request.TagRequest;
import unv.upb.safi.domain.dto.response.TagResponse;
import unv.upb.safi.domain.entity.Tag;
import unv.upb.safi.exception.entityNotFoundException.TagNotFoundException;
import unv.upb.safi.repository.TagRepository;
import unv.upb.safi.service.TagService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    private PagedResourcesAssembler<TagResponse> pagedResourcesAssembler;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Autowired
    public void setPagedResourcesAssembler(PagedResourcesAssembler<TagResponse> pagedResourcesAssembler) {
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @Transactional
    @Override
    public EntityModel<TagResponse> createTag(TagRequest tagRequest) {
        Tag tag = new Tag();
        tag.setTagName(tagRequest.getTagName());
        tag = tagRepository.save(tag);

        return mapToResponse(tag);
    }

    @Override
    public EntityModel<TagResponse> updateTag(Long tagId, TagRequest tagRequest) {
        Tag tag = getTagByIdOrThrow(tagId);
        tag.setTagName(tagRequest.getTagName());
        tag = tagRepository.save(tag);

        return mapToResponse(tag);
    }

    @Override
    public EntityModel<TagResponse> getTag(Long tagId) {
        Tag tag = getTagByIdOrThrow(tagId);

        return mapToResponse(tag);
    }

    @Transactional
    @Override
    public void deleteTag(Long tagId) {

        Tag tag = getTagByIdOrThrow(tagId);

        tagRepository.delete(tag);
    }

    @Override
    public PagedModel<EntityModel<TagResponse>> getAllTags(Pageable pageable) {

        Page<TagResponse> tagResponses = tagRepository.findAll(pageable)
        .map(
                tag -> new TagResponse(
                    tag.getTagId(),
                    tag.getTagName()
                )
        );

        return pagedResourcesAssembler.toModel(tagResponses, this::mapToEntityModelToResourceModel);
    }


    private EntityModel<TagResponse> mapToResponse(Tag tag) {
        TagResponse tagResponse = new TagResponse(
            tag.getTagId(),
            tag.getTagName()
        );

        return mapToEntityModel(tagResponse);
    }

    private EntityModel<TagResponse> mapToEntityModel(TagResponse tagResponse) {
        return EntityModel.of(tagResponse,
                linkTo(methodOn(TagController.class).getTag(tagResponse.getTagId())).withSelfRel(),
                linkTo(methodOn(TagController.class).deleteTag(tagResponse.getTagId())).withRel("delete-tag")
        );
    }

    private EntityModel<TagResponse> mapToEntityModelToResourceModel(TagResponse tagResponse) {
        return EntityModel.of(tagResponse,
                linkTo(methodOn(TagController.class).getTag(tagResponse.getTagId())).withSelfRel()
        );
    }

    private Tag getTagByIdOrThrow(Long tagId) {
        return tagRepository.findById(tagId)
            .orElseThrow(() -> new TagNotFoundException(tagId.toString()));
    }
}
