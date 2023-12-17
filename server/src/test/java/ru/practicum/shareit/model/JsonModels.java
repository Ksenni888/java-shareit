package ru.practicum.shareit.model;

import com.jayway.jsonpath.JsonPath;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


public final class JsonModels {
    public static final String userName = "user";
    public static final String email = "user@user.ru";

    public static final String userName2 = "update";
    public static final String email2 = "update@user.com";

    public static final String itemName = "Дрель";
    public static final String itemDescription = "Простая дрель";
    public static final boolean available = true;

    public static final String itemName2 = "Дрель";
    public static final String itemDescription2 = "Аккумуляторная дрель";
    public static final boolean availableUpd = false;

    public static final LocalDateTime start = LocalDateTime.now().plusMinutes(2).truncatedTo(ChronoUnit.SECONDS);
    public static final LocalDateTime end = LocalDateTime.now().plusMinutes(3).truncatedTo(ChronoUnit.SECONDS);
    public static final String statusWait = Status.WAITING.toString();
    public static final String statusApr = Status.APPROVED.toString();

    public static final String requestDescription = "Хотел бы воспользоваться щёткой для обуви";

    public static final String text = "Here is a comment here";
    public static final LocalDateTime created = LocalDateTime.now().plusSeconds(4).truncatedTo(ChronoUnit.SECONDS);


    public static Integer getItemId(MockMvc mockMvc,
                                    Integer userId,
                                    String name,
                                    String description,
                                    boolean available) throws Exception {

        String item = createItemDtoJson(name, description, available);

        MvcResult resultItem = mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", userId)
                        .content(item)
                        .contentType(MediaType.APPLICATION_JSON))

                .andReturn();

        return JsonPath.read(resultItem.getResponse().getContentAsString(), "$.id");
    }

    public static String createItemDtoJson(String name, String description, boolean available) {
        return "{\n" +
                "    \"name\": \"" + name + "\",\n" +
                "    \"description\": \"" + description + "\",\n" +
                "    \"available\": " + available + "\n" +
                "}";
    }

    public static Integer getUserId(MockMvc mockMvc, String name, String email) throws Exception {
        String addUser = createUserJson(name, email);

        MvcResult result = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addUser))
                .andReturn();

        return JsonPath.read(result.getResponse().getContentAsString(), "$.id");
    }

    public static String createUserJson(String name, String email) {
        return "{\n" +
                "    \"name\": \"" + name + "\",\n" +
                "    \"email\": \"" + email + "\"\n" +
                "}";

    }

    public static Integer getBookingId(MockMvc mockMvc, Integer itemId, Integer userId2) throws Exception {
        String bookingJson = createBookingJson(itemId, start, end);

        MvcResult result = mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookingJson)
                        .header("X-Sharer-User-Id", userId2))
                .andReturn();

        return JsonPath.read(result.getResponse().getContentAsString(), "$.id");
    }

    public static String createBookingJson(Integer itemId, LocalDateTime start, LocalDateTime end) {


        return "{\n" +
                "    \"itemId\": " + itemId + ",\n" +
                "    \"start\": \"" + start + "\",\n" +
                "    \"end\": \"" + end + "\"\n" +
                "}";
    }

    public static Integer getRequestId(MockMvc mockMvc, Integer userId, String description) throws Exception {
        String requestJson = createRequestJson(description);

        MvcResult result = mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .header("X-Sharer-User-Id", userId))
                .andReturn();

        return JsonPath.read(result.getResponse().getContentAsString(), "$.id");
    }

    public static String createRequestJson(String text) {
        return "{\n" +
                "    \"description\": \"" + text + "\"\n" +
                "}";
    }

}