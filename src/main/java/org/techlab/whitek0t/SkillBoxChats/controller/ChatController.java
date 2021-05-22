package org.techlab.whitek0t.SkillBoxChats.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.techlab.whitek0t.SkillBoxChats.model.Message;
import org.techlab.whitek0t.SkillBoxChats.model.User;
import org.techlab.whitek0t.SkillBoxChats.repos.MessageRepository;
import org.techlab.whitek0t.SkillBoxChats.repos.UserRepository;
import org.techlab.whitek0t.SkillBoxChats.response.AddMessageResponse;
import org.techlab.whitek0t.SkillBoxChats.response.AuthResponse;
import org.techlab.whitek0t.SkillBoxChats.response.MessageResponse;
import org.techlab.whitek0t.SkillBoxChats.response.UserResponse;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
public class ChatController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageRepository messageRepository;

    private static final SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");

    @GetMapping(path = "/api/auth")
    public AuthResponse auth() {
        AuthResponse response = new AuthResponse();
        String sessionId = getSessionId();
        User user = userRepository.getBySessionId(sessionId);
        response.setResult(user != null);
        if(user != null) {
            response.setName(user.getName());
        }
        return response;
    }

    @PostMapping(path = "/api/users")
    public HashMap<String, Boolean> addUser(HttpServletRequest request) {
        String name = request.getParameter("name");
        String sessionId = getSessionId();
        User user = new User();
        user.setName(name);
        user.setRegTime(new Date());
        user.setSessionId(sessionId);
        userRepository.save(user);
        HashMap<String, Boolean> response = new HashMap<>();
        response.put("result", true);
        return response;
    }

    @PostMapping(path = "/api/messages")
    public AddMessageResponse addMessage(HttpServletRequest request) {
        String text = request.getParameter("text");

        String sessionId = getSessionId();
        User user = userRepository.getBySessionId(sessionId);

        Date time = new Date();
        Message message = new Message();
        message.setSendTime(time);
        message.setUser(user);
        message.setText(text);
        messageRepository.save(message);

        AddMessageResponse response = new AddMessageResponse();
        response.setResult(true);
        response.setTime(formatter.format(time));
        return response;
    }
    @GetMapping(path = "/api/users")
    public List<UserResponse> getAllUser() {
        List<UserResponse> userResponses = new ArrayList<>();
        Iterable<User> users = userRepository.findAll();
        for (User user : users) {
            UserResponse userResponse = new UserResponse();
            userResponse.setId(user.getId());
            userResponse.setName(user.getName());
            userResponses.add(userResponse);
        }
        return userResponses;
    }

    @GetMapping(path = "/api/messages")
    public HashMap<String, List> getMessages() {
        ArrayList<MessageResponse> messagesList =
                new ArrayList<>();
        Iterable<Message> messages = messageRepository.findAll();
        for(Message message : messages) {
            MessageResponse messageItem = new MessageResponse();
            messageItem.setName(message.getUser().getName());
            messageItem.setTime(
                    formatter.format(message.getSendTime())
            );
            messageItem.setText(message.getText());
            messagesList.add(messageItem);
        }
        HashMap<String, List> response = new HashMap<>();
        response.put("messages", messagesList);
        return response;
    }

    private String getSessionId() {
        return RequestContextHolder.currentRequestAttributes().getSessionId();
    }
}
