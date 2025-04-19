package com.eventure.events.model;

import java.util.List;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.eventure.events.dto.Ticket;

@Setter
@Getter
@Data
@Document(collection = "Users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users {

	@Id
	private String id;
	private String userId;
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNo;
	//need to remove password
	private String password;
	private String usertype;
	private int numberOfTickets;
    private List<Ticket> tickets;


}
