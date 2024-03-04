package com.rs.retailstore.service;

import com.rs.retailstore.model.Contact;
import com.rs.retailstore.model.ContactList;
import com.rs.retailstore.respository.ContactRelationshipRepo;
import com.rs.retailstore.respository.ContactRepository;
import com.sun.jdi.InternalException;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.rs.retailstore.constant.Constant.PHOTO_DIRECTORY;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@Data
public class ContactService {
    @Autowired
    private final ContactRepository contactRepository;
    @Autowired
    private  final ContactRelationshipRepo contactRelationshipRepo;

    public ContactService(ContactRepository contactRepository, ContactRelationshipRepo contactRelationshipRepo) {
        this.contactRepository = contactRepository;
        this.contactRelationshipRepo = contactRelationshipRepo;
    }

    public Page<Contact> getALlContacts(int page, int size){

            return  contactRepository.findAll(PageRequest.of(page, size, Sort.by("name")));
    }


    public Contact getContact(int id){
        return  contactRepository.findById(id).orElseThrow();
    }

    public ContactList addToList(int id, int contactId) {
        try {
            Random rd= new Random();
            ContactList contactList = new ContactList();
            contactList.setId(rd.nextInt(0,1000000));
            contactList.setUserId(id);
            Contact contact=contactRepository.findById(contactId).orElseThrow();
            contactList.setContact(contact);
            contactRelationshipRepo.save(contactList);
            return contactList;
        }catch (Exception e){
            throw  new InternalException("Error while adding");
        }
    }
    public List<Contact> getListContact(int  id){
        try {
            List<ContactList> contactList = contactRelationshipRepo.findContactListByUserId(id);
            System.out.println(contactList);
            List<Contact> list = new ArrayList<>();
            for (ContactList cl : contactList) {
                list.add(cl.getContact());
            }
            return list;
        }catch (Exception e){
            throw  new InternalException("Error while get contactList");
        }
    }

    public Contact createContact(Contact contact){
        return contactRepository.save(contact);
    }

    public void deleteContact(Contact contact){
        contactRepository.delete(contact);
    }

    public String uploadPhoto(int id, MultipartFile file){
        Contact contact= getContact(id);
        String photoUrl= photoFunction.apply(id,file);
        System.out.println("photo url: "+photoUrl);
        contact.setPhotoUrl(photoUrl);
        contactRepository.save(contact);
        return photoUrl;
    }

    private final Function<String, String> fileExtension= (filename)->
        Optional.of(filename).filter(name -> name.contains(".")).map(name->"."+ name.substring(filename.lastIndexOf(".")+1)).orElse(".png");



    private final BiFunction<Integer, MultipartFile, String> photoFunction=( id, image)->{
        String filename= id+ fileExtension.apply(image.getOriginalFilename());
        System.out.println(filename);
        try{
            //get specific location in computer to store photo image
            Path fileStorageLocation= Paths.get(PHOTO_DIRECTORY).toAbsolutePath().normalize();
            System.out.println(fileStorageLocation);
            System.out.println(PHOTO_DIRECTORY + filename);
            if(!Files.exists(fileStorageLocation)){
                Files.createDirectories(fileStorageLocation);

            }
            Files.copy(image.getInputStream(),fileStorageLocation.resolve(id+".png"),REPLACE_EXISTING);
            return ServletUriComponentsBuilder.fromCurrentContextPath().path("/contact/image/"+filename).toUriString();
        }catch(Exception e){
            System.out.println(e);
            throw new RuntimeException("unable to save image");
        }
    };

}
