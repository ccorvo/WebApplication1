/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testEntities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ccorvo
 */
@Entity
@Table(name = "CUSTOMER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CustomerData.findAll", query = "SELECT c FROM CustomerData c"),
    @NamedQuery(name = "CustomerData.findByCustomerId", query = "SELECT c FROM CustomerData c WHERE c.customerId = :customerId"),
    @NamedQuery(name = "CustomerData.findByName", query = "SELECT c FROM CustomerData c WHERE c.name = :name"),
    @NamedQuery(name = "CustomerData.findByAddressline1", query = "SELECT c FROM CustomerData c WHERE c.addressline1 = :addressline1"),
    @NamedQuery(name = "CustomerData.findByAddressline2", query = "SELECT c FROM CustomerData c WHERE c.addressline2 = :addressline2"),
    @NamedQuery(name = "CustomerData.findByCity", query = "SELECT c FROM CustomerData c WHERE c.city = :city"),
    @NamedQuery(name = "CustomerData.findByState", query = "SELECT c FROM CustomerData c WHERE c.state = :state"),
    @NamedQuery(name = "CustomerData.findByPhone", query = "SELECT c FROM CustomerData c WHERE c.phone = :phone"),
    @NamedQuery(name = "CustomerData.findByFax", query = "SELECT c FROM CustomerData c WHERE c.fax = :fax"),
    @NamedQuery(name = "CustomerData.findByEmail", query = "SELECT c FROM CustomerData c WHERE c.email = :email"),
    @NamedQuery(name = "CustomerData.findByCreditLimit", query = "SELECT c FROM CustomerData c WHERE c.creditLimit = :creditLimit")})
public class CustomerData implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    @Basic(optional = false)
    @NotNull
    @Column(name = "CUSTOMER_ID")
    
    private Integer customerId;
    @Size(max = 30)
    @Column(name = "NAME")
   
    private String name;
    @Size(max = 30)
    @Column(name = "ADDRESSLINE1")
    private String addressline1;
    @Size(max = 30)
    @Column(name = "ADDRESSLINE2")
    private String addressline2;
    @Size(max = 25)
    @Column(name = "CITY")
    private String city;
    @Size(max = 2)
    @Column(name = "STATE")
    private String state;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 12)
    @Column(name = "PHONE")
    private String phone;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 12)
    @Column(name = "FAX")
    private String fax;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 40)
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "CREDIT_LIMIT")
    private Integer creditLimit;
    @JoinColumn(name = "ZIP", referencedColumnName = "ZIP_CODE")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private MicroMarket zip;
    @JoinColumn(name = "DISCOUNT_CODE", referencedColumnName = "DISCOUNT_CODE")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private DiscountCode discountCode;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customerId", fetch = FetchType.EAGER)
    private Collection<PurchaseOrder> purchaseOrderCollection;

    public CustomerData() {
    }

    public CustomerData(Integer customerId) {
        this.customerId = customerId;
    }
    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddressline1() {
        return addressline1;
    }

    public void setAddressline1(String addressline1) {
        this.addressline1 = addressline1;
    }
    public String getAddressline2() {
        return addressline2;
    }

    public void setAddressline2(String addressline2) {
        this.addressline2 = addressline2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Integer creditLimit) {
        this.creditLimit = creditLimit;
    }

    public MicroMarket getZip() {
        return zip;
    }

    public void setZip(MicroMarket zip) {
        this.zip = zip;
    }

    public DiscountCode getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(DiscountCode discountCode) {
        this.discountCode = discountCode;
    }

    @XmlTransient
    public Collection<PurchaseOrder> getPurchaseOrderCollection() {
        return purchaseOrderCollection;
    }

    public void setPurchaseOrderCollection(Collection<PurchaseOrder> purchaseOrderCollection) {
        this.purchaseOrderCollection = purchaseOrderCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (customerId != null ? customerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CustomerData)) {
            return false;
        }
        CustomerData other = (CustomerData) object;
        if ((this.customerId == null && other.customerId != null) || (this.customerId != null && !this.customerId.equals(other.customerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "testEntities.CustomerData[ customerId=" + customerId + " ]";
    }
    
}
