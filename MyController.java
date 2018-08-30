package com.cg.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cg.dao.CarDAO;
import com.cg.dto.CarDTO;

@Controller
@RequestMapping("/controller")
public class MyController {

	 private static final String VIEW_CAR_LIST_ACTION = "viewCarList";
	 private static final String ADD_CAR_ACTION = "addCar";
	 private static final String SAVE_CAR_ACTION = "saveCar";
	 private static final String EDIT_CAR_ACTION = "editCar";
	 private static final String DELETE_CAR_ACTION = "deleteCar";
	 private static final String ERROR_KEY = "errorMessage";
	 
	 @Autowired
	 private CarDAO carDAO;
		

	 @RequestMapping(method = RequestMethod.GET)
		public String processViewAddCarList(ModelMap map, @RequestParam("action") String action) {

		 if(VIEW_CAR_LIST_ACTION.equals(action)) {
			List<CarDTO> cars = carDAO.findAll();
			map.addAttribute("carList", cars);
			return "carList";
		 }
		 else if(ADD_CAR_ACTION.equals(action)) {
	         CarDTO car = new CarDTO();
	        // map.addAttribute("car", car);
	         return "carForm";
	        }  
	      else {
	            String errorMessage = "[" + action + "] is not a valid action.";
	            map.addAttribute(ERROR_KEY, errorMessage);
	            return null;
	        }
		}

		@RequestMapping(method = RequestMethod.POST,params={"id"})
		public String deleteCars(ModelMap map, @RequestParam("action") String action, @RequestParam("id") String[] ids) {

			if(DELETE_CAR_ACTION.equals(action)) {
			carDAO.delete(ids);
			List<CarDTO> cars = carDAO.findAll();
			map.addAttribute("carList", cars);
			return "carList";
			}   
			else {
	        String errorMessage = "[" + action + "] is not a valid action.";
	        map.addAttribute(ERROR_KEY, errorMessage);
	        return null;
	        }

		}
		@RequestMapping(method = RequestMethod.POST, params= {"make","model","modelYear","id"})
	    public String processSaveRequest(ModelMap map,@RequestParam("action") String action, @ModelAttribute("car") CarDTO car) {
			
			if(SAVE_CAR_ACTION.equals(action)) {
	    	car.setMake(car.getMake());
	    	car.setModel(car.getModel());
	    	car.setModelYear(car.getModelYear());
	    	car.setId(car.getId());
	    	if (car.getId() == -1)
	    	carDAO.create(car);
	    	else
	    	carDAO.update(car);
	    	List<CarDTO> cars = carDAO.findAll();
	    	map.addAttribute("carList", cars);
	    	return "carList";	        
	        }   
			else {
	        String errorMessage = "[" + action + "] is not a valid action.";
	        map.addAttribute(ERROR_KEY, errorMessage);
	        return null;
	        } 
			
	    }
		
		@RequestMapping(method= RequestMethod.GET,params={"id"})
		public String editCar(ModelMap map, @RequestParam("action") String action, @ModelAttribute("car") CarDTO car){
			
			if(EDIT_CAR_ACTION.equals(action)) {
			map.addAttribute("car",carDAO.findById(car.getId()));
			return "carForm";
			}   
			else {
	        String errorMessage = "[" + action + "] is not a valid action.";
	        map.addAttribute(ERROR_KEY, errorMessage);
	        return null;
	        }
			
		}
		
		@ModelAttribute("car")
	    public CarDTO createCar(){
	    	//System.out.println("TRYING TO CREATE AN ENTRY FOR A CAR");
	    	return new CarDTO();
	    }

}
