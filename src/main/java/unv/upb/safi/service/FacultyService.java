package unv.upb.safi.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import unv.upb.safi.domain.dto.request.FacultyRequest;
import unv.upb.safi.domain.dto.response.FacultyResponse;

public interface FacultyService {
    @Transactional
    FacultyResponse addFaculty(FacultyRequest facultyRequest);

    FacultyResponse updateFaculty(Long facultyId, FacultyRequest facultyRequest);

    @Transactional
    void deleteFaculty(Long id);

    FacultyResponse getFaculty(Long id);

    Page<FacultyResponse> getFacultyByCollegeId(Long collegeId, int page, int size, String sortBy, Sort.Direction direction);
}
