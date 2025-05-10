package com.eventure.events.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


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
	private String usertype;


}
