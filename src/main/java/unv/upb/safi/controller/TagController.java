package unv.upb.safi.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import unv.upb.safi.domain.dto.request.TagRequest;
import unv.upb.safi.domain.dto.response.TagResponse;
import unv.upb.safi.service.TagService;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/tags")
@Validated
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
    public ResponseEntity<TagResponse> createTag(@Valid @RequestBody TagRequest tagRequest) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Adding tag {}", transactionId, tagRequest.getTagName());
        MDC.put("transactionId", transactionId.toString());

        try {
            TagResponse tagResponse = tagService.createTag(tagRequest);
            log.info("Transaction ID: {}, Tag {} added successfully", transactionId, tagRequest.getTagName());
            return ResponseEntity.status(HttpStatus.CREATED).body(tagResponse);
        } finally {
            MDC.clear();
        }
    }

    @PutMapping("/{tagId:\\d+}")
    public ResponseEntity<TagResponse> updateTag(@PathVariable Long tagId, @Valid @RequestBody TagRequest tagRequest) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Updating tag with ID {}", transactionId, tagId);
        MDC.put("transactionId", transactionId.toString());

        try {
            TagResponse tagResponse = tagService.updateTag(tagId, tagRequest);
            log.info("Transaction ID: {}, Tag with ID {} updated successfully", transactionId, tagId);
            return ResponseEntity.status(HttpStatus.OK).body(tagResponse);
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
    public ResponseEntity<TagResponse> getTag(@PathVariable Long tagId) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Fetching tag with ID {}", transactionId, tagId);
        MDC.put("transactionId", transactionId.toString());

        try {
            TagResponse tagResponse = tagService.getTag(tagId);
            log.info("Transaction ID: {}, Tag with ID {} fetched successfully", transactionId, tagId);
            return ResponseEntity.status(HttpStatus.OK).body(tagResponse);
        } finally {
            MDC.clear();
        }
    }

    @GetMapping
    public ResponseEntity<Page<TagResponse>> getAllTags(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction
    ) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Fetching all tags", transactionId);
        MDC.put("transactionId", transactionId.toString());

        try {
            Page<TagResponse> tags = tagService.getAllTags(page, size, sortBy, direction);
            log.info("Transaction ID: {}, All tags fetched successfully", transactionId);
            return ResponseEntity.status(HttpStatus.OK).body(tags);
        } finally {
            MDC.clear();
        }
    }
}
