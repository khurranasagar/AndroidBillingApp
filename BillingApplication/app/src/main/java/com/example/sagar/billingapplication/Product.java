package com.example.sagar.billingapplication;

/**
 * Created by Sagar on 2/3/2018.
 */

public class Product implements Comparable<Product> {

    private String Product_Name;
    private double Wholesale_Rate;
    private double Retail_Rate;
    private double MRP;


    public String getName(){
        return Product_Name;
    }

    public double getWholesaleRate(){
        return Wholesale_Rate;
    }

    public double getRetailRate(){
        return Retail_Rate;
    }

    public double getMRP(){
        return MRP;
    }

    public Product(String Name,double wPrice, double rPrice, double MRP){
        this.Product_Name = Name;
        this.Wholesale_Rate = wPrice;
        this.Retail_Rate = rPrice;
        this.MRP = MRP;
    }

    public void setName(String name){
        this.Product_Name = name;
    }

    public void setWholesaleRate(double wr){
        this.Wholesale_Rate = wr;
    }

    public void setRetailRate(double rr){
        this.Retail_Rate = rr;
    }

    public void setMRP(double mrp){
        MRP = mrp;
    }

    public String toString(){

        String obj = "Product Name : " + Product_Name;
        obj += "\nMRP : " + MRP;
        obj += "\nWholesale Price : " + Wholesale_Rate;
        obj += "\nRetail Price : " + Retail_Rate;
        return obj;

    }

    public String writeToFile(){

        String obj = Product_Name + "  |  " + Wholesale_Rate + "  |  " + Retail_Rate + "  |  " + MRP;
        return obj;

    }

    public String getCSV(){
        return(this.Product_Name + "," +  Double.toString(this.getMRP()) + "," + Double.toString(this.getWholesaleRate()) + "," + Double.toString(this.getRetailRate()) + "\n");
    }

    @Override
    public int compareTo(Product comp) {

        return(this.getName().toLowerCase().compareTo(comp.getName().toLowerCase()));

    }

}