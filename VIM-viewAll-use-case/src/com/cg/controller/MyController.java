
package com.cg.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cg.dao.CarDAO;
import com.cg.dao.impl.JDBCCarDAO;
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
	public String viewCarList(ModelMap map, @RequestParam("action") String action) {
		if (action.equals(VIEW_CAR_LIST_ACTION)) {
			List<CarDTO> cars = carDAO.findAll();
			map.addAttribute("carList", cars);
			System.out.println("viewlist");
			return "carList";
		}

		else if (action.equals(ADD_CAR_ACTION)) {
			CarDTO car = new CarDTO();
			map.addAttribute("car", car);
			System.out.println("addcar");
			return "carForm";
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.POST, params = { "id" })
	public String deleteCars(ModelMap map, @RequestParam("id") String[] ids) {
		carDAO.delete(ids);
		List<CarDTO> cars = carDAO.findAll();
		map.addAttribute("carList", cars);
		return "carList";

	}

	@RequestMapping(method = RequestMethod.POST, params = {"id", "make", "model", "modelYear"})
	public String saveCar(ModelMap map, @RequestParam("id") int id, @RequestParam("make") String make,
			@RequestParam("model") String model, @RequestParam("modelYear") String modelYear) {
		CarDTO car = new CarDTO();
		car.setMake(make);
		car.setModel(model);
		car.setId(id);

		car.setModelYear(modelYear);
		System.out.println(id);
		if(id!=-1){
			System.out.println("in update");
			carDAO.update(car);	
		}
		
		else{
			System.out.println("in create");
			carDAO.create(car);
		}
		List<CarDTO> cars = carDAO.findAll();
		map.addAttribute("carList", cars);
		System.out.println(car);
		return "carList";
	}

	@RequestMapping(method = RequestMethod.GET, params ={"id"})
	public String editCar(ModelMap map, @RequestParam("id") int id) {
		CarDTO car = new CarDTO();
		car=carDAO.findById(id);
		map.addAttribute("car", car);
		return "carForm";
	}

}

