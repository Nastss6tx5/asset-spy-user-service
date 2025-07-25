package asset.spy.user.service.controller;

import asset.spy.user.service.dto.contact.ContactCreateDto;
import asset.spy.user.service.dto.contact.ContactResponseDto;
import asset.spy.user.service.dto.contact.ContactUpdateDto;
import asset.spy.user.service.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/contacts")
@CrossOrigin(origins = "http://77.110.126.88:30081", methods = {RequestMethod.GET, RequestMethod.POST})
@Tag(name = "Contacts", description = "Operation with contacts")
public class ContactController {

    private static final String HAS_ACCESS_TO_CONTACT = "@privilegeService.hasAccessToContact(#externalId)";
    private static final String HAS_ACCESS_TO_USER = "@privilegeService.hasAccessToUser(#userExternalId)";

    private final ContactService contactService;

    @Operation(summary = "Save contact by user ID")
    @PostMapping("/save/{userExternalId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize(HAS_ACCESS_TO_USER)
    public ContactResponseDto createContact(@PathVariable UUID userExternalId,
                                            @Valid @RequestBody ContactCreateDto contactCreateDto) {
        return contactService.createContact(contactCreateDto, userExternalId);
    }

    @Operation(summary = "Get contact by ID")
    @GetMapping("/{externalId}")
    @PreAuthorize(HAS_ACCESS_TO_CONTACT)
    public ContactResponseDto getContactById(@PathVariable UUID externalId) {
        return contactService.getContactByExternalId(externalId);
    }

    @Operation(summary = "Update contact by ID")
    @PutMapping("/{externalId}")
    @PreAuthorize(HAS_ACCESS_TO_CONTACT)
    public ContactResponseDto updateContact(@PathVariable UUID externalId,
                                            @Valid @RequestBody ContactUpdateDto contactUpdateDto) {
        return contactService.updateContact(externalId, contactUpdateDto);
    }

    @Operation(summary = "Delete contact by ID")
    @DeleteMapping("/{externalId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize(HAS_ACCESS_TO_CONTACT)
    public void deleteContact(@PathVariable UUID externalId) {
        contactService.deleteContact(externalId);
    }

    @Operation(summary = "Get all contacts")
    @GetMapping
    public Page<ContactResponseDto> getAllContacts(Pageable pageable,
                                                   @RequestParam(required = false) String contactType,
                                                   @RequestParam(required = false) String contactValue,
                                                   @RequestParam(required = false) Integer priority) {
        return contactService.getAllContacts(pageable,
                contactType, contactValue, priority);
    }
}
