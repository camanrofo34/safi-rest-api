package unv.upb.safi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unv.upb.safi.domain.dto.response.RoleResponse;
import unv.upb.safi.repository.RoleRepository;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<RoleResponse> getAllRoles() {
        logger.info("Transaction ID {}: Getting all roles", MDC.get("transactionId"));
        return roleRepository.findAll().stream().map(role ->
            new RoleResponse(
                role.getRoleId(),
                role.getName()
            )
        ).toList();
    }
}
