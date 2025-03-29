package unv.upb.safi.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import unv.upb.safi.domain.dto.request.NewsRequest;
import unv.upb.safi.domain.dto.response.NewsResponse;

import java.util.List;

public interface NewsService {
    @Transactional
    EntityModel<NewsResponse> createNews(NewsRequest newsRequest);

    EntityModel<NewsResponse> updateNews(Long newsId, NewsRequest newsRequest);

    @Transactional
    void deleteNews(Long id);

    EntityModel<NewsResponse> getNews(Long id);

    PagedModel<EntityModel<NewsResponse>> getAllNews(Pageable pageable);

    PagedModel<EntityModel<NewsResponse>> getNewsByTagsId(List<Long> tagsId, Pageable pageable);

    PagedModel<EntityModel<NewsResponse>> getNewsByTitle(String title, Pageable pageable);
}
