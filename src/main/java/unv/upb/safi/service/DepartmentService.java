package unv.upb.safi.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import unv.upb.safi.domain.dto.request.DepartmentRequest;
import unv.upb.safi.domain.dto.response.DepartmentResponse;

public interface DepartmentService {
    @Transactional
    EntityModel<DepartmentResponse> createDepartment(DepartmentRequest departmentRequest);

    @Transactional
    void deleteDepartment(Long id);

    EntityModel<DepartmentResponse> updateDepartment(Long departmentId, DepartmentRequest departmentRequest);

    EntityModel<DepartmentResponse> getDepartmentById(Long departmentId);

    PagedModel<EntityModel<DepartmentResponse>> getDepartments(Pageable pageable);

    PagedModel<EntityModel<DepartmentResponse>> getDepartmentsByName(String name, Pageable pageable);
}
