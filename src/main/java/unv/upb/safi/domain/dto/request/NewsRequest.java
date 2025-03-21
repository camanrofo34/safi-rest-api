package unv.upb.safi.domain.dto.request;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
public class NewsRequest {

    @NotBlank
    private String newsTitle;

    @NotBlank
    private String newsContent;

    @NotBlank
    private String urlNewsImage;

    @NotBlank
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date newsDate;

    @NotNull
    private List<Long> tagsId;
}
