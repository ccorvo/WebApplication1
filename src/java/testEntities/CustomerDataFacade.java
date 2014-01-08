/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testEntities;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
        return em;
    }

    public CustomerDataFacade() {
        super(CustomerData.class);
    }
    
}
