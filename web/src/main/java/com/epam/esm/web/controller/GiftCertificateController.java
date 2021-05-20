package com.epam.esm.web.controller;

import com.epam.esm.service.dto.certificate.GiftCertificateTagDto;
import com.epam.esm.service.exception.ValidationException;
import com.epam.esm.service.model.RequestParams;
import com.epam.esm.service.service.GiftCertificateService;
import com.epam.esm.service.validation.PatchGroup;
import com.epam.esm.service.validation.SaveGroup;
import com.epam.esm.service.validation.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/certificates")
@Validated
public class GiftCertificateController {
    private static final String ID = "id";

    private final GiftCertificateService certificateService;
    private final PagedResourcesAssembler<GiftCertificateTagDto> certificateAssembler;


    @Autowired
    public GiftCertificateController(GiftCertificateService certificateService, PagedResourcesAssembler<GiftCertificateTagDto> certificateAssembler) {
        this.certificateService = certificateService;

        this.certificateAssembler = certificateAssembler;
    }

    @GetMapping("/{id}")
    public ResponseEntity<GiftCertificateTagDto> getGiftCertificateById(@Min(value = 1, message = "value must be more than 0!") @PathVariable(ID) Long id) {
        GiftCertificateTagDto certificate = certificateService.getCertificateById(id);
        certificate.add((linkTo(methodOn(GiftCertificateController.class).getGiftCertificateById(id)).withSelfRel()));
        addMappingToAll(certificate);
        return ResponseEntity.ok(certificate);
    }

    private void addMappingToAll(RepresentationModel<?> certificate) {
        certificate.add((linkTo(methodOn(GiftCertificateController.class).getByParam(null, null)).withRel("all")));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('write_certificates')")
    public ResponseEntity<GiftCertificateTagDto> saveGiftCertificate(@Validated(SaveGroup.class) @RequestBody GiftCertificateTagDto certificate) throws ValidationException {
        GiftCertificateTagDto saved = certificateService.save(certificate);
        saved.add(linkTo(methodOn(GiftCertificateController.class).getGiftCertificateById(saved.getId())).withRel("this"));
        addMappingToAll(saved);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('write_certificates')")
    public void deleteCertificate(@Min(value = 1, message = "value must be more than 0!") @PathVariable(ID) Long id) {
        certificateService.deleteCertificate(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('write_certificates')")
    public ResponseEntity<GiftCertificateTagDto> updateCertificate(@Validated(UpdateGroup.class) @RequestBody GiftCertificateTagDto certificateDto, @Min(value = 1, message = "value must be more than 0!") @PathVariable Long id) throws ValidationException {
        GiftCertificateTagDto updated = certificateService.updateCertificate(certificateDto, id);
        updated.add((linkTo(methodOn(GiftCertificateController.class).getGiftCertificateById(id)).withRel("this")));
        addMappingToAll(updated);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('write_certificates')")
    public ResponseEntity<GiftCertificateTagDto> patchCertificate(@Validated(PatchGroup.class) @RequestBody GiftCertificateTagDto certificateDto, @Min(value = 1, message = "value must be more than 0!") @PathVariable Long id) throws ValidationException {
        GiftCertificateTagDto updated = certificateService.patchUpdate(id, certificateDto);
        updated.add((linkTo(methodOn(GiftCertificateController.class).getGiftCertificateById(id)).withRel("this")));
        addMappingToAll(updated);
        return ResponseEntity.ok(updated);
    }

    @GetMapping()
    public ResponseEntity<PagedModel<EntityModel<GiftCertificateTagDto>>> getByParam(RequestParams specification, Pageable pageable) {
        Page<GiftCertificateTagDto> bySpecification = certificateService.getBySpecification(specification, pageable);
        PagedModel<EntityModel<GiftCertificateTagDto>> entityModels = certificateAssembler.toModel(bySpecification);
        return ResponseEntity.ok(entityModels);
    }

}
