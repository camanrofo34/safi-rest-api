package unv.upb.safi.service.impl;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unv.upb.safi.domain.dto.request.StudentRequest;
import unv.upb.safi.domain.dto.response.StudentResponse;
import unv.upb.safi.domain.entity.Faculty;
import unv.upb.safi.domain.entity.Role;
import unv.upb.safi.domain.entity.Student;
import unv.upb.safi.domain.entity.User;
import unv.upb.safi.exception.entityNotFoundException.StudentNotFoundException;
import unv.upb.safi.repository.FacultyRepository;
import unv.upb.safi.repository.RoleRepository;
import unv.upb.safi.repository.StudentRepository;
import unv.upb.safi.repository.UserRepository;
import unv.upb.safi.service.StudentService;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    private final UserRepository userRepository;

    private final FacultyRepository facultyRepository;

    private final RoleRepository roleRepository;

    private final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository,
                              UserRepository userRepository,
                              FacultyRepository facultyRepository,
                              RoleRepository roleRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.facultyRepository = facultyRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    @Override
    public StudentResponse registerStudent(StudentRequest studentRequest) {
        logger.info("Transaction Id: {}, Registering student: {}", MDC.get("transactionId"), studentRequest);

            User user = mapToUser(studentRequest);
            userRepository.save(user);

            Set<Faculty> faculties = studentRequest.getFacultyId().stream()
                    .map(facultyId -> facultyRepository.findById(facultyId).orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            Student student = new Student();
            student.setUser(user);
            student.setFaculty(faculties);
            student = studentRepository.save(student);

            logger.info("Transaction Id: {}, Student registered successfully: {}", MDC.get("transactionId"), student.getStudentId());
            return mapToResponse(student);
    }

    @Override
    public StudentResponse updateStudent(Long studentId, StudentRequest studentRequest) {
        logger.info("Transaction Id: {}, Updating student: {}", MDC.get("transactionId"), studentRequest);

            Student student = getStudentByIdOrThrow(studentId);

            User user = mapToUser(studentRequest);
            userRepository.save(user);

            Set<Faculty> faculties = studentRequest.getFacultyId().stream()
                    .map(facultyId -> facultyRepository.findById(facultyId).orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            student.setUser(user);
            student.setFaculty(faculties);
            student = studentRepository.save(student);


            return mapToResponse(student);
    }

    @Transactional
    @Override
    public void deleteStudent(Long studentId) {
        logger.info("Transaction Id: {}, Deleting student: {}", MDC.get("transactionId"), studentId);

        Student student = getStudentByIdOrThrow(studentId);

        studentRepository.delete(student);

        logger.info("Transaction Id: {}, Student deleted successfully: {}", MDC.get("transactionId"), studentId);

    }

    @Override
    public StudentResponse getStudent(Long studentId) {
        logger.info("Transaction Id: {}, Fetching student: {}", MDC.get("transactionId"), studentId);

            Student student = getStudentByIdOrThrow(studentId);

            return mapToResponse(student);
    }

    private User mapToUser(StudentRequest studentRequest) {
        User user = new User();
        user.setUsername(studentRequest.getUsername());
        user.setPassword(studentRequest.getPassword());
        user.setFirstName(studentRequest.getFirstName());
        user.setLastName(studentRequest.getLastName());
        user.setEmail(studentRequest.getEmail());
        user.setEnabled(true);
        Set<Role> roles = studentRequest.getRolesId().stream()
                .map(roleId -> roleRepository.findById(roleId).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        user.setRoles(
                roles
        );
        return user;
    }

    private StudentResponse mapToResponse(Student student) {
        return new StudentResponse(
            student.getStudentId(),
            student.getUser().getFirstName(),
            student.getUser().getLastName(),
            student.getUser().getEmail(),
            student.getUser().getUsername(),
            student.getUser().getRoles().stream().map(Role::getName).toList(),
            student.getFaculty().stream().map(Faculty::getFacultyName).toList()
        );
    }

    private Student getStudentByIdOrThrow(Long studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId.toString()));
    }
}
