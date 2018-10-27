/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionhotelclient;

import Enum.EmployeeTypeEnum;
import java.util.Scanner;
import stateless.MainControllerBeanRemote;

/**
 *
 * @author samue
 */
public class MainApp {

    private MainControllerBeanRemote mainControllerBeanRemote;
    private Scanner sc = new Scanner(System.in);

    public void run(MainControllerBeanRemote mainControllerBeanRemote) {
        this.mainControllerBeanRemote = mainControllerBeanRemote;
        while (true) {
            System.out.println("Welcome you!\n1. Login\n2. Exit");
            int c = sc.nextInt();
            if (c == 1) {
                System.out.println("Please enter your username:");
                String username = sc.next();
                System.out.println("Your password: ");
                String password = sc.next();
                if (mainControllerBeanRemote.doLogin(username, password)) {
                    System.out.println("What is goingo n?");
                    loggedin(username);
                } else {
                    System.out.println("Wrong user name or password");
                    continue;
                }
            } else {
                break;
            }
        }
    }

    public void loggedin(String username) {
        System.out.println("I have logged in");
        EmployeeTypeEnum eType = mainControllerBeanRemote.getEmployeeTypeEnum(username);
        if (eType == EmployeeTypeEnum.SystemAdministrator) {
            while (true) {
                System.out.println("1. Create New Employee\n2. View All Employees\n3. Create Partner\n4. View All Partners\n5. Exit");
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
                    break;
                } else {
                    continue;
                }
            }
        }
    }
}
