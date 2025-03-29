package unv.upb.safi.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@AllArgsConstructor
@Getter
public class PqrsfResponse {

    private Long pqrsfId;

    private Long studentId;

    private String requestType;

    private String description;

    private Date submissionDate;

}
