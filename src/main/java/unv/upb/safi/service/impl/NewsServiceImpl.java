package unv.upb.safi.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import unv.upb.safi.controller.NewsController;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final TagRepository tagRepository;
    private final SearchNormalizerUtil searchNormalizerUtil;

    private PagedResourcesAssembler<NewsResponse> pagedResourcesAssembler;

    @Autowired
    public NewsServiceImpl(NewsRepository newsRepository,
                           TagRepository tagRepository,
                           SearchNormalizerUtil searchNormalizerUtil) {
        this.newsRepository = newsRepository;
        this.tagRepository = tagRepository;
        this.searchNormalizerUtil = searchNormalizerUtil;
    }

    @Autowired
    public void setPagedResourcesAssembler(PagedResourcesAssembler<NewsResponse> pagedResourcesAssembler) {
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @Transactional
    @Override
    public EntityModel<NewsResponse> createNews(NewsRequest newsRequest) {
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
    public EntityModel<NewsResponse> updateNews(Long newsId, NewsRequest newsRequest) {
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
    public EntityModel<NewsResponse> getNews(Long id) {
        News news = getNewsByIdOrThrow(id);

        return mapToResponse(news);
    }

    @Override
    public PagedModel<EntityModel<NewsResponse>> getAllNews(Pageable pageable) {
        Page<NewsResponse> responsePage = newsRepository.findAll(pageable)
                .map(
                        newsResponse -> new NewsResponse(
                                newsResponse.getNewsId(),
                                newsResponse.getNewsTitle(),
                                newsResponse.getNewsContent(),
                                newsResponse.getUrlNewsImage(),
                                newsResponse.getNewsDate(),
                                newsResponse.getTags().stream().map(Tag::getTagName).toList()
                        )
                );
        return pagedResourcesAssembler.toModel(responsePage, this::mapToEntityModelToResourceModel);
    }

    @Override
    public PagedModel<EntityModel<NewsResponse>> getNewsByTagsId(List<Long> tagsId, Pageable pageable) {
        Set<Tag> tags = new HashSet<>(tagRepository.findAllById(tagsId));

        Page<NewsResponse> responsePage = newsRepository.findAllByTags(tags, pageable)
                .map(
                        newsResponse -> new NewsResponse(
                                newsResponse.getNewsId(),
                                newsResponse.getNewsTitle(),
                                newsResponse.getNewsContent(),
                                newsResponse.getUrlNewsImage(),
                                newsResponse.getNewsDate(),
                                newsResponse.getTags().stream().map(Tag::getTagName).toList()
                        )
                );

        return pagedResourcesAssembler.toModel(responsePage, this::mapToEntityModelToResourceModel);
    }

    @Override
    public PagedModel<EntityModel<NewsResponse>> getNewsByTitle(String title, Pageable pageable) {

        Page<NewsResponse> responsePage = newsRepository.findAllByNewsTitleContainingIgnoreCase(
                searchNormalizerUtil.normalize(title), pageable)
                .map(
                        newsResponse -> new NewsResponse(
                                newsResponse.getNewsId(),
                                newsResponse.getNewsTitle(),
                                newsResponse.getNewsContent(),
                                newsResponse.getUrlNewsImage(),
                                newsResponse.getNewsDate(),
                                newsResponse.getTags().stream().map(Tag::getTagName).toList()
                        )
                );

        return pagedResourcesAssembler.toModel(responsePage, this::mapToEntityModelToResourceModel);

    }

    private EntityModel<NewsResponse> mapToResponse(News news) {
        NewsResponse newsResponse = new NewsResponse(
                news.getNewsId(),
                news.getNewsTitle(),
                news.getNewsContent(),
                news.getUrlNewsImage(),
                news.getNewsDate(),
                news.getTags().stream().map(Tag::getTagName).toList()
        );

        return mapToEntityModel(newsResponse);
    }

    private EntityModel<NewsResponse> mapToEntityModel(NewsResponse newsResponse) {
        EntityModel<NewsResponse> response = EntityModel.of(newsResponse);

        response.add(
                linkTo(
                        methodOn(NewsController.class).getNews(newsResponse.getNewsId()))
                        .withSelfRel(),
                linkTo(
                        methodOn(NewsController.class).deleteNews(newsResponse.getNewsId()))
                        .withRel("delete-news")
        );
        return response;
    }
    private EntityModel<NewsResponse> mapToEntityModelToResourceModel(NewsResponse newsResponse) {
        EntityModel<NewsResponse> response = EntityModel.of(newsResponse);

        response.add(
                linkTo(
                        methodOn(NewsController.class).getNews(newsResponse.getNewsId()))
                        .withSelfRel()
        );
        return response;
    }


    private News getNewsByIdOrThrow(Long newsId) {
        return newsRepository.findById(newsId)
                .orElseThrow(() -> new NewsNotFoundException(newsId.toString()));
    }
}

