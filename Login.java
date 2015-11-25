import javax.swing.*;

import java.awt.*;
import java.util.*;
import java.util.*;
import java.sql.*;

import oracle.jdbc.*;

import java.io.Console;

public class Login {

    //main is log in menu
    public static void main(String[] args) throws SQLException {
        Scanner loginMenuInput = new Scanner(System.in);
        String loginMenuUserInput;
        String password = null;
        String userID = null;

        System.out.println("Welcome to the Information System Class System\nEnter the number of the option you wish to execute\n1. Create a student account\n2. Login to an existing account\n3. Exit program");
        loginMenuUserInput = loginMenuInput.nextLine();

        if(loginMenuUserInput.equals("1")){
            createUser(userID, password);

        }
        else if(loginMenuUserInput.equals("2")){
            System.out.println("Please enter your N#");
            userID = loginMenuInput.nextLine();
            System.out.println("Please enter your password");
            password = loginMenuInput.nextLine();

            logUserIn(userID, password);
        }
        else if(loginMenuUserInput.equals("3")) {
            System.exit(0);
        }

        else{
            System.out.println("invalid input redirecting to login");
            main(new String[] {});
        }
    }

    public static Connection getOracleConnection(){
        String url = "jdbc:oracle:thin:@olympia.unfcsd.unf.edu:1521:dworcl";
        String username = "teama4dm1f14";
        String password = "team4hhrsw";
        Connection con = null;
        try {

            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            con = DriverManager.getConnection(url, username, password);
        }
        catch(Exception ex){
            System.out.println("fucked connection");
        }
        return con;
    }

   /* public static void createstudentUser(String userid) throws SQLException{
    	 String in = userid;
    	 int allow = 1; // used to break the loop around the menu
         String f_name = "";
         String l_name = "";
         int stu_role = 1;
         int fac_role = 0;
         int admin_role = 0;
         int role_choice = 0;
         Scanner stream = new Scanner(System.in);

         while(allow !=0)
         {
             System.out.println("Lease enter the new N# to the student account that you would like to create.");

             //error handeling should a non int be entered
             try
             {
                 in = stream.next();
                 //input is not in the correct integer range
                 if(in.length() == 0)
                 {
                     allow = 1;
                     System.out.println("Invalid Input length. Please re-enter");
                 }
                 else//input is correct
                 {
                     allow = 0;
                 }
             }
             catch(Exception e)
             {
                 System.out.println("Input Error!");
             }
         }

         System.out.println("Please enter the first name for this new account.");
         f_name = stream.next();
         System.out.println("Please enter the last name for this new account.");
         l_name = stream.next();
         String query = "INSERT INTO user_table VALUES('"+in+"','"+f_name+"','"+l_name+"',"+stu_role+","+fac_role+","+admin_role+",'000',NULL);";
         try
         {
             DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
             Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@olympia.unfcsd.unf.edu:1521:dworcl", "teama4dm1f14", "team4hhrsw");
             Statement stmt = conn.createStatement();
             ResultSet rset = stmt.executeQuery(query);
             //stmt.executeUpdate(query);
         }
         catch(SQLException ex)
         {
             ex.printStackTrace();
         }
         System.out.printf("The default password for this user is: 000");
         System.out.println(query);
    }
*/
    public static void logUserIn(String userName, String Password) throws SQLException{

        //ResultSet s = null;
        Connection con = getOracleConnection();
        try{
            
            Statement stmt = con.createStatement();
            String query = "select password from 'user_table' where 'user_id' = "+userName;
            ResultSet s = stmt.executeQuery(query);
            //s = stmt.executeQuery(query);
            stmt.close();

            // will be null if no user returned
            if(s.next()) {
                //will fail if incorrect password
                if (Password.equals(s.getString("password"))) {
                    mainMenu(userName);
                }
                else {
                    System.out.println("fuck you");
                }
            }
        }
        catch(Exception ex){
            System.out.println("error redirecting back to login");
            main(new String[] {});
        }
        //this will never be called if mainMenu() gets called
        System.out.println("incorrect N# or password");
        main(new String[] {});


    }

    public static void mainMenu(String userid) throws SQLException{

        int hasStudentMenuAccess = 0;
        int hasFacultyMenuAccess = 0;
        int hasReportMenuAccess = 0;
        int hasAdminMenuAccess = 0;

        String studentAccess = "*";
        String facultyAccess = "*";
        String reportAccess = "*";
        String adminAccess = "*";

        //query the database for the user permissions
        ResultSet s = null;
        try{
            Connection con = getOracleConnection();
            Statement stmt = con.createStatement();
            String query = "select stu_role, fac_role, admin_role from user_table where user_id= " + userid;
            s = stmt.executeQuery(query);
            stmt.close();
        }
        catch(Exception ex){
            System.out.println("error redirecting back to login");
            main(new String[] {});
        }
        if(!(s.next())){
            System.out.println("error, exiting program");
            System.exit(0);
        }
        if(s.getInt(1) == 1){
            hasStudentMenuAccess = 1;
            studentAccess = null;
        }
        if(s.getInt(2) == 1){
            hasFacultyMenuAccess = 1;
            facultyAccess = null;
        }
        if(s.getInt(3) == 1){
            hasAdminMenuAccess = 1;
            adminAccess = null;
        }
        if(hasFacultyMenuAccess == 1 || hasAdminMenuAccess == 1){
            hasReportMenuAccess = 1;
            reportAccess = null;
        }

        System.out.println("Please select a menu you wish to access, those menu's with a '*' next to them are not available to you\n" +
                studentAccess + "1. Student Menu\n" +
                facultyAccess + "2. Faculty Menu\n" +
                reportAccess + "3. Reports Menu\n" +
                adminAccess + "4. Admin Menu\n" +
                "5. Exit program\n");

        Scanner mainMenuInput = new Scanner(System.in);
        String mainMenuUserInput;

        mainMenuUserInput = mainMenuInput.nextLine();

        if(mainMenuUserInput.equals("1") && hasStudentMenuAccess == 1){
            student_menu(userid);
        }
        else if(mainMenuUserInput.equals("2") && hasFacultyMenuAccess == 1){
            facultymenu(userid); //Mark's code starts here
        }
        else if(mainMenuUserInput.equals("3") && hasReportMenuAccess == 1){
        	reportsMenu(); //Kevin's code starts here
   
        }
        else if(mainMenuUserInput.equals("4") && hasAdminMenuAccess == 1){
            admin_menu(userid);
        }
        else if(mainMenuUserInput.equals("5")){
            System.exit(0);
        }
        else{
            mainMenu(userid);
        }
    }
    
