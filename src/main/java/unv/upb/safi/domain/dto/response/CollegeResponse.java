package unv.upb.safi.domain.dto.response;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class CollegeResponse {

    private Long collegeId;

    private String name;

    private List<String> facultiesNames;
}
