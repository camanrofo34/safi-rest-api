package unv.upb.safi.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import unv.upb.safi.domain.dto.request.TagRequest;
import unv.upb.safi.domain.dto.response.TagResponse;
import unv.upb.safi.service.TagService;
import unv.upb.safi.service.impl.TagServiceImpl;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/tags")
@Validated
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagServiceImpl tagService) {
        this.tagService = tagService;
    }

    @PostMapping
    public ResponseEntity<EntityModel<TagResponse>> createTag(@Valid @RequestBody TagRequest tagRequest) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Adding tag {}", transactionId, tagRequest.getTagName());
        MDC.put("transactionId", transactionId.toString());

        try {
            EntityModel<TagResponse> response = tagService.createTag(tagRequest);
            log.info("Transaction ID: {}, Tag {} added successfully", transactionId, tagRequest.getTagName());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } finally {
            MDC.clear();
        }
    }

    @PutMapping("/{tagId:\\d+}")
    public ResponseEntity<EntityModel<TagResponse>> updateTag(@PathVariable Long tagId, @Valid @RequestBody TagRequest tagRequest) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Updating tag with ID {}", transactionId, tagId);
        MDC.put("transactionId", transactionId.toString());

        try {
            EntityModel<TagResponse> response = tagService.updateTag(tagId, tagRequest);
            log.info("Transaction ID: {}, Tag with ID {} updated successfully", transactionId, tagId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } finally {
            MDC.clear();
        }
    }

    @DeleteMapping("/{tagId:\\d+}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long tagId) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Deleting tag with ID {}", transactionId, tagId);
        MDC.put("transactionId", transactionId.toString());

        try {
            tagService.deleteTag(tagId);
            log.info("Transaction ID: {}, Tag with ID {} deleted successfully", transactionId, tagId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } finally {
            MDC.clear();
        }
    }

    @GetMapping("/{tagId:\\d+}")
    public ResponseEntity<EntityModel<TagResponse>> getTag(@PathVariable Long tagId) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Fetching tag with ID {}", transactionId, tagId);
        MDC.put("transactionId", transactionId.toString());

        try {
            EntityModel<TagResponse> response = tagService.getTag(tagId);
            log.info("Transaction ID: {}, Tag with ID {} fetched successfully", transactionId, tagId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } finally {
            MDC.clear();
        }
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<TagResponse>>> getAllTags(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Fetching all tags", transactionId);
        MDC.put("transactionId", transactionId.toString());

        try {
            PagedModel<EntityModel<TagResponse>> response = tagService.getAllTags(pageable);
            log.info("Transaction ID: {}, All tags fetched successfully", transactionId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } finally {
            MDC.clear();
        }
    }
}