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
import unv.upb.safi.domain.dto.request.NewsRequest;
import unv.upb.safi.domain.dto.response.NewsResponse;
import unv.upb.safi.service.NewsService;
import unv.upb.safi.service.impl.NewsServiceImpl;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/news")
@Validated
public class NewsController {

    private final NewsService newsService;

    @Autowired
    public NewsController(NewsServiceImpl newsService) {
        this.newsService = newsService;
    }

    @PostMapping
    public ResponseEntity<EntityModel<NewsResponse>> createNews(@Valid @RequestBody NewsRequest newsRequest) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Adding news {}", transactionId, newsRequest.getNewsTitle());
        MDC.put("transactionId", transactionId.toString());

        try {
            EntityModel<NewsResponse> response = newsService.createNews(newsRequest);
            log.info("Transaction ID: {}, News {} added successfully", transactionId, newsRequest.getNewsTitle());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } finally {
            MDC.clear();
        }
    }

    @PutMapping("/{newsId:\\d+}")
    public ResponseEntity<EntityModel<NewsResponse>> updateNews(@PathVariable Long newsId, @Valid @RequestBody NewsRequest newsRequest) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Updating news with ID {}", transactionId, newsId);
        MDC.put("transactionId", transactionId.toString());

        try {
            EntityModel<NewsResponse> response = newsService.updateNews(newsId, newsRequest);
            log.info("Transaction ID: {}, News with ID {} updated successfully", transactionId, newsId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } finally {
            MDC.clear();
        }
    }

    @DeleteMapping("/{newsId:\\d+}")
    public ResponseEntity<Void> deleteNews(@PathVariable Long newsId) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Deleting news with ID {}", transactionId, newsId);
        MDC.put("transactionId", transactionId.toString());

        try {
            newsService.deleteNews(newsId);
            log.info("Transaction ID: {}, News with ID {} deleted successfully", transactionId, newsId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } finally {
            MDC.clear();
        }
    }

    @GetMapping("/{newsId:\\d+}")
    public ResponseEntity<EntityModel<NewsResponse>> getNews(@PathVariable Long newsId) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Fetching news with ID {}", transactionId, newsId);
        MDC.put("transactionId", transactionId.toString());

        try {
            EntityModel<NewsResponse> response = newsService.getNews(newsId);
            log.info("Transaction ID: {}, News with ID {} fetched successfully", transactionId, newsId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } finally {
            MDC.clear();
        }
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<NewsResponse>>> getAllNews(
            @PageableDefault(size = 10, sort = "newsId") Pageable pageable) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Fetching all news", transactionId);
        MDC.put("transactionId", transactionId.toString());

        try {
            PagedModel<EntityModel<NewsResponse>> response = newsService.getAllNews(pageable);
            log.info("Transaction ID: {}, All news fetched successfully", transactionId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } finally {
            MDC.clear();
        }
    }

    @GetMapping("/tags")
    public ResponseEntity<PagedModel<EntityModel<NewsResponse>>> getNewsByTagsId(
            @RequestParam List<Long> tagsId,
            @PageableDefault(size = 10, sort = "newsId") Pageable pageable) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Fetching news by tags ID", transactionId);
        MDC.put("transactionId", transactionId.toString());

        try {
            PagedModel<EntityModel<NewsResponse>> response = newsService.getNewsByTagsId(tagsId, pageable);
            log.info("Transaction ID: {}, News by tags ID fetched successfully", transactionId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } finally {
            MDC.clear();
        }
    }
}