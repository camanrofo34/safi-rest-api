package unv.upb.safi.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import unv.upb.safi.domain.dto.request.DepartmentRequest;
import unv.upb.safi.domain.dto.response.DepartmentResponse;

public interface DepartmentService {
    @Transactional
    DepartmentResponse createDepartment(DepartmentRequest departmentRequest);

    @Transactional
    void deleteDepartment(Long id);

    DepartmentResponse updateDepartment(Long departmentId, DepartmentRequest departmentRequest);

    DepartmentResponse getDepartmentById(Long departmentId);

    Page<DepartmentResponse> getDepartments(int page, int size, String sortBy, Sort.Direction direction);

    Page<DepartmentResponse> getDepartmentsByName(String name, int page, int size, String sortBy, Sort.Direction direction);
}
