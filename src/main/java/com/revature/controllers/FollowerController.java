package com.revature.controllers;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.revature.annotations.Authorized;
import com.revature.dtos.UserDTO;
import com.revature.keys.FollowerKey;
import com.revature.models.User;
import com.revature.services.FollowerService;

@RestController
public class FollowerController {
	
	private FollowerService fs;

	public FollowerController(FollowerService fs) {
		super();
		this.fs = fs;
	}

	@Authorized
	@GetMapping("/following")
	public ResponseEntity<Set<UserDTO>> getFollowing(HttpSession session, @RequestParam Optional<Integer> offset, @RequestParam Optional<Integer> limit) {
		User currentUser = (User) session.getAttribute("user");
		Set<UserDTO> fdto = new HashSet<>();
		Pageable p;
		if (limit.isPresent()) {
			p = PageRequest.of(offset.isPresent() ? offset.get() : 0, limit.get());
		} else {
			p = Pageable.unpaged();
		}
		fs.getFollowingByFollower(currentUser, p).forEach(u -> {
			UserDTO f = new UserDTO();
			f.setId(u.getId());
			f.setFirstName(u.getFirstName());
			f.setLastName(u.getLastName());
			fdto.add(f);
		});
		return ResponseEntity.ok(fdto);
	}
	
	@Authorized
	@GetMapping("/followers")
	public ResponseEntity<Set<UserDTO>> getFollowers(HttpSession session, @RequestParam Optional<Integer> offset, @RequestParam Optional<Integer> limit) {
		User currentUser = (User) session.getAttribute("user");
		Set<UserDTO> fdto = new HashSet<>();
		Pageable p;
		if (limit.isPresent()) {
			p = PageRequest.of(offset.isPresent() ? offset.get() : 0, limit.get());
		} else {
			p = Pageable.unpaged();
		}
		fs.getFollowersByFollowing(currentUser, p).forEach(u -> {
			UserDTO f = new UserDTO();
			f.setId(u.getId());
			f.setFirstName(u.getFirstName());
			f.setLastName(u.getLastName());
			fdto.add(f);
		});
		return ResponseEntity.ok(fdto);
	}
	
	@Authorized
	@PostMapping("/following")
	public ResponseEntity<String> addFollowing(@RequestBody FollowerKey fk) {
		fs.addFollowing(fk);
		return ResponseEntity.ok("Followed");
	}
	
	@Authorized
	@DeleteMapping("/following")
	public ResponseEntity<String> removeFollowing(@RequestBody FollowerKey fk) {
		fs.removeFollowing(fk);
		return ResponseEntity.ok("Unfollowed");
	}
}