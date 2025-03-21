package unv.upb.safi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unv.upb.safi.domain.dto.request.TeacherRequest;
import unv.upb.safi.domain.dto.response.TeacherResponse;
import unv.upb.safi.domain.entity.Role;
import unv.upb.safi.domain.entity.User;
import unv.upb.safi.exception.entityNotFoundException.CollegeNotFoundException;
import unv.upb.safi.exception.entityNotFoundException.TeacherNotFoundException;
import unv.upb.safi.repository.*;
import unv.upb.safi.domain.entity.Teacher;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;

    private final UserRepository userRepository;

    private final CollegeRepository collegeRepository;

    private final RoleRepository roleRepository;

    private final Logger logger = LoggerFactory.getLogger(TeacherService.class);

    @Autowired
    public TeacherService(TeacherRepository teacherRepository,
                          UserRepository userRepository,
                          CollegeRepository collegeRepository,
                          RoleRepository roleRepository) {
        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
        this.collegeRepository = collegeRepository;
        this.roleRepository = roleRepository;
    }

    public TeacherResponse   registerTeacher(TeacherRequest teacherRequest) {
        logger.info("Transaction Id: {}, Registering teacher: {}", MDC.get("transactionId"), teacherRequest);
            User user = mapToUser(teacherRequest);
            user = userRepository.save(user);

            Teacher teacher = new Teacher();
            teacher.setUser(user);
            teacher.setCollege(collegeRepository.findById(teacherRequest.getCollegeId()).orElseThrow(() -> new CollegeNotFoundException(teacherRequest.getCollegeId().toString())));

            teacherRepository.save(teacher);
            logger.info("Transaction Id: {}, Registered teacher: {}", MDC.get("transactionId"), teacherRequest);
            return mapToResponse(teacher);
    }

    public TeacherResponse getTeacher(Long teacherId) {
        logger.info("Transaction ID: {}, Getting teacher with id: {}", MDC.get("transactionId"), teacherId);

            Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(() -> new TeacherNotFoundException(teacherId.toString()));
            logger.info("Transaction Id: {}, Getted teacher: {}", MDC.get("transactionId"), teacherId);
            return mapToResponse(teacher);
    }

    public TeacherResponse updateTeacher(Long teacherId, TeacherRequest teacherRequest) {
        logger.info("Transaction ID: {}, Updating teacher: {}", MDC.get("transactionId"), teacherRequest);

            Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(() -> {
                return new TeacherNotFoundException(teacherId.toString());
            });

            User user = mapToUser(teacherRequest);
            user = userRepository.save(user);

            teacher.setCollege(collegeRepository.findById(teacherRequest.getCollegeId()).orElseThrow(() -> new CollegeNotFoundException(teacherRequest.getCollegeId().toString())));
            teacherRepository.save(teacher);
            logger.info("Transaction Id: {}, Updated teacher: {}", MDC.get("transactionId"), teacherRequest);
            return mapToResponse(teacher);

    }

    public void deleteTeacher(Long teacherId) {
        logger.info("Transaction ID: {}, Deleting teacher with id: {}", MDC.get("transactionId"), teacherId);

        if (!teacherRepository.existsById(teacherId)) {
            throw new TeacherNotFoundException(teacherId.toString());
        }

            teacherRepository.deleteById(teacherId);
            logger.info("Transaction ID: {}, Deleted teacher: {}", MDC.get("transactionId"), teacherId);
    }

    public List<TeacherResponse> getAllTeachers(){
        logger.info("Transaction ID: {}, Getting all teachers", MDC.get("transactionId"));
            List<Teacher> teachers = teacherRepository.findAll();

            return teachers.stream().map(
                this::mapToResponse
            ).toList();
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
}
