package com.tsvika.home.ledger.storage.logic;

import com.tsvika.home.ledger.storage.entities.dao.Category;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collection;

public class CategoryCompositeUtilityTest {
    private CategoryCompositeUtility target;

    private Category cat1;
    private Category cat2;
    private Category cat3;
    private Category cat4;
    private Category cat5;

    private Collection<Category> categories;

    @BeforeMethod
    public void beforeMethod(){
        target = new CategoryCompositeUtility();
        cat1 = new Category() {{ setId("1"); setName("cat1");}};
        cat2 = new Category() {{ setId("2"); setName("cat2"); setParentId("1");}};
        cat3 = new Category() {{ setId("3"); setName("cat3"); setParentId("1");}};
        cat4 = new Category() {{ setId("4"); setName("cat4"); setParentId("2");}};
        cat5 = new Category() {{ setId("5"); setName("cat5"); setParentId("2");}};
        categories = new ArrayList<Category>() {{ add(cat1); add(cat2); add(cat3); add(cat4); add(cat5);}};
    }

    @Test
    public void getChildren(){
        Assert.assertEquals(target.getChildrenOf("1", categories).size(), 2);
    }

    @Test
    public void getDecendents(){
        Assert.assertEquals(target.getDecendentsOf("1", categories).size(), 4);
    }

    @Test
    public void getParent(){
        Assert.assertEquals(target.getParentOf("2", categories).getId(), "1");
    }

    @Test
    public void getAncestors(){
        Assert.assertEquals(target.getAncestorsOf("4", categories).size(), 2);
        Assert.assertEquals(target.getAncestorsOf("2", categories).size(), 1);
        Assert.assertEquals(target.getAncestorsOf("1", categories).size(), 0);
    }
}
