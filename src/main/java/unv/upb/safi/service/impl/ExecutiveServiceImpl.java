package unv.upb.safi.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import unv.upb.safi.controller.ExecutiveController;
import unv.upb.safi.domain.dto.request.ExecutiveRequest;
import unv.upb.safi.domain.dto.response.ExecutiveResponse;
import unv.upb.safi.domain.entity.Department;
import unv.upb.safi.domain.entity.Executive;
import unv.upb.safi.domain.entity.Role;
import unv.upb.safi.domain.entity.User;
import unv.upb.safi.exception.entityNotFoundException.DepartmentNotFoundException;
import unv.upb.safi.repository.DepartmentRepository;
import unv.upb.safi.repository.ExecutiveRepository;
import unv.upb.safi.repository.RoleRepository;
import unv.upb.safi.repository.UserRepository;
import unv.upb.safi.service.ExecutiveService;
import unv.upb.safi.util.SearchNormalizerUtil;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class ExecutiveServiceImpl implements ExecutiveService {

    private final ExecutiveRepository executiveRepository;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final DepartmentRepository departmentRepository;

    private final SearchNormalizerUtil searchNormalizerUtil;

    private PagedResourcesAssembler<ExecutiveResponse> pagedResourcesAssembler;

    @Autowired
    public ExecutiveServiceImpl(ExecutiveRepository executiveRepository,
                                UserRepository userRepository,
                                RoleRepository roleRepository,
                                DepartmentRepository departmentRepository,
                                SearchNormalizerUtil searchNormalizerUtil) {
        this.executiveRepository = executiveRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.departmentRepository = departmentRepository;
        this.searchNormalizerUtil = searchNormalizerUtil;
    }

    @Autowired
    public void setPagedResourcesAssembler(PagedResourcesAssembler<ExecutiveResponse> pagedResourcesAssembler) {
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @Transactional
    @Override
    public EntityModel<ExecutiveResponse> createExecutive(ExecutiveRequest executiveRequest) {
        User user = mapToUser(executiveRequest);

        user = userRepository.save(user);

        Department department = getDepartmentByIdOrThrow(executiveRequest.getDepartmentId());
        Executive executive = new Executive();
        executive.setUser(user);
        executive.setDepartment(department);
        executive = executiveRepository.save(executive);

        return mapToResponse(executive);
    }

    @Override
    public EntityModel<ExecutiveResponse> updateExecutive(Long executiveId, ExecutiveRequest executiveRequest) {

        Executive executive = getExecutiveByIdOrThrow(executiveId);
        User user = mapToUser(executiveRequest);
        userRepository.save(user);

        Department department = getDepartmentByIdOrThrow(executiveRequest.getDepartmentId());

        executive.setDepartment(department);
        executive = executiveRepository.save(executive);

        return mapToResponse(executive);
    }

    @Transactional
    @Override
    public void deleteExecutive(Long id) {
        Executive executive = getExecutiveByIdOrThrow(id);

        executiveRepository.delete(executive);
    }

    @Override
    public EntityModel<ExecutiveResponse> getExecutive(Long id) {

        Executive executive = getExecutiveByIdOrThrow(id);

        return mapToResponse(executive);
    }

    @Override
    public PagedModel<EntityModel<ExecutiveResponse>> getExecutives(Pageable pageable) {
        Page<ExecutiveResponse> executiveResponses = executiveRepository.findAll(pageable)
                .map(executive ->
                        new ExecutiveResponse(
                                executive.getExecutiveId(),
                                executive.getUser().getFirstName(),
                                executive.getUser().getLastName(),
                                executive.getUser().getEmail(),
                                executive.getUser().getUsername(),
                                executive.getUser().getRoles().stream().map(Role::getName).toList(),
                                executive.getDepartment().getDepartmentName()
                        ));

        return pagedResourcesAssembler.toModel(executiveResponses, this::mapToEntityModelToResourceModel);
    }

    @Override
    public PagedModel<EntityModel<ExecutiveResponse>> getExecutivesByExecutiveName(String name, Pageable pageable) {

        Page<ExecutiveResponse> executiveResponses = executiveRepository.findAllByExecutiveNameContainingIgnoreCase(
                searchNormalizerUtil.normalize(name), pageable)
                .map(executive ->
                        new ExecutiveResponse(
                                executive.getExecutiveId(),
                                executive.getUser().getFirstName(),
                                executive.getUser().getLastName(),
                                executive.getUser().getEmail(),
                                executive.getUser().getUsername(),
                                executive.getUser().getRoles().stream().map(Role::getName).toList(),
                                executive.getDepartment().getDepartmentName()
                        ));

        return pagedResourcesAssembler.toModel(executiveResponses, this::mapToEntityModelToResourceModel);
    }

    private User mapToUser(ExecutiveRequest executiveRequest) {
        User user = new User();
        user.setUsername(executiveRequest.getUsername());
        user.setPassword(executiveRequest.getPassword());
        user.setFirstName(executiveRequest.getFirstName());
        user.setLastName(executiveRequest.getLastName());
        user.setEmail(executiveRequest.getEmail());
        user.setEnabled(true);
        Set<Role> roles = executiveRequest.getRolesId().stream()
                .map(roleId -> roleRepository.findById(roleId).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        user.setRoles(
                roles
        );
        return user;
    }


    private EntityModel<ExecutiveResponse> mapToResponse(Executive executive) {
        User user = executive.getUser();
        Department department = executive.getDepartment();
        ExecutiveResponse executiveResponse = new ExecutiveResponse(
            executive.getExecutiveId(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getUsername(),
            user.getRoles().stream().map(Role::getName).toList(),
            department.getDepartmentName()
        );

        return mapToEntityModel(executiveResponse);
    }

    private EntityModel<ExecutiveResponse> mapToEntityModel(ExecutiveResponse executiveResponse) {
        return EntityModel.of(
                executiveResponse,
                linkTo(methodOn(ExecutiveController.class).getExecutive(executiveResponse.getExecutiveId())).withSelfRel(),
                linkTo(methodOn(ExecutiveController.class).deleteExecutive(executiveResponse.getExecutiveId())).withRel("delete-executive")
        );
    }

    private EntityModel<ExecutiveResponse> mapToEntityModelToResourceModel(ExecutiveResponse executiveResponse) {
        return EntityModel.of(
                executiveResponse,
                linkTo(methodOn(ExecutiveController.class).getExecutive(executiveResponse.getExecutiveId())).withSelfRel()
        );
    }

    private Executive getExecutiveByIdOrThrow(Long executiveId) {
        return executiveRepository.findById(executiveId)
                .orElseThrow(() -> new DepartmentNotFoundException(executiveId.toString()));
    }

    private Department getDepartmentByIdOrThrow(Long departmentId) {
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException(departmentId.toString()));
    }

}
