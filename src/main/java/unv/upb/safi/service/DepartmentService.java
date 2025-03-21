package unv.upb.safi.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unv.upb.safi.domain.dto.request.DepartmentRequest;
import unv.upb.safi.domain.dto.response.DepartmentResponse;
import unv.upb.safi.domain.entity.Department;
import unv.upb.safi.exception.entityNotFoundException.DepartmentNotFoundException;
import unv.upb.safi.repository.DepartmentRepository;

import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final Logger logger = LoggerFactory.getLogger(DepartmentService.class);

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

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
    public void deleteDepartment(Long id) {
        logger.info("Transaction ID: {}, Deleting department with id {}",
                MDC.get("transactionId"), id);

        if (!departmentRepository.existsById(id)) {
            throw new DepartmentNotFoundException(id.toString());
        }

        departmentRepository.deleteById(id);
        logger.info("Transaction ID: {}, Department deleted", MDC.get("transactionId"));
    }

    public DepartmentResponse updateDepartment(Long departmentId, DepartmentRequest departmentRequest) {
        logger.info("Transaction ID: {}, Updating department with id {}",
                MDC.get("transactionId"), departmentId);

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException(departmentId.toString()));

        department.setDepartmentName(departmentRequest.getDepartmentName());
        department = departmentRepository.save(department);

        logger.info("Transaction ID: {}, Department updated", MDC.get("transactionId"));
        return mapToResponse(department);
    }

    public DepartmentResponse getDepartmentById(Long departmentId) {
        logger.info("Transaction ID: {}, Getting department with id {}",
                MDC.get("transactionId"), departmentId);

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException(departmentId.toString()));

        return mapToResponse(department);
    }

    public List<DepartmentResponse> getDepartments() {
        logger.info("Transaction ID: {}, Getting all departments", MDC.get("transactionId"));

        return departmentRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<DepartmentResponse> getDepartmentsByName(String name) {
        logger.info("Transaction ID: {}, Searching departments by name '{}'", MDC.get("transactionId"), name);

        return departmentRepository.findByDepartmentNameIgnoreCase(name).stream()
                .map(this::mapToResponse)
                .toList();
    }

    private DepartmentResponse mapToResponse(Department department) {
        return new DepartmentResponse(
                department.getDepartmentId(),
                department.getDepartmentName()
        );
    }
}

