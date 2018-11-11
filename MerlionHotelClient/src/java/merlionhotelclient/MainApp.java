/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionhotelclient;

import Enum.EmployeeTypeEnum;
import entity.ExceptionReport;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import stateless.MainControllerBeanRemote;

/**
 *
 * @author samue
 */
public class MainApp {

    private MainControllerBeanRemote mainControllerBeanRemote;
    private Scanner sc = new Scanner(System.in);

    public void run(MainControllerBeanRemote mainControllerBean) {
        this.mainControllerBeanRemote = mainControllerBean;
        System.out.println("\n\n"+mainControllerBeanRemote+"\n\n");
        while (true) {
            System.out.println("Welcome you!\n1. Login\n2. Exit");
            int c = sc.nextInt();
            if (c == 1) {
                System.out.println("Please enter your username:");
                String username = sc.next();
                System.out.println("Your password: ");
                String password = sc.next();
                if (mainControllerBeanRemote.doLogin(username, password)) {
                    EmployeeTypeEnum e = mainControllerBeanRemote.getEmployeeTypeEnum(username);
                    if (e.equals(EmployeeTypeEnum.SystemAdministrator)) {
                        loggedInSysAdmin(username); 
                    } else if (e.equals(EmployeeTypeEnum.GuestRelationOfficer)) {
                        loggedInGuestRelations(username);
                    } else if (e.equals(EmployeeTypeEnum.OperationManager)) {
                        loggedInOperationManager(username);
                    } else if (e.equals(EmployeeTypeEnum.SalesManager)) {
                        loggedInSalesManager(username);
                    }
                } else {
                    System.out.println("Wrong user name or password");
                    continue;
                }
            } else {
                break;
            }
        }
    }
    
    public void loggedInOperationManager(String username) {
        
    }
    
    public void loggedInGuestRelations(String username) {
        
    }
    
    public void loggedInSalesManager(String username) {
        
    }
    
    public void loggedInSysAdmin(String username) {
        System.out.println("I have logged in");
        EmployeeTypeEnum eType = mainControllerBeanRemote.getEmployeeTypeEnum(username);
        if (eType == EmployeeTypeEnum.SystemAdministrator) {
            while (true) {
                System.out.println("1. Create New Employee\n2. View All Employees\n3. Create Partner\n4. View All Partners\n5. timer\n6. Exit\n7. View Exception Report\n8. Persist a er");
                int choice = sc.nextInt();
                if (choice == 1) {
                    System.out.println("What is the employee type?\n0. GuestRelations\n1. Operations\n2. Sales");
                    EmployeeTypeEnum employeeTypeEnum = EmployeeTypeEnum.values()[sc.nextInt()];
                    System.out.println("What is the username?");
                    String newUsername = sc.next();
                    System.out.println("What is the password?");
                    String password = sc.next();
                    mainControllerBeanRemote.createEmployee(newUsername, password, employeeTypeEnum);
                } else if (choice == 2) {
                    System.out.println(mainControllerBeanRemote.viewEmployees());
                } else if (choice == 3) {
                    System.out.println("What is the employee's password?");
                    String emp = sc.next();
                    System.out.println("What is the manager's password?");
                    String manager = sc.next();
                    System.out.println("What is the username?");
                    username = sc.next();
                    mainControllerBeanRemote.createPartner(emp, manager, username);
                } else if (choice == 4) {
                    System.out.println(mainControllerBeanRemote.viewPartners());
                } else if (choice == 5) {
                    mainControllerBeanRemote.timer();
                } else if (choice == 6) {
                    break;
                } else if (choice == 7) {
                    viewExceptionReport();
                } else if (choice == 8) {
                    mainControllerBeanRemote.persistEr();
                } else {
                    continue;
                }
            }
        }
    }
    
    private void viewExceptionReport() {
        ExceptionReport er = mainControllerBeanRemote.viewExceptionReport(LocalDate.now());
        List<String> ls = er.getExceptions();
        if (ls.isEmpty()) {
            System.out.println("There is no excpetions");
        } else {
            for (String s : ls) {
                System.out.println(s);
            }
        }
    }
}
