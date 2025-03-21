package unv.upb.safi.domain.dto.response;

import lombok.AllArgsConstructor;

import java.util.Date;

@AllArgsConstructor
public class PqrsfResponse {

    private Long pqrsfId;

    private Long studentId;

    private String requestType;

    private String description;

    private Date submissionDate;

}
