package unv.upb.safi.domain.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;
import java.util.List;


@AllArgsConstructor
@Getter
public class NewsResponse {

    private Long newsId;

    private String newsTitle;

    private String newsContent;

    private String urlNewsImage;

    private Date newsDate;

    private List<String> tagsNames;

}
