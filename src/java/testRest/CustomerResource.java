/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testRest;

import com.corvo.customerRestSupport.Address;
import com.corvo.customerRestSupport.Customer;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.transaction.UserTransaction;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import sessonBeanForEntities.CustomerDataFacade;
import testEntities.CustomerData;
import testEntities.CustomerDataJpaController;

/**
 * REST Web Service
 *
 * @author ccorvo
 */
@Stateless
@Path("/Customer")
public class CustomerResource {

    @Context
    private UriInfo context;
    
    //To use the Netbeans wizard created JPA controllers, each jpa controller requires as input to the constructor
    //an Entity Manager Factory object and a User Tranaction object. These can be created via dependency injection as done
    //below 
    
  @PersistenceUnit(unitName="WebApplication1PU") //om your application server 
    EntityManagerFactory emf; 
  
    @Resource //inject from your application server 
    UserTransaction utx;
    
  
    
    /**
     * Creates a new instance of CustomerResource
     */
    public CustomerResource() {
        
      
        System.out.println("CORVO: CustomerResource constructor called");
        
        
    }

    /**
     * Retrieves representation of an instance of testRest.CustomerResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/xml")
    public String getXml() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of CustomerResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/xml")
    public void putXml(String content) {
 
    }
    
  // If the GET request requires an application XML response, the following method will be called.
    @GET
    @Produces (MediaType.APPLICATION_XML)
    @Path("/Info/{inputString}")
    public Customer getCustomer( @PathParam("inputString") int customerId)
    {
            
      
            Customer customerObject = new Customer();
            customerObject.setCustomerName("Christopher Corvo");
            customerObject.setCustomerId(customerId);
            
            Address myaddress = new Address();
            myaddress.setCity("Lumberton");
            myaddress.setLine1("5 Stonehenge Drive");
            myaddress.setState("New Jersey");
            myaddress.setZipcode("08048"); 
            customerObject.setAddress(myaddress);
            
                 
        return customerObject;
        
    }  
    
 // If the GET request requires an application XML response, the following method will be called.
    @GET
    @Produces (MediaType.APPLICATION_XML)
    @Path("/CustomerData/{inputString}")
    public CustomerData getCustomerData( @PathParam("inputString") int customerDataId)
    {
        CustomerData customerData = null;
       
        CustomerDataJpaController customerJpaController = new CustomerDataJpaController(utx, emf); //create an instance of your jpa controller and pass in the injected emf and utx 
        try { 
             customerData = customerJpaController.findCustomerData(customerDataId);
        } catch (Exception ex) { 
            System.out.println("Exception Using JPA Controller: " + ex.getMessage() );
            
        } 
        
        return customerData;
        
        

    }    
    
    
    //If the GET Request requires an application-JSON response, the following method will be called.
  @GET
    @Produces (MediaType.APPLICATION_JSON)
    @Path("/Info/{inputString}")
    public Customer getCustomerJson( @PathParam("inputString") int customerId)
    {
       
            Customer customerObject = new Customer();
            customerObject.setCustomerName("Christopher Corvo");
            customerObject.setCustomerId(customerId);
            
            Address myaddress = new Address();
            myaddress.setCity("Lumberton");
            myaddress.setLine1("5 Stonehenge Drive");
            myaddress.setState("New Jersey");
            myaddress.setZipcode("08048"); 
            customerObject.setAddress(myaddress);
        
        return customerObject;
        
    }
 //If the GET Request requires an application-JSON response, the following method will be called.
  @GET
    @Produces (MediaType.APPLICATION_JSON)
    @Path("/CustomerData/{inputString}")
    public CustomerData getCustomerJson1( @PathParam("inputString") int customerDataId)
    {
       
          
     
        CustomerData customerData = null;
        /*        
       // customerFacade = new CustomerDataFacade();
        if (customerFacade == null)
            System.out.println("customerFacade is null");
        
        customerData = customerFacade.find(customerDataId);       
        
        System.out.println("Cutomer name for id: "+ customerDataId + " is: " + customerData.getName());
        return customerData;   
        
     */   
        
        
        return customerData;
    }    
    
}
