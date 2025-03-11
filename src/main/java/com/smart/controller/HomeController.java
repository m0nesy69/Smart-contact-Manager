package com.smart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@RequestMapping("/")
	public String home(Model model) {
		model.addAttribute("title","Home-Smart Contact Manager");
		return "home";
	}
	
	
	@RequestMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title","Register-Smart Contact Manager");
		model.addAttribute("user",new User());
		return "signup";
	}
	@RequestMapping(value="/do_register",method = RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result,@RequestParam(value = "agreement",defaultValue = "false") boolean agreement,Model model) {
		if(result.hasErrors()) {
			model.addAttribute("user",user);
	        model.addAttribute("message", new Message("Validation failed! Please check your inputs.", "alert-danger"));

			return "signup";
		}
		if(!agreement) {
			model.addAttribute("user", user);
			model.addAttribute("message", new Message("You must agree to the terms and conditions", "alert-danger"));
			return "signup";
		}
		
		try {
			/*
			 * System.out.println("Agreement"+agreement); System.out.println("User"+user);
			 */
			
			
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("default.png");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			User res =this.userRepository.save(user);
			
			model.addAttribute("user",new User());
			model.addAttribute("message", new Message("Registered Successfully","alert-success"));
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user",user);
			model.addAttribute("message", new Message("Something Went Wrong"+e.getMessage(),"alert-danger"));
			
		}
		
		return "signup";
		
	}
	@GetMapping("signin")
	public String customlogin(Model model) {
		model.addAttribute("title", "Login Page");
		return "Login";
	}
	
	
}
