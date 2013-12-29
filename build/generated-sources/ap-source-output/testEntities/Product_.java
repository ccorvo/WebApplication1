package testEntities;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import testEntities.Manufacturer;
import testEntities.ProductCode;
import testEntities.PurchaseOrder;

@Generated(value="EclipseLink-2.3.0.v20110604-r9504", date="2013-12-28T20:24:07")
@StaticMetamodel(Product.class)
public class Product_ { 

    public static volatile SingularAttribute<Product, BigDecimal> markup;
    public static volatile SingularAttribute<Product, Integer> quantityOnHand;
    public static volatile SingularAttribute<Product, Manufacturer> manufacturerId;
    public static volatile SingularAttribute<Product, ProductCode> productCode;
    public static volatile SingularAttribute<Product, String> description;
    public static volatile SingularAttribute<Product, BigDecimal> purchaseCost;
    public static volatile CollectionAttribute<Product, PurchaseOrder> purchaseOrderCollection;
    public static volatile SingularAttribute<Product, String> available;
    public static volatile SingularAttribute<Product, Integer> productId;

}