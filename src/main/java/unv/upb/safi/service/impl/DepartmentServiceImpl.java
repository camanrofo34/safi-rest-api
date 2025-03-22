package unv.upb.safi.service.impl;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import unv.upb.safi.domain.dto.request.DepartmentRequest;
import unv.upb.safi.domain.dto.response.DepartmentResponse;
import unv.upb.safi.domain.entity.Department;
import unv.upb.safi.exception.entityNotFoundException.DepartmentNotFoundException;
import unv.upb.safi.repository.DepartmentRepository;
import unv.upb.safi.service.DepartmentService;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final Logger logger = LoggerFactory.getLogger(DepartmentServiceImpl.class);

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Transactional
    @Override
    public DepartmentResponse createDepartment(DepartmentRequest departmentRequest) {
        logger.info("Transaction ID: {}, Adding department {}",
                MDC.get("transactionId"), departmentRequest.getDepartmentName());

        Department department = new Department();
        department.setDepartmentName(departmentRequest.getDepartmentName());
        department = departmentRepository.save(department);

        logger.info("Transaction ID: {}, Department added", MDC.get("transactionId"));

        return mapToResponse(department);
    }

    @Transactional
    @Override
    public void deleteDepartment(Long id) {
        logger.info("Transaction ID: {}, Deleting department with id {}",
                MDC.get("transactionId"), id);

        Department department = getDepartmentByIdOrThrow(id);

        if (!department.getExecutives().isEmpty()) {
            throw new DataIntegrityViolationException("Cannot delete department with associated executives.");
        }

        departmentRepository.deleteById(id);
        logger.info("Transaction ID: {}, Department deleted", MDC.get("transactionId"));
    }

    @Override
    public DepartmentResponse updateDepartment(Long departmentId, DepartmentRequest departmentRequest) {
        logger.info("Transaction ID: {}, Updating department with id {}",
                MDC.get("transactionId"), departmentId);

        Department department = getDepartmentByIdOrThrow(departmentId);

        department.setDepartmentName(departmentRequest.getDepartmentName());
        department = departmentRepository.save(department);

        logger.info("Transaction ID: {}, Department updated", MDC.get("transactionId"));
        return mapToResponse(department);
    }

    @Override
    public DepartmentResponse getDepartmentById(Long departmentId) {
        logger.info("Transaction ID: {}, Getting department with id {}",
                MDC.get("transactionId"), departmentId);

        Department department = getDepartmentByIdOrThrow(departmentId);

        return mapToResponse(department);
    }

    @Override
    public Page<DepartmentResponse> getDepartments(int page, int size, String sortBy, Sort.Direction direction) {
        logger.info("Transaction ID: {}, Getting all departments", MDC.get("transactionId"));

        return departmentRepository.findAll(
                PageRequest.of(page, size,
                        Sort.by(direction,
                                sortBy)))
                .map(this::mapToResponse);
    }

    @Override
    public Page<DepartmentResponse> getDepartmentsByName(String name, int page, int size, String sortBy, Sort.Direction direction) {
        logger.info("Transaction ID: {}, Searching departments by name '{}'", MDC.get("transactionId"), name);

        return departmentRepository.findByDepartmentNameIgnoreCase(
                name, PageRequest.of(page, size,
                        Sort.by(direction,
                                sortBy)))
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

