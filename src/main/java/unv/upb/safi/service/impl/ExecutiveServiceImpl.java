package unv.upb.safi.service.impl;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
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

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExecutiveServiceImpl implements ExecutiveService {

    private final ExecutiveRepository executiveRepository;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final DepartmentRepository departmentRepository;


    private final Logger logger = LoggerFactory.getLogger(ExecutiveServiceImpl.class);

    @Autowired
    public ExecutiveServiceImpl(ExecutiveRepository executiveRepository,
                                UserRepository userRepository,
                                RoleRepository roleRepository,
                                DepartmentRepository departmentRepository) {
        this.executiveRepository = executiveRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.departmentRepository = departmentRepository;
    }

    @Transactional
    @Override
    public ExecutiveResponse createExecutive(ExecutiveRequest executiveRequest) {
        logger.info("Transaction ID: {}, Adding executive {}",
            MDC.get("transactionId") ,executiveRequest.getFirstName());

            User user = mapToUser(executiveRequest);

            user = userRepository.save(user);

            Department department = getDepartmentByIdOrThrow(executiveRequest.getDepartmentId());
            Executive executive = new Executive();
            executive.setUser(user);
            executive.setDepartment(department);
            executiveRepository.save(executive);
            logger.info("Transaction ID: {}, Executive added successfully", MDC.get("transactionId"));
            return mapToResponse(executive);
    }

    @Override
    public ExecutiveResponse updateExecutive(Long executiveId, ExecutiveRequest executiveRequest) {
        logger.info("Transaction ID: {}, Updating executive {}",
            MDC.get("transactionId") ,executiveRequest.getFirstName());

            Executive executive = getExecutiveByIdOrThrow(executiveId);
            User user = mapToUser(executiveRequest);
            userRepository.save(user);

            Department department = getDepartmentByIdOrThrow(executiveRequest.getDepartmentId());

            executive.setDepartment(department);
            executiveRepository.save(executive);

            logger.info("Transaction ID: {}, Executive updated successfully", MDC.get("transactionId"));
            return mapToResponse(executive);
    }

    @Transactional
    @Override
    public void deleteExecutive(Long id) {
        logger.info("Transaction ID: {}, Deleting executive with id {}",
            MDC.get("transactionId") ,id);

        Executive executive = getExecutiveByIdOrThrow(id);

        executiveRepository.delete(executive);

        logger.info("Transaction ID: {}, Deleted executive {}", MDC.get("transactionId"), id);
    }

    @Override
    public ExecutiveResponse getExecutive(Long id) {
        logger.info("Transaction ID: {}, Getting executive with id {}",
            MDC.get("transactionId") ,id);
            Executive executive = getExecutiveByIdOrThrow(id);
            logger.info("Transaction ID: {}, Executive found", MDC.get("transactionId"));
            return mapToResponse(executive);
    }

    @Override
    public Page<ExecutiveResponse> getExecutives(int page, int size, String sortBy, Sort.Direction direction) {
        logger.info("Transaction ID: {}, Getting all executives",
            MDC.get("transactionId"));

        return executiveRepository.findAll(
            PageRequest.of(page, size, Sort.by(direction, sortBy))
        ).map(this::mapToResponse);
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

    private ExecutiveResponse mapToResponse(Executive executive) {
        User user = executive.getUser();
        Department department = executive.getDepartment();
        return new ExecutiveResponse(
            executive.getExecutiveId(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getUsername(),
            user.getRoles().stream().map(Role::getName).toList(),
            department.getDepartmentName()
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
