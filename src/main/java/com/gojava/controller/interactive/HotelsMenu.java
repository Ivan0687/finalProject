package com.gojava.controller.interactive;

import com.gojava.dao.Utils;
import com.gojava.model.Hotel;
import com.gojava.model.Interactive;
import com.gojava.service.HotelService;
import com.gojava.service.impl.HotelServiceImpl;

import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static com.gojava.dao.Utils.*;

public class HotelsMenu implements Interactive {

    private Interactive previousMenu;
    private HotelRoomsMenu hotelRoomsMenu;
    private HotelService<Hotel> hotelService = new HotelServiceImpl();

    HotelsMenu(Interactive interactive) {
        this.previousMenu = interactive;
    }

    @Override
    public void showMenu() {
        printBorder();
        System.out.println("Hotels menu");
        System.out.println("1) Show all hotels");
        System.out.println("2) Add hotel");
        System.out.println("3) Update hotel");
        System.out.println("4) Delete hotel");
        System.out.println("5) Find hotel");
        System.out.println("6) Manage hotel rooms");
        System.out.println("7) Back to main menu");
        printBorder();

        Integer selectedItem = provideIntInputStream();

        if (selectedItem == null) {
            System.err.println("not correct entered data, try again");
            showMenu();
        } else {
            switch (selectedItem) {
                case 1:
                    showAllHotels();
                    break;
                case 2:
                    addHotel();
                    break;
                case 3:
                    updateHotel();
                    break;
                case 4:
                    deleteHotel();
                    break;
                case 5:
                    findHotel();
                    break;
                case 6:
                    manageHotelRooms();
                    break;
                case 7:
                    previousMenu.showMenu();
                    break;
                default:
                    showMenu();
                    break;
            }
        }
    }

    private void findHotel() {
        System.out.println("1) Find hotel by name");
        System.out.println("2) Find hotel by city");
        System.out.println("3) Return to Hotels menu");

        Integer selectedItem = provideIntInputStream();

        if (selectedItem == null) {
            System.err.println("not correct entered data, try again");
            findHotel();
        } else {
            switch (selectedItem) {
                case 1:
                    findHotelByName();
                    break;
                case 2:
                    findHotelByCity();
                    break;
                case 3:
                    showMenu();
                    break;
                default:
                    showMenu();
                    break;
            }
        }
    }

    private void findHotelByName() {

        if (hotelService.getAll().isEmpty()) {
            System.out.println("There is no registered hotel in the system.");
            showMenu();
        }

        Set<Hotel> result = hotelService.getAllHotels();

        String city = provideStringInputStream("Enter city name or press 'Enter' to return to menu: ");

        if (!isValidString(city))
            showMenu();

        result = hotelService.findHotelsByCity(city, result);

        if (result.isEmpty()) {
            System.out.println("There is no hotels in " + city);
            showMenu();
        } else {
            System.out.println("Hotels in " + city + ": ");
            result.forEach(System.out::println);
            showMenu();
        }

    }

    private void findHotelByCity() {


        if (hotelService.getAll().isEmpty()) {
            System.out.println("There is no registered hotel in the system.");
            showMenu();
        }

        Set<Hotel> result = hotelService.getAllHotels();

        String name = provideStringInputStream("Enter hotel name or press 'Enter' to return to menu: ");

        if (!isValidString(name))
            showMenu();

        result = hotelService.findHotelsByName(name, result);

        if (result.isEmpty()) {
            System.out.println("There is no hotels named '" + name + "'");
            showMenu();
        } else {
            System.out.println("Hotels named '" + name + "': ");
            result.forEach(System.out::println);
            showMenu();
        }
    }

    private void showAllHotels() {
        System.out.println("Count of hotels: " + hotelService.getAll().size());
        if (hotelService.getAll().isEmpty()) {
            showMenu();
        } else
            new TreeSet<>(hotelService.getAll().values()).forEach(System.out::println);
        showMenu();
    }

    private void addHotel() {

        String city = provideStringInputStream("Enter hotel city or press 'Enter' to return to menu: : ");
        if (!isValidString(city)) {
            addHotel();
        }

        String name = provideStringInputStream("Enter hotel name or press 'Enter' to return to menu: : ");
        if (!isValidString(name)) {
            addHotel();
        }

        if (hotelService.isHotelExistsInCity(name, city)) {
            System.out.println("Hotel '" + name + "' in " + city + " city already exists");
            addHotel();
        }

        Hotel hotel = new Hotel(name, city);

        Hotel addedHotel = hotelService.create(hotel);

        hotelRoomsMenu = new HotelRoomsMenu(hotel, this);
        hotelRoomsMenu.showMenu();

        System.out.println("added new hotel: " + addedHotel.toString());
        showMenu();
    }

    private void updateHotel() {

        Integer idChoose = provideIntInputStreamWithMessage("Select hotel by id or press 'Enter' to return to menu: ");

        if (idChoose == null) {
           showMenu();
        }

        Hotel hotel = hotelService.findById(idChoose);

        if (hotel == null) {
            System.out.println("Hotel with id = "+idChoose+ " doesn't exist. Please choose another id.");
            updateHotel();
        }else
            System.out.println(hotel);


        String nameHotel = provideStringInputStream("Enter new hotel's name or press 'Enter' to return to menu: ");
        if (!isValidString(nameHotel)) {
            showMenu();
        }

        String cityName = provideStringInputStream("Enter new hotel's city or press 'Enter' to return to menu: ");
        if (!isValidString(cityName)) {
            showMenu();
        }

        hotel.setName(nameHotel);
        hotel.setCity(cityName);
        System.out.println("Hotel has been changed to " + hotel);
        showMenu();
    }

    private void deleteHotel() {

        Integer removeHotelId = provideIntInputStreamWithMessage("Enter hotel's id or press 'Enter' to return to menu: ");

        if (removeHotelId == null)
            showMenu();

        Hotel removedHotel = hotelService.findById(removeHotelId);

        if (removedHotel == null) {
            System.out.println("Hotel with this id doesn't exist");
        } else {
            hotelService.delete(removedHotel);
            System.out.println("This Hotel with id " + removeHotelId + " has been deleted");
        }
        showMenu();
    }


    private void manageHotelRooms() {
        Integer selectedItem = provideIntInputStreamWithMessage("Enter hotel's id or press 'Enter' to return to menu: ");

        if (selectedItem == null)
            showMenu();

        Hotel hotel = hotelService.findById(selectedItem);

        if (hotel == null) {
            System.out.println("Hotel with id " + selectedItem + " hasn't been found. Choose another id.");
            manageHotelRooms();
        }

        new HotelRoomsMenu(hotel, this).showMenu();
    }
}
