/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package merlionhotelclient;

import Enum.EmployeeTypeEnum;
import Enum.RateTypeEnum;
import entity.Employee;
import entity.ExceptionReport;
import entity.Guest;
import entity.Partner;
import entity.Rate;
import entity.ReservationLineItem;
import entity.RoomType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.persistence.NoResultException;
import stateless.MainControllerBeanRemote;
import stateless.PartnerControllerBeanRemote;
import util.exception.GuestNotFoundException;
import util.exception.RateNameNotUniqueException;
import util.exception.RateNotFoundException;
import util.exception.ReservationNotFoundException;
import util.exception.RoomNotAllocatedException;
import util.exception.RoomTypeNotFoundException;
import util.exception.StillInUseException;

/**
 *
 * @author samue
 */
public class MainApp {
    
    private MainControllerBeanRemote mainControllerBeanRemote;

    private PartnerControllerBeanRemote partnerControllerBeanRemote;

    private Scanner sc = new Scanner(System.in);

    public void run(MainControllerBeanRemote mainControllerBean, PartnerControllerBeanRemote partnerControllerBeanRemote) {
        this.mainControllerBeanRemote = mainControllerBean;
        this.partnerControllerBeanRemote = partnerControllerBeanRemote;
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
            sc.nextLine();
            switch (selection) {
                case 1:
                    System.out.println("What is the name?");
                    String name = sc.nextLine();
                    System.out.println("What are the amenities?");
                    String amenities = sc.nextLine();
                    System.out.println("What is the bed size?");
                    String bed = sc.nextLine();
                    System.out.println("What are the description?");
                    String description = sc.nextLine();
                    System.out.println("What is the capacity?");
                    Integer capacity = sc.nextInt();
                    sc.nextLine();
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
                    sc.nextLine();
                    String nameCase3 = sc.nextLine().trim();
                    RoomType rtCase3 = new RoomType();
                    rtCase3 = mainControllerBeanRemote.viewSpecificRoomType(nameCase3);
                    if (rtCase3 == null) {
                        System.out.println("Please try again. RoomType cannot be found. ");
                        break;
                    } 
                    String s = String.format("The roomType is called: %-20S and has InitialRoomAvailability of: %-5d and can hold a maximum of %5d adults. It can be described as %-25s", rtCase3.getName(), rtCase3.getInitialRoomAvailability(), rtCase3.getCapacity(), rtCase3.getDescription());
                    System.out.println(s);
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
                    System.out.println("We have the following Room Types: ");
                    for (RoomType rtCase5 : lsCase5) {
                        String s5 = String.format("%1$-25s%2$-5d%3$-5d", rtCase5.getName(), rtCase5.getGrade(), rtCase5.getId());
                        System.out.println(s5);
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
        while(true){
            System.out.println("***You are logged in as a Guest Relations Officer***");
            System.out.println("Please select what you want to do");
            System.out.println("1: Check-in");
            System.out.println("2: Check-out");
            System.out.println("3: Search Rooms");
            System.out.println("4: Reserve Rooms");
            System.out.println("5: Logout");
            int i = sc.nextInt();
            sc.nextLine();
            switch(i){
                case 1:
                    checkIn();
                    break;
                case 2:
                    checkOut();
                    break;
                case 3:
                    searchRooms();
                    break;
                case 4:
                    reserveRooms();
                    break;
                case 5:
                    return;
            }
        }
    }

    public void loggedInSalesManager(String username) {
        while(true){
            System.out.println("***You are logged in as a Sales Manager***");
            System.out.println("Please select what you want to do");
            System.out.println("1: Create new room rate");
            System.out.println("2: View all room rates");
            System.out.println("3: View a particular room rate");
            System.out.println("4: Update a room rate");
            System.out.println("5: Delete room rate");
            System.out.println("6: Logout");
            int i = sc.nextInt();
            sc.nextLine();
            switch(i){
                case 1:
                    createRate();
                    break;
                case 2:
                    viewAllRates();
                    System.out.println("Enter any key to continue");
                    sc.nextLine();
                    break;
                case 3:
            {
                try {
                    viewRate();
                    System.out.println("Enter any key to continue");
                    sc.nextLine();
                } catch (RateNotFoundException ex) {
                    System.out.println(ex.getMessage());
                }
            }
                    break;
                case 4:
                    updateRate();
                    break;
                case 5:
                    deleteRate();
                    break;
                case 6:
                    return;
            }
        }      
    }

    public void loggedInSysAdmin(String username) {
        System.out.println("I have logged in");
        EmployeeTypeEnum eType = mainControllerBeanRemote.getEmployeeTypeEnum(username);
        if (eType == EmployeeTypeEnum.SystemAdministrator) {
            while (true) {
                System.out.println("1. Create New Employee\n2. View All Employees\n3. Create Partner\n4. View All Partners\n5. timer\n6. Exit\n7. View Exception Report\n8. Persist a er");
                int choice = sc.nextInt();
                if (choice == 1) {
                    System.out.println("What is the employee type?\n0. GuestRelations\n1. Operations\n2. Sales\n3. System Admin");
                    EmployeeTypeEnum employeeTypeEnum = EmployeeTypeEnum.values()[sc.nextInt()];
                    System.out.println("What is the username?");
                    String newUsername = sc.next();
                    System.out.println("What is the password?");
                    String password = sc.next();
                    mainControllerBeanRemote.createEmployee(newUsername, password, employeeTypeEnum);
                } else if (choice == 2) {
                    List<Employee> ls = mainControllerBeanRemote.viewEmployees(); 
                    for (Employee e : ls) {
                        String s = String.format("Employee username: %-15S and their ID is: %-7d and their type is: %-20S", e.getUsername(), e.getId(), e.getEmployeeType());
                        System.out.println(s);
                    }
                } else if (choice == 3) {
                    System.out.println("What is the password?");
                    String emp = sc.next();
                    String manager = "";
                    System.out.println("What is the username?");
                    username = sc.next();
                    mainControllerBeanRemote.createPartner(emp, manager, username);
                } else if (choice == 4) {
                    List<Partner> ls = mainControllerBeanRemote.viewPartners();
                    for (Partner p : ls) {
                        String s = String.format("Partner username: %-15s and their ID is: %-7d", p.getUsername(), p.getId());
                        System.out.println(s);
                    }
                    
                    
                } else if (choice == 5) {
System.out.println("I am about to call timer()");
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
    
    private void createRate(){
        System.out.print("Room Type>");
        String roomType = sc.nextLine();
        System.out.print("Rate Name>");
        String name = sc.nextLine();
        System.out.print("Rate Type (1.Published, 2.Normal, 3.Peak, 4.Promo)>");
        int rt = sc.nextInt();
        RateTypeEnum rateType = RateTypeEnum.values()[rt-1];
        System.out.print("Price>");
        BigDecimal price = sc.nextBigDecimal();
        if(rateType.toString().equals("Promotion") || rateType.toString().equals("Peak")){
            System.out.println("Start of validity (YYYY-MM-DD)>");
            String start = sc.next();
            System.out.println("End of validity (YYYY-MM-DD)>");
            String end = sc.next();
            LocalDate dateStart = LocalDate.parse(start);
            LocalDate dateEnd = LocalDate.parse(end);
            try {
                mainControllerBeanRemote.createRate(roomType, name, rateType, price, dateStart, dateEnd);
                System.out.println("New room rate created");
            } catch (RoomTypeNotFoundException ex) {
                System.out.println(ex.getMessage());
            }
        } else {
            try {
                mainControllerBeanRemote.createRate(roomType, name, rateType, price);
                System.out.println("New room rate created");
            } catch (RoomTypeNotFoundException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    
    private void viewAllRates(){
        try {
            List<Rate> rates = mainControllerBeanRemote.viewAllRates();
            System.out.printf("%24s %32s %10s %5S %10S %10S \n", "ROOM TYPE", "NAME", "TYPE", "PRICE", "DATE START", "DATE END");
            for(Rate rate: rates){
                if(rate.getType().toString().equals("Promotion") ||rate.getType().toString().equals("Peak")){
                    System.out.printf("%24s %32s %10s %5s %10s %10s \n", rate.getRoomType().getName(), rate.getName(),
                            rate.getType().toString(), rate.getPrice().toString(), rate.getDateStart().toString(), rate.getDateEnd().toString());
                } else{
                    System.out.printf("%24s %32s %10s %5s \n", rate.getRoomType().getName(), rate.getName(),
                            rate.getType().toString(), rate.getPrice().toString());
                }
            }
        } catch (RateNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    private Long viewRate() throws RateNotFoundException{
        System.out.println("Room Rate Name>");
        String name = sc.nextLine();
            Rate rate = mainControllerBeanRemote.viewRate(name);
            System.out.println("Name: "+rate.getName());
            System.out.println("Type: "+rate.getType());
            System.out.println("Room Type: "+rate.getRoomType().getName());
            System.out.println("Price: " + rate.getPrice());
            if(rate.getType().toString().equals("Promotion") || rate.getType().toString().equals("Peak")){
                System.out.println("Start of validity: "+rate.getDateStart());
                System.out.println("End of validity: "+rate.getDateEnd());
            }
        return rate.getId();
    }
    
    private void updateRate(){
        System.out.println("***Update Rate***");
        try {
            Long rateId = viewRate();
            System.out.println("***Enter Updated Rate***");
            System.out.print("Name>");
            String name = sc.nextLine();
            System.out.print("Room Type>");
            String roomType = sc.nextLine();
            System.out.print("Rate Type (1.Published, 2.Normal, 3.Peak, 4.Promo)>");
            int rt = sc.nextInt();
            RateTypeEnum rateType = RateTypeEnum.values()[rt-1];
            System.out.print("Price>");
            BigDecimal price = sc.nextBigDecimal();
            if(rateType.toString().equals("Promotion") || rateType.toString().equals("Peak")){
                System.out.print("Start of validity (YYYY-MM-DD)>");
                String start = sc.next();
                System.out.print("End of validity (YYYY-MM-DD)>");
                String end = sc.next();
                LocalDate dateStart = LocalDate.parse(start);
                LocalDate dateEnd = LocalDate.parse(end);
                try{
                    mainControllerBeanRemote.updateRate(rateId, name, roomType, rateType, price, dateStart, dateEnd);
                    System.out.println("Rate "+ name +" has been updated!");
                } catch (RateNameNotUniqueException | RoomTypeNotFoundException ex) {
                    System.out.println(ex.getMessage());
                }
            } else {
                try{
                    mainControllerBeanRemote.updateRate(rateId, name, roomType, rateType, price);
                    System.out.println("Rate "+ name +" has been updated!");
                } catch (RateNameNotUniqueException | RoomTypeNotFoundException ex) {
                    System.out.println(ex.getMessage());
                }
            } 
        } catch (RateNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        
    }
    
    private void deleteRate(){
        System.out.println("***Delete Rate***");
        try {
            Long rateId = viewRate();
            System.out.println("1: Delete");
            System.out.println("Any key to Cancel");
            if(sc.nextInt() == 1){
                sc.nextLine();
                mainControllerBeanRemote.deleteRate(rateId);
                System.out.println("Rate deleted!");
            }
        } catch (RateNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    private void checkIn(){
        System.out.println("***Check-in***");
        System.out.print("Enter guest passport number>");
        String passport = sc.nextLine();
        try {
            List<Integer> rooms = this.mainControllerBeanRemote.retrieveAllocatedRooms(passport);
            int i = 1;
            System.out.println("Rooms Allocated");
            for(Integer room: rooms){
                System.out.println(i+". Room "+ room);
                i++;
            }
        } catch (GuestNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (ReservationNotFoundException ex) {
            System.out.println("Guest hasn't reserved any rooms.");
        } catch (RoomNotAllocatedException ex) {
            System.out.println("Rooms are full, guest didn't get allocated.");
        }
        
    }
    
    private void checkOut(){
        System.out.println("***Check-out***");
        System.out.print("Enter guest passport number>");
        String passport = sc.nextLine();
        try {
            this.mainControllerBeanRemote.checkOut(passport);
            System.out.println("Guest checked-out!");
        } catch (GuestNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    private void searchRooms(){
        System.out.print("Check-out Date(YYYY-MM-DD)>");
        String end = sc.next();
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.parse(end);
        List<Boolean> ls1 = this.partnerControllerBeanRemote.search(startDate, endDate);
        List<RoomType> ls2 = this.mainControllerBeanRemote.sortRoomTypeAsc();
        System.out.println("Result of Room Search with the dates given: \n");
        int i = 0;
        for (RoomType rt : ls2) {
            System.out.println("Room Type: " + rt.getName() + "-> " + ls1.get(i));
            ++i;
        }
        System.out.println("");
    }
    
    private void reserveRooms(){
        System.out.println("***Reserve Rooms***");
        searchRooms();
        List<RoomType> roomTypes = this.mainControllerBeanRemote.sortRoomTypeAsc();

        System.out.println("Enter number of rooms booked for each type");
        List<ReservationLineItem> rlis = new ArrayList<>();
        for(RoomType room: roomTypes){
            System.out.print(room.getName() + "> ");
            int n = sc.nextInt();
            rlis.add(new ReservationLineItem(new Integer(n) , room));
        }
        System.out.println("Re-confirm stay dates");
        System.out.print("Check-out Date(YYYY-MM-DD)>");
        String end = sc.next();
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.parse(end);
        System.out.print("Guest Email> ");
        String email = sc.next();
        System.out.print("Guest Telephone> ");
        String telephone = sc.next();
        sc.nextLine();
        System.out.print("Guest Passport> ");
        String passport = sc.nextLine();   
        
        Guest guest = new Guest(email, telephone, passport);
        try {
            mainControllerBeanRemote.reserveGuestRooms(guest, startDate, endDate, rlis);
            System.out.println("Reservation has been made.");
        } catch (ReservationNotFoundException ex) {
            System.out.println("Reservation failed!");
        }
    }
}
