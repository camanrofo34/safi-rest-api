package unv.upb.safi.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import unv.upb.safi.controller.DepartmentController;
import unv.upb.safi.domain.dto.request.DepartmentRequest;
import unv.upb.safi.domain.dto.response.DepartmentResponse;
import unv.upb.safi.domain.entity.Department;
import unv.upb.safi.exception.entityNotFoundException.DepartmentNotFoundException;
import unv.upb.safi.repository.DepartmentRepository;
import unv.upb.safi.service.DepartmentService;
import unv.upb.safi.util.SearchNormalizerUtil;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    private final SearchNormalizerUtil searchNormalizerUtil;

    private PagedResourcesAssembler<DepartmentResponse> pagedResourcesAssembler;

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository, SearchNormalizerUtil searchNormalizerUtil) {
        this.departmentRepository = departmentRepository;
        this.searchNormalizerUtil = searchNormalizerUtil;
    }

    @Autowired
    public void setPagedResourcesAssembler(PagedResourcesAssembler<DepartmentResponse> pagedResourcesAssembler) {
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @Transactional
    @Override
    public EntityModel<DepartmentResponse> createDepartment(DepartmentRequest departmentRequest) {

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
    public EntityModel<DepartmentResponse> updateDepartment(Long departmentId, DepartmentRequest departmentRequest) {

        Department department = getDepartmentByIdOrThrow(departmentId);

        department.setDepartmentName(departmentRequest.getDepartmentName());
        department = departmentRepository.save(department);

        return mapToResponse(department);
    }

    @Override
    public EntityModel<DepartmentResponse> getDepartmentById(Long departmentId) {

        Department department = getDepartmentByIdOrThrow(departmentId);

        return mapToResponse(department);
    }

    @Override
    public PagedModel<EntityModel<DepartmentResponse>> getDepartments(Pageable pageable) {

        Page<DepartmentResponse> departmentResponses = departmentRepository.findAll(pageable)
                .map(department ->
                        new DepartmentResponse(
                                department.getDepartmentId(),
                                department.getDepartmentName()
                        )
                );

        return pagedResourcesAssembler.toModel(departmentResponses, this::mapToEntityModelToResourceModel);
    }

    @Override
    public PagedModel<EntityModel<DepartmentResponse>> getDepartmentsByName(String name, Pageable pageable) {

        Page<DepartmentResponse> departmentResponses = departmentRepository.findAllByDepartmentNameContainingIgnoreCase(
                searchNormalizerUtil.normalize(name), pageable)
                .map(department ->
                        new DepartmentResponse(
                                department.getDepartmentId(),
                                department.getDepartmentName()
                        )
                );

        return pagedResourcesAssembler.toModel(departmentResponses, this::mapToEntityModelToResourceModel);
    }

    private EntityModel<DepartmentResponse> mapToResponse(Department department) {
        DepartmentResponse departmentResponse = new DepartmentResponse(
                department.getDepartmentId(),
                department.getDepartmentName()
        );

        return mapToEntityModel(departmentResponse);
    }

    private EntityModel<DepartmentResponse> mapToEntityModel(DepartmentResponse departmentResponse) {
        return EntityModel.of(departmentResponse,
                linkTo(methodOn(DepartmentController.class).getById(departmentResponse.getDepartmentId())).withSelfRel(),
                linkTo(methodOn(DepartmentController.class).delete(departmentResponse.getDepartmentId())).withRel("delete-department")
        );
    }

    private EntityModel<DepartmentResponse> mapToEntityModelToResourceModel(DepartmentResponse departmentResponse) {
        return EntityModel.of(departmentResponse,
                linkTo(methodOn(DepartmentController.class).getById(departmentResponse.getDepartmentId())).withSelfRel()
        );
    }

    private Department getDepartmentByIdOrThrow(Long departmentId) {
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException(departmentId.toString()));
    }
}

