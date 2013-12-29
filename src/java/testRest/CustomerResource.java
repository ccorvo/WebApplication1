/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testRest;

import com.corvo.customerRestSupport.Address;
import com.corvo.customerRestSupport.Customer;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
import testEntities.exceptions.NonexistentEntityException;

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
    
  
    
    @EJB   //Use Dependency Injection to inject CustomerDataFacade Stateless EJB
    CustomerDataFacade customerFacade;
    
    //private CustomerDataFacade customerFacade;
    
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
       
        CustomerData customerData;
                
       // customerFacade = new CustomerDataFacade();
        if (customerFacade == null)
            System.out.println("customerFacade is null");
        try
        {
        customerData = customerFacade.find(customerDataId);       
        
        System.out.println("Cutomer name for id: "+ customerDataId + " is: " + customerData.getName());
        
        return customerData;
        
        }
        catch( Exception ex)
        {
            System.out.println("Corvo: Cauught Exception: Requested Record Not Found");
            
            CustomerData customerData1 = null;
            
            return customerData1;
        }
        

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
       
          
        
        CustomerData customerData;
                
       // customerFacade = new CustomerDataFacade();
        if (customerFacade == null)
            System.out.println("customerFacade is null");
        
        customerData = customerFacade.find(customerDataId);       
        
        System.out.println("Cutomer name for id: "+ customerDataId + " is: " + customerData.getName());
        return customerData;   
        
        
        
    }    
    
}
