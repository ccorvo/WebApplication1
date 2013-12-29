/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testEntities;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import testEntities.exceptions.IllegalOrphanException;
import testEntities.exceptions.NonexistentEntityException;
import testEntities.exceptions.PreexistingEntityException;
import testEntities.exceptions.RollbackFailureException;

/**
 *
 * @author ccorvo
 */
public class DiscountCodeJpaController implements Serializable {

    public DiscountCodeJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DiscountCode discountCode) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (discountCode.getCustomerDataCollection() == null) {
            discountCode.setCustomerDataCollection(new ArrayList<CustomerData>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<CustomerData> attachedCustomerDataCollection = new ArrayList<CustomerData>();
            for (CustomerData customerDataCollectionCustomerDataToAttach : discountCode.getCustomerDataCollection()) {
                customerDataCollectionCustomerDataToAttach = em.getReference(customerDataCollectionCustomerDataToAttach.getClass(), customerDataCollectionCustomerDataToAttach.getCustomerId());
                attachedCustomerDataCollection.add(customerDataCollectionCustomerDataToAttach);
            }
            discountCode.setCustomerDataCollection(attachedCustomerDataCollection);
            em.persist(discountCode);
            for (CustomerData customerDataCollectionCustomerData : discountCode.getCustomerDataCollection()) {
                DiscountCode oldDiscountCodeOfCustomerDataCollectionCustomerData = customerDataCollectionCustomerData.getDiscountCode();
                customerDataCollectionCustomerData.setDiscountCode(discountCode);
                customerDataCollectionCustomerData = em.merge(customerDataCollectionCustomerData);
                if (oldDiscountCodeOfCustomerDataCollectionCustomerData != null) {
                    oldDiscountCodeOfCustomerDataCollectionCustomerData.getCustomerDataCollection().remove(customerDataCollectionCustomerData);
                    oldDiscountCodeOfCustomerDataCollectionCustomerData = em.merge(oldDiscountCodeOfCustomerDataCollectionCustomerData);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findDiscountCode(discountCode.getDiscountCode()) != null) {
                throw new PreexistingEntityException("DiscountCode " + discountCode + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DiscountCode discountCode) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            DiscountCode persistentDiscountCode = em.find(DiscountCode.class, discountCode.getDiscountCode());
            Collection<CustomerData> customerDataCollectionOld = persistentDiscountCode.getCustomerDataCollection();
            Collection<CustomerData> customerDataCollectionNew = discountCode.getCustomerDataCollection();
            List<String> illegalOrphanMessages = null;
            for (CustomerData customerDataCollectionOldCustomerData : customerDataCollectionOld) {
                if (!customerDataCollectionNew.contains(customerDataCollectionOldCustomerData)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain CustomerData " + customerDataCollectionOldCustomerData + " since its discountCode field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<CustomerData> attachedCustomerDataCollectionNew = new ArrayList<CustomerData>();
            for (CustomerData customerDataCollectionNewCustomerDataToAttach : customerDataCollectionNew) {
                customerDataCollectionNewCustomerDataToAttach = em.getReference(customerDataCollectionNewCustomerDataToAttach.getClass(), customerDataCollectionNewCustomerDataToAttach.getCustomerId());
                attachedCustomerDataCollectionNew.add(customerDataCollectionNewCustomerDataToAttach);
            }
            customerDataCollectionNew = attachedCustomerDataCollectionNew;
            discountCode.setCustomerDataCollection(customerDataCollectionNew);
            discountCode = em.merge(discountCode);
            for (CustomerData customerDataCollectionNewCustomerData : customerDataCollectionNew) {
                if (!customerDataCollectionOld.contains(customerDataCollectionNewCustomerData)) {
                    DiscountCode oldDiscountCodeOfCustomerDataCollectionNewCustomerData = customerDataCollectionNewCustomerData.getDiscountCode();
                    customerDataCollectionNewCustomerData.setDiscountCode(discountCode);
                    customerDataCollectionNewCustomerData = em.merge(customerDataCollectionNewCustomerData);
                    if (oldDiscountCodeOfCustomerDataCollectionNewCustomerData != null && !oldDiscountCodeOfCustomerDataCollectionNewCustomerData.equals(discountCode)) {
                        oldDiscountCodeOfCustomerDataCollectionNewCustomerData.getCustomerDataCollection().remove(customerDataCollectionNewCustomerData);
                        oldDiscountCodeOfCustomerDataCollectionNewCustomerData = em.merge(oldDiscountCodeOfCustomerDataCollectionNewCustomerData);
                    }
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Character id = discountCode.getDiscountCode();
                if (findDiscountCode(id) == null) {
                    throw new NonexistentEntityException("The discountCode with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Character id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            DiscountCode discountCode;
            try {
                discountCode = em.getReference(DiscountCode.class, id);
                discountCode.getDiscountCode();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The discountCode with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<CustomerData> customerDataCollectionOrphanCheck = discountCode.getCustomerDataCollection();
            for (CustomerData customerDataCollectionOrphanCheckCustomerData : customerDataCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This DiscountCode (" + discountCode + ") cannot be destroyed since the CustomerData " + customerDataCollectionOrphanCheckCustomerData + " in its customerDataCollection field has a non-nullable discountCode field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(discountCode);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DiscountCode> findDiscountCodeEntities() {
        return findDiscountCodeEntities(true, -1, -1);
    }

    public List<DiscountCode> findDiscountCodeEntities(int maxResults, int firstResult) {
        return findDiscountCodeEntities(false, maxResults, firstResult);
    }

    private List<DiscountCode> findDiscountCodeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DiscountCode.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public DiscountCode findDiscountCode(Character id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DiscountCode.class, id);
        } finally {
            em.close();
        }
    }

    public int getDiscountCodeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DiscountCode> rt = cq.from(DiscountCode.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
