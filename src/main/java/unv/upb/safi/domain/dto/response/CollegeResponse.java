package unv.upb.safi.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Getter
public class CollegeResponse {

    private Long collegeId;

    private String name;

    private Set<Long> facultiesId;
}
