package unv.upb.safi.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import unv.upb.safi.domain.dto.request.DepartmentRequest;
import unv.upb.safi.domain.dto.response.DepartmentResponse;
import unv.upb.safi.domain.entity.Department;
import unv.upb.safi.exception.entityNotFoundException.DepartmentNotFoundException;
import unv.upb.safi.repository.DepartmentRepository;
import unv.upb.safi.service.DepartmentService;
import unv.upb.safi.util.SearchNormalizerUtil;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    private final SearchNormalizerUtil searchNormalizerUtil;

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository, SearchNormalizerUtil searchNormalizerUtil) {
        this.departmentRepository = departmentRepository;
        this.searchNormalizerUtil = searchNormalizerUtil;
    }

    @Transactional
    @Override
    public DepartmentResponse createDepartment(DepartmentRequest departmentRequest) {

        Department department = new Department();
        department.setDepartmentName(departmentRequest.getDepartmentName());
        department = departmentRepository.save(department);

        return mapToResponse(department);
    }

    @Transactional
    @Override
    public void deleteDepartment(Long id) {

        Department department = getDepartmentByIdOrThrow(id);

        if (!department.getExecutives().isEmpty()) {
            throw new DataIntegrityViolationException("Cannot delete department with associated executives.");
        }

        departmentRepository.deleteById(id);
    }

    @Override
    public DepartmentResponse updateDepartment(Long departmentId, DepartmentRequest departmentRequest) {

        Department department = getDepartmentByIdOrThrow(departmentId);

        department.setDepartmentName(departmentRequest.getDepartmentName());
        department = departmentRepository.save(department);

        return mapToResponse(department);
    }

    @Override
    public DepartmentResponse getDepartmentById(Long departmentId) {

        Department department = getDepartmentByIdOrThrow(departmentId);

        return mapToResponse(department);
    }

    @Override
    public Page<DepartmentResponse> getDepartments(Pageable pageable) {

        return departmentRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Override
    public Page<DepartmentResponse> getDepartmentsByName(String name, Pageable pageable) {

        return departmentRepository.findByDepartmentNameContainingIgnoreCase(name, pageable)
                .map(this::mapToResponse);
    }

    private DepartmentResponse mapToResponse(Department department) {
        return new DepartmentResponse(
                department.getDepartmentId(),
                department.getDepartmentName()
        );
    }

    private Department getDepartmentByIdOrThrow(Long departmentId) {
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException(departmentId.toString()));
    }
}

