package com.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;

@Controller
@RequestMapping("user")

public class UserController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ContactRepository contactrepository;

	// to add common data
	@ModelAttribute
	public void addcommondata(Model model, Principal principal) {
		String username = principal.getName();
		User user = userRepository.getUserByUserName(username);
		// System.out.println(username);
		System.out.println(user);
		model.addAttribute("user", user);
	}

	// dashboard home
	@RequestMapping("/index")
	public String dashboard(Model model, Principal principal) {

		model.addAttribute("title", "User Dashboard");
		return "normal/user_dashboard";
	}

	@GetMapping("/add-contact")
	public String openaddcontactform(Model model) {
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new Contact());

		return "normal/add_contact_form";
	}

	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file,
			Model model, Principal principal) {
		try {
			System.out.println("Data" + contact);
			String name = principal.getName();
			User user = this.userRepository.getUserByUserName(name);
			contact.setUser(user);
			user.getContact().add(contact);
			// processing and uploading file
			if (file.isEmpty()) {
				System.out.println("File is empty");
				contact.setImage("contact.png");
			} else {
				contact.setImage(file.getOriginalFilename());
				File saveFile = new ClassPathResource("static/img").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("Image Uploaded");

			}

			this.userRepository.save(user);
			System.out.println("adde to database");
			model.addAttribute("message", new Message("Your contact is added! Add more.", "success"));

		} catch (Exception e) {
			// TODO: handle exception
			model.addAttribute("message", new Message("Something went wrong! Try again.", "danger"));

			System.out.println("Error" + e.getMessage());
			e.printStackTrace();
		}

		return "normal/add_contact_form";
	}

	//
	@GetMapping("/show-contact/{page}")
	public String showContacts(@PathVariable("page") Integer page, Model model, Principal principal) {

		model.addAttribute("title", "Show Contacts");
		String username = principal.getName();
		User user = this.userRepository.getUserByUserName(username);
//		List<Contact> contacts= user.getContact();
//		
		// System.out.println(user.getId());

		Pageable pageable = PageRequest.of(page, 5);
		Page<Contact> contacts = this.contactrepository.findContactsByUser(user.getId(), pageable);
		model.addAttribute("contacts", contacts);
		model.addAttribute("curentpage", page);
		model.addAttribute("totalpages", contacts.getTotalPages());
		return "normal/show_contact";
	}

	@RequestMapping("/{cId}/contact")
	public String showContactDetail(@PathVariable("cId") Integer cId, Model model, Principal principal) {
		System.out.println("CID " + cId);
		String username = principal.getName();
		User user = this.userRepository.getUserByUserName(username);

		Optional<Contact> contactoptional = this.contactrepository.findById(cId);
		Contact contact = contactoptional.get();

		if (user.getId() == contact.getUser().getId()) {
			model.addAttribute("contact", contact);

		}

		return "normal/contact_detail";
	}

	@GetMapping("/delete/{cId}")
	public String deleteContact(@PathVariable("cId") Integer cId, Model model,Principal principal) {
		Optional<Contact> contactoptional = this.contactrepository.findById(cId);
		Contact contact = this.contactrepository.findById(cId).get();
		String username = principal.getName();
		User user = this.userRepository.getUserByUserName(username);
        
		user.getContact().remove(contact);
		
		this.contactrepository.delete(contact);
		model.addAttribute("message", new Message("contact delete successfully", "success"));
		
		return "redirect:/user/show-contact/0";
	}
	@PostMapping("/update-contact/{cId}")
	public String updateFormHandler( @PathVariable("cId") Integer cId ,Model model) {
		model.addAttribute("title", "Update Contact");
		Contact contact    =this.contactrepository.findById(cId).get();
		model.addAttribute("contact", contact);
		return "normal/update_form";
	}
	
	@PostMapping("/process-update")
	public String updateHandler(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file,Model model,Principal principal) {
		Contact oldcontactDetail = this.contactrepository.findById(contact.getcId()).get();

		
		try {
			if(!file.isEmpty()) {
				
				File deleteFile = new ClassPathResource("static/img").getFile();
				File file1 = new File(deleteFile, oldcontactDetail.getImage());
				file1.delete();

//				update new photo

				File saveFile = new ClassPathResource("static/img").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				contact.setImage(file.getOriginalFilename());
			}
			else {
				contact.setImage(oldcontactDetail.getImage());
				
			}
			User user=this.userRepository.getUserByUserName(principal.getName()) ;
			contact.setUser(user);
			this.contactrepository.save(contact);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
		System.out.println("Contact"+contact.getcId());
		System.out.println("Contact"+contact.getName());
		
		return "redirect:/user/"+contact.getcId()+"/contact";
	}
	
	@GetMapping("/profile")
	public String yourProfile(Model model) {
		model.addAttribute("title","Profile Page");
		return "normal/profile";
	}

}
