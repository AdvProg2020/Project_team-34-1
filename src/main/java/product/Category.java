package product;

import exceptionalMassage.ExceptionalMassage;

import java.util.ArrayList;

/**
 * @author Aryan Ahadinia
 * @since 0.0.1
 */

public class Category {
    public static final ArrayList<Category> allCategories = new ArrayList<>();
    public static final Category superCategory = new Category();

    private String name;
    private final String parentCategoryName;
    private final ArrayList<String> allCategoriesInName;
    private final ArrayList<Product> allProductsIn;

    //Constructors:
    private Category() {
        this.name = "All Products";
        this.parentCategoryName = null;
        this.allProductsIn = null;
        this.allCategoriesInName = new ArrayList<>();
        allCategories.add(this);
        //DataBase Banned
    }

    private Category(String name, boolean isParentCategory, String parentCategoryName) {
        this.name = name;
        if (parentCategoryName == null) {
            parentCategoryName = superCategory.getName();
        }
        this.parentCategoryName = parentCategoryName;
        if (isParentCategory) {
            this.allCategoriesInName = new ArrayList<>();
            this.allProductsIn = null;
        } else {
            this.allProductsIn = new ArrayList<>();
            this.allCategoriesInName = null;
        }
        allCategories.add(this);
        //file modifications required
    }

    public static Category getInstance(String name, boolean isParentCategory, String parentCategoryName) throws ExceptionalMassage {
        if (getCategoryByName(name) != null)
            throw new ExceptionalMassage("Category with name " + name + " has already created.");
        Category parentCategory = getCategoryByName(parentCategoryName);
        if (parentCategory == null)
            throw new ExceptionalMassage("Parent category not found.");
        if (!parentCategory.isCategoryClassifier())
            throw new ExceptionalMassage("Category " + parentCategoryName + " is a product classifier.");
        Category addingCategory = new Category(name, isParentCategory, parentCategoryName);
        parentCategory.addSubCategory(addingCategory);
        return addingCategory;
    }

    //Getters:
    public String getName() {
        return name;
    }

    public Category getParentCategory() {
        return getCategoryByName(parentCategoryName);
    }

    public ArrayList<Product> getAllProductsIn() {
        return allProductsIn;
    }

    public ArrayList<Category> getAllCategoriesIn() {
        ArrayList<Category> allCategoriesIn = new ArrayList<>();
        for (String name : allCategoriesInName) {
            allCategoriesIn.add(getCategoryByName(name));
        }
        return allCategoriesIn;
    }

    //Setters:
    public void setName(String name) throws ExceptionalMassage {
        if (getCategoryByName(name) != null) {
            throw new ExceptionalMassage("Category with this name has already initialized. <Category.setName>");
        }
        this.name = name;
        //file modification required
    }

    //Modeling methods:
    public boolean isCategoryClassifier() {
        return this.allProductsIn == null;
    }

    public boolean isProductIn(Product product) {
        return this.allProductsIn.contains(product);
    }

    public void addProduct(Product product) throws ExceptionalMassage {
        if (this.isCategoryClassifier())
            throw new ExceptionalMassage("Cannot Add Product to this category. <Category.addProduct>");
        if (this.isProductIn(product))
            throw new ExceptionalMassage("This product has already added to this category. <Category.addProduct>");
        if (Category.getProductCategory(product) != null)
            throw new ExceptionalMassage("This product has already added to another category. <Category.addProduct>");
        this.allProductsIn.add(product);
        //file modification required
    }

    public void removeProduct(Product product) throws ExceptionalMassage {
        if (this.isCategoryClassifier())
            throw new ExceptionalMassage("This is not a product category. <Category.removeProduct>");
        if (!this.isProductIn(product))
            throw new ExceptionalMassage("This product is not in this category. <Category.removeProduct>");
        this.allProductsIn.remove(product);
        //file modification required
    }

    public void addSubCategory(Category addingCategory) throws ExceptionalMassage {
        if (!this.isCategoryClassifier())
            throw new ExceptionalMassage("Cannot add a subcategory to this category. <Category.addSubCategory>");
        this.allCategoriesInName.add(addingCategory.getName());
        //file modifications required
    }

    public void addSubCategory(String addingCategoryName, boolean isParentCategory) throws ExceptionalMassage {
        if (!this.isCategoryClassifier())
            throw new ExceptionalMassage("Cannot add a subcategory to this category. <Category.addSubCategory>");
        Category.getInstance(addingCategoryName, isParentCategory, this.getName());
        //file modifications required
    }

    public void removeSubCategory(String removingCategoryName) throws ExceptionalMassage {
        if (!this.isCategoryClassifier())
            throw new ExceptionalMassage("This category is a product classifier. <Category.removeSubCategory>");
        if (!this.allCategoriesInName.contains(removingCategoryName))
            throw new ExceptionalMassage("This subcategory is not in this category. <Category.removeSubCategory>");
        //check if category is empty if required
        this.allCategoriesInName.remove(removingCategoryName);
        //file modification required
    }

    public ArrayList<Category> getReference() {
        ArrayList<Category> previousReference;
        if (this == superCategory) {
            previousReference = new ArrayList<>();
            previousReference.add(superCategory);
            return previousReference;
        }
        previousReference = this.getParentCategory().getReference();
        previousReference.add(this);
        return previousReference;
        //completed
    }

    public ArrayList<Product> getAllProductInAllSubCategories() {
        ArrayList<Product> allProductsInAll = new ArrayList<>();
        if (!this.isCategoryClassifier()) {
            allProductsInAll.addAll(this.allProductsIn);
        } else {
            for (Category category : this.getAllCategoriesIn()) {
                allProductsInAll.addAll(category.getAllProductInAllSubCategories());
            }
        }
        return allProductsInAll;
    }

    public boolean isProductInSubCategories(Product product) {
        return getAllProductInAllSubCategories().contains(product);
    }

    public static Category getCategoryByName(String searchingName) {
        if (allCategories.size() != 0) {
            for (Category category : allCategories) {
                if (category.getName().equals(searchingName)) {
                    return category;
                }
            }
        }
        return null;
    }

    public static Category getProductCategory(Product product) {
        if (allCategories.size() != 0) {
            for (Category category : allCategories) {
                if (category.isProductIn(product)) {
                    return category;
                }
            }
        }
        return null;
    }
}
