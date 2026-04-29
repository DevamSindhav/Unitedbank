//this controller handles the operations listed below
//deposit , withdraw , mini statement or all the trasnsactions display



package com.bank.Unitedbank.controller;

import com.bank.Unitedbank.service.TransactionService;
import com.bank.Unitedbank.entity.Transaction;
import com.bank.Unitedbank.entity.Customer;
import com.bank.Unitedbank.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class DashboardController {
	
	private final TransactionService transactionService;
	private final CustomerService customerService;
	
	public DashboardController(TransactionService tService, CustomerService cService) {
		this.transactionService = tService;
		this.customerService = cService;
	}
	
	@GetMapping("/dashboard")
	public String showDashboard(HttpSession session , RedirectAttributes redirectAttributes , Model model) {
		
		try {
			
			//this first fetches the data then we redirect this to the dasboardPage.jsp
			//which displays the fetched data
			
			Integer accNo = (Integer)(session.getAttribute("accNo"));
			
			if (accNo == null) {
	            return "redirect:/login"; 
	        }
			
			
			Customer customer = customerService.getCustomerById(accNo);
			model.addAttribute("customerProfile" , customer);
			
			List<Transaction> transactions = transactionService.getMiniStatement(accNo); 
			model.addAttribute("recentTransactions",transactions);
			
			return "dashboardPage";
			
		}catch(RuntimeException e) {
			redirectAttributes.addFlashAttribute("error" , e.getMessage());
			return "redirect:/login";
		}
		
	}
	
	@GetMapping("/allTransaction")
	public String getAllTransaction(HttpSession session ,RedirectAttributes redirectAttributes , Model model) {
		
		try {
			
			Integer accNo = (Integer)session.getAttribute("accNo");
			if(accNo == null) {
				return "redirect:/login";
			}
			
			Customer customer = customerService.getCustomerById(accNo);
			model.addAttribute("customerProfile" , customer);
			
			List<Transaction> allTransactions = transactionService.getAllStatement(accNo);
			model.addAttribute("allTransactions" , allTransactions);
			
			return "fullStatementPage";
			
		}catch(RuntimeException e) {
			redirectAttributes.addFlashAttribute("error" , e.getMessage());
			return "redirect:/dashboard";
		}
	}

}
