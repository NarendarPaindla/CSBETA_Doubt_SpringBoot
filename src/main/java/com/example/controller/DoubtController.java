package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.model.Doubt;
import com.example.service.DoubtService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/doubts")
public class DoubtController {

    @Autowired
    private DoubtService doubtService;

    @PostMapping("/ask")
    public ResponseEntity<Doubt> askDoubt(@RequestBody Doubt doubt) {
        return ResponseEntity.ok(doubtService.askDoubt(doubt));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Doubt>> getAllDoubts() {
        return ResponseEntity.ok(doubtService.getAllDoubts());
    }

    @PostMapping("/reply/{doubtId}")
    public ResponseEntity<?> replyToDoubt(@PathVariable String doubtId, @RequestBody Doubt.Reply reply) {
        Optional<Doubt> doubt = doubtService.addReply(doubtId, reply);
        if (doubt.isPresent()) {
            return ResponseEntity.ok(doubt.get());
        } else {
            return ResponseEntity.badRequest().body("Doubt not found");
        }
    }
}
