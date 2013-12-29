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
public class MicroMarketJpaController implements Serializable {

    public MicroMarketJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MicroMarket microMarket) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (microMarket.getCustomerDataCollection() == null) {
            microMarket.setCustomerDataCollection(new ArrayList<CustomerData>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<CustomerData> attachedCustomerDataCollection = new ArrayList<CustomerData>();
            for (CustomerData customerDataCollectionCustomerDataToAttach : microMarket.getCustomerDataCollection()) {
                customerDataCollectionCustomerDataToAttach = em.getReference(customerDataCollectionCustomerDataToAttach.getClass(), customerDataCollectionCustomerDataToAttach.getCustomerId());
                attachedCustomerDataCollection.add(customerDataCollectionCustomerDataToAttach);
            }
            microMarket.setCustomerDataCollection(attachedCustomerDataCollection);
            em.persist(microMarket);
            for (CustomerData customerDataCollectionCustomerData : microMarket.getCustomerDataCollection()) {
                MicroMarket oldZipOfCustomerDataCollectionCustomerData = customerDataCollectionCustomerData.getZip();
                customerDataCollectionCustomerData.setZip(microMarket);
                customerDataCollectionCustomerData = em.merge(customerDataCollectionCustomerData);
                if (oldZipOfCustomerDataCollectionCustomerData != null) {
                    oldZipOfCustomerDataCollectionCustomerData.getCustomerDataCollection().remove(customerDataCollectionCustomerData);
                    oldZipOfCustomerDataCollectionCustomerData = em.merge(oldZipOfCustomerDataCollectionCustomerData);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findMicroMarket(microMarket.getZipCode()) != null) {
                throw new PreexistingEntityException("MicroMarket " + microMarket + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MicroMarket microMarket) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            MicroMarket persistentMicroMarket = em.find(MicroMarket.class, microMarket.getZipCode());
            Collection<CustomerData> customerDataCollectionOld = persistentMicroMarket.getCustomerDataCollection();
            Collection<CustomerData> customerDataCollectionNew = microMarket.getCustomerDataCollection();
            List<String> illegalOrphanMessages = null;
            for (CustomerData customerDataCollectionOldCustomerData : customerDataCollectionOld) {
                if (!customerDataCollectionNew.contains(customerDataCollectionOldCustomerData)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain CustomerData " + customerDataCollectionOldCustomerData + " since its zip field is not nullable.");
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
            microMarket.setCustomerDataCollection(customerDataCollectionNew);
            microMarket = em.merge(microMarket);
            for (CustomerData customerDataCollectionNewCustomerData : customerDataCollectionNew) {
                if (!customerDataCollectionOld.contains(customerDataCollectionNewCustomerData)) {
                    MicroMarket oldZipOfCustomerDataCollectionNewCustomerData = customerDataCollectionNewCustomerData.getZip();
                    customerDataCollectionNewCustomerData.setZip(microMarket);
                    customerDataCollectionNewCustomerData = em.merge(customerDataCollectionNewCustomerData);
                    if (oldZipOfCustomerDataCollectionNewCustomerData != null && !oldZipOfCustomerDataCollectionNewCustomerData.equals(microMarket)) {
                        oldZipOfCustomerDataCollectionNewCustomerData.getCustomerDataCollection().remove(customerDataCollectionNewCustomerData);
                        oldZipOfCustomerDataCollectionNewCustomerData = em.merge(oldZipOfCustomerDataCollectionNewCustomerData);
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
                String id = microMarket.getZipCode();
                if (findMicroMarket(id) == null) {
                    throw new NonexistentEntityException("The microMarket with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            MicroMarket microMarket;
            try {
                microMarket = em.getReference(MicroMarket.class, id);
                microMarket.getZipCode();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The microMarket with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<CustomerData> customerDataCollectionOrphanCheck = microMarket.getCustomerDataCollection();
            for (CustomerData customerDataCollectionOrphanCheckCustomerData : customerDataCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This MicroMarket (" + microMarket + ") cannot be destroyed since the CustomerData " + customerDataCollectionOrphanCheckCustomerData + " in its customerDataCollection field has a non-nullable zip field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(microMarket);
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

    public List<MicroMarket> findMicroMarketEntities() {
        return findMicroMarketEntities(true, -1, -1);
    }

    public List<MicroMarket> findMicroMarketEntities(int maxResults, int firstResult) {
        return findMicroMarketEntities(false, maxResults, firstResult);
    }

    private List<MicroMarket> findMicroMarketEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MicroMarket.class));
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

    public MicroMarket findMicroMarket(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MicroMarket.class, id);
        } finally {
            em.close();
        }
    }

    public int getMicroMarketCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MicroMarket> rt = cq.from(MicroMarket.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
