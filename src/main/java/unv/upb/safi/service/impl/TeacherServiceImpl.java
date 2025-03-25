package unv.upb.safi.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
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

@Service
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;

    private final UserRepository userRepository;

    private final CollegeRepository collegeRepository;

    private final RoleRepository roleRepository;

    private final SearchNormalizerUtil searchNormalizerUtil;

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

    @Transactional
    @Override
    public TeacherResponse   registerTeacher(TeacherRequest teacherRequest) {
        User user = mapToUser(teacherRequest);
        user = userRepository.save(user);

        Teacher teacher = new Teacher();
        teacher.setUser(user);
        teacher.setCollege(getCollegeByIdOrThrow(teacherRequest.getCollegeId()));

        teacher = teacherRepository.save(teacher);
        return mapToResponse(teacher);
    }

    @Override
    public TeacherResponse getTeacher(Long teacherId) {
        Teacher teacher = getTeacherByIdOrThrow(teacherId);

        return mapToResponse(teacher);
    }

    @Override
    public TeacherResponse updateTeacher(Long teacherId, TeacherRequest teacherRequest) {
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
    public Page<TeacherResponse> getAllTeachers(Pageable pageable){
        return teacherRepository.findAll(pageable).map(this::mapToResponse);
    }

    @Override
    public Page<TeacherResponse> getTeachersByName(String name, Pageable pageable) {
        return teacherRepository.findByTeacherNameContainingIgnoreCase(
                searchNormalizerUtil.normalize(name),
                pageable
        ).map(this::mapToResponse);
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

    private TeacherResponse mapToResponse(Teacher teacher) {
        return new TeacherResponse(
            teacher.getTeacherId(),
            teacher.getUser().getFirstName(),
            teacher.getUser().getLastName(),
            teacher.getUser().getEmail(),
            teacher.getUser().getUsername(),
            teacher.getUser().getRoles().stream().map(Role::getName).toList(),
            teacher.getCollege().getName()
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
