/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testRest;

import com.corvo.customerRestSupport.Address;
import com.corvo.customerRestSupport.Customer;
import java.math.BigDecimal;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.transaction.UserTransaction;
import javax.ws.rs.*;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.MediaType;
import testEntities.CustomerData;
import testEntities.CustomerDataJpaController;
import testEntities.DiscountCode;
import testEntities.MicroMarket;

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
    
  @PersistenceUnit(unitName="WebApplication1PU") //inject from your application server 
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
             System.out.println("Corvo: Before return from getCustomerData");
                     
        } catch (Exception ex) { 
            System.out.println("Exception Using JPA Controller: " + ex.getMessage() );
            
        } 
        
        return customerData;
        
        

    }    
    
    
   // If the GET request requires an application XML response, the following method will be called.
    @GET
    @Produces (MediaType.APPLICATION_XML)
    @Path("/AllCustomerData")
    public List <CustomerData> getAllCustomers()
    {
        
        System.out.println("Corvo: got to getAllCustomers");
        List <CustomerData> customerData = null;
       
        CustomerDataJpaController customerJpaController = new CustomerDataJpaController(utx, emf); //create an instance of your jpa controller and pass in the injected emf and utx 
        try { 
             customerData = customerJpaController.findCustomerDataEntities();
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
    
  
    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Path("/createCustomer")
    
    public void createCustomer(@FormParam("name") String customerName, @FormParam("address1") String address_1,
            @FormParam("address2") String address_2, @FormParam("city") String city, @FormParam("state") String state,
            @FormParam("zip") String zip_code, @FormParam("discountCode") String discountCode, 
            @FormParam("discountRate") BigDecimal rate) {
        
       
        System.out.println("Corvo: createCustomer Called with name = " + customerName + " Address1: " + address_1); 
        CustomerData customer = new CustomerData();
        customer.setName(customerName);
        customer.setAddressline1(address_1);
        customer.setAddressline2(address_2);
        customer.setCity(city);
        customer.setState(state);
        customer.setCustomerId(14654);
        
        DiscountCode discount = new DiscountCode();
        Character discountCodeCharacter = discountCode.charAt(0);
        
        discount.setDiscountCode('Q');
        discount.setRate(rate);
        
        customer.setDiscountCode(discount);
        
        MicroMarket zip = new MicroMarket();
        zip.setZipCode(zip_code);
        
        customer.setZip(zip);  //Populate MicroMarket Object of customer

        CustomerDataJpaController customerJpaController = new CustomerDataJpaController(utx, emf); //create an instance of your jpa controller and pass in the injected emf and utx 
        try { 
             customerJpaController.create(customer);
      } catch (Exception ex) { 
          System.out.println("Exception Using JPA Controller: " + ex.getMessage() );
            
        } 
        
      
    }
  
}
