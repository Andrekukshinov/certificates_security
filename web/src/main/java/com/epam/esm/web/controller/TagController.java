package com.epam.esm.web.controller;

import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.service.TagService;
import com.epam.esm.service.validation.SaveGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/tags")
@Validated
public class TagController {

    private final TagService tagService;
    private final PagedResourcesAssembler<TagDto> tagAssembler;


    @Autowired
    public TagController(TagService tagService, PagedResourcesAssembler<TagDto> tagAssembler) {
        this.tagService = tagService;
        this.tagAssembler = tagAssembler;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('write_tags')")
    public ResponseEntity<TagDto> saveTag(@Validated(SaveGroup.class) @RequestBody TagDto tag) {
        TagDto saved = tagService.save(tag);
        saved.add(linkTo(methodOn(TagController.class).getTagById(saved.getId())).withRel("this_tag"));
        getAllRef(saved);
        return ResponseEntity.ok(saved);

    }

    private void getAllRef(TagDto tag) {
        tag.add(linkTo(methodOn(TagController.class).getAll(null)).withRel("all"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagDto> getTagById(@Min(value = 1, message = "value must be more than 0!") @PathVariable Long id) {
        TagDto tag = tagService.getTag(id);
        tag.add(linkTo(methodOn(TagController.class).getTagById(id)).withSelfRel());
        getAllRef(tag);
        return ResponseEntity.ok(tag);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('write_tags')")
    public void deleteTag(@Min(value = 1, message = "value must be more than 0!") @PathVariable Long id) {
        tagService.deleteTag(id);
    }

    @GetMapping()
    public ResponseEntity<PagedModel<EntityModel<TagDto>>> getAll(Pageable pageable) {
        Page<TagDto> all = tagService.getAll(pageable);
        PagedModel<EntityModel<TagDto>> entityModels = tagAssembler.toModel(all);
        return ResponseEntity.ok(entityModels);
    }

}
