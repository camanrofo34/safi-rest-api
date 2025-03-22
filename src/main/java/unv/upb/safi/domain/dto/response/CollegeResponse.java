package unv.upb.safi.domain.dto.response;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
public class CollegeResponse {

    private Long collegeId;

    private String name;

    private Set<Long> facultiesId;
}
