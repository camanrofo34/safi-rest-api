package unv.upb.safi.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import unv.upb.safi.controller.RoleController;
import unv.upb.safi.domain.dto.response.RoleResponse;
import unv.upb.safi.repository.RoleRepository;
import unv.upb.safi.service.RoleService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    private PagedResourcesAssembler<RoleResponse> pagedResourcesAssembler;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public PagedModel<EntityModel<RoleResponse>> getAllRoles(Pageable pageable) {
        Page<RoleResponse> roles = roleRepository.findAll(pageable)
                .map( role ->
                        new RoleResponse(
                        role.getRoleId(),
                        role.getName()
                    )
                );

        return pagedResourcesAssembler.toModel(roles, this::mapToEntityModel);
    }

    private EntityModel<RoleResponse> mapToEntityModel(RoleResponse roleResponse) {
        return EntityModel.of(roleResponse);
    }


}
