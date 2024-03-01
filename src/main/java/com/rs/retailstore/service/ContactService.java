package com.rs.retailstore.service;

import com.rs.retailstore.model.Contact;
import com.rs.retailstore.respository.ContactRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;

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

    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public Page<Contact> getALlContacts(int page, int size){
            return  contactRepository.findAll(PageRequest.of(page, size, Sort.by("name")));
    }

    public Contact getContact(int id){
        return  contactRepository.findById(id).orElseThrow();
    }

    public Contact createContact(Contact contact){
        Random random = new Random();
        contact.setId(random.nextInt(0,10000));
        return contactRepository.save(contact);
    }

    public void deleteContact(Contact contact){
        contactRepository.delete(contact);
    }

    public String uploadPhoto(int id, MultipartFile file){
        Contact contact= getContact(id);
        String photoUrl= null;
        contact.setPhotoUrl(photoUrl);
        contactRepository.save(contact);
        return photoUrl;
    }

    private final Function<String, String> fileExtension= (filename)->
        Optional.of(filename).filter(name -> name.contains(".")).map(name->"."+ name.substring(filename.lastIndexOf(".")+1)).orElse(".png");



    private final BiFunction<String, MultipartFile, String> photoFunction=( id, image)->{
        String filename= id+ fileExtension.apply(image.getOriginalFilename());
        try{
            //get specific location in computer to store photo image
            Path fileStorageLocation= Paths.get(PHOTO_DIRECTORY).toAbsolutePath().normalize();
            if(!Files.exists(fileStorageLocation)){
                Files.createDirectories(fileStorageLocation);

            }
            Files.copy(image.getInputStream(),fileStorageLocation.resolve(id+".png"),REPLACE_EXISTING);
            return ServletUriComponentsBuilder.fromCurrentContextPath().path("/contacts/image"+filename).toUriString();
        }catch(Exception e){
            System.out.println(e);
            throw new RuntimeException("unable ton save image");
        }
    };

}
