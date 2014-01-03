package testEntities;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import testEntities.DiscountCode;
import testEntities.MicroMarket;
import testEntities.PurchaseOrder;

@Generated(value="EclipseLink-2.3.0.v20110604-r9504", date="2014-01-03T12:05:59")
@StaticMetamodel(CustomerData.class)
public class CustomerData_ { 

    public static volatile SingularAttribute<CustomerData, MicroMarket> zip;
    public static volatile SingularAttribute<CustomerData, String> phone;
    public static volatile SingularAttribute<CustomerData, String> fax;
    public static volatile SingularAttribute<CustomerData, String> state;
    public static volatile SingularAttribute<CustomerData, Integer> creditLimit;
    public static volatile CollectionAttribute<CustomerData, PurchaseOrder> purchaseOrderCollection;
    public static volatile SingularAttribute<CustomerData, String> city;
    public static volatile SingularAttribute<CustomerData, String> addressline2;
    public static volatile SingularAttribute<CustomerData, Integer> customerId;
    public static volatile SingularAttribute<CustomerData, String> addressline1;
    public static volatile SingularAttribute<CustomerData, String> email;
    public static volatile SingularAttribute<CustomerData, String> name;
    public static volatile SingularAttribute<CustomerData, DiscountCode> discountCode;

}