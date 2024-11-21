package tech.siloxa.clipboard.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import tech.siloxa.clipboard.domain.ClipBoard;
import tech.siloxa.clipboard.domain.User;
import tech.siloxa.clipboard.domain.WorkSpace;
import tech.siloxa.clipboard.repository.ClipBoardRepository;
import tech.siloxa.clipboard.repository.WorkSpaceRepository;
import tech.siloxa.clipboard.service.UserService;
import tech.siloxa.clipboard.service.dto.InviteDTO;
import tech.siloxa.clipboard.web.rest.errors.BadRequestAlertException;

import javax.annotation.Resource;

/**
 * REST controller for managing {@link tech.siloxa.clipboard.domain.WorkSpace}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class WorkSpaceResource {

    private final Logger log = LoggerFactory.getLogger(WorkSpaceResource.class);

    private static final String ENTITY_NAME = "workSpace";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Resource
    private WorkSpaceRepository workSpaceRepository;

    @Resource
    private ClipBoardRepository clipBoardRepository;

    @Resource
    private UserService userService;

    /**
     * {@code POST  /work-spaces} : Create a new workSpace.
     *
     * @param workSpace the workSpace to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workSpace, or with status {@code 400 (Bad Request)} if the workSpace has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/work-spaces")
    public ResponseEntity<WorkSpace> createWorkSpace(@RequestBody WorkSpace workSpace) throws URISyntaxException {
        log.debug("REST request to save WorkSpace : {}", workSpace);
        if (workSpace.getId() != null) {
            throw new BadRequestAlertException("A new workSpace cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WorkSpace result = workSpaceRepository.save(workSpace);
        return ResponseEntity
            .created(new URI("/api/work-spaces/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/work-spaces/{id}/invite")
    public void inviteToWorkSpace(@RequestBody InviteDTO inviteDTO) {
    }

    /**
     * {@code PATCH  /work-spaces/:id} : Partial updates given fields of an existing workSpace, field will ignore if it is null
     *
     * @param id the id of the workSpace to save.
     * @param workSpace the workSpace to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workSpace,
     * or with status {@code 400 (Bad Request)} if the workSpace is not valid,
     * or with status {@code 404 (Not Found)} if the workSpace is not found,
     * or with status {@code 500 (Internal Server Error)} if the workSpace couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/work-spaces/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WorkSpace> partialUpdateWorkSpace(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WorkSpace workSpace
    ) throws URISyntaxException {
        log.debug("REST request to partial update WorkSpace partially : {}, {}", id, workSpace);
        if (workSpace.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workSpace.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workSpaceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WorkSpace> result = workSpaceRepository
            .findById(workSpace.getId())
            .map(existingWorkSpace -> {
                if (workSpace.getName() != null) {
                    existingWorkSpace.setName(workSpace.getName());
                }

                return existingWorkSpace;
            })
            .map(workSpaceRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, workSpace.getId().toString())
        );
    }

    /**
     * {@code GET  /work-spaces} : get all the workSpaces.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workSpaces in body.
     */
    @GetMapping("/work-spaces")
    public List<WorkSpace> getAllWorkSpacesOfCurrentUser() {
        log.debug("REST request to get all WorkSpaces");
        final User currentUser = userService.getUserWithAuthorities().orElseThrow();
        return workSpaceRepository.findAllByUser(currentUser);
    }

    /**
     * {@code GET  /work-spaces/:id} : get the "id" workSpace.
     *
     * @param id the id of the workSpace to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workSpace, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/work-spaces/{id}/clip-boards")
    public List<ClipBoard> getWorkSpace(@PathVariable Long id) {
        log.debug("REST request to get WorkSpace : {}", id);
        return clipBoardRepository.findAllByWorkSpace_id(id);
    }

    /**
     * {@code DELETE  /work-spaces/:id} : delete the "id" workSpace.
     *
     * @param id the id of the workSpace to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/work-spaces/{id}")
    public ResponseEntity<Void> deleteWorkSpace(@PathVariable Long id) {
        log.debug("REST request to delete WorkSpace : {}", id);
        workSpaceRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
