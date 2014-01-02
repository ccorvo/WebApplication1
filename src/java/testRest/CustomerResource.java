/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testRest;

import com.corvo.customerRestSupport.Address;
import com.corvo.customerRestSupport.Customer;
import java.math.BigDecimal;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.MediaType;
import sessonBeanForEntities.CustomerDataFacade;
import testEntities.CustomerData;
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
    @POST
    @Path("/createCustomer")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    
    public void createCustomer(@FormParam("name") String customerName, @FormParam("address1") String address_1, 
    @FormParam("address2") String address_2, @FormParam("city") String city, @FormParam("state") String state, 
    @FormParam("zip") String zip_code, @FormParam("discountCode") String discountCode,
    @FormParam("discountRate") BigDecimal rate, @FormParam("customerId") Integer customerId ) {
        
       System.out.println("Corvo: createCustomer Called with name = " + customerName + " Address1: " + address_1); 
        CustomerData customer = new CustomerData();
        customer.setName(customerName);
        customer.setAddressline1(address_1);
        customer.setAddressline2(address_2);
        customer.setCity(city);
        customer.setState(state);
        customer.setCustomerId(customerId);
        
        DiscountCode discount = new DiscountCode();
        discount.setDiscountCode('M');
        discount.setRate(rate);
        customer.setDiscountCode(discount);   //Populate Discount Code of customer
        
        MicroMarket zip = new MicroMarket();
        zip.setZipCode(zip_code);
        
        customer.setZip(zip);  //Populate MicroMarket Object of customer
        
       
        if (customerFacade == null)
            System.out.println("customerFacade is null");
        try
        {
        customerFacade.create(customer);   
        
        }
        catch( Exception ex)
        {
            System.out.println("Corvo: Cauught Exception: " + ex.getMessage());
            
           
        }
        
        
        
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
        
        int count = customerFacade.count();
        
        System.out.println("Cutomer name for id: "+ customerDataId + " is: " + customerData.getName() + " Count: " + count);
        
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
