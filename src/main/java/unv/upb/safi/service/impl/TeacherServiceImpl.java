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

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;

    private final UserRepository userRepository;

    private final CollegeRepository collegeRepository;

    private final RoleRepository roleRepository;

    private final Logger logger = LoggerFactory.getLogger(TeacherServiceImpl.class);

    @Autowired
    public TeacherServiceImpl(TeacherRepository teacherRepository,
                              UserRepository userRepository,
                              CollegeRepository collegeRepository,
                              RoleRepository roleRepository) {
        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
        this.collegeRepository = collegeRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    @Override
    public TeacherResponse   registerTeacher(TeacherRequest teacherRequest) {
        logger.info("Transaction Id: {}, Registering teacher: {}", MDC.get("transactionId"), teacherRequest);
        User user = mapToUser(teacherRequest);
        user = userRepository.save(user);

        Teacher teacher = new Teacher();
        teacher.setUser(user);
        teacher.setCollege(getCollegeByIdOrThrow(teacherRequest.getCollegeId()));

        teacherRepository.save(teacher);
        logger.info("Transaction Id: {}, Registered teacher: {}", MDC.get("transactionId"), teacherRequest);
        return mapToResponse(teacher);
    }

    @Override
    public TeacherResponse getTeacher(Long teacherId) {
        logger.info("Transaction ID: {}, Getting teacher with id: {}", MDC.get("transactionId"), teacherId);

        Teacher teacher = getTeacherByIdOrThrow(teacherId);
        logger.info("Transaction Id: {}, Getted teacher: {}", MDC.get("transactionId"), teacherId);
        return mapToResponse(teacher);
    }

    @Override
    public TeacherResponse updateTeacher(Long teacherId, TeacherRequest teacherRequest) {
        logger.info("Transaction ID: {}, Updating teacher: {}", MDC.get("transactionId"), teacherRequest);

        Teacher teacher = getTeacherByIdOrThrow(teacherId);

        User user = mapToUser(teacherRequest);
        user = userRepository.save(user);
        teacher.setUser(user);
        teacher.setCollege(getCollegeByIdOrThrow(teacherRequest.getCollegeId()));
        teacherRepository.save(teacher);

        logger.info("Transaction Id: {}, Updated teacher: {}", MDC.get("transactionId"), teacherRequest);
        return mapToResponse(teacher);

    }

    @Transactional
    @Override
    public void deleteTeacher(Long teacherId) {
        logger.info("Transaction ID: {}, Deleting teacher with id: {}", MDC.get("transactionId"), teacherId);

        Teacher teacher = getTeacherByIdOrThrow(teacherId);

        teacherRepository.delete(teacher);
        logger.info("Transaction ID: {}, Deleted teacher: {}", MDC.get("transactionId"), teacherId);
    }

    @Override
    public Page<TeacherResponse> getAllTeachers(int page, int size, String sortBy, Sort.Direction direction){
        logger.info("Transaction ID: {}, Getting all teachers", MDC.get("transactionId"));

        return teacherRepository.findAll(
                PageRequest.of(page, size, Sort.by(direction, sortBy))
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
