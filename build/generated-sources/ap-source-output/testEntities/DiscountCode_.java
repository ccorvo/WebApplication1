package testEntities;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import testEntities.CustomerData;

@Generated(value="EclipseLink-2.3.0.v20110604-r9504", date="2013-12-31T15:02:38")
@StaticMetamodel(DiscountCode.class)
public class DiscountCode_ { 

    public static volatile SingularAttribute<DiscountCode, BigDecimal> rate;
    public static volatile CollectionAttribute<DiscountCode, CustomerData> customerDataCollection;
    public static volatile SingularAttribute<DiscountCode, Character> discountCode;

}