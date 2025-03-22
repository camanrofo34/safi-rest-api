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
import unv.upb.safi.domain.dto.request.NewsRequest;
import unv.upb.safi.domain.dto.response.NewsResponse;
import unv.upb.safi.service.NewsService;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/news")
@Validated
public class NewsController {

    private final NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @PostMapping
    public ResponseEntity<NewsResponse> createNews(@Valid @RequestBody NewsRequest newsRequest) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Adding news {}", transactionId, newsRequest.getNewsTitle());
        MDC.put("transactionId", transactionId.toString());

        try {
            NewsResponse newsResponse = newsService.createNews(newsRequest);
            log.info("Transaction ID: {}, News {} added successfully", transactionId, newsRequest.getNewsTitle());
            return ResponseEntity.status(HttpStatus.CREATED).body(newsResponse);
        } finally {
            MDC.clear();
        }
    }

    @PutMapping("/{newsId:\\d+}")
    public ResponseEntity<NewsResponse> updateNews(@PathVariable Long newsId, @Valid @RequestBody NewsRequest newsRequest) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Updating news with ID {}", transactionId, newsId);
        MDC.put("transactionId", transactionId.toString());

        try {
            NewsResponse newsResponse = newsService.updateNews(newsId, newsRequest);
            log.info("Transaction ID: {}, News with ID {} updated successfully", transactionId, newsId);
            return ResponseEntity.status(HttpStatus.OK).body(newsResponse);
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
    public ResponseEntity<NewsResponse> getNews(@PathVariable Long newsId) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Fetching news with ID {}", transactionId, newsId);
        MDC.put("transactionId", transactionId.toString());

        try {
            NewsResponse newsResponse = newsService.getNews(newsId);
            log.info("Transaction ID: {}, News with ID {} fetched successfully", transactionId, newsId);
            return ResponseEntity.status(HttpStatus.OK).body(newsResponse);
        } finally {
            MDC.clear();
        }
    }

    @GetMapping
    public ResponseEntity<Page<NewsResponse>> getAllNews(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction
    ) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Fetching all news", transactionId);
        MDC.put("transactionId", transactionId.toString());

        try {
            Page<NewsResponse> news = newsService.getAllNews(page, size, sortBy, direction);
            log.info("Transaction ID: {}, All news fetched successfully", transactionId);
            return ResponseEntity.status(HttpStatus.OK).body(news);
        } finally {
            MDC.clear();
        }
    }

    @GetMapping("/tags")
    public ResponseEntity<Page<NewsResponse>> getNewsByTagsId(
            @RequestParam List<Long> tagsId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction
    ) {
        UUID transactionId = UUID.randomUUID();
        log.info("Transaction ID: {}, Fetching news by tags ID", transactionId);
        MDC.put("transactionId", transactionId.toString());

        try {
            Page<NewsResponse> news = newsService.getNewsByTagsId(tagsId, page, size, sortBy, direction);
            log.info("Transaction ID: {}, News by tags ID fetched successfully", transactionId);
            return ResponseEntity.status(HttpStatus.OK).body(news);
        } finally {
            MDC.clear();
        }
    }
}