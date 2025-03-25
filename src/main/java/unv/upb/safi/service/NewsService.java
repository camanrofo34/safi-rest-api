package unv.upb.safi.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import unv.upb.safi.domain.dto.request.NewsRequest;
import unv.upb.safi.domain.dto.response.NewsResponse;

import java.util.List;

public interface NewsService {
    @Transactional
    NewsResponse createNews(NewsRequest newsRequest);

    NewsResponse updateNews(Long newsId, NewsRequest newsRequest);

    @Transactional
    void deleteNews(Long id);

    NewsResponse getNews(Long id);

    Page<NewsResponse> getAllNews(Pageable pageable);

    Page<NewsResponse> getNewsByTagsId(List<Long> tagsId, Pageable pageable);

    Page<NewsResponse> getNewsByTitle(String title, Pageable pageable);
}
