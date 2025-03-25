package unv.upb.safi.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import unv.upb.safi.domain.dto.request.NewsRequest;
import unv.upb.safi.domain.dto.response.NewsResponse;
import unv.upb.safi.domain.entity.News;
import unv.upb.safi.domain.entity.Tag;
import unv.upb.safi.exception.entityNotFoundException.NewsNotFoundException;
import unv.upb.safi.repository.NewsRepository;
import unv.upb.safi.repository.TagRepository;
import unv.upb.safi.service.NewsService;
import unv.upb.safi.util.SearchNormalizerUtil;

import java.util.*;

@Service
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final TagRepository tagRepository;
    private final SearchNormalizerUtil searchNormalizerUtil;

    @Autowired
    public NewsServiceImpl(NewsRepository newsRepository,
                           TagRepository tagRepository,
                           SearchNormalizerUtil searchNormalizerUtil) {
        this.newsRepository = newsRepository;
        this.tagRepository = tagRepository;
        this.searchNormalizerUtil = searchNormalizerUtil;
    }

    @Transactional
    @Override
    public NewsResponse createNews(NewsRequest newsRequest) {
        News news = new News();
        news.setNewsTitle(newsRequest.getNewsTitle());
        news.setNewsContent(newsRequest.getNewsContent());
        news.setNewsDate(newsRequest.getNewsDate());
        news.setUrlNewsImage(newsRequest.getUrlNewsImage());

        Set<Tag> tags = new HashSet<>(tagRepository.findAllById(newsRequest.getTagsId()));
        news.setTags(tags);

        news = newsRepository.save(news);

        return mapToResponse(news);
    }

    @Override
    public NewsResponse updateNews(Long newsId, NewsRequest newsRequest) {
        News news = getNewsByIdOrThrow(newsId);

        news.setNewsTitle(newsRequest.getNewsTitle());
        news.setNewsContent(newsRequest.getNewsContent());
        news.setNewsDate(newsRequest.getNewsDate());
        news.setUrlNewsImage(newsRequest.getUrlNewsImage());

        Set<Tag> tags = new HashSet<>(tagRepository.findAllById(newsRequest.getTagsId()));
        news.setTags(tags);

        news = newsRepository.save(news);
        return mapToResponse(news);
    }

    @Transactional
    @Override
    public void deleteNews(Long id) {
        News news = getNewsByIdOrThrow(id);

        newsRepository.delete(news);
    }

    @Override
    public NewsResponse getNews(Long id) {
        News news = getNewsByIdOrThrow(id);

        return mapToResponse(news);
    }

    @Override
    public Page<NewsResponse> getAllNews(Pageable pageable) {
        return newsRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Override
    public Page<NewsResponse> getNewsByTagsId(List<Long> tagsId, Pageable pageable) {
        Set<Tag> tags = new HashSet<>(tagRepository.findAllById(tagsId));
        return newsRepository.findAllByTags(tags, pageable).map(this::mapToResponse);
    }

    @Override
    public Page<NewsResponse> getNewsByTitle(String title, Pageable pageable) {
        return newsRepository.findByNewsTitleContainingIgnoreCase(searchNormalizerUtil.normalize(title), pageable)
                .map(this::mapToResponse);
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

