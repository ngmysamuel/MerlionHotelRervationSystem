/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionhotelclient;

import Enum.EmployeeTypeEnum;
import com.sun.xml.ws.rx.rm.policy.wsrm200502.Rm10Assertion;
import entity.ExceptionReport;
import entity.RoomType;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import stateless.MainControllerBeanRemote;
import util.exception.StillInUseException;

/**
 *
 * @author samue
 */
public class MainApp {

    private MainControllerBeanRemote mainControllerBeanRemote;
    private Scanner sc = new Scanner(System.in);

    public void run(MainControllerBeanRemote mainControllerBean) {
        this.mainControllerBeanRemote = mainControllerBean;
        System.out.println("\n\n" + mainControllerBeanRemote + "\n\n");
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
        while (true) {
            System.out.println("Please select what you want to do.\n1. Create Room Type\n2. Update Room Type\n3. View Room Type Details\n4. Delete Room Type\n5. View All Room Types\n6. Create New Room\n7. Update Room\n8. Delete Room\n9. View All Rooms\n 10. View Allocation Report\n11. Exit");
            int selection = sc.nextInt();
            switch (selection) {
                case 1:
                    System.out.println("What is the name?");
                    String name = sc.next();
                    System.out.println("What are the amenities?");
                    String amenities = sc.next();
                    System.out.println("What is the bed size?");
                    String bed = sc.next();
                    sc.nextLine();
                    System.out.println("What are the description?");
                    String description = sc.nextLine();
                    System.out.println("What is the capacity?");
                    Integer capacity = sc.nextInt();
                    
                    List<RoomType> listOfRoomTypes = mainControllerBeanRemote.sortRoomTypeAsc();
                    int highest = listOfRoomTypes.get(listOfRoomTypes.size()-1).getGrade();
                    ++highest;
                    
                    System.out.println("What are the grade? If there is a clash of grades, the grades lower on the ladder will be pushed down automatically");
                    System.out.println("Please select a number from 1 to "+highest);
                    Integer grade = sc.nextInt();
                    if ((grade < 1) || (grade > highest)) {
                    System.out.println("You have made a wrong choice.");
                    break;
                    }
                    
                    System.out.println("What are the room size?");
                    Integer roomSize = sc.nextInt();
                    mainControllerBeanRemote.createRoomType(bed, name, amenities, capacity, description, grade, roomSize);
                    System.out.println("Please remeber to create and set the number of rooms.");
                    break;
                case 2:
                    System.out.println("What is the room type you want to update for?");
                    int count = 0;
                    List<RoomType> ls = mainControllerBeanRemote.sortRoomTypeAsc();
                    for (RoomType rt : ls) {
                        System.out.println(count+". " + rt.getName());
                        count++;
                    }
                    int i = sc.nextInt();
                    sc.nextLine();
                    Long roomTypeId = ls.get(i).getId();
                    updateRoomType(roomTypeId);
                    break;
                case 3:
                    System.out.println("What is the name?");
                    String nameCase3 = sc.next();
                    RoomType rtCase3 = mainControllerBeanRemote.viewSpecificRoomType(nameCase3);
                    System.out.println(rtCase3.getDescription());
                    break;
                case 4:
                    System.out.println("What is the room type you want to delete for?");
                    int cCase4 = 0;
                    List<RoomType> lsCase4 = mainControllerBeanRemote.sortRoomTypeAsc();
                    for (RoomType rtCase4 : lsCase4) {
                        System.out.println(cCase4 +". " + rtCase4.getName());
                        cCase4++;
                    }
                    int iCase4 = sc.nextInt();
                    Long roomTypeIdCase4 = lsCase4.get(iCase4).getId();
            {
                try {
                    mainControllerBeanRemote.deleteRoomType(roomTypeIdCase4);
                } catch (StillInUseException ex) {
                    System.out.println("Warning! NOT DELETED. Because there are still rooms using the room type, the status is set to disabled instead.");
                }
            }
                    break;
                case 5:
                    List<RoomType> lsCase5 = mainControllerBeanRemote.sortRoomTypeAsc();
                    for (RoomType rtCase5 : lsCase5) {
                        System.out.println("The roomtype with name: "+rtCase5.getName());
                        System.out.println(rtCase5.getAmenities()+"\n"+rtCase5.getGrade());
                    }
                    break;
                case 6:
                    System.out.println("What is the room type you want to create for?");
                    int c = 0;
                    List<RoomType> ls2 = mainControllerBeanRemote.sortRoomTypeAsc();
                    for (RoomType rt : ls2) {
                        System.out.println(c +". " + rt.getName());
                        c++;
                    }
                    int i2 = sc.nextInt();
                    Long roomTypeId2 = ls2.get(i2).getId();
                    
                    if (!ls2.get(i2).getIsEnabled()) {
                        System.out.println("Warning! You cannot create for this room type because it is disabled.");
                        break;
                    }
                    
                    System.out.println("What is the floor of the rooms?");
                    String rmNum1 = sc.next();
                    System.out.println("What number do you want to start with?");
                    String rmNum2 = sc.next();
                    String rmNum = rmNum1 + rmNum2;
                    Integer rmNumber = Integer.valueOf(rmNum);
                    System.out.println("What is the status?\n1. Avail\n2. Not avail");
                    String status;
                    int k = sc.nextInt();
                    if (k == 1) {
                        status = "Available";
                    } else {
                        status = "Not Available";
                    }
                    System.out.println("How many of this rooms do you want to create?");
                    int num = sc.nextInt();
                    for (int j = 0; j < num; j++) {
                        mainControllerBeanRemote.createRoom(rmNumber, status, roomTypeId2);
                        ++rmNumber;
                    }
                    updateRoomType(num, roomTypeId2);
                    break;
                case 7:
                    System.out.println("Please enter the room number you want to update");
                    Long roomNumCase7 = sc.nextLong();
                    System.out.println("Please enter the room status.\n1. Available\n2.Unavailable");
                    int choiceCase7 = sc.nextInt();
                    String statusCase7;
                    if (choiceCase7 == 1) {
                        statusCase7 = "Available";
                    } else {
                        statusCase7 = "Unavailable";
                    }
                    System.out.println("What is the room type you want to update to?");
                    int cCase7 = 0;
                    List<RoomType> ls2Case7 = mainControllerBeanRemote.viewAllRoomTypes();
                    for (RoomType rt : ls2Case7) {
                        System.out.println(cCase7 +". " + rt.getName());
                        cCase7++;
                    }
                    System.out.println(cCase7+". Skip.");
                    int i2Case7 = sc.nextInt();
                    Long roomTypeIdCase7 = new Long("-1");
                    if (i2Case7 >= ls2Case7.size()) {
                        
                    } else {
                        roomTypeIdCase7 = ls2Case7.get(i2Case7).getId();
                    }
                    mainControllerBeanRemote.updateRoom(roomNumCase7, statusCase7, roomTypeIdCase7);
                    break;
                case 8:
                    System.out.println("Enter the number of the room you want to delete");
                    Long roomNumCase8 = sc.nextLong();
                    try {
                        mainControllerBeanRemote.deleteRoom(roomNumCase8);
                    } catch (StillInUseException ex) {
                        System.out.println("Warning! NOT DELETED. Because there are still rooms using the room type, the status is set to disabled instead.");
                    }
                    break;
                case 9:
                    System.out.println(mainControllerBeanRemote.viewRooms());
                    break;
                case 10:
                    viewExceptionReport();
                    break;
                case 11:
                    return;
                default:
                    continue;
            }
        }
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

    private void updateRoomType(Long roomTypeId) {
        System.out.println("What is the initial room avail? Leave blank if no update. ");
        String avail = sc.nextLine();
        int availNum;
        if (avail.length() != 0) {
            availNum = Integer.valueOf(avail);
        } else {
            availNum = -1;
        }
        System.out.println("Leave blank for any field that do not need to change.");
        System.out.println("What is the name?");
        String name = sc.nextLine();
        System.out.println("What are the amenities?");
        String amenities = sc.nextLine();
        System.out.println("What is the bed size?");
        String bed = sc.nextLine();
        System.out.println("What are the description?");
        String description = sc.nextLine();
        System.out.println("What is the capacity?");
        String capacity = sc.nextLine();
        
        List<RoomType> listOfRoomTypes = mainControllerBeanRemote.sortRoomTypeAsc();
        int highest = listOfRoomTypes.get(listOfRoomTypes.size()-1).getGrade();
        
        System.out.println("What are the grade? If there is a clash of grades, the grades lower on the ladder will be pushed down automatically");
        System.out.println("Please enter a number from 1 to "+highest);
        String grade = sc.nextLine();
        if (grade.length() != 0) {
            int gra = Integer.valueOf(grade);
            if ((gra < 1) || (gra > highest)) {
                System.out.println("You have made a wrong choice.");
                return;
            }
        }
        System.out.println("What are the room size?");
        String roomSize = sc.nextLine();
        System.out.println("Do you want it 1.Disabled 2.Enabled 3.Skip");
        int selection = sc.nextInt();
        String b = "";
        if(selection == 1) {
            b = "false";
        } else if (selection == 2) {
            b = "true";
        } else {
            b = "";
        }
        mainControllerBeanRemote.updateRoomType(bed, name, amenities, capacity, description, grade, roomSize, availNum, roomTypeId, b);
System.out.println("I am back in client");
    }

    private void updateRoomType(int num, Long id) {
        mainControllerBeanRemote.updateRomType(num, id);
    }
}
