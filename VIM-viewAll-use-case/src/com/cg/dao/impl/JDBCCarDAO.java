package com.cg.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.cg.dao.CarDAO;
import com.cg.dto.CarDTO;
import com.cg.util.ServiceLocator;

//TODO 1 Import appropriate classes based on following TODOs
//Follow TODOs (if available)
/**
 * 
 * This is a JDBCCarDAO class
 * @see java.lang.Object
 * @author Abhishek
 * 
 *
 */
 
 //TODO 2 Implement appropriate Interface
public class JDBCCarDAO implements CarDAO {
	//TODO 3 Declare a local variable datasource of type DataSource follow encapsulation principle
	private DataSource datasource;

	
	public JDBCCarDAO() {
		
		String dataSourceJndiName= "jdbc/VIMDataSource";
		datasource = ServiceLocator.getDataSource(dataSourceJndiName);
		//TODO 4 Initialize the dataSource in TODO 3 using ServiceLocator API
		//TODO 5 If any error occur in getting this service then throw ServiceLocatorException
		//with error message as 'Container Service not available'
		
	}

	
	public DataSource getDatasource() {
		return datasource;
	}


	public void setDatasource(DataSource datasource) {
		this.datasource = datasource;
	}


	@Override
	/**
	 * This method is mapped to ADD_CAR_ACTION
	 * @param car a CarDTO 
	 */
	public void create(CarDTO car) {
		// TODO Auto-generated method stub
		Connection connection = null;

		String insertQuery = "insert into Car (MAKE,MODEL,MODEL_YEAR) values(?,?,?)";
		
		try{
			try {
				//TODO 6 
				//Get a connection using datasource
				//TODO 6 
				//Get a connection using datasource
				connection = datasource.getConnection();
				//Start the JDBC transaction
				connection.setAutoCommit(true);
				//Create a PreparedStatement using insertQuery
				PreparedStatement selectStatement = connection.prepareStatement(insertQuery);
				//Set the parameters of the PreparedStatement
//				selectStatement.setString(1, "MAKE");
//				selectStatement.setString(2, "MODEL");
//				selectStatement.setString(3, "MODEL_YEAR");
				selectStatement.setString(1, car.getMake());
				selectStatement.setString(2, car.getModel());
				selectStatement.setString(3, car.getModelYear());
				int set = selectStatement.executeUpdate();
				//Invoke appropriate API of JDBC to update and commit the record

				
				//Set the parameters of the PreparedStatement
				//Invoke appropriate API of JDBC to update and commit the record
				
			} 
			catch (SQLException e) {
//				e.printStackTrace();
				
				if (connection != null)
					connection.rollback();
				throw e;
			} 
			finally {
				if (connection != null)
					connection.close();			
			}
		}
		catch(SQLException e){
			throw new JDBCDaoException("SQL error while excecuting this query: "+ insertQuery,e);
		}
	
		
	}

	@Override
	/**
	 * This method is mapped to DELETE_CAR_ACTION
	 * @param ids collection of CAR ids. 
	 */
	public void delete(String[] ids) {
		Connection connection = null;
		String deleteQuery = "delete from car where id=?";

		try{
			try {
				//TODO 7 
				//Get a connection using datasource
				connection= datasource.getConnection();
				connection.setAutoCommit(false);
				//Create a PreparedStatement using insertQuery
				PreparedStatement selectStatement = connection.prepareStatement(deleteQuery);
				for(int i= 0;i < ids.length;i++){
					selectStatement.setInt(1, Integer.parseInt(ids[i]));				
					int set = selectStatement.executeUpdate();
				}


				
				//Start the JDBC transaction
				//Create a PreparedStatement using deleteQuery
				//Set the parameters of the PreparedStatement
				//Invoke appropriate API of JDBC to update and commit the record
				
			} 
			catch (SQLException e) {
				if (connection != null)
					connection.rollback();	
				
				throw e;
			} 
			finally {
				if (connection != null)
					connection.commit();
					connection.close();				
			}
		}
		catch(SQLException e){
			throw new JDBCDaoException("SQL error while excecuting query: "+ deleteQuery,e);
		}		
	}

