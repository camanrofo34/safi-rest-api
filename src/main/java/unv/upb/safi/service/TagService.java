package unv.upb.safi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unv.upb.safi.domain.dto.request.TagRequest;
import unv.upb.safi.domain.dto.response.TagResponse;
import unv.upb.safi.domain.entity.Tag;
import unv.upb.safi.exception.entityNotFoundException.TagNotFoundException;
import unv.upb.safi.repository.TagRepository;

import java.util.List;

@Service
public class TagService {

    private final TagRepository tagRepository;

    private final Logger logger = LoggerFactory.getLogger(TagService.class);

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

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
            Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new TagNotFoundException(tagId.toString()));
            tag.setTagName(tagRequest.getTagName());
            tag = tagRepository.save(tag);
            logger.info("Transaction ID: {}, Tag updated successfully", MDC.get("transactionId"));
            return mapToResponse(tag);
    }

    public TagResponse getTag(Long tagId) {
        logger.info("Transaction ID: {}, Getting tag with id: {}", MDC.get("transactionId"), tagId);
            Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new TagNotFoundException(tagId.toString()));
            return mapToResponse(tag);
    }

    public void deleteTag(Long tagId) {
        logger.info("Transaction ID: {}, Deleting tag with id: {}", MDC.get("transactionId"), tagId);
        if (!tagRepository.existsById(tagId)) {
            throw new TagNotFoundException(tagId.toString());
        }
        tagRepository.deleteById(tagId);
        logger.info("Transaction ID: {}, Deleted tag {}", MDC.get("transactionId"), tagId);
    }

    public List<TagResponse> getAllTags() {
        logger.info("Transaction ID: {}, Getting all tags", MDC.get("transactionId"));
            List<Tag> tags = tagRepository.findAll();

            return tags.stream().map(
                this::mapToResponse
            ).toList();
    }


    private TagResponse mapToResponse(Tag tag) {
        return new TagResponse(
            tag.getTagId(),
            tag.getTagName()
        );
    }
}
