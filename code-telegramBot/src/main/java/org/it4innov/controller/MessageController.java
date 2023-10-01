package org.it4innov.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.it4innov.entity.MessageReceived;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Path("/api/message")
public class MessageController {

    record Message( Long id, String firstName, String messageText, LocalDateTime dateTime){};
    @Path("/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Message> getMessage(){
        List<Message> listMessage = new ArrayList<>();
        List<MessageReceived> messageReceiveds = MessageReceived.listAll();
        messageReceiveds.forEach( mr -> {
            Message message = new Message(mr.id, mr.firstName, mr.messageText, mr.dateTime);
            listMessage.add(message);
        });
         return listMessage;
    }
}
