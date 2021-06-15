package cn.gjyniubi.cinema.common.util;

import cn.gjyniubi.cinema.common.entry.Customer;

/**
 * @Author gujianyang
 * @Date 2021/5/24
 * @Class CustomerUtil
 */
public class CustomerUtil {

    private static final ThreadLocal<Customer> CUSTOMER_THREAD_LOCAL=new ThreadLocal<>();

    public static Customer getCurrentCustomer(){
        return CUSTOMER_THREAD_LOCAL.get();
    }

    public static void setCurrentCustomer(Customer customer){
        CUSTOMER_THREAD_LOCAL.set(customer);
    }

    public static void removeCurrentCustomer(){
        CUSTOMER_THREAD_LOCAL.remove();
    }
}
