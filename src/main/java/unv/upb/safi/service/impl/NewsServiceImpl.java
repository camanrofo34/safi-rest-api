package unv.upb.safi.service.impl;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import unv.upb.safi.domain.dto.request.NewsRequest;
import unv.upb.safi.domain.dto.response.NewsResponse;
import unv.upb.safi.domain.entity.News;
import unv.upb.safi.domain.entity.Tag;
import unv.upb.safi.exception.entityNotFoundException.NewsNotFoundException;
import unv.upb.safi.repository.NewsRepository;
import unv.upb.safi.repository.TagRepository;
import unv.upb.safi.service.NewsService;

import java.util.*;

@Service
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final TagRepository tagRepository;
    private final Logger logger = LoggerFactory.getLogger(NewsServiceImpl.class);

    @Autowired
    public NewsServiceImpl(NewsRepository newsRepository, TagRepository tagRepository) {
        this.newsRepository = newsRepository;
        this.tagRepository = tagRepository;
    }

    @Transactional
    @Override
    public NewsResponse createNews(NewsRequest newsRequest) {
        logger.info("Transaction ID: {}, Adding news {}", MDC.get("transactionId"), newsRequest.getNewsTitle());
        News news = new News();
        news.setNewsTitle(newsRequest.getNewsTitle());
        news.setNewsContent(newsRequest.getNewsContent());
        news.setNewsDate(newsRequest.getNewsDate());
        news.setUrlNewsImage(newsRequest.getUrlNewsImage());

        Set<Tag> tags = new HashSet<>(tagRepository.findAllById(newsRequest.getTagsId()));
        news.setTags(tags);

        news = newsRepository.save(news);
        logger.info("Transaction ID: {}, News added successfully", MDC.get("transactionId"));
        return mapToResponse(news);
    }

    @Override
    public NewsResponse updateNews(Long newsId, NewsRequest newsRequest) {
        logger.info("Transaction ID: {}, Updating news with id {}", MDC.get("transactionId"), newsId);
        News news = getNewsByIdOrThrow(newsId);

        news.setNewsTitle(newsRequest.getNewsTitle());
        news.setNewsContent(newsRequest.getNewsContent());
        news.setNewsDate(newsRequest.getNewsDate());
        news.setUrlNewsImage(newsRequest.getUrlNewsImage());

        Set<Tag> tags = new HashSet<>(tagRepository.findAllById(newsRequest.getTagsId()));
        news.setTags(tags);

        news = newsRepository.save(news);
        logger.info("Transaction ID: {}, News updated successfully", MDC.get("transactionId"));
        return mapToResponse(news);
    }

    @Transactional
    @Override
    public void deleteNews(Long id) {
        logger.info("Transaction ID: {}, Deleting news with id {}", MDC.get("transactionId"), id);

        News news = getNewsByIdOrThrow(id);

        newsRepository.delete(news);
    }

    @Override
    public NewsResponse getNews(Long id) {
        logger.info("Transaction ID: {}, Getting news with id {}", MDC.get("transactionId"), id);
            News news = getNewsByIdOrThrow(id);
            logger.info("Transaction ID: {}, News retrieved successfully", MDC.get("transactionId"));
            return mapToResponse(news);
    }

    @Override
    public Page<NewsResponse> getAllNews(int page, int size, String sortBy, Sort.Direction direction) {
        logger.info("Transaction ID: {}, Getting all news", MDC.get("transactionId"));

        return newsRepository.findAll(PageRequest.of(page, size, Sort.by(direction, sortBy)))
                .map(this::mapToResponse);
    }

    @Override
    public Page<NewsResponse> getNewsByTagsId(List<Long> tagsId, int page, int size, String sortBy, Sort.Direction direction) {
        logger.info("Transaction ID: {}, Getting all news by tags id", MDC.get("transactionId"));
            Set<Tag> tags = new HashSet<>(tagRepository.findAllById(tagsId));
            return newsRepository.findAllByTags(
                    tags,
                    PageRequest.of(page, size, Sort.by(direction, sortBy))
            ).map(this::mapToResponse);
    }

    private NewsResponse mapToResponse(News news) {
        return new NewsResponse(
                news.getNewsId(),
                news.getNewsTitle(),
                news.getNewsContent(),
                news.getUrlNewsImage(),
                news.getNewsDate(),
                news.getTags().stream().map(Tag::getTagName).toList()
        );
    }

    private News getNewsByIdOrThrow(Long newsId) {
        return newsRepository.findById(newsId)
                .orElseThrow(() -> new NewsNotFoundException(newsId.toString()));
    }
}

