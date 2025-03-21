package unv.upb.safi.domain.dto.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
public class NewsResponse {

    private Long newsId;

    private String newsTitle;

    private String newsContent;

    private String urlNewsImage;

    private Date newsDate;

    private List<String> tagsNames;
}
