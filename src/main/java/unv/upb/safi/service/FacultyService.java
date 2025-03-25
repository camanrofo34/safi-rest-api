package unv.upb.safi.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    Page<FacultyResponse> getFacultiesByCollegeId(Long collegeId, Pageable pageable);

    Page<FacultyResponse> getFacultiesByName(String name, Pageable pageable);
}
