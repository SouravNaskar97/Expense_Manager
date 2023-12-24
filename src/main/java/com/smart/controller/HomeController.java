package com.smart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
//import com.smart.helper.Message;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;
//import jakarta.validation.Valid;
import jakarta.validation.Valid;


@Controller
public class HomeController {
	

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping("/")
	public String home(Model model)
	{
		model.addAttribute("title","Home - Smart Expense Manager");
		return "home";
	}
	
	
	@RequestMapping("/signup")
	public String signup(Model model)
	{
		model.addAttribute("title","Register - Smart Expense Manager");
		model.addAttribute("user",new User());
		return "signup";
	}
	
	
	// handler for registering user
	
	@RequestMapping(value="/do_register", method=RequestMethod.POST)
	public  String registerUser(@Valid  @ModelAttribute("user") User user,BindingResult bindresult,@RequestParam(value="agreement",defaultValue = "false") boolean agreement, Model model,HttpSession session)
	{
		
		try {
			
			if(!agreement)  
			{
				System.out.println("You have not agreed to terms and conditions");
				throw new Exception("You have not agreed to terms and conditions");
			}
			if(bindresult.hasErrors())
			{
				System.out.println("ERROR"+ bindresult.toString());
				model.addAttribute("user",user);
				return "signup";
			}
			
			 user.setRole("ROLE_USER");
		
			
			 user.setPassword(passwordEncoder.encode(user.getPassword()));
			 
			System.out.println("agreement :"+agreement);
			System.out.println("User :"+user);
			
			 User result = this.userRepository.save(user);
			 
			model.addAttribute("user",new User());
			session.setAttribute("message", new Message("Successfully Registered !!","alert-success"));

			
		} catch (Exception e) {
			
			e.printStackTrace();
			model.addAttribute("user",user);
			session.setAttribute("message", new Message("Something went wrong !!"+ e.getMessage(),"alert-danger"));
		}
		
		return "signup";
	}
	
	
	//handler for custom login
	@GetMapping("/signin")
	public String customLogin(Model model)
	{
		model.addAttribute("title","Login Page");
		return "login";
	}
	
	//handler for about page
	@GetMapping("/about")
	public String aboutinfo()
	{
		
		return "about_page";
	}

}
