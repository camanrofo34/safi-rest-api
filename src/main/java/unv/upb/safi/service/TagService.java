package unv.upb.safi.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import unv.upb.safi.domain.dto.request.TagRequest;
import unv.upb.safi.domain.dto.response.TagResponse;
import unv.upb.safi.domain.entity.Tag;
import unv.upb.safi.exception.entityNotFoundException.TagNotFoundException;
import unv.upb.safi.repository.TagRepository;


@Service
public class TagService {

    private final TagRepository tagRepository;

    private final Logger logger = LoggerFactory.getLogger(TagService.class);

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Transactional
    public TagResponse createTag(TagRequest tagRequest) {
        logger.info("Transaction ID: {}, Adding tag {}",
            MDC.get("transactionId") ,tagRequest.getTagName());

            Tag tag = new Tag();
            tag.setTagName(tagRequest.getTagName());
            tag = tagRepository.save(tag);
            logger.info("Transaction ID: {}, Tag added successfully", MDC.get("transactionId"));
            return mapToResponse(tag);
    }

    public TagResponse updateTag(Long tagId ,TagRequest tagRequest) {
        logger.info("Transaction ID: {}, Updating tag {}",
            MDC.get("transactionId") ,tagRequest.getTagName());
            Tag tag = getTagByIdOrThrow(tagId);
            tag.setTagName(tagRequest.getTagName());
            tag = tagRepository.save(tag);
            logger.info("Transaction ID: {}, Tag updated successfully", MDC.get("transactionId"));
            return mapToResponse(tag);
    }

    public TagResponse getTag(Long tagId) {
        logger.info("Transaction ID: {}, Getting tag with id: {}", MDC.get("transactionId"), tagId);
            Tag tag = getTagByIdOrThrow(tagId);
            return mapToResponse(tag);
    }

    @Transactional
    public void deleteTag(Long tagId) {
        logger.info("Transaction ID: {}, Deleting tag with id: {}", MDC.get("transactionId"), tagId);

        Tag tag = getTagByIdOrThrow(tagId);

        tagRepository.delete(tag);
        logger.info("Transaction ID: {}, Deleted tag {}", MDC.get("transactionId"), tagId);
    }

    public Page<TagResponse> getAllTags(int page, int size, String sortBy, String direction) {
        logger.info("Transaction ID: {}, Getting all tags", MDC.get("transactionId"));

        return tagRepository.findAll(
            PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy))
        ).map(this::mapToResponse);
    }


    private TagResponse mapToResponse(Tag tag) {
        return new TagResponse(
            tag.getTagId(),
            tag.getTagName()
        );
    }

    private Tag getTagByIdOrThrow(Long tagId) {
        return tagRepository.findById(tagId)
            .orElseThrow(() -> new TagNotFoundException(tagId.toString()));
    }
}
