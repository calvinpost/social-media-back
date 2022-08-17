package com.revature.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.models.Likes;
import com.revature.services.LikesService;
import com.revature.services.NotificationService;

@WebMvcTest(LikesController.class)
class LikesControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private NotificationService ns;

	@MockBean
	private LikesService likesService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void findLikesByUserIdAndPostIdTest() throws Exception {
		Likes likeExpected = new Likes();
		likeExpected.setId(1);
		likeExpected.setPostId(1);
		likeExpected.setUserId(1);

		when(likesService.findLikesByUserIdAndPostId(1, 1)).thenReturn(likeExpected);

		mockMvc.perform(get("/likes/user/1/post/1")).andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(likeExpected)));
	}

	@Test
	void userLikesPostTest() throws Exception {
		Likes likeExpected = new Likes();
		likeExpected.setId(1);
		likeExpected.setPostId(1);
		likeExpected.setUserId(1);

		when(likesService.findLikesByUserIdAndPostId(1, 1)).thenReturn(null);
		when(likesService.userLikesPost(likeExpected)).thenReturn(likeExpected);

		mockMvc.perform(post("/likes").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(likeExpected))).andExpect(status().isOk());
	}

	@Test
	void deleteByIdExists() throws Exception {
		mockMvc.perform(delete("/likes/1")).andExpect(status().isOk());
	}

}
