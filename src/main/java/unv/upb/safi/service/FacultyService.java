package unv.upb.safi.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import unv.upb.safi.domain.dto.request.FacultyRequest;
import unv.upb.safi.domain.dto.response.FacultyResponse;

public interface FacultyService {
    @Transactional
    EntityModel<FacultyResponse> addFaculty(FacultyRequest facultyRequest);

    EntityModel<FacultyResponse> updateFaculty(Long facultyId, FacultyRequest facultyRequest);

    @Transactional
    void deleteFaculty(Long id);

    EntityModel<FacultyResponse> getFaculty(Long id);

    PagedModel<EntityModel<FacultyResponse>> getFacultiesByCollegeId(Long collegeId, Pageable pageable);

    PagedModel<EntityModel<FacultyResponse>> getFacultiesByName(String name, Pageable pageable);
}
