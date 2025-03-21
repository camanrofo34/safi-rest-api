package unv.upb.safi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unv.upb.safi.domain.dto.request.NewsRequest;
import unv.upb.safi.domain.dto.response.NewsResponse;
import unv.upb.safi.domain.entity.News;
import unv.upb.safi.domain.entity.Tag;
import unv.upb.safi.exception.entityNotFoundException.NewsNotFoundException;
import unv.upb.safi.repository.NewsRepository;
import unv.upb.safi.repository.TagRepository;

import java.util.*;

@Service
public class NewsService {

    private final NewsRepository newsRepository;
    private final TagRepository tagRepository;
    private final Logger logger = LoggerFactory.getLogger(NewsService.class);

    @Autowired
    public NewsService(NewsRepository newsRepository, TagRepository tagRepository) {
        this.newsRepository = newsRepository;
        this.tagRepository = tagRepository;
    }

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

    public NewsResponse updateNews(Long newsId, NewsRequest newsRequest) {
        logger.info("Transaction ID: {}, Updating news with id {}", MDC.get("transactionId"), newsId);
            News news = newsRepository.findById(newsId).orElseThrow(() -> new NewsNotFoundException(newsId.toString()));

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

    public void deleteNews(Long id) {
        logger.info("Transaction ID: {}, Deleting news with id {}", MDC.get("transactionId"), id);
            if (!newsRepository.existsById(id)) {
                throw new NewsNotFoundException(id.toString());
            }

        newsRepository.deleteById(id);
    }

    public NewsResponse getNews(Long id) {
        logger.info("Transaction ID: {}, Getting news with id {}", MDC.get("transactionId"), id);
            News news = newsRepository.findById(id).orElseThrow(() -> new NewsNotFoundException(id.toString()));
            logger.info("Transaction ID: {}, News retrieved successfully", MDC.get("transactionId"));
            return mapToResponse(news);
    }

    public List<NewsResponse> getAllNews() {
        logger.info("Transaction ID: {}, Getting all news", MDC.get("transactionId"));

            return newsRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    public List<NewsResponse> getNewsByTagsId(List<Long> tagsId) {
        logger.info("Transaction ID: {}, Getting all news by tags id", MDC.get("transactionId"));
            Set<Tag> tags = new HashSet<>(tagRepository.findAllById(tagsId));
            List<News> news = newsRepository.findAllByTags(tags);
            return news.stream().map(this::mapToResponse).toList();
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
}

