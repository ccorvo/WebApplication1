/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessonBeanForEntities;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import testEntities.CustomerData;

/**
 *
 * @author ccorvo
 */
@Stateless
public class CustomerDataFacade extends AbstractFacade<CustomerData> {
    @PersistenceContext(unitName = "WebApplication1PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        System.out.println("CORVO: Entity Manager is :"+ em);
        return em;
    }

    public CustomerDataFacade() {
        super(CustomerData.class);
    }
    
    @PostConstruct
    public void callPostConstruct(){
        
      System.out.println("Corvo:: Post Construct of CustomerDataFacade, EntityManager: " + em.toString());  
        
        
    }
    
}
