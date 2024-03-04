package com.rs.retailstore.Controller;

import com.rs.retailstore.model.Contact;
import com.rs.retailstore.model.ContactList;
import com.rs.retailstore.service.ContactService;
import com.rs.retailstore.service.JwtService;
import jakarta.websocket.server.PathParam;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.rs.retailstore.constant.Constant.PHOTO_DIRECTORY;
import static org.springframework.util.MimeTypeUtils.IMAGE_JPEG_VALUE;
import static org.springframework.util.MimeTypeUtils.IMAGE_PNG_VALUE;

@RestController
@RequiredArgsConstructor
public class ContactController {

    @Autowired
    ContactService contactService;
    @Autowired
    JwtService jwtService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping(value="/contact/create", produces="application/json", consumes = "application/json")
    public ResponseEntity<String> createContact(@RequestBody Contact request, @RequestHeader("Authorization") String header){
        String token= header.substring(7);
        String name= jwtService.extractUsername(token);
        String id= jwtService.extractId(token);
        request.setId(Integer.parseInt(id));
        request.setName(name);
        System.out.println(request);
        contactService.createContact(request);
        Contact contact = contactService.getContact(request.getId());
        if( contact!= null) {
            return ResponseEntity.ok("Create successful");
        }else return ResponseEntity.ok("Create failed");
    }

    @GetMapping(value ="contact/profile/{contactId}")
    public ResponseEntity<Contact> getProfile(@PathVariable("contactId") int contactId){
        System.out.println("Contact : "+contactId);
        return ResponseEntity.ok(contactService.getContact(contactId));
    }

    @PutMapping(value="/contact/photo",   produces="application/json", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadPhoto(@RequestParam("id") String id, @RequestParam("file") MultipartFile file){
        try {
            int Id = Integer.parseInt(id);
            System.out.println(id);
            return ResponseEntity.ok(contactService.uploadPhoto(Id, file));
        }catch (NumberFormatException numberFormatException){
            System.out.println(numberFormatException);
            return ResponseEntity.badRequest().body("Invalid input id parameter");
        }
    }

    @GetMapping("/contact/allContact")
    public ResponseEntity<Page<Contact>> getALlContact(@RequestParam(value="page", defaultValue = "0") int page, @RequestParam(value="size", defaultValue = "10") int size){
        return  ResponseEntity.ok(contactService.getALlContacts(page,size));
    }
    @GetMapping(value="/contact/image/{filename}", produces={ IMAGE_PNG_VALUE, IMAGE_JPEG_VALUE})
    public ResponseEntity<byte[]> getPhoto(@PathVariable("filename") String filename) throws IOException {
        System.out.println("Running");
        try {
            filename=filename.replace(".JPG",".png");
            Path fileStorageLocation= Paths.get(PHOTO_DIRECTORY).toAbsolutePath().normalize();
            return ResponseEntity.ok(Files.readAllBytes(Paths.get(fileStorageLocation+"/"+filename)));
        }catch (IOException e){
            System.out.println(e);
            return null;
        }
    }
    @GetMapping(path = "/contact/getList/{contactId}")
    public ResponseEntity<List<Contact>> getList(@PathVariable("contactId")int id){
        System.out.println(id);
        return ResponseEntity.ok(contactService.getListContact(id));
    }

    @PostMapping(value="/contact/addList/{contactId}",  produces="application/json", consumes = "application/json")
    public ResponseEntity<ContactList> addList(@PathVariable("contactId") Integer contactId, @RequestBody Contact contact) throws Exception {
        System.out.println(contactId+" "+ contact);
        return  ResponseEntity.ok(contactService.addToList(contactId,contact.getId()));
    }
}
