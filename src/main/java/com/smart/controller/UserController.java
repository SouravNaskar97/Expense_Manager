package com.smart.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.smart.dao.ExpenseRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Expense;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ExpenseRepository expenseRepository;
	
	
	//method to add common data to response
	@ModelAttribute
	public void addCommondata(Model model, Principal principal)
	{
		String userName=principal.getName();
		System.out.println("USERNAME"+ userName);
		
		//get the user using username(email)
		
		User user=userRepository.getUserByUserName(userName);
		System.out.println("USER"+ user);
		model.addAttribute("user",user);
	}
	
	@RequestMapping("/index")
	public String dashboard(Model model,Principal principal)
	{
		model.addAttribute("title","User Dashboard");
		String name=principal.getName();
		User user=userRepository.getUserByUserName(name);
		List<Expense> expenses=user.getExpenses();
		model.addAttribute("expenses",expenses);
		return "normal/user_dashboard" ;
	}
	 
	
	//Add form handler
	@GetMapping("/add-expense")
	public String addExpense(Model model)
	{
		model.addAttribute("title","Add Expense");
		model.addAttribute("expense",new Expense());
		return "normal/add_expense_form";
	}
	
	// processing add form 
	@PostMapping("/handle-expense")
	public String processExpense(@ModelAttribute Expense expense , Principal principal, Model model){
		String name=principal.getName();
		User user=userRepository.getUserByUserName(name);
		expense.setUser(user);
		user.getExpenses().add(expense);
		this.userRepository.save(user);
		System.out.println("Added to database");
		List<Expense> expenses=user.getExpenses();
		model.addAttribute("expenses",expenses);
		return "normal/user_dashboard" ;
		
	}
	
	// processing update form 
		@PostMapping("/handle-update/{id}")
		public String processUpdate(@ModelAttribute Expense expensenew,@PathVariable("id") int id,Principal principal, Model model )
		{
			String name=principal.getName();
			User user=userRepository.getUserByUserName(name);
			
			Expense expense=this.expenseRepository.findById(id).get();
			expense.setType(expensenew.getType());
			expense.setDescription(expensenew.getDescription());
			expense.setCost(expensenew.getCost());
		     this.expenseRepository.save(expense);
			List<Expense> expenses=user.getExpenses();
			model.addAttribute("expenses",expenses);
			return "normal/user_dashboard" ;
		}
	
	
	
	
	  //delete expense
    @RequestMapping("/delete/{id}")
    public String deleteexpense(@PathVariable("id") int id,Principal principal, Model model , HttpSession session, RedirectAttributes redirAttrs)
    {   String name=principal.getName();
	User user=userRepository.getUserByUserName(name);
    	 
	         Expense expense=this.expenseRepository.findById(id).get();
              //Expense expense=expenseoptional.get();
              if(user.getId()==expense.getUser().getId())
              {
            	 // expense.setUser(null);
              //this.expenseRepository.delete(expense); 
            	  this.expenseRepository.deleteByIdCustom(id);
              }
           //   session.setAttribute("message",new Message("expense deleted successfully !!","success"));
              redirAttrs.addFlashAttribute("success", "Expense deleted successfully !!");
		     return "redirect:/user/index" ;
		
    	
    }
    
  //open update form handler
   @RequestMapping("/update-expense/{eid}")
    public String updateexpense(@PathVariable("eid")Integer eid,Model m)
    {
	   m.addAttribute("title","Update Expense");
	   Expense expense=this.expenseRepository.findById(eid).get();
	   m.addAttribute("expense",expense);
    	
    	return "normal/update_form";
    }
	
}
 