package unv.upb.safi.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import unv.upb.safi.domain.dto.response.RoleResponse;
import unv.upb.safi.repository.RoleRepository;
import unv.upb.safi.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Page<RoleResponse> getAllRoles(int page, int size, String sortBy, Sort.Direction direction) {
        logger.info("Transaction ID {}: Getting all roles", MDC.get("transactionId"));

        return roleRepository.findAll(
                PageRequest.of(page, size, Sort.by(direction, sortBy))
        ).map(
                role -> new RoleResponse(role.getRoleId(), role.getName())
        );
    }
}
