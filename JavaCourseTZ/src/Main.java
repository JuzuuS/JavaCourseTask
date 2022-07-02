import java.sql.*;
import java.util.Scanner;

public class Main {
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "rasaartem01";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/Clinic";

    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        Scanner scanner = new Scanner(System.in);

        for (int i = 0; i < 3; i++) {
            System.out.println("Input username:");
            String userName = scanner.nextLine();
            System.out.println("Input password:");
            String password = scanner.nextLine();

            if ("admin".equals(userName) && "admin".equals(password)) {
                System.out.println("Welcome " + userName);
                System.out.println();
                break;// выход из цикла;
            } else {
                System.out.println("Wrong password or username");
                if (i==2) {
                    System.exit(0);
                }
            }
        }


        Class.forName("org.postgresql.Driver");
        Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        while (true){
            System.out.println();
            System.out.println("1. Show list of patients");
            System.out.println("2. Add Doctor");
            System.out.println("3. Add Patient");
            System.out.println("4. Create appointment");
            System.out.println("5. Show appointments for patient");
            System.out.println("6. Delete patient");
            System.out.println("7. Edit patient");
            System.out.println("8. Edit state of appointment");
            System.out.println("9. Exit");
            System.out.println();
            int command = scanner.nextInt();

            if (command == 1){
                Statement statement = connection.createStatement();
                String SQL_SELECT_PATIENTS = "select * from patient";
                ResultSet result = statement.executeQuery(SQL_SELECT_PATIENTS);
                while (result.next()) {
                    System.out.println(result.getInt("idp") + " " + result.getString("name")
                            + " " + result.getDate("regdate"));
                }
                
            } else if (command == 2) {
                String sql = "insert into doctor (name) values (?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                System.out.println("Input name of Doctor: ");
                scanner.nextLine();
                String doctorName = scanner.nextLine();
                preparedStatement.setString(1, doctorName);
                preparedStatement.executeUpdate();

            } else if (command == 3) {
                String sql = "insert into patient (name, regdate) values (?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);

                System.out.println("Input name of Patient: ");
                scanner.nextLine();
                String patientName = scanner.nextLine();
                preparedStatement.setString(1, patientName);

                System.out.println("Input registration date: ");
                String date = scanner.nextLine();
                preparedStatement.setDate(2, Date.valueOf(date));
                preparedStatement.executeUpdate();
            } else if (command == 4 ) {
                String sql = "insert into appointment (appdate, idd, idp, state) values (?, ?, ?, 'New')";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);

                System.out.println("Input Patient id: ");
                int idPatient = scanner.nextInt();
                preparedStatement.setInt(3, idPatient);

                System.out.println("Input registration date: ");
                scanner.nextLine();
                String date = scanner.nextLine();
                preparedStatement.setDate(1, Date.valueOf(date));

                System.out.println("Input Doctor id: ");
                int idDoctor = scanner.nextInt();
                preparedStatement.setInt(2, idDoctor);
                preparedStatement.executeUpdate();
            } else if (command == 5) {
                System.out.println("Input Patient id: ");
                int idPatient = scanner.nextInt();
                Statement statement = connection.createStatement();
                String SQL_SELECT_APPOINTMENTS = String.format("select * from appointment where idp = %d",idPatient);
                ResultSet result = statement.executeQuery(SQL_SELECT_APPOINTMENTS);

                System.out.println("id state  date  DoctorId ");
                while (result.next()) {
                    System.out.println(result.getInt("ida") + " " + result.getString("state")
                            + " " + result.getDate("appdate") + " " + result.getInt("idd"));
                }
            } else if (command == 6) {
                String sql = "delete from patient where idp = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);

                System.out.println("Input Patient id: ");
                int idPatient = scanner.nextInt();
                preparedStatement.setInt(1, idPatient);
                preparedStatement.executeUpdate();
            } else if (command == 7) {
                String sql ="update patient set name = ? where idp = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);

                System.out.println("Input Patient id: ");
                int idPatient = scanner.nextInt();
                preparedStatement.setInt(2, idPatient);

                System.out.println("Input new name of patient: ");
                scanner.nextLine();
                String patientName = scanner.nextLine();
                preparedStatement.setString(1, patientName);
                preparedStatement.executeUpdate();
            } else if (command == 8) {
                String sql ="update appointment set state = ? where ida = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);

                System.out.println("Input appointment id: ");
                int idAppointment = scanner.nextInt();
                preparedStatement.setInt(2, idAppointment);

                System.out.println("Input new state of appointment: ");
                scanner.nextLine();
                String appointmentName = scanner.nextLine();
                preparedStatement.setString(1, appointmentName);
                preparedStatement.executeUpdate();
            } else if (command == 9) {
                System.exit(0);
            } else {
                System.err.println("Сommand not recognized");
            }
        }
    }
}