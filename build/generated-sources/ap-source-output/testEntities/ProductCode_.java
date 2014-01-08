package testEntities;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import testEntities.Product;

@Generated(value="EclipseLink-2.3.0.v20110604-r9504", date="2014-01-07T15:53:19")
@StaticMetamodel(ProductCode.class)
public class ProductCode_ { 

    public static volatile SingularAttribute<ProductCode, String> description;
    public static volatile SingularAttribute<ProductCode, String> prodCode;
    public static volatile CollectionAttribute<ProductCode, Product> productCollection;
    public static volatile SingularAttribute<ProductCode, Character> discountCode;

}