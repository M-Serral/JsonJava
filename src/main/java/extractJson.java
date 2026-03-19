import com.fasterxml.jackson.databind.ObjectMapper;

void main() throws IOException {


    ObjectMapper o = new ObjectMapper();
    Map<String, Object> map = o.readValue(new File("customerData.json"), Map.class);
    List<Map<String, Object>> data = (List<Map<String, Object>>) map.get("data");
    Map<String, Object> first = data.getFirst();
    CustomerDetailsAppium c = o.convertValue(first, CustomerDetailsAppium.class);

    IO.println(c.getCourseName());
}
