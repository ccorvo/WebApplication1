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
public class CustomerDataJpaController implements Serializable {

    public CustomerDataJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CustomerData customerData) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (customerData.getPurchaseOrderCollection() == null) {
            customerData.setPurchaseOrderCollection(new ArrayList<PurchaseOrder>());
        }
        
        System.out.println("Corvo: Before try catch block in create Function");
        EntityManager em = null;
        try {
           // utx.begin();
            em = getEntityManager();
            MicroMarket zip = customerData.getZip();
            if (zip != null) {
                zip = em.getReference(zip.getClass(), zip.getZipCode());
                customerData.setZip(zip);
            }
            System.out.println("Corvo: After MiroMarket");
            DiscountCode discountCode = customerData.getDiscountCode();
            if (discountCode != null) {
                discountCode = em.getReference(discountCode.getClass(), discountCode.getDiscountCode());
                customerData.setDiscountCode(discountCode);
            }
            System.out.println("Corvo: After DiscountCode");
            Collection<PurchaseOrder> attachedPurchaseOrderCollection = new ArrayList<PurchaseOrder>();
            for (PurchaseOrder purchaseOrderCollectionPurchaseOrderToAttach : customerData.getPurchaseOrderCollection()) {
                purchaseOrderCollectionPurchaseOrderToAttach = em.getReference(purchaseOrderCollectionPurchaseOrderToAttach.getClass(), purchaseOrderCollectionPurchaseOrderToAttach.getOrderNum());
                attachedPurchaseOrderCollection.add(purchaseOrderCollectionPurchaseOrderToAttach);
            }
            
            System.out.println("Corvo: After PurchaseOrder collection");
            customerData.setPurchaseOrderCollection(attachedPurchaseOrderCollection);
            em.persist(customerData);
            if (zip != null) {
                zip.getCustomerDataCollection().add(customerData);
                zip = em.merge(zip);
            }
            System.out.println("Corvo: AFter persist customerData");
            if (discountCode != null) {
                discountCode.getCustomerDataCollection().add(customerData);
                discountCode = em.merge(discountCode);
            }
            for (PurchaseOrder purchaseOrderCollectionPurchaseOrder : customerData.getPurchaseOrderCollection()) {
                CustomerData oldCustomerIdOfPurchaseOrderCollectionPurchaseOrder = purchaseOrderCollectionPurchaseOrder.getCustomerId();
                purchaseOrderCollectionPurchaseOrder.setCustomerId(customerData);
                purchaseOrderCollectionPurchaseOrder = em.merge(purchaseOrderCollectionPurchaseOrder);
                if (oldCustomerIdOfPurchaseOrderCollectionPurchaseOrder != null) {
                    oldCustomerIdOfPurchaseOrderCollectionPurchaseOrder.getPurchaseOrderCollection().remove(purchaseOrderCollectionPurchaseOrder);
                    oldCustomerIdOfPurchaseOrderCollectionPurchaseOrder = em.merge(oldCustomerIdOfPurchaseOrderCollectionPurchaseOrder);
                }
            }
           // utx.commit();
        } catch (Exception ex) {
            try {
               // utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findCustomerData(customerData.getCustomerId()) != null) {
                throw new PreexistingEntityException("CustomerData " + customerData + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CustomerData customerData) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            CustomerData persistentCustomerData = em.find(CustomerData.class, customerData.getCustomerId());
            MicroMarket zipOld = persistentCustomerData.getZip();
            MicroMarket zipNew = customerData.getZip();
            DiscountCode discountCodeOld = persistentCustomerData.getDiscountCode();
            DiscountCode discountCodeNew = customerData.getDiscountCode();
            Collection<PurchaseOrder> purchaseOrderCollectionOld = persistentCustomerData.getPurchaseOrderCollection();
            Collection<PurchaseOrder> purchaseOrderCollectionNew = customerData.getPurchaseOrderCollection();
            List<String> illegalOrphanMessages = null;
            for (PurchaseOrder purchaseOrderCollectionOldPurchaseOrder : purchaseOrderCollectionOld) {
                if (!purchaseOrderCollectionNew.contains(purchaseOrderCollectionOldPurchaseOrder)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PurchaseOrder " + purchaseOrderCollectionOldPurchaseOrder + " since its customerId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (zipNew != null) {
                zipNew = em.getReference(zipNew.getClass(), zipNew.getZipCode());
                customerData.setZip(zipNew);
            }
            if (discountCodeNew != null) {
                discountCodeNew = em.getReference(discountCodeNew.getClass(), discountCodeNew.getDiscountCode());
                customerData.setDiscountCode(discountCodeNew);
            }
            Collection<PurchaseOrder> attachedPurchaseOrderCollectionNew = new ArrayList<PurchaseOrder>();
            for (PurchaseOrder purchaseOrderCollectionNewPurchaseOrderToAttach : purchaseOrderCollectionNew) {
                purchaseOrderCollectionNewPurchaseOrderToAttach = em.getReference(purchaseOrderCollectionNewPurchaseOrderToAttach.getClass(), purchaseOrderCollectionNewPurchaseOrderToAttach.getOrderNum());
                attachedPurchaseOrderCollectionNew.add(purchaseOrderCollectionNewPurchaseOrderToAttach);
            }
            purchaseOrderCollectionNew = attachedPurchaseOrderCollectionNew;
            customerData.setPurchaseOrderCollection(purchaseOrderCollectionNew);
            customerData = em.merge(customerData);
            if (zipOld != null && !zipOld.equals(zipNew)) {
                zipOld.getCustomerDataCollection().remove(customerData);
                zipOld = em.merge(zipOld);
            }
            if (zipNew != null && !zipNew.equals(zipOld)) {
                zipNew.getCustomerDataCollection().add(customerData);
                zipNew = em.merge(zipNew);
            }
            if (discountCodeOld != null && !discountCodeOld.equals(discountCodeNew)) {
                discountCodeOld.getCustomerDataCollection().remove(customerData);
                discountCodeOld = em.merge(discountCodeOld);
            }
            if (discountCodeNew != null && !discountCodeNew.equals(discountCodeOld)) {
                discountCodeNew.getCustomerDataCollection().add(customerData);
                discountCodeNew = em.merge(discountCodeNew);
            }
            for (PurchaseOrder purchaseOrderCollectionNewPurchaseOrder : purchaseOrderCollectionNew) {
                if (!purchaseOrderCollectionOld.contains(purchaseOrderCollectionNewPurchaseOrder)) {
                    CustomerData oldCustomerIdOfPurchaseOrderCollectionNewPurchaseOrder = purchaseOrderCollectionNewPurchaseOrder.getCustomerId();
                    purchaseOrderCollectionNewPurchaseOrder.setCustomerId(customerData);
                    purchaseOrderCollectionNewPurchaseOrder = em.merge(purchaseOrderCollectionNewPurchaseOrder);
                    if (oldCustomerIdOfPurchaseOrderCollectionNewPurchaseOrder != null && !oldCustomerIdOfPurchaseOrderCollectionNewPurchaseOrder.equals(customerData)) {
                        oldCustomerIdOfPurchaseOrderCollectionNewPurchaseOrder.getPurchaseOrderCollection().remove(purchaseOrderCollectionNewPurchaseOrder);
                        oldCustomerIdOfPurchaseOrderCollectionNewPurchaseOrder = em.merge(oldCustomerIdOfPurchaseOrderCollectionNewPurchaseOrder);
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
                Integer id = customerData.getCustomerId();
                if (findCustomerData(id) == null) {
                    throw new NonexistentEntityException("The customerData with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            CustomerData customerData;
            try {
                customerData = em.getReference(CustomerData.class, id);
                customerData.getCustomerId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The customerData with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<PurchaseOrder> purchaseOrderCollectionOrphanCheck = customerData.getPurchaseOrderCollection();
            for (PurchaseOrder purchaseOrderCollectionOrphanCheckPurchaseOrder : purchaseOrderCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This CustomerData (" + customerData + ") cannot be destroyed since the PurchaseOrder " + purchaseOrderCollectionOrphanCheckPurchaseOrder + " in its purchaseOrderCollection field has a non-nullable customerId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            MicroMarket zip = customerData.getZip();
            if (zip != null) {
                zip.getCustomerDataCollection().remove(customerData);
                zip = em.merge(zip);
            }
            DiscountCode discountCode = customerData.getDiscountCode();
            if (discountCode != null) {
                discountCode.getCustomerDataCollection().remove(customerData);
                discountCode = em.merge(discountCode);
            }
            em.remove(customerData);
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

    public List<CustomerData> findCustomerDataEntities() {
        return findCustomerDataEntities(true, -1, -1);
    }

    public List<CustomerData> findCustomerDataEntities(int maxResults, int firstResult) {
        return findCustomerDataEntities(false, maxResults, firstResult);
    }

    private List<CustomerData> findCustomerDataEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CustomerData.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            System.out.println("Corvo: Right Before getResultList call");
            return q.getResultList();
        } finally {
            System.out.println("Corvo: In Finally Block of findCustomerDataEntities");
          
            em.close();
        }
    }

    public CustomerData findCustomerData(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CustomerData.class, id);
        } finally {
            em.close();
        }
    }

    public int getCustomerDataCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CustomerData> rt = cq.from(CustomerData.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