	public static void facultymenu(String userid) throws SQLException {
		// this is them faculty menu
		Scanner facultymenu = new Scanner(System.in);
		String input;

		System.out
				.println("Faculty Menu\nEnter 1,2,3,4 to choose an option\n1. Create Form or edit past forms\n2. Review his/her past forms from the database\n3. Printout forms for courses\n4.Log out");
		input = facultymenu.nextLine();

		if (input.equals("1")) {
			createform(userid);
		} else if (input.equals("2")) {
			reviewforms(userid);
		} else if (input.equals("3")) {
			printforms(userid);
		} else if (input.equals("4")) {
			System.out.println("Login out now....");
			System.exit(0);			
		}
	}


    public static void admin_menu(String userid) throws SQLException
    {
        int allow = 1; // used to break the loop around the menu
        int in =0;
        Scanner stream = new Scanner(System.in);

        while(allow !=0)
        {
            //the menu
            System.out.println("\nAdministrative Menu");
            System.out.println("1. Create Specialty Account");
            System.out.println("2. Delete Existing Account");
            System.out.println("3. Main Menu");
            System.out.println("4. Log Out");

            //error handeling should a non int be entered
            try
            {
                in = stream.nextInt();
                //input is not in the correct integer range
                if(in < 1 || in > 4)
                {
                    allow = 1;
                    System.out.println("Invalid Input.Please re-enter");
                }
                else//input is correct
                {
                    allow = 0;
                }
            }
            catch(Exception e)
            {
                System.out.println("Incorrect input!");
            }
        }

        //now that input is validated lets proceed
        switch (in)
        {
            case 1:
                create_specialty_account(userid);
                break;

            case 2:
                delete_existing_account(userid);
                break;

            case 3:
                mainMenu(userid);
                break;

            case 4:
                System.exit(0);
                break;

            default:
                System.out.println("Error with input! Exiting now");
                System.exit(0);
                break;
        }
        admin_menu(userid);
    }