	@Override
	/**
	 * This method is mapped to EDIT_CAR_ACTION
	 * @param car a CarDTO 
	 */
	public void update(CarDTO car) {
		// TODO Auto-generated method stub
		String updateQuery = "update car set make=?,model=?,model_year=? where id=?";
		Connection connection = null;
		
		try{
			try {
				//TODO 8 
				//Get a connection using datasource
				connection= datasource.getConnection();
				connection.setAutoCommit(false);
				//Create a PreparedStatement using insertQuery
				PreparedStatement selectStatement = connection.prepareStatement(updateQuery);
				
				selectStatement.setString(1, car.getMake());
				selectStatement.setString(2, car.getModel());
				selectStatement.setString(3, car.getModelYear());
				selectStatement.setInt(4, car.getId());

				int set = selectStatement.executeUpdate();
				//Start the JDBC transaction
				
				//Create a PreparedStatement using updateQuery
				//Set the parameters of the PreparedStatement
				//Invoke appropriate API of JDBC to update and commit the record
				
			} 
			catch (SQLException e) {
				if(connection != null)
					connection.rollback();
				
				throw e;
			}
			finally {
				if (connection != null)
					connection.commit();

					connection.close();
			}
		}
		catch(SQLException e){
			throw new JDBCDaoException("SQL error while excecuting query: "+ updateQuery,e);
		}
	}
	
	
	@Override
	/**
	 * This method is mapped to VIEW_CAR_LIST_ACTION
	 * @return List list of cars 
	 */
	public List<CarDTO> findAll() {
		List<CarDTO> carList = new ArrayList<CarDTO>();

		Connection connection = null;
		String selectQuery = "select * from CAR";
		
		try{
			try {
				connection= datasource.getConnection();
				//TODO 9 
				//Get a connection using datasource
				//Don't start the JDBC transaction
				connection.setAutoCommit(true);
				//Create a Statement using selectQuery
				Statement selectStatement=connection.createStatement();
				//Invoke appropriate API of JDBC to fire the query
				ResultSet result= selectStatement.executeQuery(selectQuery);
				//For iteration over the ResultSet populate carList with CarDTO 
				while(result.next()){
					CarDTO car= new CarDTO();
					car.setId(result.getInt("ID"));
					car.setMake(result.getString("MAKE"));
					car.setModel(result.getString("MODEL"));
					car.setModelYear(result.getString("MODEL_YEAR"));
					carList.add(car);
					
					
				}
			} 
			finally {
				if (connection != null)
					connection.close();				
			}
		}
		catch(SQLException e){
			throw new JDBCDaoException("SQL error while excecuting query: "+ selectQuery,e);
		}		

		return carList;
	}

	@Override
	/**
	 * This method is utility method for finding car by id.
	 * @param id id of the car to be searched
	 * @return  CarDTO A CarDTO
	 */
	public CarDTO findById(int id) {
		// TODO Auto-generated method stub
		String selectQuery = "select * from CAR where id=?";
		CarDTO car = new CarDTO();
		Connection connection = null;
		
		try{
			try {
				connection = datasource.getConnection();
				connection.setAutoCommit(false);
				PreparedStatement selectStatement = connection
				.prepareStatement(selectQuery);
				selectStatement.setInt(1, id);
				ResultSet result = selectStatement.executeQuery();
				result.next();

				car.setId(result.getInt("id"));
				car.setMake(result.getString("make"));
				car.setModel(result.getString("model"));
				car.setModelYear(result.getString("MODEL_YEAR"));
			} 
			finally {
				if (connection != null)
					connection.close();			
			}
		}
		catch(SQLException e){
			throw new JDBCDaoException("SQL error while excecuting query: "+ selectQuery,e);
		}		

		return car;
	}

	

}

