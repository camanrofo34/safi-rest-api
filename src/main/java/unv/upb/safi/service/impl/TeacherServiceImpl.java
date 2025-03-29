package unv.upb.safi.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import unv.upb.safi.controller.TeacherController;
import unv.upb.safi.domain.dto.request.TeacherRequest;
import unv.upb.safi.domain.dto.response.TeacherResponse;
import unv.upb.safi.domain.entity.College;
import unv.upb.safi.domain.entity.Role;
import unv.upb.safi.domain.entity.User;
import unv.upb.safi.exception.entityNotFoundException.CollegeNotFoundException;
import unv.upb.safi.exception.entityNotFoundException.TeacherNotFoundException;
import unv.upb.safi.repository.*;
import unv.upb.safi.domain.entity.Teacher;
import unv.upb.safi.service.TeacherService;
import unv.upb.safi.util.SearchNormalizerUtil;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;

    private final UserRepository userRepository;

    private final CollegeRepository collegeRepository;

    private final RoleRepository roleRepository;

    private final SearchNormalizerUtil searchNormalizerUtil;

    private PagedResourcesAssembler<TeacherResponse> pagedResourcesAssembler;

    @Autowired
    public TeacherServiceImpl(TeacherRepository teacherRepository,
                              UserRepository userRepository,
                              CollegeRepository collegeRepository,
                              RoleRepository roleRepository,
                              SearchNormalizerUtil searchNormalizerUtil) {
        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
        this.collegeRepository = collegeRepository;
        this.roleRepository = roleRepository;
        this.searchNormalizerUtil = searchNormalizerUtil;
    }

    @Autowired
    public void setPagedResourcesAssembler(PagedResourcesAssembler<TeacherResponse> pagedResourcesAssembler) {
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @Transactional
    @Override
    public EntityModel<TeacherResponse> registerTeacher(TeacherRequest teacherRequest) {
        User user = mapToUser(teacherRequest);
        user = userRepository.save(user);

        Teacher teacher = new Teacher();
        teacher.setUser(user);
        teacher.setCollege(getCollegeByIdOrThrow(teacherRequest.getCollegeId()));

        teacher = teacherRepository.save(teacher);
        return mapToResponse(teacher);
    }

    @Override
    public EntityModel<TeacherResponse> getTeacher(Long teacherId) {
        Teacher teacher = getTeacherByIdOrThrow(teacherId);

        return mapToResponse(teacher);
    }

    @Override
    public EntityModel<TeacherResponse> updateTeacher(Long teacherId, TeacherRequest teacherRequest) {
        Teacher teacher = getTeacherByIdOrThrow(teacherId);

        User user = mapToUser(teacherRequest);
        user = userRepository.save(user);
        teacher.setUser(user);
        teacher.setCollege(getCollegeByIdOrThrow(teacherRequest.getCollegeId()));
        teacherRepository.save(teacher);

        return mapToResponse(teacher);

    }

    @Transactional
    @Override
    public void deleteTeacher(Long teacherId) {
        Teacher teacher = getTeacherByIdOrThrow(teacherId);

        teacherRepository.delete(teacher);
    }

    @Override
    public PagedModel<EntityModel<TeacherResponse>> getAllTeachers(Pageable pageable){
        Page<TeacherResponse> teacherResponses = teacherRepository.findAll(pageable).
                map(
                        teacher -> new TeacherResponse(
                                teacher.getTeacherId(),
                                teacher.getUser().getFirstName(),
                                teacher.getUser().getLastName(),
                                teacher.getUser().getEmail(),
                                teacher.getUser().getUsername(),
                                teacher.getUser().getRoles().stream().map(Role::getName).toList(),
                                teacher.getCollege().getName()
                        )
                );

        return pagedResourcesAssembler.toModel(teacherResponses, this::mapToModelToResourceModel);
    }

    @Override
    public PagedModel<EntityModel<TeacherResponse>> getTeachersByName(String name, Pageable pageable) {
        Page<TeacherResponse> teacherResponses = teacherRepository.findAllByTeacherNameContainingIgnoreCase(
                searchNormalizerUtil.normalize(name), pageable)
                .map(teacher ->
                        new TeacherResponse(
                                teacher.getTeacherId(),
                                teacher.getUser().getFirstName(),
                                teacher.getUser().getLastName(),
                                teacher.getUser().getEmail(),
                                teacher.getUser().getUsername(),
                                teacher.getUser().getRoles().stream().map(Role::getName).toList(),
                                teacher.getCollege().getName()
                        )
                );

        return pagedResourcesAssembler.toModel(teacherResponses, this::mapToModelToResourceModel);
    }

    private User mapToUser(TeacherRequest teacherRequest) {
        User user = new User();
        user.setUsername(teacherRequest.getUsername());
        user.setPassword(teacherRequest.getPassword());
        user.setFirstName(teacherRequest.getFirstName());
        user.setLastName(teacherRequest.getLastName());
        user.setEmail(teacherRequest.getEmail());
        user.setEnabled(true);
        Set<Role> roles = teacherRequest.getRolesId().stream()
                .map(roleId -> roleRepository.findById(roleId).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        user.setRoles(
                roles
        );
        return user;
    }

    private EntityModel<TeacherResponse> mapToResponse(Teacher teacher) {
        TeacherResponse teacherResponse = new TeacherResponse(
            teacher.getTeacherId(),
            teacher.getUser().getFirstName(),
            teacher.getUser().getLastName(),
            teacher.getUser().getEmail(),
            teacher.getUser().getUsername(),
            teacher.getUser().getRoles().stream().map(Role::getName).toList(),
            teacher.getCollege().getName()
        );

        return mapToModel(teacherResponse);
    }

    private EntityModel<TeacherResponse> mapToModel(TeacherResponse teacherResponse) {
        return EntityModel.of(teacherResponse,
                linkTo(methodOn(TeacherController.class).getTeacher(teacherResponse.getTeacherId())).withSelfRel(),
                linkTo(methodOn(TeacherController.class).deleteTeacher(teacherResponse.getTeacherId())).withRel("delete-teacher")
                );
    }

    private EntityModel<TeacherResponse> mapToModelToResourceModel(TeacherResponse teacherResponse) {
        return EntityModel.of(teacherResponse,
                linkTo(methodOn(TeacherController.class).getTeacher(teacherResponse.getTeacherId())).withSelfRel()
        );
    }

    private Teacher getTeacherByIdOrThrow(Long teacherId) {
        return teacherRepository.findById(teacherId)
                .orElseThrow(() -> new TeacherNotFoundException(teacherId.toString()));
    }

    private College getCollegeByIdOrThrow(Long collegeId) {
        return collegeRepository.findById(collegeId)
                .orElseThrow(() -> new CollegeNotFoundException(collegeId.toString()));
    }
}