    public static void create_specialty_account(String userid)//throws SQLException
    {
        System.out.println("\nLets create a specialty account");
        String in = userid;
        int allow = 1; // used to break the loop around the menu
        String f_name = "";
        String l_name = "";
        int stu_role = 0;
        int fac_role = 0;
        int admin_role = 0;
        int role_choice = 0;
        Scanner stream = new Scanner(System.in);

        while(allow !=0)
        {
            System.out.println("Lease enter the new N# to the account that you would like to create.");

            //error handeling should a non int be entered
            try
            {
                in = stream.next();
                //input is not in the correct integer range
                if(in.length() == 0)
                {
                    allow = 1;
                    System.out.println("Invalid Input length. Please re-enter");
                }
                else//input is correct
                {
                    allow = 0;
                }
            }
            catch(Exception e)
            {
                System.out.println("Input Error!");
            }
        }

        System.out.println("Please enter the first name for this new account.");
        f_name = stream.next();
        System.out.println("Please enter the last name for this new account.");
        l_name = stream.next();
        System.out.println("Choose a status for this account(Enter the corresponding numeric value from the following...)");
        System.out.println("1. Student account");
        System.out.println("2. Faculty account");
        System.out.println("3. Admin account");
        role_choice = stream.nextInt();

        if(role_choice == 1)
        {
            stu_role = 1;
        }
        else if(role_choice == 2)
        {
            fac_role = 1;
        }
        else if(role_choice == 3)
        {
            admin_role = 1;
        }

        String query = "INSERT INTO user_table VALUES('"+in+"','"+f_name+"','"+l_name+"',"+stu_role+","+fac_role+","+admin_role+",'000',NULL);";
        try
        {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@olympia.unfcsd.unf.edu:1521:dworcl", "teama4dm1f14", "team4hhrsw");
            Statement stmt = conn.createStatement();
            ResultSet rset = stmt.executeQuery(query);
            //stmt.executeUpdate(query);
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
        System.out.printf("The default password for this user is: 000");
        System.out.println(query);
    }

    public static void delete_existing_account(String userid)
    {
        System.out.println("\nLets delete an account");
        System.out.println("Please enter the N# of the account that you would like to delete:");

        int allow = 1; // used to break the loop around the menu
        String in = null;
        Scanner stream = new Scanner(System.in);


        while(allow !=0)
        {
            //error handeling should a non int be entered
            try
            {
                userid = stream.nextLine();
                //input is not in the correct integer range
                if(userid.length() == 0 )
                {
                    allow = 1;
                    System.out.println("Invalid Input.Please re-enter.");
                }
                else//input is correct
                {
                    allow = 0;
                }
            }
            catch(Exception e)
            {
                System.out.println("Incorrect input length! Please re-enter");
            }
        }
        //here we make the sql call do delete the account stored in the variable in
        String query = "DELETE FROM user_table WHERE USER_ID = '"+userid+"';";
        try
        {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@olympia.unfcsd.unf.edu:1521:dworcl", "teama4dm1f14", "team4hhrsw");
            Statement stmt = conn.createStatement();
            ResultSet rset = stmt.executeQuery (query);
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
        System.out.println(query);
//      System.out.println("Account Deleted.");
    }

    public static void student_menu(String UID)
    {
        int allow = 1; // used to break the loop around the menu
        int in = 0;
        Scanner stream = new Scanner(System.in);

        while(allow !=0)
        {
            //the menu
            System.out.println("\nStudent Menu");
            System.out.println("1. Create Course Preferences");
            System.out.println("2. Main Menu");
            System.out.println("3. Log Out");

            //error handeling should a non int be entered
            try
            {
                in = stream.nextInt();
                //input is not in the correct integer range
                if(in < 1 || in > 3)
                {
                    allow = 1;
                    System.out.println("Invalid Input.Please re-enter");
                }
                else//input is correct
                {
                    allow = 0;
                }
            }
            catch(Exception e)
            {
                System.out.println("Incorrect input!");
            }
        }

        switch (in)
        {
            case 1:
                create_prefs(UID);
                break;

            case 2:
                //go to MM
                break;

            case 3:
                System.exit(0);
                break;

            default:
                System.out.println("Error with input! Exiting now");
                System.exit(0);
                break;
        }
        student_menu(UID);
    }

    public static void create_prefs(String UID)
    {
        char term;
        int year = 0000;
        String crs_num = null;
        String crs_name = null;
        int pref_rank = 0;
        int time_of_day_9am = 0;
        int time_of_day_12n = 0;
        int time_of_day_430pm = 0;
        int pref_hold = 0;
        char order_pref_1;
        char order_pref_2;
        char order_pref_3;
        int o_h = 0;
        int MW = 0;
        int TR = 0;
        int MWF = 0;
        int num_of_courses = 0;

        //summer specific
        int term_a = 0;
        int term_b = 0;
        String term_c = null;
        String week8 = null;
        String week10 = null;

        Scanner stream = new Scanner(System.in);



        System.out.println("Lets create class preferences");
        System.out.println("Please enter character corresponding to the term. EX (f = fall , s = spring , u = summer)");
        term = stream.nextLine().charAt(0);
        System.out.println("Please enter the year.Ex(2014)");
        year = stream.nextInt();
        System.out.println("How many courses do you want to take this term? Enter a integer.");
        num_of_courses = stream.nextInt();
        System.out.println("Please enter the course ID of thee class.Ex(COP5716)");
        crs_num = stream.next();
        System.out.println("Please enter the full Name of the course.Ex(Data_Modeling) IMPORTANT! no spaces in the string use one word or underscore");
        crs_name = stream.next();
        System.out.println("Please rank this class with an integer. The rank tells us how much you want to take this course. 1 being the highest.");
        pref_rank = stream.nextInt();
        System.out.println("Now for time preferences. Enter the integer that correspondes to one of the time intervals.Ex(1,2,3)");
        System.out.println("1. 9am - 12noon");
        System.out.println("2. 12 noon - 4:30pm");
        System.out.println("3. 4:30pm and after ");
        pref_hold = stream.nextInt();
        if(pref_hold == 1)
        {
            time_of_day_9am = 1;
        }
        else if(pref_hold == 2)
        {
            time_of_day_12n = 1;
        }
        else if(pref_hold == 3)
        {
            time_of_day_430pm = 1;
        }
        System.out.println("Now lets set priorities on preferences. c = cources , t = times, d = days");
        System.out.println("This will set which is more important to the user.Getting the course, time or day previously stated. Enter c, t,or d for the following inputs");
        System.out.println("Please enter the first priority");
        order_pref_1 = stream.next().charAt(0);
        System.out.println("Please enter the second priority");
        order_pref_2 = stream.next().charAt(0);
        System.out.println("Please enter the third priority");
        order_pref_3 = stream.next().charAt(0);
        System.out.println("Now to set priority on the days that courses can be schedualed on. Enter 1,2, or 3 for the following inputes where 1 is highest and 3 is lowest");
        System.out.println("Enter priority value for Monday/Wednsday. ");
        MW = stream.nextInt();
        System.out.println("Enter priority value for Tuesday/Thursday. ");
        TR = stream.nextInt();
        System.out.println("Enter priority value for Monday/Wednesday/Friday. ");
        MWF = stream.nextInt();

        //its summer, have to ask for special attributes and add to another table
        if(term == 'u' || term == 'U')
        {
            System.out.println("since the summer term is unique lets select the appropriate time interval.");
            System.out.println("Do you want term a, b, or c in the summer? Please enter 1,2, or 3");
            System.out.println("1. Term a");
            System.out.println("2. Term b");
            System.out.println("3. Term c");
            int j = stream.nextInt();
            if(j == 1)
            {
                term_a = 1;
            }
            else if(j == 2)
            {
                term_b = 1;
            }
            else if(j == 3)
            {
                System.out.println(" Do you want to have class on MW or TR during this term?");
                System.out.println("1. MW");
                System.out.println("2. TR");
                int t = stream.nextInt();
                if(t == 1)
                {
                    term_c = "MW";
                }
                else
                {
                    term_c = "TR";
                }

            }

            System.out.println("Would you prefer this be a 8 or a 10 week course? Enter 1 or 2");
            System.out.println("1. 8 week");
            System.out.println("2. 10 week");
            int k = stream.nextInt();

            if(k == 1)
            {
                System.out.println("This semester can schedual courses only on TR and MWF. Which would you prefer?");
                System.out.println("1. TR");
                System.out.println("2. MWF");
                int l = stream.nextInt();
                if(l == 1)
                {
                    week8 = "TR";
                }
                else
                {
                    week8 = "MWF";
                }
            }
            else if(k == 2)
            {
                System.out.println("This semester can schedual courses only on TR and MW. Which would you prefer?");
                System.out.println("1. TR");
                System.out.println("2. MW");
                int a = stream.nextInt();
                if(a == 1)
                {
                    week10 = "TR";
                }
                else
                {
                    week10 = "MW";
                }

            }
            String query3 = "INSERT INTO TABLE summer_schedule_prefs VALUES('"+UID+"','"+term+"',"+year+","+num_of_courses+","+time_of_day_9am+","+time_of_day_12n+","+time_of_day_430pm+",'"+order_pref_1+"','"+order_pref_2+"','"+order_pref_3+"',"+term_a+","+term_b+",'"+term_c+"','"+week8+"','"+week10+"');";
            try
            {
                DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
                Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@olympia.unfcsd.unf.edu:1521:dworcl", "teama4dm1f14", "team4hhrsw");
                Statement stmt = conn.createStatement();
                ResultSet rset = stmt.executeQuery (query3);
            }
            catch(SQLException ex)
            {
                ex.printStackTrace();
            }

            System.out.println(query3);
        }
        else
        {
            String query2 = "INSERT INTO TABLE student_schedule_prefs VALUES('"+UID+"','"+term+"',"+year+","+num_of_courses+","+time_of_day_9am+","+time_of_day_12n+","+time_of_day_430pm+",'"+order_pref_1+"','"+order_pref_2+"','"+order_pref_3+"',"+MW+","+TR+","+MWF+");";
            try
            {
                DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
                Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@olympia.unfcsd.unf.edu:1521:dworcl", "teama4dm1f14", "team4hhrsw");
                Statement stmt = conn.createStatement();
                ResultSet rset = stmt.executeQuery (query2);
            }
            catch(SQLException ex)
            {
                ex.printStackTrace();
            }

            System.out.println(query2);
        }
        String query1 = "INSERT INTO TABLE course_prefs VALUES('"+UID+"','"+term+"',"+year+",'"+crs_num+"','"+crs_name+"',"+pref_rank+");";
        try
        {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@olympia.unfcsd.unf.edu:1521:dworcl", "teama4dm1f14", "team4hhrsw");
            Statement stmt = conn.createStatement();
            ResultSet rset = stmt.executeQuery (query1);
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }

        System.out.println(query1);


    }
    
    public static void createUser(String userName, String password) throws SQLException {
    		
    		System.out.println("\nLets create a student account");

            int allow = 1; // used to break the loop around the menu
            String in = userName;
            String f_name = "";
            String l_name = "";
            int stu_role = 1;
            int fac_role = 0;
            int admin_role = 0;
            int role_choice = 0;
            Scanner stream = new Scanner(System.in);

            while(allow !=0)
            {
                System.out.println("Please enter the new N# to the account that you would like to create.");

                //error handeling should a non int be entered
                try
                {
                    in = stream.next();
                    //input is not in the correct integer range
                    if(in.length() == 0)
                    {
                        allow = 1;
                        System.out.println("Invalid Input length. Please re-enter");
                    }
                    else//input is correct
                    {
                        allow = 0;
                    }
                }
                catch(Exception e)
                {
                    System.out.println("Input Error!");
                }
            }

            System.out.println("Please enter the first name for this new account.");
            f_name = stream.next();
            System.out.println("Please enter the last name for this new account.");
            l_name = stream.next();
            System.out.println("Please enter a password for this new account");
            password = stream.next();
            System.out.println("Choose a status for this account(Enter the corresponding numeric value from the following...)");
            System.out.println("1. Student account");
            

           // String query = "INSERT INTO user_table (user_id, last_name, first_name, stu_role, fac_role, admin_role, password, view_only) VALUES ('"+in+"', '"+f_name+"', '"+l_name+"', "+stu_role+", "+fac_role+", "+admin_role+",'000',default);";
            String query = "INSERT INTO user_table VALUES('"+in+"', '"+password+"', '"+l_name+"', "+stu_role+", "+stu_role+", "+fac_role+", "+admin_role+", 0)";
            try
            {
                DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
                Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@olympia.unfcsd.unf.edu:1521:dworcl", "teama4dm1f14", "team4hhrsw");
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(query);
                //stmt.executeUpdate(query);
            }
            catch(SQLException ex)
            {
                ex.printStackTrace();
            }
           // System.out.printf("The default password for this user is: 000\n");
            System.out.println(query);
    	}

    public static void createform(String userid) throws SQLException {
		Scanner scanner = new Scanner(System.in);
		String url = "jdbc:oracle:thin:@olympia.unfcsd.unf.edu:1521:dworcl";
		String username = "teama4dm1f14";
		String password = "team4hhrsw";
		Connection con = null;

		System.out
				.println("What term will you like to fill a course preference form for(f= fall, s= spring, u= summer): ");
		char term = scanner.next().charAt(0);
		System.out.println("What year are you choosing your preferences for? ");
		int year = scanner.nextInt();

		System.out
				.println("What are the number of courses will you like to take? ");
		int numofcourses = scanner.nextInt();
		System.out.println("Are you expected to course Release? Enter Y/N");
		char releaseyn = scanner.next().charAt(0);
		int release = 0;
		if (releaseyn == 'Y' || releaseyn == 'y') {
			release = 1;
		} else if (releaseyn == 'N' || releaseyn == 'n') {
			release = 0;
		}

		System.out.println("Are you expected to sabbatical? ");
		char sabbaticalyn = scanner.next().charAt(0);
		int sabbatical = 0;
		if (sabbaticalyn == 'Y' || sabbaticalyn == 'y') {
			sabbatical = 1;
		} else if (sabbaticalyn == 'N' || sabbaticalyn == 'n') {
			sabbatical = 0;
		}

		System.out.println("Are you expected to leave? ");
		char profleaveyn = scanner.next().charAt(0);
		int profleave = 0;
		if (profleaveyn == 'Y' || profleaveyn == 'y') {
			profleave = 1;
		} else if (profleaveyn == 'N'  || profleaveyn == 'n') {
			sabbatical = 0;
		}

		System.out
				.println("What are you scheduling factors importance rank order?\nOptions are: Course Preference"
						+ " Days of the Week, and Times of the Day");
		System.out
				.println("Choices are c=courses, t= times, d= days, with seperating each character with a space: ");
		String orderpref1 = scanner.next();
		String orderpref2 = scanner.next();
		String orderpref3 = scanner.next();
		
		System.out
				.println("Choose your first choice of class by their course number(Ex: COP4715) , then course name: ");
		String crsnum1 = scanner.next();
		String crsname1 = scanner.next();

		System.out
				.println("Choose your second choice of class by their course number(Ex: COP4715), then course name: ");
		String crsnum2 = scanner.next();
		String crsname2 = scanner.next();

		System.out
				.println("Choose your third choice of class by their course number(Ex: COP4715), then course name: ");
		String crsnum3 = scanner.next();
		String crsname3 = scanner.next();

		System.out
				.println("Choose your fourth choice of class by their course number(Ex: COP4715), then course name: ");
		String crsnum4 = scanner.next();
		String crsname4 = scanner.next();

		System.out
				.println("Choose your fifth choice of class by their course number(Ex: COP4715), then course name: ");
		String crsnum5 = scanner.next();
		String crsname5 = scanner.next();

		System.out
				.println("Choose your times of day preference rank order(Enter 1,2, or 3): ");
		System.out.println("Morning (9 am to Noon): ");
		int morning = scanner.nextInt();

		System.out.println("Afternoon (Noon to 4:15 pm): ");
		int afternoon = scanner.nextInt();

		System.out.println("Evening (4:30pm to 9:10 pm): ");
		int evening = scanner.nextInt();

		int mw = 0, tr = 0, mwf = 0;
		String sumterm = null;
		int terma = 0, termb = 0;
		char termc = 0, eightweek = 0, tenweek = 0;

		if (term == 'f' || term == 's') {
			System.out.println("Choose the times of day preference: ");
			System.out
					.println("With MW = 1, TR = 2, and MWF =  being your choices, what number for the rank preference do you choose for MW?");
			mw = scanner.nextInt();
			System.out
					.println("With MW = 1, TR = 2, and MWF =  being your choices, what number for the rank preference do you choose for TR?");
			tr = scanner.nextInt();
			System.out
					.println("With MW = 1, TR = 2, and MWF =  being your choices, what number for the rank preference do you choose for MWF?");
			mwf = scanner.nextInt();

			String sql1 = "INSERT INTO TABLE faculty_schedule_prefs VALUES ('"
					+ userid
					+ "','"
					+ term
					+ "',"
					+ year
					+ ","
					+ numofcourses
					+ ","
					+ morning
					+ ","
					+ afternoon
					+ ","
					+ evening
					+ ",'"
					+ orderpref1
					+ "','"
					+ orderpref2
					+ "','"
					+ orderpref3 + "'," + mw + "," + tr + "," + mwf + "'," + release + "," + sabbatical + "," + profleave + ");";
			try {
				DriverManager
						.registerDriver(new oracle.jdbc.driver.OracleDriver());
				Connection conn = DriverManager.getConnection(
						"jdbc:oracle:thin:@olympia.unfcsd.unf.edu:1521:dworcl",
						"teama4dm1f14", "team4hhrsw");
				Statement stmt = conn.createStatement();
				ResultSet rset = stmt.executeQuery(sql1);
				stmt.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}

			System.out.println(sql1);
		}

		else if (term == 'u') {
			System.out
					.println("Choose a summer term preference, by entering a,b, or c for the summer or 8 or 10 for the number of weeks: ");
			sumterm = scanner.next();
			if (scanner.equals("a")) {
				terma = 1;
				termb = 0;
			}
			if (scanner.equals("b")) {
				terma = 0;
				termb = 1;
			}
			if (scanner.equals("c")) {
				System.out
						.println("Choose MW or TH for the preference of days: ");
				termc = scanner.next().charAt(termc);
			}
			if (scanner.equals("8")) {
				System.out
						.println("Choose MWF or TH for the preference of days: ");
				eightweek = scanner.next().charAt(eightweek);
			}
			if (scanner.equals("10")) {
				System.out
						.println("Choose MW or TH for the preference of days: ");
				tenweek = scanner.next().charAt(tenweek);
			}

			String sql2 = "INSERT INTO TABLE summer_schedule_prefs VALUES('"
					+ userid + "','" + term + "'," + year + "," + numofcourses
					+ "," + morning + "," + afternoon + "," + evening + ",'"
					+ orderpref1 + "','" + orderpref2 + "','" + orderpref3
					+ "'," + terma + "," + termb + ",'" + termc + "','"
					+ eightweek + "','" + tenweek + "');";
			try {
				DriverManager
						.registerDriver(new oracle.jdbc.driver.OracleDriver());
				Connection conn = DriverManager.getConnection(
						"jdbc:oracle:thin:@olympia.unfcsd.unf.edu:1521:dworcl",
						"teama4dm1f14", "team4hhrsw");
				Statement stmt = conn.createStatement();
				ResultSet rset = stmt.executeQuery(sql2);
			} catch (SQLException ex) {
				ex.printStackTrace();
			}

			System.out.println(sql2);
		}
		String editterm = ("UPDATE faculty_schedule_prefs SET term= '"
				+ term + "', Where term= '" + term + "' AND year= '" + year + "';");
		String sql3 = "INSERT INTO TABLE course_prefs VALUES('" + userid
				+ "','" + term + "'," + year + ",'" + crsnum1 + "','"
				+ crsname1 + "'," + "1" + ");";
		String sql4 = "INSERT INTO TABLE course_prefs VALUES('" + userid
				+ "','" + term + "'," + year + ",'" + crsnum2 + "','"
				+ crsname2 + "'," + "2" + ");";
		String sql5 = "INSERT INTO TABLE course_prefs VALUES('" + userid
				+ "','" + term + "'," + year + ",'" + crsnum3 + "','"
				+ crsname3 + "'," + "3" + ");";
		String sql6 = "INSERT INTO TABLE course_prefs VALUES('" + userid
				+ "','" + term + "'," + year + ",'" + crsnum4 + "','"
				+ crsname4 + "'," + "4" + ");";
		String sql7 = "INSERT INTO TABLE course_prefs VALUES('" + userid
				+ "','" + term + "'," + year + ",'" + crsnum5 + "','"
				+ crsname5 + "'," + "5" + ");";
		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			Connection conn = DriverManager.getConnection(
					"jdbc:oracle:thin:@olympia.unfcsd.unf.edu:1521:dworcl",
					"teama4dm1f14", "team4hhrsw");
			Statement stmt = conn.createStatement();
			stmt.addBatch(editterm);
			stmt.addBatch(sql3);
			stmt.addBatch(sql4);
			stmt.addBatch(sql5);
			stmt.addBatch(sql6);
			stmt.addBatch(sql7);
			stmt.executeBatch();
			stmt.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		System.out.println(editterm);
		System.out.println(sql3);
		System.out.println(sql4);
		System.out.println(sql5);
		System.out.println(sql6);
		System.out.println(sql7);

    }
	public static void printforms(String userid) throws SQLException {
		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			Connection conn = DriverManager.getConnection(
					"jdbc:oracle:thin:@olympia.unfcsd.unf.edu:1521:dworcl",
					"teama4dm1f14", "team4hhrsw");
			Statement stmt = conn
					.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
			String sql9 = ("SELECT * FROM course_prefs by user_id ");
			System.out.println("\nExecuting query: " + sql9);
			ResultSet rset = stmt.executeQuery(sql9);
			stmt.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	public static void reviewforms(String userid) {
		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			Connection conn = DriverManager.getConnection(
					"jdbc:oracle:thin:@olympia.unfcsd.unf.edu:1521:dworcl",
					"teama4dm1f14", "team4hhrsw");
			Statement stmt = conn
					.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
			String sql8 = ("SELECT * FROM faculty_schedule_prefs by user_id ");
			System.out.println("\nExecuting query: " + sql8);
			ResultSet rset = stmt.executeQuery(sql8);
			stmt.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

    //----------------------------------------reportsMenu----------------------

    public static void reportsMenu() {
        chooseReport();
    }

//-----------------------------------------------------------------------
  public static void chooseReport()
  {
    Console console = System.console();

    System.out.println("\n          Reports Menu\n1. Student Preference Reports\n" +
      "2. Faculty Preference Reports\n3. Administrative Reports\n4. Exit System\n\n");

    String menu_option = console.readLine("Enter menu number: ");
    String report_type = "";

    if (menu_option.equals("1"))
      {
      System.out.println("\n\n       Student Reports\n" +
        "A) Preferred Courses by Term\n" + 
        "B) Preferred Days by Term\n" +
        "C) Preferred Times by Term\n"
        );
      report_type= "student";
      String report = console.readLine("Enter Report by Letter: ");
      reportByTerm(report_type, report);

      }
    else if (menu_option.equals("2"))
      {
      System.out.println("\n\n       Faculty Reports\n" +
        "A) Preferred Courses by Term\n" + 
        "B) Preferred Days by Term\n" +
        "C) Preferred Times by Term\n" +
        "D) Faculty Status by Term\n"  //are we on sabbatica/prof-dev/course-release?
        );
      report_type= "faculty";
      String report = console.readLine("Enter Report by Letter: ");
      reportByTerm(report_type, report);
      }
    else if (menu_option.equals("3"))
      {
      System.out.println("\n      Administrative Reports\n" +
        "A) List Students\n" + 
        "B) List Faculty\n" +
        "C) List Courses\n "
        );
      report_type= "admin";
      String report = console.readLine("Enter Report by Letter: ");
      reportByTerm(report_type, report);
      }
    else if (menu_option.equals("4"))
       {
       System.exit(0);
       }
    else
      {
       System.out.println("You didn't follow directions. Goode bye.");
      }

    }//chooseReport

//------------------------------------------------------------------
  public static void reportByTerm(String report_type, String report)
  {
    //String option = report_type;
    Connection con= getOracleConnection();

    //  if student
    if (report_type.equals("student"))
      {
        if (report.equals("a"))
          {
            try
            {
            Statement stmt = con.createStatement();
            String query = "SELECT term, year, crs_number, count(crs_number) AS count FROM course_prefs group by crs_number, term, year";
            ResultSet rset = stmt.executeQuery(query);
            //System.out.println("what the fuck");
            while (rset.next()) 
              {
                String term = rset.getString("term");
                int year = rset.getInt("year");
                String num = rset.getString("crs_number");
                int count = rset.getInt("count");
                //System.out.println("we're in");
                System.out.println(term + " " + year + " " + num + " " + count);
              }
            System.out.println();
            System.out.println();
            chooseReport();
            }
            catch(SQLException ex)
            {
            ex.printStackTrace();
            }
          }//if report == a

        else if (report.equals("b"))
        {
            try
            {
            Statement stmt = con.createStatement();
            //String query = "SELECT term, year AS year, count(mw) AS mw, count(tr) as tr, count(mwf) as mwf FROM student_schedule_prefs group by term, year";
            //String query = "SELECT year, term, count(mw) AS mw, count(tr) AS tr, count(mwf) AS mwf FROM student_schedule_prefs WHERE " +
            //    "mw=1 GROUP BY year, term";

            String query = "SELECT year, term, (SELECT COUNT(mw) FROM student_schedule_prefs ssp WHERE ssp.mw=1) mw, " +
            "(SELECT COUNT(tr) FROM student_schedule_prefs ssp2 WHERE ssp2.tr=1) tr, " +
            "(SELECT COUNT(mwf) FROM student_schedule_prefs ssp3 WHERE ssp3.mwf=1) mwf " +
            "FROM student_schedule_prefs group by year, term";


            ResultSet rset = stmt.executeQuery(query);
           // ResultSet rset2 = stmt.executeQuery(query2);
            System.out.println("\n         Student Preferred Days by Term\n");
            System.out.println("YEAR|TERM|MW|TH|MWF\n");
            while (rset.next()) 
              {
                String term = rset.getString("term");
                int year = rset.getInt("year");
                int MW = rset.getInt("mw");
                int TH = rset.getInt("tr");
                int MWF = rset.getInt("mwf");
                //System.out.println("we're in");
               // System.out.println(term + " " + year + " " + MW + " " + TH + " " + MWF);
                System.out.println(year+ " " + term + " " + MW + " " + TH + " " + MWF);

              }

            System.out.println();
            System.out.println();
            chooseReport();
            }
            catch(SQLException ex)
            {
            ex.printStackTrace();
            }
        }//if report == b

        else if (report.equals("c"))
        {
            try
            {
            Statement stmt = con.createStatement();
            String query = "SELECT term as term, year, SUM(time_of_day_9am) AS before12, SUM(time_of_day_12n) AS noon, SUM(time_of_day_430pm) AS evening FROM student_schedule_prefs group by term, year";
            ResultSet rset = stmt.executeQuery(query);
            System.out.println("\n         Student Preferred Times by Term\n\n");
            System.out.println("Term|Year|Early|Afternoon|Late\n");
            while (rset.next()) 
              {
                String term = rset.getString("term");
                int year = rset.getInt("year");
                int before12 = rset.getInt("before12");
                int noon = rset.getInt("noon");
                int evening = rset.getInt("evening");
                //System.out.println("we're in");
                System.out.println(term + " " + year + "  " + before12 + "    " + noon + "    " + evening);
              }

            System.out.println();

            System.out.println();
            chooseReport();
            }
            catch(SQLException ex)
            {
            ex.printStackTrace();
            }
        }//if report == c
      else
      {
        System.out.println("\n        Please try again");
        chooseReport();
      }

      }//if report_type == student

    // if faculty
    else if (report_type.equals("faculty"))
      {
        if (report.equals("a"))
          {
            try
            {
            Statement stmt = con.createStatement();
            String query = "SELECT term, year, crs_number, count(crs_number) AS count FROM course_prefs group by crs_number, term, year";
            ResultSet rset = stmt.executeQuery(query);
            System.out.println("\n        Faculty Preferred Courses by Term");
            while (rset.next()) 
              {
                String term = rset.getString("term");
                int year = rset.getInt("year");
                String num = rset.getString("crs_number");
                int count = rset.getInt("count");
                //System.out.println("we're in");
                System.out.println(term + " " + year + " " + num + " " + count);
              }
            System.out.println();
            System.out.println();
            chooseReport();
            }
            catch(SQLException ex)
            {
            ex.printStackTrace();
            }
          }//if report == a

        else if (report.equals("b"))
        {
            try
            {
            Statement stmt = con.createStatement();
            String query = "SELECT year, term, (SELECT COUNT(mw) FROM faculty_schedule_prefs fsp WHERE fsp.mw=1) mw, " +
            "(SELECT COUNT(tr) FROM faculty_schedule_prefs fsp2 WHERE fsp2.tr=1) tr, " +
            "(SELECT COUNT(mwf) FROM faculty_schedule_prefs fsp3 WHERE fsp3.mwf=1) mwf " +
            "FROM faculty_schedule_prefs group by year, term";

            ResultSet rset = stmt.executeQuery(query);
            System.out.println("\n         Faculty Preferred Days by Term\n");
            System.out.println("YEAR|TERM|MW|TH|MWF\n");
            while (rset.next()) 
              {
                String term = rset.getString("term");
                int year = rset.getInt("year");
                int MW = rset.getInt("mw");
                int TH = rset.getInt("tr");
                int MWF = rset.getInt("mwf");
                //System.out.println("we're in");
                System.out.println(term + " " + year + " " + MW + " " + TH + " " + MWF);
              }

            System.out.println();
            System.out.println();
            chooseReport();
            }
            catch(SQLException ex)
            {
            ex.printStackTrace();
            }
        }//if report == b

        else if (report.equals("c"))
        {
            try
            {
            Statement stmt = con.createStatement();
            String query = "SELECT term as term, year, SUM(time_of_day_9am) AS before12, SUM(time_of_day_12n) AS noon, SUM(time_of_day_430pm) AS evening FROM faculty_schedule_prefs group by term, year";
            ResultSet rset = stmt.executeQuery(query);
            System.out.println("\n         Faculty Preferred Times by Term\n\n");
            System.out.println("Term|Year|Early|Afternoon|Evening\n");
            while (rset.next()) 
              {
                String term = rset.getString("term");
                int year = rset.getInt("year");
                int before12 = rset.getInt("before12");
                int noon = rset.getInt("noon");
                int evening = rset.getInt("evening");
                //System.out.println("we're in");
                System.out.println(term + " " + year + "   " + before12 + "    " + noon + "    " + evening);
              }

            System.out.println();

            System.out.println();
            chooseReport();
            }
            catch(SQLException ex)
            {
            ex.printStackTrace();
            }
        }//if report == c

        else if (report.equals("d"))
        {
            try
            {
            Statement stmt = con.createStatement();
            String query = "SELECT user_id, term, year, crs_release, sabbatical, prof_dev FROM faculty_schedule_prefs";
            ResultSet rset = stmt.executeQuery(query);
            System.out.println("\n         Faculty Status by Term\n\n");
            System.out.println("Term|Year|Course Release|Sabbatical|Prof Dev\n");
            while (rset.next()) 
              {
                String term = rset.getString("term");
                int year = rset.getInt("year");
                int before12 = rset.getInt("crs_release");
                int noon = rset.getInt("sabbatical");
                int evening = rset.getInt("prof_dev");
                //System.out.println("we're in");
                System.out.println(term + "  " + year + "  " + before12 + " " + noon + " " + evening);
              }

            System.out.println();

            System.out.println();
            chooseReport();
            }
            catch(SQLException ex)
            {
            ex.printStackTrace();
            }
        }//if report == c

      else
      {
        System.out.println("\n        Please try again");
        chooseReport();
      }

    }//if report_type == faculty


  else if (report_type.equals("admin"))
    {
        if (report.equals("a"))
          {
            try
            {
            Statement stmt = con.createStatement();
            String query = "select user_id, first_name, last_name from user_table where stu_role=1";
            ResultSet rset = stmt.executeQuery(query);
            System.out.println("\n         Faculty Members\n\n");
            while (rset.next()) 
              {
                String user = rset.getString("user_id");
                String first = rset.getString("first_name");
                String last = rset.getString("last_name");
                //System.out.println("we're in");
                System.out.println(user + " " + first + " " + last);
              }
            System.out.println();
            System.out.println();
            chooseReport();
            }
            catch(SQLException ex)
            {
            ex.printStackTrace();
            }
          }//if report == a

        else if (report.equals("b"))
          {
            try
            {
            Statement stmt = con.createStatement();
            String query = "select user_id, first_name, last_name from user_table where fac_role=1";
            ResultSet rset = stmt.executeQuery(query);
            System.out.println("\n         Faculty Members\n\n");
            while (rset.next()) 
              {
                String user = rset.getString("user_id");
                String first = rset.getString("first_name");
                String last = rset.getString("last_name");
                //System.out.println("we're in");
                System.out.println(user + " " + first + " " + last);
              }

            System.out.println();
            System.out.println();
            chooseReport();
            }
            catch(SQLException ex)
            {
            ex.printStackTrace();
            }
        }//if report == b

        else if (report.equals("c"))
          {
            try
            {
            Statement stmt = con.createStatement();
            String query = "select crs_number, crs_name FROM course_list";
            ResultSet rset = stmt.executeQuery(query);
            System.out.println("\n         List of Courses\n\n");
            while (rset.next()) 
              {
                String user = rset.getString("crs_number");
                String first = rset.getString("crs_name");
                //String last = rset.getString("last_name");
                //System.out.println("we're in");
                System.out.println(user + "  " + first);
              }

            System.out.println();
            System.out.println();
            chooseReport();
            }
            catch(SQLException ex)
            {
            ex.printStackTrace();
            }
        }//if report == b

        else
        {
        System.out.println("\n        Please try again");
        chooseReport();
        }

    }// if admin

  }//reportByTerm

}// end class
