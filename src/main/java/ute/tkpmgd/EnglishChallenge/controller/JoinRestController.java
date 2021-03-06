package ute.tkpmgd.EnglishChallenge.controller;

import java.util.concurrent.ScheduledFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ute.tkpmgd.EnglishChallenge.service.IJoinService;

@RestController
@RequestMapping("/api/join")
public class JoinRestController {

    @Autowired
    private TaskScheduler taskScheduler;

    private ScheduledFuture<?> scheduledFuture;
    
	@Autowired
	IJoinService joinService;
	
	@GetMapping(value="/create/{id}")
	public ResponseEntity<?> create(@PathVariable("id") int userId) {
		return ResponseEntity.ok(joinService.readyToJoin(userId));
	}
	
	@GetMapping(value="/wait/{id}")
	public ResponseEntity<?> wait(@PathVariable("id") int joinId) {
		return ResponseEntity.ok(joinService.readyToJoin(joinId));
	}

	@GetMapping(value="/start/{id}")
	public ResponseEntity<?> start(@PathVariable("id") int joinId) {
		scheduledFuture = taskScheduler.scheduleAtFixedRate(processTimeSecond(joinId), IJoinService.FIXED_RATE);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping(value="/progress/{id}")
	public ResponseEntity<?> progress(@PathVariable("id") int joinId) {
		return ResponseEntity.ok(joinService.getTimeSecond(joinId));
	}
	
	private Runnable processTimeSecond(int joinId) {
		return () -> joinService.processTimeSecond(joinId, scheduledFuture);
	}

}
