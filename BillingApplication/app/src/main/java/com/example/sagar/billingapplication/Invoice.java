package com.example.sagar.billingapplication;

/**
 * Created by Sagar on 2/3/2018.
 */

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class Invoice{

    LinkedHashMap<Product,Integer> ListOfProducts = new LinkedHashMap<Product,Integer>();
    private String CustomerName;
    private String Contact;
    private char type; //W-WholeSaler, R-Retailer
    private double Total;
    private String Remarks;
    private Date TimeStamp;
    private String Address;
    private double PaidAmount;
    private double NetAmount;
    private String WordTot;
    private double RoundOff;

    private static final String[] tensNames = {""," ten"," twenty"," thirty"," forty"," fifty"," sixty"," seventy"," eighty"," ninety"};

    private static final String[] numNames = {""," one"," two"," three"," four"," five"," six"," seven"," eight"," nine"," ten"," eleven"," twelve"," thirteen"," fourteen"," fifteen"," sixteen"," seventeen"," eighteen"," nineteen"};

    private static String convertLessThanOneThousand(int number) {

        String soFar;

        if (number % 100 < 20){
            soFar = numNames[number % 100];
            number /= 100;
        }
        else{
            soFar = numNames[number % 10];
            number /= 10;

            soFar = tensNames[number % 10] + soFar;
            number /= 10;
        }
        if (number == 0){
            return soFar;
        }
        return numNames[number] + " hundred" + soFar;
    }

    public static String convert(long number) {
        // 0 to 999 999 999 999
        if (number == 0) { return "zero"; }

        String snumber = Long.toString(number);

        // pad with "0"
        String mask = "000000000000";
        DecimalFormat df = new DecimalFormat(mask);
        snumber = df.format(number);

        // XXXnnnnnnnnn
        int billions = Integer.parseInt(snumber.substring(0,3));
        // nnnXXXnnnnnn
        int millions  = Integer.parseInt(snumber.substring(3,6));
        // nnnnnnXXXnnn
        int hundredThousands = Integer.parseInt(snumber.substring(6,9));
        // nnnnnnnnnXXX
        int thousands = Integer.parseInt(snumber.substring(9,12));

        String tradBillions;
        switch (billions) {
            case 0:
                tradBillions = "";
                break;
            case 1 :
                tradBillions = convertLessThanOneThousand(billions)
                        + " billion ";
                break;
            default :
                tradBillions = convertLessThanOneThousand(billions)
                        + " billion ";
        }
        String result =  tradBillions;

        String tradMillions;
        switch (millions) {
            case 0:
                tradMillions = "";
                break;
            case 1 :
                tradMillions = convertLessThanOneThousand(millions)
                        + " million ";
                break;
            default :
                tradMillions = convertLessThanOneThousand(millions)
                        + " million ";
        }
        result =  result + tradMillions;

        String tradHundredThousands;
        switch (hundredThousands) {
            case 0:
                tradHundredThousands = "";
                break;
            case 1 :
                tradHundredThousands = "one thousand ";
                break;
            default :
                tradHundredThousands = convertLessThanOneThousand(hundredThousands)
                        + " thousand ";
        }
        result =  result + tradHundredThousands;

        String tradThousand;
        tradThousand = convertLessThanOneThousand(thousands);
        result =  result + tradThousand;

        // remove extra spaces!
        return result.replaceAll("^\\s+", "").replaceAll("\\b\\s{2,}\\b", " ");
    }

    public String getWordTotal(){
        int rupees = (int) Math.floor(this.NetAmount);
        int paise = (int)Math.floor((this.NetAmount - rupees) * 100.0f);
        if(rupees > 1){
            this.WordTot = convert(rupees) + " rupees and " + convert(paise) + " paisa";
        }
        else{
            this.WordTot = convert(rupees) + " rupee and " + convert(paise) + " paisa";
        }
        return WordTot;
    }

    public double getPaidAmount(){
        return PaidAmount;
    }

    public void DeleteProduct(Product key){
        int no = ListOfProducts.get(key);
        if(this.getType() == 'W'){
            Total -= no*key.getWholesaleRate();
        }
        else{
            Total -= no*key.getRetailRate();
        }
        ListOfProducts.remove(key);
    }

    public double getNetAmount(){
        return NetAmount;
    }

    public void setNetAmount(double amt){
        int am = (int) amt;
        if(amt - am >= 0.5d){
            this.NetAmount = am + 1;
            RoundOff = 1 - (amt - am);
        }
        else{
            this.NetAmount = am;
            RoundOff = -1 * (amt - am);
        }
    }

    public double getRoundOff(){
        return this.RoundOff;
    }

    public void setPaidAmount(double Amount){
        this.PaidAmount = Amount;
    }

    public String getCustomerName(){
        return CustomerName;
    }

    public String getAddress(){
        return Address;
    }

    public String getContact(){
        return Contact;
    }

    public LinkedHashMap<Product,Integer> getListOfProducts(){
        return ListOfProducts;
    }

    public char getType(){
        return type;
    }

    public double getTotal(){
        return Total;
    }

    @SuppressWarnings("deprecation")
    public String getDate(){
        return (Integer.toString(TimeStamp.getDate()) + "/" + Integer.toString(TimeStamp.getMonth() + 1) + "/" + Integer.toString(TimeStamp.getYear() + 1900) );
    }

    public String getTime(){
        DateFormat timeInstance = SimpleDateFormat.getTimeInstance();
        return(timeInstance.format(TimeStamp));
//		if(TimeStamp.getTime() > 12){
//			return(Integer.toString(TimeStamp.getHours() - 12) + ":" + Integer.toString(TimeStamp.getMinutes()) + " PM");
//		}
//		else if(TimeStamp.getHours() == 12){
//			return(Integer.toString(TimeStamp.getHours()) + ":" + Integer.toString(TimeStamp.getMinutes()) + " PM");
//		}
//		else{
//			return(Integer.toString(TimeStamp.getHours()) + ":" + Integer.toString(TimeStamp.getMinutes()) + " AM");
//		}
    }

    public String getRemarks(){
        return Remarks;
    }

    public Date getTimeStamp(){
        return TimeStamp;
    }

    public void setTime(Date dt){
        TimeStamp = dt;
    }

    public void setRemarks(String st){
        this.Remarks = st;
    }

    public Invoice(String cName,String Contact,String Add,char type){
        this.type = type;
        CustomerName = cName;
        this.Contact = Contact;
        this.Address = Add;
        Total = 0.0;
        TimeStamp = new Date();
    }

    public int getNoProducts(){
        return this.ListOfProducts.size();
    }

    public void removeProduct(Product p){
        ListOfProducts.remove(p);
    }

    public void addProduct(Product p, int Quantity){

        if(ListOfProducts.containsKey(p)){
            int q = ListOfProducts.get(p);
            ListOfProducts.remove(p);
            ListOfProducts.put(p,q + Quantity);
        }
        else{
            ListOfProducts.put(p, Quantity);
        }
        if(type == 'W'){
            Total += p.getWholesaleRate()*Quantity;
        }
        else if(type == 'R'){
            Total += p.getRetailRate()*Quantity;
        }
    }

    public String getProducts(){
        String obj = "";

        for (Map.Entry<Product,Integer> entry : ListOfProducts.entrySet())
        {
            Product p = entry.getKey();
            int no = entry.getValue();
            if(type == 'W'){
                obj += no + "                    				" + p.getName() + "              						" + p.getWholesaleRate();
            }
            else if(type == 'R'){
                obj += no + "                    				" + p.getName() + "              						" + p.getRetailRate();
            }
        }

        return obj;
    }

    public String toString(){
        String obj = "Customer Name";
        obj += "                                   ";
        obj += "Contact\n";
        obj += CustomerName;
        obj += "                                   ";
        obj += Contact;
        obj += "\n\n\n\n";
        obj += "Quantity                    				Product              						Rate\n";
        obj += this.getProducts();

        obj +="\n\n";
        obj += Remarks;

        return(obj);
    }



}