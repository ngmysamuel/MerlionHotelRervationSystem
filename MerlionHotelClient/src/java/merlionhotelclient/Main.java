/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionhotelclient;

import java.util.Scanner;
import javax.ejb.EJB;
import stateless.EmployeeControllerBeanRemote;

/**
 *
 * @author samue
 */
public class Main {

    @EJB
    private static EmployeeControllerBeanRemote employeeControllerBean;

    public static void main(String[] args) {
        System.out.println(employeeControllerBean.create());
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter id:");
        Integer id = sc.nextInt();
        Long id2 = id.longValue();
        System.out.println(employeeControllerBean.getIdAndAddOne(id2));
    }
    
}
