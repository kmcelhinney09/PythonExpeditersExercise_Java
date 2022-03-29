import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/*
    I assumed that the data was coming in as a CSV file with no headers and treated as such. I wanted to be able to
    normalize data to allow parsing to be easier. I know that this is most likely not the most efficient way to do this
    project but given the limited amount of time and knowledge of Java I did what I could to achieve the objective, and
    I can always go back and optimize the code now that it works. I assumed that a script such as this would be used
    multiple CSV files, and so I tried to abstract away as much as I could so that any txt file could be used. Because
    Java is famous for it Object-Oriented Programing I wanted to try and create a new object that could be used in my
    script and be used later to extend the capability of the program. A lot of what I used to build this program was a
    general understanding of how programs should be written (such as separation of concerns and iterating through
    lists), and then searching for how to accomplish what I wanted in Java (such as how to use Arrays, HashMaps,
    and Comparators).
*/

// Create a user object to handle all the data from CSV to make it easier to sort and print information
//  also makes an object available if user data is needed for further use
class User {
    private String first_name;
    private String last_name;
    private String address;
    private String city;
    private String state;
    private String age;
 // constructor for the new object ingesting all the information from the CSV file
    public User(String first_name, String last_name, String address, String city, String state, String age) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.address = address;
        this.city = city;
        this.state = state;
        this.age = age;
    }
// Getter and setters for all User Data
    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

}

// This is a way to sort my ArrayList of User Objects by last name then first name
class SortByLastName implements Comparator<User> {

    @Override
    public int compare(User o1, User o2) {
        int value1 = o1.getLast_name().compareTo(o2.getLast_name());
        // if the two last names are equal then compare and return first names
        if (value1 == 0) {
            return o1.getFirst_name().compareTo(o2.getFirst_name());
        } else {
            return value1;
        }
    }
}

public class Main {
    public static void main(String[] args) {
        // This is the input file. It is set to be in the same folder as this program
        String path = "src/input_data.txt";

        // A seperate method is used to take the CSV in and parse out each line into an ArrayList that contains
        //  a list of strings
        ArrayList<List<String>> parsedCSV = new ArrayList<>(parseCSV(path));

        /*The ArrayList from above is then used to count the number of users in a household and to create a User object
          for each row of the CSV. The household count and new User objects are returned in a HashMap using the street
          address as a key and then either the member count or list of User objects as the values. Return an ArrayList
          of the two HashMaps.
         */
        ArrayList<HashMap> houseHoldData = new ArrayList<HashMap>(houseHoldParsing(parsedCSV));

        // Separate out the two HashMaps into individual variables
        HashMap<String, Integer> count = new HashMap<>(houseHoldData.get(0));
        HashMap<String, List<User>> members = new HashMap<>(houseHoldData.get(1));

        /*
            The following for loop goes through each Key Value pair of the User Object HashMap and breaks out the
            household by street address and then members of that household. It then sorts the list of household members
            first by last name and then by first name.
            It then prints out to the terminal the household street address and the number of occupants followed by the
            name, address, and age of the occupants older the 18.
         */
        for (Map.Entry<String, List<User>> member : members.entrySet()) {
            String houseHold = member.getKey();
            List<User> houseMembers = member.getValue();
            Collections.sort(houseMembers, new SortByLastName());
            System.out.println(houseHold + ":" + count.get(houseHold));
            for (User houseMember : houseMembers
            ) {
                if (Integer.parseInt(houseMember.getAge()) > 18) {
                    String first_name = houseMember.getFirst_name().substring(0, 1).toUpperCase() +
                            houseMember.getFirst_name().substring(1);
                    String last_name = houseMember.getLast_name().substring(0, 1).toUpperCase() +
                            houseMember.getLast_name().substring(1);
                    String address = houseMember.getAddress();
                    String city = houseMember.getCity().substring(0, 1).toUpperCase() +
                            houseMember.getCity().substring(1);
                    String state = houseMember.getState().toUpperCase();
                    String age = houseMember.getAge();
                    String userPrintOut = "First Name:" + first_name + " Last Name:" + last_name + " Address: " +
                            address + " " + city + "," + state + " Age: " + age;
                    System.out.println(userPrintOut);
                }
            }
        }
    }

    /*
        This is a method that takes in an ArrayList that contains lists of strings that represent each occupant of a
        household. The method returns an ArrayList of two HashMaps. The first HashMap contains keys that are the street
        address of the households with an integer value of the number of occupants that share that address. The second
        HashMap again contains keys that are the street address of the occupants with a list value of User Object that
        contain the first and last name, full address, and age of each occupant.
     */
    private static ArrayList<HashMap> houseHoldParsing(ArrayList<List<String>> users) {
        HashMap<String, Integer> houseHoldCount = new HashMap<>();
        HashMap<String, List<User>> houseHolds = new HashMap<>();

        // Create an ArrayList, so I can return more than one HashMap
        ArrayList<HashMap> returnValues = new ArrayList<>();

        // This for loop starts by breaking out each occupants' data from the ArrayList argument users
        for (List<String> data : users) {
            String address = data.get(2);
            User newUser = new User(data.get(0), data.get(1), data.get(2), data.get(3), data.get(4), data.get(5));
            // Logic to produce occupant count for each address
            if (houseHoldCount.containsKey(address.strip())) {
                Integer userCount = houseHoldCount.get(address.strip()) + 1;
                houseHoldCount.put(address.strip(), userCount);
            } else {
                houseHoldCount.put(address.strip(), 1);
            }
            //Logic for adding each new User Object to the key that matches their address
            if (houseHolds.containsKey(address.strip())) {
                List<User> houseHoldMembers = houseHolds.get(address.strip());
                houseHoldMembers.add(newUser);
            } else {
                ArrayList<User> newEntry = new ArrayList<>();
                newEntry.add(newUser);
                houseHolds.put(address.strip(), newEntry);
            }

        }
        returnValues.add(houseHoldCount);
        returnValues.add(houseHolds);
        return returnValues;
    }

    /*
        This is a method used to parse each line of the CSV and return an ArrayList containing a list of strings that
        represents each occupant of a household.
     */
    private static ArrayList<List<String>> parseCSV(String path) {
        String line;
        ArrayList<List<String>> userData = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            while ((line = br.readLine()) != null) {
                // occupant data had to be parsed out charter by charter to normalize the data and remove any unwanted
                // and pesky punctuations to allow for easier processing of data later
                List<String> result = new ArrayList<>();
                StringBuilder sb = new StringBuilder();
                int quoteCount = 0;
                for (int i = 0; i < line.length(); i++) {
                    char c = line.charAt(i);
                    if (quoteCount == 0 && c == '"') quoteCount++;
                    else if (quoteCount > 0 && c == '"') quoteCount--;
                    if (quoteCount == 0 && c == ',' || i == line.length() - 1) {
                        result.add(sb.toString());
                        sb.setLength(0);
                    } else {
                        if (c != '"' && c != '.' && c != ',') { // quotes, period and extra comas are removed
                            // Everything is converted to lower case to normalize data since it is char by char
                            // everything had to be lowered
                            sb.append(Character.toLowerCase(c));
                        }

                    }
                }
                userData.add(result);
            }
        // Error catching that Java or intelliJ made me do now sure what it is for
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userData;
    }
}
