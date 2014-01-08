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
public class DiscountCodeFacade extends AbstractFacade<DiscountCode> {
    @PersistenceContext(unitName = "WebApplication1PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DiscountCodeFacade() {
        super(DiscountCode.class);
    }
    
}
