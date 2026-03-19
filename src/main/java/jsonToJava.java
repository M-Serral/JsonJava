import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.*;

void main() throws ClassNotFoundException, SQLException, IOException {

    Class.forName("com.mysql.cj.jdbc.Driver");
    Connection conn = null;

    ArrayList<CustomerDetails> a = new ArrayList<>();

    conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Business", "dev", "dev");

//object of statement class will help us to execute queries
    Statement st = conn.createStatement();
    ResultSet rs = st.executeQuery("select * from CustomerInfo where Location = 'Asia';");
    while (rs.next()) {

        CustomerDetails c = new CustomerDetails();
        //3 different json files, 3 diff java objects
        c.setCourseName(rs.getString(1));
        c.setPurchasedDate(rs.getString(2));
        c.setAmount(rs.getInt(3));
        c.setLocation(rs.getString(4));
        a.add(c);


    }

    IO.println("Number of records found: " + a.size());

    Map<String, Object> jsonObject = new HashMap<>();
    jsonObject.put("data", a);

    ObjectMapper o = new ObjectMapper();
    o.writeValue(new File("customerData.json"), jsonObject);
    IO.println("File written: customerData.json");


    conn.close();


}
