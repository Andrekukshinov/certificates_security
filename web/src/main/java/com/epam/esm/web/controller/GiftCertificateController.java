package com.epam.esm.web.controller;

import com.epam.esm.persistence.model.page.Page;
import com.epam.esm.persistence.model.page.Pageable;
import com.epam.esm.service.dto.certificate.GiftCertificateTagDto;
import com.epam.esm.service.exception.ValidationException;
import com.epam.esm.service.model.RequestParams;
import com.epam.esm.service.service.GiftCertificateService;
import com.epam.esm.service.validation.PatchGroup;
import com.epam.esm.service.validation.SaveGroup;
import com.epam.esm.service.validation.UpdateGroup;
import com.epam.esm.web.helper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/certificates")
public class GiftCertificateController {
    private static final String ID = "id";

    private final GiftCertificateService certificateService;
    private final PageHelper pageHelper;

    @Autowired
    public GiftCertificateController(GiftCertificateService certificateService, PageHelper pageHelper) {
        this.certificateService = certificateService;
        this.pageHelper = pageHelper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<GiftCertificateTagDto> getGiftCertificateById(@PathVariable(ID) Long id) {
        GiftCertificateTagDto certificate = certificateService.getCertificateById(id);
        certificate.add((linkTo(methodOn(GiftCertificateController.class).getGiftCertificateById(id)).withSelfRel()));
        addMappingToAll(certificate);
        return ResponseEntity.ok(certificate);
    }

    private void addMappingToAll(RepresentationModel<?> certificate) {
        certificate.add((linkTo(methodOn(GiftCertificateController.class).getByParam(new HashMap<>(), null)).withRel("all")));
    }

    @PostMapping
    public ResponseEntity<GiftCertificateTagDto> saveGiftCertificate(@Validated(SaveGroup.class) @RequestBody GiftCertificateTagDto certificate) throws ValidationException {
        GiftCertificateTagDto saved = certificateService.save(certificate);
        saved.add(linkTo(methodOn(GiftCertificateController.class).getGiftCertificateById(saved.getId())).withRel("this"));
        addMappingToAll(saved);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCertificate(@PathVariable(ID) Long id) {
        certificateService.deleteCertificate(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GiftCertificateTagDto> updateCertificate(@Validated(UpdateGroup.class) @RequestBody GiftCertificateTagDto certificateDto, @PathVariable Long id) throws ValidationException {
        GiftCertificateTagDto updated = certificateService.updateCertificate(certificateDto, id);
        updated.add((linkTo(methodOn(GiftCertificateController.class).getGiftCertificateById(id)).withRel("this")));
        addMappingToAll(updated);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GiftCertificateTagDto> patchCertificate(@Validated(PatchGroup.class) @RequestBody GiftCertificateTagDto certificateDto, @PathVariable Long id) throws ValidationException {
        GiftCertificateTagDto updated = certificateService.patchUpdate(id, certificateDto);
        updated.add((linkTo(methodOn(GiftCertificateController.class).getGiftCertificateById(id)).withRel("this")));
        addMappingToAll(updated);
        return ResponseEntity.ok(updated);
    }

    @GetMapping()
    public ResponseEntity<CollectionModel<GiftCertificateTagDto>> getByParam(@RequestParam(required = false) Map<String, String> requestParams, @RequestParam(required = false) Set<String> tagNames) {
        Pageable pageable = pageHelper.getPageable(requestParams);
        RequestParams specification = getRequestParams(requestParams, tagNames);
        Page<GiftCertificateTagDto> page = certificateService.getBySpecification(specification, pageable);
        CollectionModel<GiftCertificateTagDto> of = getBuiltLinks(requestParams, tagNames, page);
        return ResponseEntity.ok(of);
    }

    private CollectionModel<GiftCertificateTagDto> getBuiltLinks(Map<String, String> requestParams, Set<String> tagNames, Page<GiftCertificateTagDto> page) {
        CollectionModel<GiftCertificateTagDto> of = CollectionModel.of(page.getContent());
        if (page.hasFirst()) {
            of.add(linkTo(
                    methodOn(GiftCertificateController.class)
                            .getByParam(pageHelper.getPageParamMap(requestParams, page.getFirstPage()), getTagNames(tagNames)))
                    .withRel("first")
            );
        }
        if (page.hasPrevious()) {
            of.add(linkTo(
                    methodOn(GiftCertificateController.class)
                            .getByParam(pageHelper.getPreviousPageParamMap(requestParams, page.getPreviousPage()), getTagNames(tagNames)))
                    .withRel("previous")
            );
        }
        of.add(linkTo(
                methodOn(GiftCertificateController.class)
                        .getByParam(pageHelper.getThisPageParamMap(requestParams, page.getPage()), getTagNames(tagNames)))
                .withRel("this")
        );
        if (page.hasNext()) {
            of.add(linkTo(
                    methodOn(GiftCertificateController.class)
                            .getByParam(pageHelper.getNextPageParamMap(requestParams, page.getNextPage()), getTagNames(tagNames)))
                    .withRel("next")
            );
        }
        if (page.hasLast()) {
            of.add(linkTo(
                    methodOn(GiftCertificateController.class)
                            .getByParam(pageHelper.getPageParamMap(requestParams, page.getLastPage()), getTagNames(tagNames)))
                    .withRel("last")
            );
        }
        return of;
    }

    private Set<String> getTagNames(Set<String> tagNames) {
        return tagNames == null ? new HashSet<>() : tagNames;
    }

    private RequestParams getRequestParams(Map<String, String> requestParams, Set<String> tagNames) {
        String description = requestParams.get("certificateDescription");
        String certificateName = requestParams.get("certificateName");
        return RequestParams.builder().setCertificateDescription(description).setTagNames(tagNames).setCertificateName(certificateName).build();
    }
}
